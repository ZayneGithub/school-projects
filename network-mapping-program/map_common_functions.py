"""
NETWORK MAP INC. THREAT DETECTION - IMPROVING NETWORK SECURITY THROUGH VISUALISATION.
    SOME FUNCTIONS DEMAND THAT EXTERNAL PROGRAMS ARE RUNNING, E.G:
        BRIM (PACKET ANALYSIS PROGRAM) ALONGSIDE IT'S DEPENDENCIES TO FUNCTION AS INTENDED
            DOWNLOAD VIA HTTPS://WWW.BRIMDATA.IO.
            MORE INFORMATION AVAILABLE AT HTTPS://GITHUB.COM/BRIMDATA/BRIM
            AND HTTPS://GITHUB.COM/BRIMDATA/ZED.
        TSHARK, TYPICALLY INSTALLED ALONGSIDE WIRESHARK.
            DOWNLOAD VIA HTTPS://WWW.WIRESHARK.ORG.
            MORE INFORMATION AVAILABLE AT HTTPS://WWW.WIRESHARK.ORG/DOCS/MAN-PAGES/TSHARK.HTML
"""

import pandas as pd
import matplotlib.pyplot as plt
import networkx as nx

pd.set_option("display.max_rows", 500) # The number of rows of a dataframe to display when print(df) is ran.

# ----------------Sets the global variables max_record_no and sample_record_no to user defined values---------------- #
def set_globals(mrn, srn):
    global max_record_no
    global sample_record_no # The number of records to sample if the record_no is > max_record_no.
    max_record_no = int(mrn)
    sample_record_no = int(srn)

# ----------------Executes when a specified figure window (e.g. NetworkX map) is closed---------------- #
def on_close(event):
    print("\nA map has been closed.\nPlease generate another map...")

# ----------------Limits the dataframe to a user specified amount (sample_record_no) and sets style attributes---------------- #
def set_size(df, record_no):
    if record_no > max_record_no:
        print(
            "\nNo. of records exceeds the maximum (%s), limiting to %s records."
            % (max_record_no, sample_record_no)
        )
        df = df.sample(n=sample_record_no)
        record_no = len(df)
    if record_no > 400:
        size = "huge"
        font_size = 6
        arrow_size = 2
        node_dot = 50
    elif (record_no <= 400) and (record_no > 200):
        size = "large"
        font_size = 6
        arrow_size = 2.5
        node_dot = 50
    elif (record_no <= 200) and (record_no > 100):
        size = "medium"
        font_size = 8
        arrow_size = 3
        node_dot = 50
    elif (record_no <= 100) and (record_no > 50):
        size = "small"
        font_size = 10
        arrow_size = 4
        node_dot = 50
    else:
        size = "tiny"
        font_size = 12
        arrow_size = 5
        node_dot = 50
    return (df, size, font_size, arrow_size, node_dot)

# ----------------Prints Pandas dataframe related data for debugging purposes---------------- #
def describe_dataframe(df):
    print("\n------------------ Dataframe Description ------------------")
    print("\nFirst 5 Records:\n")
    print(df.head(5))
    print("\nLast 5 Records:\n")
    print(df.tail(5))
    print("\nLength of the dataframe: ", len(df))
    print("\nData types within the dataframe:\n", df.dtypes)
    print("\nNo. of N/A values within the dataframe:\n", df.isna().sum())
    print("\nDescription of the dataframe:\n", df.describe())
    return

# ----------------Prints graph related data for debugging purposes---------------- #
def describe_graph(G, df):
    print("\n------------------ Graph Description ------------------")
    print("\nLength of the dataframe: ", len(df))
    print("\nNo. of nodes: ", G.number_of_nodes())
    print("\nNo. of nodes (order): ", G.order())
    print("\nNo. of edges: ", G.number_of_edges())
    print("\nNodes: ", G.nodes())
    print("\nEdges: ", G.edges())
    print("\nEdges (data): ", G.edges.data())
    print("\nDegrees: ", G.degree())
    print("\nIs the graph directed?: ", G.is_directed())
    print("\nIs the graph weighted?: ", nx.is_weighted(G))
    return

