package os.hw3;

import java.util.ArrayList;

/**
 * Represents a directory file within a FAT32 file system.
 * Author: Jeffrey Hagler, David Mandelbaum
 */
public class Directory
{
    /*
        Info from file table
     */
    private byte[] DIR_Name = new byte[11];//short name - 0 -> 11
    private byte[] DIR_Attr = new byte[1];//file attributes - 11 -> 12
    //private byte[] DIR_NTRes = new byte[1];//used by WindowsNT (?) - 12 -> 13
    private byte[] CrtTimeTenth = new byte[1];//Millisecond stamp at file creation time (count of tenths of a second) - 13 ->14
    private byte[] DIR_CrtTime = new byte[2];//Time file was created - 14 -> 16
    private byte[] DIR_CrtDate = new byte[2];//Date file was created - 16 -> 18
    private byte[] DIR_LstAccDate = new byte[2];//Last access date - 18 ->20
    private byte[] DIR_FstClusHI = new byte[2];//High word of this entry’s first cluster number - 20 -> 22
    private byte[] DIR_WrtTime = new byte[2];//Time of last write - 22 -> 24
    private byte[] DIR_WrtDate = new byte[2];//Date of last write - 24 -> 26
    private byte[] DIR_FstClusLO = new byte[2];//Low word of this entry’s first cluster number - 26 -> 28
    private byte[] DIR_FileSize = new byte[4];//32-bit DWORD holding this file’s size in bytes. - 28-31

    /*
         Info needed for this file system
      */
    private String pathToHere;
    private String name;
    private Directory parentDirectory;
    private boolean containsFiles;//see if it holds files
    private ArrayList<Directory> files;
    /*
        Stats to print
     */
    private int size;
    private String attributes;
    private String nextClusterNum;
    /*
        Field for reading
     */
    private byte[] text;
}
