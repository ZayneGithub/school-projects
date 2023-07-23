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
import networkx as nx
from pyvis.network import Network
from map_common_functions import *

# ----------------Executes the Generate button on the PCAP/CSV window is pressed and interactive_map == True, passing the dataframe, and fields---------------- #
def generate_interactive_map(
    df, interactive_map_layout, protocol_name, source_name, destination_name
):
    print("\nGenerating an interactive map...")

    # Determine which type of layout to use.
    if interactive_map_layout == "Hierarchical":
        net = Network(
            height="100%",
            width="70%",
            bgcolor="#222222",
            font_color="white",
            layout=True,
        )
    elif interactive_map_layout == "Barnes Hut":
        net = Network(height="100%", width="70%", bgcolor="#222222", font_color="white")
        net.barnes_hut()
    elif interactive_map_layout == "Force-Atlas-2-Based":
        net = Network(height="100%", width="70%", bgcolor="#222222", font_color="white")
        net.force_atlas_2based()

    df.dropna(axis=0, how="any", inplace=True) # Drop all N/A or NaN values from the dataframe.

    # Separating the dataframe into lists.
    protocols = df[protocol_name]
    sources = df[source_name]
    destinations = df[destination_name]

    # Zipping the separated lists to be iterated over.
    edge_data = zip(protocols, sources, destinations)
    
    for proto, src, dst in edge_data:
        # Change the edge color based on protocol type.
        if proto.lower() == "tcp":
            color = "#45ff42"
        elif proto.lower() == "udp":
            color = "#83fce6"
        elif proto.lower() == "tlsv1.2" or proto.lower() == "tlsv1.3":
            color = "#70c1ff"
        elif proto.lower() == "icmp":
            color = "#8295ff"
        else:
            color = "#a263ff"
        # Nodes & Edges.
        net.add_node(src, src, title=src, color="#DC7633", borderWidth=0, shape="star")
        net.add_node(dst, dst, title=dst, color="#DC7633", borderWidth=0, shape="star")
        net.add_edge(src, dst, title=proto, color=color, width=5)
    
    neighbor_map = net.get_adj_list() # Get's neighbour list for each node.

    # Add neighbor data to node, displayed when hovering over any one node.
    for node in net.nodes:
        node["title"] = (
            "<h4>"
            + node["title"]
            + "</h4> neighbors:<br>"
            + "<br>".join(neighbor_map[node["id"]])
        )
        node["value"] = len(neighbor_map[node["id"]])

    net.toggle_physics(True)
    net.show_buttons(filter_=True) # Menu next to the interactive map.
    net.show("interactive_map_auto_generated.html") # The name of the html file.
    print("Generated.")
