#----Importing Modules----#
import os, sys, hashlib
from tabulate import tabulate

#----Extracting the file names from within the given directory path into a file list----#
def ListFiles(Directory_Path):
    if os.path.isdir(Directory_Path) == True:
        print("\n✓ This directory is valid. Generating MD5 and SHA-1 hash values from each of the files within...\n")
        File_List = sorted(os.listdir(Directory_Path))
        if len(File_List) > 0:
            GenerateHashList(Directory_Path, File_List)
        else:
            print("\n✗ This directory contains no files, try again.\n")
    else:
        print("\n✗ This directory does not exist, try again.\n")

#----Generating the hash list and using the tabulate module to format the list----#
#Note: The number of bytes that is read into memory in a single read operation. This is used solarger
#files are not completely loaded into memory before computing the hash.
def GenerateHashList(Directory_Path, File_List):
    Hash_List = []
    Block_Size = 65536
    MD5_Hash = hashlib.md5()
    SHA1_Hash = hashlib.sha1()
    File_Count = 0
    try:
        for File_Name in File_List:
            with open(os.path.join(Directory_Path, File_Name), 'rb') as File:
                File_Count = File_Count + 1
                Read_File = File.read(Block_Size)
                while len(Read_File) > 0:
                    MD5_Hash.update(Read_File)
                    SHA1_Hash.update(Read_File)
                    Read_File = File.read(Block_Size)
                    Hash_List.append([File_Count, File_Name, MD5_Hash.hexdigest(),
                    SHA1_Hash.hexdigest()])
                    print(tabulate(Hash_List, headers = ["UNIQUE ID", "FILE NAME", "MD5 HASH", "SHA1 HASH"]))
    except:
        print("\n✗ This directory contains no files, try again.\n")
    ListFiles(sys.argv[1]) 