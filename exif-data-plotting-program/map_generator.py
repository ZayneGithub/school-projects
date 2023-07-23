#!/usr/bin/python

#----Importing modules specified in Task 1.2 Requirements----#
import os, sys, folium
from exif import Image

#----Extracting the file names from within the given directory path into a file list----#
def ListFiles(Directory_Path):
    if os.path.isdir(Directory_Path) == True:
        print("\n✓ This directory is valid. Extracting the Latitude, Longitude and Date/Time metadata from each of the files within...\n")
        File_List = sorted(os.listdir(Directory_Path))
        ExtractGPS(Directory_Path, File_List)
    else:
        print("\n✗ This directory does not exist, try again.\n")

#----Extracting the GPS & Date/Time metadata from each file within the file list into a data list----#
def ExtractGPS(Directory_Path, File_List):
    Data_List = []
    for File_Name in File_List:
        try:
            with open(os.path.join(Directory_Path, File_Name), 'rb') as File:
                EXIF = Image(File)
            if hasattr(EXIF, "gps_latitude") == True and hasattr(EXIF, "gps_longitude") and hasattr(EXIF, "datetime_original") == True:
                Data_List.append([File_Name,ConvertDMS(EXIF.gps_latitude, EXIF.gps_latitude_ref),ConvertDMS(EXIF.gps_longitude, EXIF.gps_longitude_ref),EXIF.datetime_original])
                print ("✓ Latitude, Longitude and Date/Time extracted from:", File_Name)
            else:
                print("✗ Latitude, Longitude and Date/Time could not be extracted from:", File_Name)
        except:
            print("✗ This file seems to be an invalid type or corrupted:", File_Name)
    if len(Data_List) != 0:
        GenerateMap(Directory_Path, Data_List)
    else:
        print("\n✗ This directory contains no files with EXIF data, try again.\n")

#----Converting the GPS metadata from decimal, minutes, second (DMS) format to single decimalvalue format----#
#Note: The latitude/longitude extracted using the exif module is a tuple and is already
#in decimal format but not a single decimal value, e.g. (51.0, 30.0, 6.39).

def ConvertDMS(DMS, Ref):
    D, M, S = DMS #Python can unpack tuples/sequences naturally.
    DEC = D + (M/60.0) + (S/3600.0)
    if (Ref == 'W'):
        DEC = 0 - DEC
    if (Ref == 'S'):
        DEC = 0 - DEC
    return (DEC)

#----Generating the folium map and plotting the markers and lines by sorting the data list by date/time----#
#Note: The data list needs to be sorted by the date/time taken in order to accurately
#track the movement of the tracking target.
#Note: The data list needs to be split into separate lists for use.

def GenerateMap(Directory_Path, Data_List):
    Data_List_Sorted = sorted(Data_List, key = lambda x: x[3]) #Sorting by date/time, which is stored in index 3.
    File_Name_List = [i[0] for i in Data_List_Sorted] #Splitting the sorted data list into separate lists.
    Coordinate_List = [[i[1],i[2]] for i in Data_List_Sorted]
    DateTime_List = [i[3] for i in Data_List_Sorted]

    folium_map = folium.Map(location = Coordinate_List[0], zoom_Start = 16)
    folium.PolyLine(Coordinate_List, color="red", weight=2.5, opacity=1).add_to(folium_map)
    for count in range(len(File_Name_List)):
        File_Name = str(File_Name_List[count])
        File_Path = os.path.join(Directory_Path, File_Name)
        popup_html = """
            <h3>""" + File_Name + """</h3>
            <p>Co-ordinates: """ + str(Coordinate_List[count]) + """</p>
            <p>Date/time: """ + str(DateTime_List[count]) + """</p>
            <img src = """ + File_Path + """ width = '300'>
        """
        popup = folium.Popup(popup_html, max_width = 500)
        if count == 0:
            folium.Marker(Coordinate_List[count], popup = popup, icon =
            folium.Icon(icon = 'home', prefix = 'fa')).add_to(folium_map)
        elif count == (len(File_Name_List)-1):
            folium.Marker(Coordinate_List[count], popup = popup, icon =
            folium.Icon(icon = 'flag', prefix = 'fa')).add_to(folium_map)
        else:
            folium.Marker(Coordinate_List[count], popup = popup, icon =
            folium.Icon(icon = 'thumb-tack', prefix = 'fa')).add_to(folium_map)
    folium_map.save(outfile = 'Task_1_Map.html')
    print("\nTask_1_Map.html generated...")

ListFiles(sys.argv[1])