# ----------------Generates a NetworkX map/graph from the dataframe, and three user-specified fields---------------- #
def generate_map(df, protocol_name, source_name, destination_name):
    df.dropna(axis=0, how="any", inplace=True) # Drop all N/A or NaN values from the dataframe.

    # Determine the size of the graph, and acquire styling attributes.
    record_no = len(df)
    df, size, font_size, arrow_size, node_dot = set_size(df, record_no)
    print("\nGraph Size: %s" % size)
    
    # Create the graph (G) using source and target nodes.
    # The edge connecting them represents the connection via a protocol,
    # which is used as an edge attribute.
    G = nx.from_pandas_edgelist(
        df,
        source=source_name,
        target=destination_name,
        edge_key=protocol_name,
        edge_attr=True,
        create_using=nx.DiGraph(),
    )
    
    # Debug the dataframe and graph.
    describe_dataframe(df)
    describe_graph(G, df)
    
    # Creating lists of edges based on the protocol type, assigning style attributes to each.
    
    tcp_list = [ # Edge styling for for TCP connections.
        (s, t) for s, t in G.edges if G.edges[s, t][protocol_name].lower() == "tcp"
    ]
    tcp_color = "#45ff42" # Sets the color of the edge.
    tcp_line_style = "solid" # Set the style of the edge i.e., solid, dashed, dotted, dashdot.

    udp_list = [ # Edge styling for for UDP connections.
        (s, t) for s, t in G.edges if G.edges[s, t][protocol_name].lower() == "udp"
    ]
    udp_color = "#83fce6"
    udp_line_style = "dashdot"

    tls_list = [ # Edge styling for for TLS-based connections.
        (s, t)
        for s, t in G.edges
        if G.edges[s, t][protocol_name].lower() == "tlsv1.2"
        or G.edges[s, t][protocol_name].lower() == "tlsv1.3"
    ]
    tls_color = "#70c1ff"
    tls_line_style = "dashdot"

    icmp_list = [ # Edge styling for for ICMP connections.
        (s, t) for s, t in G.edges if G.edges[s, t][protocol_name].lower() == "icmp"
    ]
    icmp_color = "#8295ff"
    icmp_line_style = "dashed"

    other_list = [ # Edge styling for for other connections.
        (s, t)
        for s, t in G.edges
        if (
            G.edges[s, t][protocol_name].lower() != "tcp"
            and G.edges[s, t][protocol_name].lower() != "udp"
            and G.edges[s, t][protocol_name].lower() != "tlsv1.2"
            and G.edges[s, t][protocol_name].lower() != "tlsv1.3"
            and G.edges[s, t][protocol_name].lower() != "icmp"
        )
    ]
    other_color = "#a263ff"
    other_line_style = "dotted"

    # Additional styling attributes.
    edge_font_color = "#000000"
    label_font_color = "#ffffff"
    nodes_color = "#DC7633"
    lwidth = 3

    # Get edge attributes, used by draw_networkx_labels later on.
    edge_proto = nx.get_edge_attributes(G, protocol_name)

    # Define the preferred NetworkX layouts.
    layouts = (nx.planar_layout, nx.random_layout, nx.spiral_layout, nx.circular_layout)

    # Create 4 subplots with a fixed figure size.
    _, plot = plt.subplots(2, 2, figsize=(50, 50))
    subplots = plot.reshape(1, 4)[0]

    # For each plot (subplot) and layout, draw edges, nodes and labels.
    for plot, layout in zip(subplots, layouts):
        plot.set_facecolor("#222222") # Setting the background colour.
        pos = layout(G)
        # Edges
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=tcp_list,
            width=lwidth,
            style=tcp_line_style,
            edge_color=tcp_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="-|>",
        )
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=udp_list,
            width=lwidth,
            style=udp_line_style,
            edge_color=udp_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="-|>",
        )
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=tls_list,
            width=lwidth,
            style=tls_line_style,
            edge_color=tls_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="-|>",
        )
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=icmp_list,
            width=lwidth,
            style=icmp_line_style,
            edge_color=icmp_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="->",
        )
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=other_list,
            width=lwidth,
            style=other_line_style,
            edge_color=other_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="->",
        )
        # Nodes
        nx.draw_networkx_nodes(
            G,
            pos,
            ax=plot,
            node_color=nodes_color,
            node_shape="*",
            nodelist=dict(G.degree).keys(),
            node_size=[d * node_dot for d in dict(G.degree).values()],
            alpha=1,
        )
        # Labels
        nx.draw_networkx_labels(
            G,
            pos,
            ax=plot,
            font_color=label_font_color,
            font_size=font_size
        )
        nx.draw_networkx_edge_labels(
            G,
            pos,
            ax=plot,
            font_color=edge_font_color,
            edge_labels=edge_proto,
            font_size=font_size,
        )

    # Drawing the final plots.
    plt.tight_layout() # Draw with tight layout https://matplotlib.org/stable/api/_as_gen/matplotlib.pyplot.tight_layout.html
    manager = plt.get_current_fig_manager()
    manager.full_screen_toggle() # Enables full screen of the plot/figure window.
    manager.set_window_title("Network Map") # Sets the title of the window.
    _.canvas.mpl_connect("close_event", on_close) # When the window is closed, execute on_close() function.
    plt.show(block=False) # Show the final plot. If block=True, PyVis interactive map won't display until the NetworkX map window is closed.
