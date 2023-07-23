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
import os
from map_common_functions import *
from interactive_map import *

#pd.set_option("display.max_rows", 500)

# ----------------Executes the Generate button on the PCAP/CSV windows is pressed, passing the path, filetype, conditions and fields---------------- #
def generate_map_from_file(
    path,
    file_type,
    interactive_map,
    interactive_map_layout,
    protocol_name,
    source_name,
    destination_name,
):
    file_name, file_extension = os.path.splitext(path)
    if file_type == "CSV":
        if file_extension == ".csv":
            try:
                df = pd.read_csv(path)
                if {protocol_name, source_name, destination_name}.issubset(
                    df.columns
                ) == False:
                    print(
                        "%s, %s and %s fields not found using path:\n%s\nPlease try new fields or reformat this file with these fields."
                        % (protocol_name, source_name, destination_name, path)
                    )
                else:
                    df = df[[protocol_name, source_name, destination_name]].copy() # Eliminating irrelevant columns from the dataframe.
                    df.sort_values(
                        [source_name], axis=0, ascending=[True], inplace=True
                    )
                    if len(df) == 0:
                        print("Problem with dataframe using path:\n%s\nDataframe is empty, try another path." % path)
                    else:
                        generate_map(df, protocol_name, source_name, destination_name)
                        if interactive_map == True:
                            if (
                                interactive_map_layout != "Barnes Hut"
                                and interactive_map_layout != "Force-Atlas-2-Based"
                                and interactive_map_layout != "Hierarchical"
                            ):
                                print(
                                    "\n%s is not a valid interactive map layout.\nPlease choose a predefined option and regenerate..."
                                    % interactive_map_layout
                                )
                            else:
                                generate_interactive_map(
                                    df,
                                    interactive_map_layout,
                                    protocol_name,
                                    source_name,
                                    destination_name,
                                )
            except:
                print(
                    "\nThere has been a problem generating a map from a CSV file using the path:\n%s\nTry again."
                    % path
                )
        else:
            print(
                "\nThere has been a problem generating a map from a CSV file using the path:\n%s\nTry again."
                % path
            )
    elif file_type == "PCAP":
        if file_extension == ".pcap" or file_extension == ".pcapng":
            try:
                # Convert PCAP to CSV using tshark executable path.
                path_out = file_name + "-auto-generated.csv"
                tshark_template = (
                    r'"C:\Program Files\Wireshark\tshark.exe" -r "{}" -T fields -e frame.number -e frame.time -e eth.src -e eth.dst -e %s -e %s -e %s -E header=y -E separator=, -E quote=d -E occurrence=f > "{}"'
                    % (source_name, destination_name, protocol_name)
                )
                tshark_cmd = tshark_template.format(path, path_out)
                print(
                    "Converting PCAP-like file to CSV file:\n%s\n%s" % (path, path_out)
                )
                # print(tshark_cmd)
                os.popen(tshark_cmd).read() # Executing the conversion command.
            except:
                print(
                    "\nThere has been a problem converting your PCAP-like file to CSV using the path:\n%s\nRecommended fix: tshark path within tshark_template variable, line 45.\nTry again."
                    % path
                )
            try:
                # Creating a Pandas dataframe using the generated CSV file.
                df = pd.read_csv(path_out)
                if {protocol_name, source_name, destination_name}.issubset(
                    df.columns
                ) == False:
                    print(
                        "\n%s, %s and %s fields not found using path:\n%s\nPlease try new fields or reformat this file with these fields."
                        % (protocol_name, source_name, destination_name, path)
                    )
                else:
                    df = df[[protocol_name, source_name, destination_name]].copy() # Eliminating irrelevant columns from the dataframe.
                    if len(df) == 0:
                        print("Problem with dataframe using path:\n%s\nDataframe is empty, try another path." % path)
                    else:
                        generate_map(df, protocol_name, source_name, destination_name)
                        if interactive_map == True:
                            if (
                                interactive_map_layout != "Barnes Hut"
                                and interactive_map_layout != "Force-Atlas-2-Based"
                                and interactive_map_layout != "Hierarchical"
                            ):
                                print(
                                    "\n%s is not a valid interactive map layout.\nPlease choose a predefined option and regenerate..."
                                    % interactive_map_layout
                                )
                            else:
                                generate_interactive_map(
                                    df,
                                    interactive_map_layout,
                                    protocol_name,
                                    source_name,
                                    destination_name,
                                )
            except:
                print(
                    "\nThere has been a problem generating a map from a PCAP file using the path:\n%s\nTry again."
                    % path_out
                )
            # except Exception as e:
            # print(e)
        else:
            print(
                "\nThere has been a problem generating a map from a PCAP file using the path:\n%s\nTry again."
                % path
            )
