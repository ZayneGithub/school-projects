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
import zed
from map_common_functions import *
from interactive_map import *

# ----------------Executes the Generate button on the Pool window is pressed and use_suricata == False, passing the pool name and fields---------------- #
def generate_map_from_pool(pool, protocol_name, source_name, destination_name):
    # ZQL query, similar to SQL. It is sent in a query to get data from BRIM via Zed.
    ZQL = (
        "from '%s' | _path == 'conn' | cut proto, id.orig_h, id.resp_h | sort id.orig_h, id.resp_h"
        % pool
    )
    try:
        client = zed.Client() # Create a new client to connect to BRIM.
        records = client.query(ZQL) # Execute a Zed ZQL query.
        # Stream records from the server, appending each record to the unvalidated data list.
        dataUnvalidated = []
        for record in records:
            dataUnvalidated.append(record)
        # Validation of the dataframe.
        data = []
        for x in dataUnvalidated:
            if "missing" not in str(x):
                data.append(x)
        df = pd.json_normalize(data) # Creating a Pandas dataframe.
        generate_map(df, protocol_name, source_name, destination_name) # Calling generate_map from map_common_functions.
    except:
        print(
            "\nThere has been a problem generating a map from a BRIM pool called:\n%s\nTry again."
            % pool
        )
    # except Exception as e:
    # print(e)

# ----------------Executes the Generate button on the Pool window is pressed and use_suricata == True, passing the pool name and fields---------------- #
def generate_threat_map_from_pool(pool, protocol_name, source_name, destination_name):
    # ZQL query, similar to SQL. It is sent in a query to get data from BRIM via Zed.
    ZQL = (
        "from '%s' | _path == 'conn' | cut proto, id.orig_h, id.resp_h | sort id.orig_h, id.resp_h"
        % pool
    )
    # ZQL query. It is sent to get Suricata alerts, severity and category alongside protocol/source/destination fields.
    ZQL2 = (
        "from '%s' | event_type == 'alert' | cut proto, src_ip, dest_ip, alert.severity, alert.category | sort src_ip, dest_ip, alert.severity, alert.category"
        % pool
    )

    try:
        client = zed.Client() # Create a new client to connect to BRIM.
        
        records = client.query(ZQL) # Execute a Zed ZQL query.
        df = pd.json_normalize(records) # Creating a Pandas dataframe.

        records2 = client.query(ZQL2) # Execute a second Zed ZQL query.
        df2 = pd.json_normalize(records2) # Creating a second Pandas dataframe.

        # If no Suricata alerts are returned in the query, generate a typical NetworkX map
        # from map_common_functions, otherwise create a threat-based NetworkX map.
        if len(df2) == 0:
            print(
                "\nNo alerts found in pool:\n%s\nGenerating a Non-Suricata Map instead...\n"
                % pool
            )
            generate_map_from_pool(pool, protocol_name, source_name, destination_name)
        else:
            generate_threat_map(df, df2, protocol_name, source_name, destination_name)
    except:
        print(
            "\nThere has been a problem generating a Suricata Network Map from a BRIM pool called:\n%s\nTry again."
            % pool
        )
    #except Exception as e:
        #print(e)

# ----------------Generates a threat-based NetworkX map/graph from the two dataframes---------------- #
def generate_threat_map(df, df2, protocol_name, source_name, destination_name):
    df.dropna(axis=0, how="any", inplace=True) # Drop all N/A or NaN values from the dataframe.
    # Sorting the Pandas dataframes.
    df = pd.DataFrame(
        {
            "proto": df["proto"],
            "source": df["id.orig_h"],
            "target": df["id.resp_h"],
        }
    )
    df2 = pd.DataFrame(
        {
            "proto": df2["proto"],
            "source": df2["src_ip"],
            "target": df2["dest_ip"],
            "severity": df2["alert.severity"],
            "category": df2["alert.category"],
        }
    )
    df3 = pd.concat([df, df2], ignore_index=True).fillna(0) # Merging the two dataframes and filling N/A or NaN values with 0.0.

    # Determine the size of the graph, and acquire styling attributes.
    record_no = len(df3)
    df3, size, font_size, arrow_size, node_dot = set_size(df3, record_no)
    print("\nGraph Size: %s" % size)
    
    # Create the graph (G) using source and target nodes.
    # The edge connecting them represents the connection via a protocol,
    # which is used as an edge attribute.
    G = nx.from_pandas_edgelist(
        df3,
        source="source",
        target="target",
        edge_key="proto",
        edge_attr=True,
        create_using=nx.DiGraph(),
    )

    # Debug the dataframe and graph.
    describe_dataframe(df3)
    describe_graph(G, df3)

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

    # Creating lists of edges based on the alert severity, assigning style attributes to each.

    # Edge styling for for severity 1 connections.
    sev1_list = [(s, t) for s, t in G.edges if G.edges[s, t]["severity"] == 1]
    sev1_color = "#fcfc03"
    sev1_line_style = "solid"
    sev1_lwidth = 5

    # Edge styling for for severity 2 connections.
    sev2_list = [(s, t) for s, t in G.edges if G.edges[s, t]["severity"] == 2]
    sev2_color = "#fc9003"
    sev2_line_style = "solid"
    sev2_lwidth = 7

    # Edge styling for for severity 3 connections.
    sev3_list = [(s, t) for s, t in G.edges if G.edges[s, t]["severity"] == 3]
    sev3_color = "#fc0303"
    sev3_line_style = "solid"
    sev3_lwidth = 9

    # Additional styling attributes.
    edge_font_color = "#000000"
    label_font_color = "#ffffff"
    nodes_color = "#DC7633"
    lwidth = 3

    # Get edge attributes, used by draw_networkx_labels later on.
    edge_sev = nx.get_edge_attributes(G, "severity")

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
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=sev1_list,
            width=sev1_lwidth,
            style=sev1_line_style,
            edge_color=sev1_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="-|>",
        )
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=sev2_list,
            width=sev2_lwidth,
            style=sev2_line_style,
            edge_color=sev2_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="-|>",
        )
        nx.draw_networkx_edges(
            G,
            pos,
            ax=plot,
            edgelist=sev3_list,
            width=sev3_lwidth,
            style=sev3_line_style,
            edge_color=sev3_color,
            arrows=True,
            arrowsize=arrow_size,
            arrowstyle="-|>",
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
            G, pos, ax=plot, font_color=label_font_color, font_size=font_size
        )
        nx.draw_networkx_edge_labels(
            G,
            pos,
            ax=plot,
            font_color=edge_font_color,
            edge_labels=edge_sev,
            font_size=font_size,
        )

    # Drawing the final plots.
    # Draw with tight layout https://matplotlib.org/tutorials/intermediate/tight_layout_guide.html
    plt.tight_layout() # Draw with tight layout https://matplotlib.org/stable/api/_as_gen/matplotlib.pyplot.tight_layout.html
    manager = plt.get_current_fig_manager()
    manager.full_screen_toggle() # Enables full screen of the plot/figure window.
    manager.set_window_title("Network Map") # Sets the title of the window.
    _.canvas.mpl_connect("close_event", on_close) # When the window is closed, execute on_close() function.
    plt.show(block=False) # Show the final plot. If block=True, PyVis interactive map won't display until the NetworkX map window is closed.
