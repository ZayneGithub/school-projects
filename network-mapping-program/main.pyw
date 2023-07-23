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

import PySimpleGUI as sg
from map_common_functions import *
from map_from_pool import *
from map_from_file import *

# ----------------Executes automatically (without console due to .pyw filetype)---------------- #
def launcher():
    # Global variables and module settings.
    sg.theme("DarkTeal5")
    font = "Helvetica, 11"
    sg.set_options(font=font)
    user_inputs = []
    user_field_inputs = []

    # ----------------Defining individual windows---------------- #
    def make_main_window(): # Appears when program is first ran, or when back button ("←") is pressed.
        layout = [
            # Start of header, consistant on each window.
            [sg.Button("←", font="Helvetica 14", disabled=True), sg.Push()],
            [sg.Push(), sg.Image(r"logo.png"), sg.Push()],
            [
                sg.Push(), # For element alignment (horizontal).
                sg.Text("NETWORK MAP INC. THREAT DETECTION", font="Helvetica 20"),
                sg.Push(),
            ],
            [
                sg.Push(),
                sg.T(
                    "Improving Network Security through Visualisation",
                    font="Helvetica 15",
                ),
                sg.Push(),
            ],
            # End of header.
            [sg.T("")],
            [sg.Push(), sg.T("Choose your data input method:"), sg.Push()],
            [
                sg.Push(),
                # Each button loads a corresponding window.
                sg.Button("PCAP", size=(30, 10), font="Helvetica 13"),
                sg.Button("CSV", size=(30, 10), font="Helvetica 13"),
                sg.Button("Pool via BRIM", size=(30, 10), font="Helvetica 13"),
                sg.Push(),
            ],
            [sg.T("")],
            [sg.Text("Choose the max record number:")],
            [
                # The maximum number of records that the dataframe can contain.
                # If set too high, the resulting map visualisation will be less comprehensible.
                sg.Slider(
                    orientation="horizontal",
                    range=(1, 5000),
                    default_value=800,
                    # Setting enable_events means that like button presses, when that element is interacted with (e.g. clicked on, a character entered into)
                    # then an event is immediately generated causing your window.read() call to return.
                    enable_events=True,
                    key="MAX-SLIDER",
                )
            ],
            [sg.T("")],
            [
                sg.Text(
                    "Choose a sample record number:",
                    visible=False,
                    key="CHOOSE-SAMPLE-TEXT",
                )
            ],
            [
                [
                    sg.Slider( # Sets the number of samples to use if the dataframe exceeds the value of MAX-SLIDER.
                        orientation="horizontal",
                        range=(1, 800), # Range changes depending on the value of the MAX-SLIDER. Must be <= MAX-SLIDER.
                        default_value=800,
                        enable_events=True,
                        visible=False,
                        key="SAMPLE-SLIDER",
                    )
                ],
                [sg.T("Huge", visible=False, key="SAMPLE-TEXT")],
            ],
            [sg.T("")],
            [
                sg.Push(),
                sg.VPush(), # For element alignment (vertical).
                sg.Button("Exit", button_color=("white", "firebrick3")), # When clicked, the program shuts down.
            ],
        ]
        return sg.Window(
            "Network Map inc. Threat Detection", # Window title.
            layout,
            element_justification="center",
            resizable=True,
            finalize=True, # Call to force a window to go through the final stages of initialization
        )

    def make_pcap_window(): # Appears when the "PCAP" button on the main window is pressed.
        layout = [
            [sg.Button("←", font="Helvetica 14"), sg.Push()], # Back button - loads the main window when pressed and resets local and global variables.
            [sg.Push(), sg.Image(r"logo.png"), sg.Push()],
            [
                sg.Push(),
                sg.Text("NETWORK MAP INC. THREAT DETECTION", font="Helvetica 20"),
                sg.Push(),
            ],
            [
                sg.Push(),
                sg.T(
                    "Improving Network Security through Visualisation",
                    font="Helvetica 15",
                ),
                sg.Push(),
            ],
            [sg.T("")],
            [
                sg.Text( # User defined path to .pcap or .pcapng file.
                    r"PCAP File Path (e.g. C:\Users\YOU\Desktop\capture-1.pcap):",
                    key="INPUT-LABEL",
                ),
                sg.Input(key="INPUT-TEXT"),
                sg.FileBrowse(key="INPUT-FILE-BROWSER"), # Loads file explorer window, not limited to filetype due to pysimplegui/tkinter OS restrictions.
                sg.Button("Submit"), # Submits the path to the listbox below.
            ],
            [
                sg.Listbox( # Contains all submitted paths of .pcap or .pcapng files.
                    # When a path is selected in the listbox, it enables the Enter and Clear buttons below.
                    # It also resets the fields listbox for user convenience.
                    user_inputs, size=(75, 5), enable_events=True, key="INPUT-BOX"
                )
            ],
            [sg.T("")],
            [
                sg.Text( # User defined fields, as different pcap files may have different fields. Wireshark standard is as listed in the example below.
                    r"Enter your Protocol, Source and Destination field names separately (e.g. _ws.col.Protocol, then ip.src, then ip.dst):",
                    key="FIELD-INPUT-LABEL",
                )
            ],
            [
                sg.Input(key="FIELD-INPUT-TEXT"),
                sg.Button("Enter", disabled=True, button_color=("white", "grey")), # Submits the field to the listbox below.
                sg.Button("Clear", disabled=True, button_color=("white", "grey")), # Clears the listbox below.
            ],
            [
                sg.Listbox( # Contains all submitted fields.
                    user_field_inputs,
                    size=(75, 5),
                    enable_events=True,
                    key="FIELD-INPUT-BOX",
                )
            ],
            [sg.T("")],
            [
                # Generates a PyVis interactive map alongside the NetworkX map when set to True/Yes.
                sg.T("Generate Interactive Map?:"),
                # If True/Yes, set visibility of the layout options below to True.
                sg.Radio(
                    "Yes",
                    "INTERACTIVE-MAP",
                    default=True,
                    enable_events=True,
                    key="INT-MAP",
                ),
                # If False/No, set visibility of the layout options below to False.
                sg.Radio(
                    "No",
                    "INTERACTIVE-MAP",
                    default=False,
                    enable_events=True,
                    key="INT-MAP-2",
                ),
            ],
            [sg.T("")],
            [
                # Determines the preferred layout of the PyVis interactive map.
                sg.T("Choose a layout:", visible=True, key="INT-MAP-LAYOUT-TEXT"),
                sg.Combo(
                    ["Barnes Hut", "Force-Atlas-2-Based", "Hierarchical"], # PyVis layouts.
                    default_value="Barnes Hut",
                    visible=True,
                    key="INT-MAP-LAYOUT",
                ),
            ],
            [sg.T("")],
            [
                sg.Button("Generate", disabled=True, button_color=("white", "grey")), # Generates the NetworkX map (and PyVis map if True and layout is valid).
                sg.Button("Reset", disabled=True, button_color=("white", "grey")), # Resets the window to default values.
            ],
            [sg.T("")],
            [sg.T("Output:")],
            [sg.Output(size=(75, 7.5), key="OUTPUT-BOX")], # Outbox box, displaying console prints, etc.
            [
                sg.Push(),
                sg.VPush(),
                sg.Button("Exit", button_color=("white", "firebrick3")),
            ],
        ]
        return sg.Window(
            "Network Map inc. Threat Detection via PCAP",
            layout,
            element_justification="center",
            resizable=True,
            finalize=True,
        )

    def make_csv_window(): # Appears when the "CSV" button on the main window is pressed.
        layout = [
            [sg.Button("←", font="Helvetica 14"), sg.Push()],
            [sg.Push(), sg.Image(r"logo.png"), sg.Push()],
            [
                sg.Push(),
                sg.Text("NETWORK MAP INC. THREAT DETECTION", font="Helvetica 20"),
                sg.Push(),
            ],
            [
                sg.Push(),
                sg.T(
                    "Improving Network Security through Visualisation",
                    font="Helvetica 15",
                ),
                sg.Push(),
            ],
            [sg.T("")],
            [
                sg.Text(
                    r"CSV File Path (e.g. C:\Users\YOU\Desktop\capture-1.csv):",
                    key="INPUT-LABEL",
                ),
                sg.Input(key="INPUT-TEXT"),
                sg.FileBrowse(key="INPUT-FILE-BROWSER"),
                sg.Button("Submit"),
            ],
            [
                sg.Listbox(
                    user_inputs, size=(75, 5), enable_events=True, key="INPUT-BOX"
                )
            ],
            [sg.T("")],
            [
                sg.Text(
                    r"Enter your Protocol, Source and Destination field names separately (e.g. _ws.col.Protocol, then ip.src, then ip.dst):",
                    key="FIELD-INPUT-LABEL",
                )
            ],
            [
                sg.Input(key="FIELD-INPUT-TEXT"),
                sg.Button("Enter", disabled=True, button_color=("white", "grey")),
                sg.Button("Clear", disabled=True, button_color=("white", "grey")),
            ],
            [
                sg.Listbox(
                    user_field_inputs,
                    size=(75, 5),
                    enable_events=True,
                    key="FIELD-INPUT-BOX",
                )
            ],
            [sg.T("")],
            [
                sg.T("Generate Interactive Map?:"),
                sg.Radio(
                    "Yes",
                    "INTERACTIVE-MAP",
                    default=True,
                    enable_events=True,
                    key="INT-MAP",
                ),
                sg.Radio(
                    "No",
                    "INTERACTIVE-MAP",
                    default=False,
                    enable_events=True,
                    key="INT-MAP-2",
                ),
            ],
            [sg.T("")],
            [
                sg.T("Choose a layout:", visible=True, key="INT-MAP-LAYOUT-TEXT"),
                sg.Combo(
                    ["Barnes Hut", "Force-Atlas-2-Based", "Hierarchical"],
                    default_value="Barnes Hut",
                    visible=True,
                    key="INT-MAP-LAYOUT",
                ),
            ],
            [sg.T("")],
            [
                sg.Button("Generate", disabled=True, button_color=("white", "grey")),
                sg.Button("Reset", disabled=True, button_color=("white", "grey")),
            ],
            [sg.T("")],
            [sg.T("Output:")],
            [sg.Output(size=(75, 7.5), key="OUTPUT-BOX")],
            [
                sg.Push(),
                sg.VPush(),
                sg.Button("Exit", button_color=("white", "firebrick3")),
            ],
        ]
        return sg.Window(
            "Network Map inc. Threat Detection via CSV",
            layout,
            element_justification="center",
            resizable=True,
            finalize=True,
        )

    def make_pool_popup(): # Appears when the "Pool via BRIM" button on the main window is pressed.
        layout = [
            [sg.Button("←", font="Helvetica 14"), sg.Push()],
            [sg.T("")],
            [sg.T("Use Suricata?:")],
            [
                # Determines whether to use Suricata in the map generation or not.
                sg.Radio("Yes", "USE SURICATA", default=True, key="USE-SURICATA"),
                sg.Radio("No", "USE SURICATA", default=False),
            ],
            [sg.T("")],
            [sg.Button("Continue", disabled=False, button_color=("white", "green"))],
            [
                sg.Push(),
                sg.VPush(),
                sg.Button("Exit", button_color=("white", "firebrick3")),
            ],
        ]
        return sg.Window(
            "Network Map inc. Threat Detection via BRIM Pool",
            layout,
            element_justification="center",
            size=(300, 300),
            finalize=True,
        )

    def make_pool_window(use_suricata): # Appears when the "Continue" button on the popup window is pressed.
        use_suricata_rev = not use_suricata
        layout = [
            [sg.Button("←", font="Helvetica 14"), sg.Push()],
            [sg.Push(), sg.Image(r"logo.png"), sg.Push()],
            [
                sg.Push(),
                sg.Text("NETWORK MAP INC. THREAT DETECTION", font="Helvetica 20"),
                sg.Push(),
            ],
            [
                sg.Push(),
                sg.T(
                    "Improving Network Security through Visualisation",
                    font="Helvetica 15",
                ),
                sg.Push(),
            ],
            [sg.T("")],
            [
                sg.Text(r"BRIM Pool Name (e.g. capture-1.pcap):", key="INPUT-LABEL"), # User defined BRIM pool name.
                sg.Input(key="INPUT-TEXT"),
                sg.Button("Submit"), # Submits the BRIM pool name to the listbox below.
            ],
            [sg.Text("Select a pool and click generate:")],
            [
                sg.Listbox(
                    user_inputs, size=(75, 5), enable_events=True, key="INPUT-BOX" # Selecting a BRIM pool name from the listbox enables the Generate button below.
                )
            ],
            [sg.T("")],
            [sg.T("Using Suricata?:")],
            [
                # Whether this radio is True/False is determined before this window even opens. The popup above which asks whether to use Suricata or not.
                # That popup passed a bool "use_suricata". If the BRIM pool does not contain any Suricata alerts, it will generate a non-Suricata based NetworkX map regardless of if this is True/False.
                # This value cannot be changed from within this window, the user must go back to the main menu and load the popup again.
                sg.Radio(
                    "Yes",
                    "THREAT-DETECTION",
                    default=use_suricata,
                    disabled=True,
                    key="THREAT-DET",
                ),
                sg.Radio(
                    "No", "THREAT-DETECTION", default=use_suricata_rev, disabled=True
                ),
            ],
            [sg.T("")],
            [
                sg.Button("Generate", disabled=True, button_color=("white", "grey")),
                sg.Button("Reset", disabled=True, button_color=("white", "grey")),
            ],
            [sg.T("")],
            [sg.T("Output:")],
            [sg.Output(size=(75, 7.5), key="OUTPUT-BOX")],
            [
                sg.Push(),
                sg.VPush(),
                sg.Button("Exit", button_color=("white", "firebrick3")),
            ],
        ]
        return sg.Window(
            "Network Map inc. Threat Detection via BRIM Pool",
            layout,
            element_justification="center",
            resizable=True,
            finalize=True,
        )

    # Loads only the main window upon running the script.
    main_window, pcap_window, csv_window, pool_popup, pool_window = (
        make_main_window(),
        None,
        None,
        None,
        None,
    )

    # ----------------Loop to handle events (e.g. button presses----------------#
    while True:
        window, event, values = sg.read_all_windows() # Reads every window for events.

        # Maximum records slider event handler.
        if window == main_window and event == "MAX-SLIDER":
            window["CHOOSE-SAMPLE-TEXT"].update(visible=True)
            window["SAMPLE-SLIDER"].update(
                range=(1, values["MAX-SLIDER"]), visible=True
            )
            window["SAMPLE-TEXT"].update(visible=True)

        # Sample records slider event handler.
        if window == main_window and event == "SAMPLE-SLIDER":
            if values["SAMPLE-SLIDER"] > 400:
                window["SAMPLE-TEXT"].update("Huge")
            elif (values["SAMPLE-SLIDER"] <= 400) and (values["SAMPLE-SLIDER"] > 200):
                window["SAMPLE-TEXT"].update("Large")
            elif (values["SAMPLE-SLIDER"] <= 200) and (values["SAMPLE-SLIDER"] > 100):
                window["SAMPLE-TEXT"].update("Medium")
            elif (values["SAMPLE-SLIDER"] <= 100) and (values["SAMPLE-SLIDER"] > 50):
                window["SAMPLE-TEXT"].update("Small")
            else:
                window["SAMPLE-TEXT"].update("Tiny")

        if (window == main_window) and (event == "PCAP" or event == "CSV" or event == "Pool via BRIM"):
            set_globals(values["MAX-SLIDER"], values["SAMPLE-SLIDER"]) # Sets the max record and sample record number global variables in map_common_functions.py.
            
        # Window loading and resetting event handler.
        if window == main_window or event == "Reset":
            user_inputs = []
            user_field_inputs = []
            if event == "PCAP" or window == pcap_window:
                window.hide()
                pcap_window = make_pcap_window()
                pcap_window.maximize()
            elif event == "CSV" or window == csv_window:
                window.hide()
                csv_window = make_csv_window()
                csv_window.maximize()
            elif event == "Pool via BRIM" or window == pool_popup:
                window.hide()
                pool_popup = make_pool_popup()

        # Popup window event handler.
        if event == "Continue" and window == pool_popup:
            if values["USE-SURICATA"] == True:
                window.hide()
                pool_window = make_pool_window(True) # Passes True to use_suricata parameter.
                pool_window.maximize()
            else:
                window.hide()
                pool_window = make_pool_window(False)
                pool_window.maximize()

        # Submit button event handler for any window containing a Submit button.
        if (
            event == "Submit"
            and values["INPUT-TEXT"] != ""
            and values["INPUT-TEXT"] not in user_inputs
        ):
            window["Generate"].update(
                disabled=True, button_color=("#000000", "#8a8a84")
            )
            window["Reset"].update(disabled=False, button_color=("white", "green"))
            user_inputs.append(values["INPUT-TEXT"])
            window["INPUT-BOX"].update(user_inputs)
            window["INPUT-TEXT"].update("")

        # Event handler for when a value is selected in the input-box listbox.
        if event == "INPUT-BOX" and user_inputs != []:
            if window == pool_window:
                window["Generate"].update(
                    disabled=False, button_color=("white", "green")
                )
            else:
                window["Enter"].update(disabled=False, button_color=("white", "green"))
                window["Clear"].update(disabled=False, button_color=("white", "green"))
                if len(user_field_inputs) == 3:
                    window["Generate"].update(
                        disabled=False, button_color=("white", "green")
                    )
                    window["Enter"].update(
                        disabled=True, button_color=("#000000", "#8a8a84")
                    )
                else:
                    window["Enter"].update(
                        disabled=False, button_color=("white", "green")
                    )
                    
        # Generate button event handler for every window containing a Generate button.
        if event == "Generate":
            window["OUTPUT-BOX"].update("")
            if window == pool_window and values["THREAT-DET"] == False:
                generate_map_from_pool(
                    values["INPUT-BOX"][0], "proto", "id.orig_h", "id.resp_h"
                )
            elif window == pool_window and values["THREAT-DET"] == True:
                generate_threat_map_from_pool(
                    values["INPUT-BOX"][0], "proto", "id.orig_h", "id.resp_h"
                )
            elif window == csv_window:
                if values["INT-MAP"] == True:
                    generate_map_from_file(
                        values["INPUT-BOX"][0],
                        "CSV",
                        True,
                        values["INT-MAP-LAYOUT"],
                        user_field_inputs[0],
                        user_field_inputs[1],
                        user_field_inputs[2],
                    )
                else:
                    generate_map_from_file(
                        values["INPUT-BOX"][0],
                        "CSV",
                        False,
                        values["INT-MAP-LAYOUT"],
                        user_field_inputs[0],
                        user_field_inputs[1],
                        user_field_inputs[2],
                    )
            elif window == pcap_window:
                if values["INT-MAP"] == True:
                    generate_map_from_file(
                        values["INPUT-BOX"][0],
                        "PCAP",
                        True,
                        values["INT-MAP-LAYOUT"],
                        user_field_inputs[0],
                        user_field_inputs[1],
                        user_field_inputs[2],
                    )
                else:
                    generate_map_from_file(
                        values["INPUT-BOX"][0],
                        "PCAP",
                        False,
                        values["INT-MAP-LAYOUT"],
                        user_field_inputs[0],
                        user_field_inputs[1],
                        user_field_inputs[2],
                    )

        # Interactive map event handler for the PCAP and CSV windows.
        if (
            (window == pcap_window or window == csv_window)
            and event == "INT-MAP"
            or event == "INT-MAP-2"
        ):
            if values["INT-MAP"] == True:
                window["INT-MAP-LAYOUT-TEXT"].update(visible=True)
                window["INT-MAP-LAYOUT"].update(visible=True)
            elif values["INT-MAP"] == False:
                window["INT-MAP-LAYOUT-TEXT"].update(visible=False)
                window["INT-MAP-LAYOUT"].update(visible=False)

        # PCAP and CSV window event handler for clearing user inputs and enabling/disabling related buttons.
        if (
            (window == pcap_window or window == csv_window)
            and event == "INPUT-BOX"
            or event == "Clear"
        ):
            window["Generate"].update(
                disabled=True, button_color=("#000000", "#8a8a84")
            )
            window["Enter"].update(disabled=False, button_color=("white", "green"))
            user_field_inputs = []
            window["FIELD-INPUT-BOX"].update(user_field_inputs)
        if (
            (window == pcap_window or window == csv_window)
            and event == "Enter"
            and values["FIELD-INPUT-TEXT"] != ""
            and values["FIELD-INPUT-TEXT"] not in user_field_inputs
        ):
            if len(user_field_inputs) < 3:
                user_field_inputs.append(values["FIELD-INPUT-TEXT"])
                window["FIELD-INPUT-BOX"].update(user_field_inputs)
                window["FIELD-INPUT-TEXT"].update("")
            if len(user_field_inputs) == 3:
                window["Generate"].update(
                    disabled=False, button_color=("white", "green")
                )
                window["Enter"].update(
                    disabled=True, button_color=("#000000", "#8a8a84")
                )

        # Back button handler for any window containing a back button.
        if event == "←":
            user_inputs = []
            window.hide()  # back button clicked
            main_window = make_main_window()

        # Exit button handler for any window containing a 
        if event == "Exit" or event == sg.WIN_CLOSED:
            break

    window.close()


# ----------------Execute launcher function upon running script----------------#
if __name__ == "__main__":
    launcher()
