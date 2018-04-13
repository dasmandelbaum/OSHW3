package os.hw3;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/************************************************************
 *   Name of program: Fat32Reader
 *   Authors: Jeffrey Hagler, David Mandelbaum
 *   Description: a program that supports file system commands (from specs)
 **********************************************************/
public class Fat32Reader {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    FileHandler fh;
    private String header;
    private Boot boot;
    private Directory fs;
    private HashMap<String, Directory> openFiles;
    private String volumeName;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Fat32Reader() throws IOException
    {
        LogManager.getLogManager().reset();//https://stackoverflow.com/a/3363747
        LOGGER.setLevel(Level.INFO);
        fh = new FileHandler("fat32.log");
        LOGGER.addHandler(fh);
        setHeader("/]");
        boot = new Boot();
        fs = new Directory();
        openFiles = new HashMap<String, Directory>();
    }

    public static void main(String[] args) throws IOException
    {
        Fat32Reader fr = new Fat32Reader();

        /* Parse args and open our image file */
        File file = new File(args[0]);
        System.out.println("File exists: " + file.exists());//TEST
        System.out.println("Fat32 file path: " + file.getAbsolutePath());//TEST
        FileInputStream fis = new FileInputStream(file);
        /* Parse boot sector and get information */
        fr.boot.setInfo(fis);

        /* Get root directory address */
        fr.boot.setRootDirAddress((fr.boot.getBPB_NumFATS() * fr.boot.getBPB_FATSz32()) + fr.boot.getBPB_RsvdSecCnt());
        int startOfRootDirectory = (fr.boot.getRootDirAddress() * 512);
        System.out.println("rootDirAddress is 0x" + Integer.toHexString(fr.boot.getRootDirAddress()) + ", " + fr.boot.getRootDirAddress());
        System.out.println("Skipping to " + (startOfRootDirectory - 40));
        fis.skip(startOfRootDirectory - 40);//subtract current location in boot
        fr.parseDirectories(fis);

        /* Main loop.  You probably want to create a helper function for each command besides quit. */
        Scanner s = new Scanner(System.in);
        String input;
        String[] inputParts;
        while(true) {
            //System.out.println("/]");
            System.out.print(fr.getHeader());//print prompt
            input = s.nextLine().toLowerCase();
            inputParts = input.split(" ");
            String command = inputParts[0];
            /* Start comparing input */
            if(inputParts.length == 2)
            {
                String fName = inputParts[1];
                if (command.equals("open"))
                {
                    System.out.println("Going to open!");
                    fr.open(fName);
                }
                else if (command.equals("close"))
                {
                    System.out.println("Going to close!");
                    fr.close(fName);
                }
                else if (command.equals("size"))
                {
                    System.out.println("Going to size!");
                    fr.size(fName);
                }
                else if (command.equals("cd"))
                {
                    System.out.println("Going to cd!");
                    fr.cd(fName);
                }
                else if (command.equals("ls"))
                {
                    //System.out.println("Going to ls.");
                    fr.ls(fName);
                }
                else if(command.equals("stat"))
                {
                    //System.out.println("Going to stat!");
                    fr.stat(fName);
                }
                else
                {
                    System.out.println("Unrecognized command.");
                }
            }
            else if(inputParts.length == 4 && command.equals("read"))
            {
                System.out.println("Going to read!");
                fr.read(inputParts[1], inputParts[2], inputParts[3]);
            }
            else if(inputParts.length == 1)
            {
                if (command.equals("info"))
                {
                    //System.out.println("Going to display info.");//TEST
                    fr.printInfo();
                }
                else if(command.equals("volume"))
                {
                    //System.out.println("Going to print volume.");//TEST
                    fr.volume();
                }
                else if (command.equals("quit"))
                {
                    System.out.println("Quitting.");
                    break;
                }
                else
                {
                    System.out.println("Unrecognized command.");
                }
            }
            else
                {
                System.out.println("Unrecognized command.");
            }
        }


        /* Close the file */


        /* Success */
    }

    private void parseDirectories(FileInputStream fis) throws IOException {
        //start with root
        Directory dir = parseDirectory(fis);
        this.fs = dir;
        //for any other directory listed after root
        for(int i = 0; i < 5; i++)//TODO: change this from 5
        {
            fis.skip(32);
            dir = parseDirectory(fis);
            this.fs.files.add(dir);
        }
    }

    private Directory parseDirectory(FileInputStream fis) throws IOException
    {
        Directory dir = new Directory();
        //byte[] buffer = new byte[32];
        //fis.read(buffer, 0, 32);
        byte[] DIR_Name = new byte[11];//short name - 0 -> 11
        fis.read(DIR_Name, 0, 11);
        String byteString = new String(DIR_Name, "UTF-8");//https://stackoverflow.com/a/18583290
        System.out.println(byteString);
        String[] splitName = byteString.split(" +");
        if(splitName.length == 2)
        {
            dir.name = splitName[0] + "." + splitName[1];
            dir.name = dir.name.toLowerCase();
        }
        else
        {
            dir.name = byteString;
        }
        System.out.println("name: " + dir.name);


        byte[] DIR_Attr = new byte[1];//file attributes - 11 -> 12
        fis.read(DIR_Attr,0,1);
        String temp = "";
        for(int i = DIR_Attr.length - 1; i >= 0; i--)
        {
            byte b = DIR_Attr[i];
            //System.out.printf("%02X\n", b);//https://stackoverflow.com/a/1748044//TEST
            temp += b;//String.format("%02X", b);
        }
        System.out.println("attribute: " + temp);
        //TODO: what about longname?
        if(temp.equals("1"))//root
        {
            dir.attributes = "ATTR_READ_ONLY";
        }
        else if(temp.equals("2"))//root
        {
            dir.attributes = "ATTR_HIDDEN";
        }
        else if(temp.equals("4"))//root
        {
            dir.attributes = "ATTR_SYSTEM";
        }
        else if(temp.equals("8"))//root - TODO: Make . and .. directories and add it?
        {
            dir.attributes = "ATTR_VOLUME_ID";
            this.volumeName = byteString.substring(0, byteString.indexOf(" "));
        }
        else if(temp.equals("16"))//root
        {
            dir.attributes = "ATTR_DIRECTORY";
        }
        else if(temp.equals("32"))
        {
            dir.attributes = "ATTR_ARCHIVE";
        }

        /*byte[] DIR_NTRes = new byte[1];//used by WindowsNT (?) - 12 -> 13
        byte[] CrtTimeTenth = new byte[1];//Millisecond stamp at file creation time (count of tenths of a second) - 13 ->14
        byte[] DIR_CrtTime = new byte[2];//Time file was created - 14 -> 16
        byte[] DIR_CrtDate = new byte[2];//Date file was created - 16 -> 18
        byte[] DIR_LstAccDate = new byte[2];//Last access date - 18 ->20*/

        byte[] DIR_FstClusHI = new byte[2];//High word of this entry’s first cluster number - 20 -> 22
        String hi = getValue(fis, DIR_FstClusHI, 8, 2);

        /*byte[] DIR_WrtTime = new byte[2];//Time of last write - 22 -> 24
        byte[] DIR_WrtDate = new byte[2];//Date of last write - 24 -> 26*/
        byte[] DIR_FstClusLO = new byte[2];//Low word of this entry’s first cluster number - 26 -> 28
        String lo = getValue(fis, DIR_FstClusLO, 4, 2);
        String clus = lo.concat(hi);
        dir.nextClusterNumber = Integer.parseInt(clus, 16);
        System.out.println("next cluster number: " + dir.nextClusterNumber);

        byte[] DIR_FileSize = new byte[4];//32-bit DWORD holding this file’s size in bytes. - 28-32
        temp = getValue(fis, DIR_FileSize, 0, 4);
        System.out.println("File size: " + temp);
        dir.size = Integer.parseInt(temp, 16);
        return dir;
    }


    private String getValue(FileInputStream fis, byte[] buffer, int skip, int size) throws IOException {
        fis.skip(skip);
        fis.read(buffer, 0, size);
        String temp = "";
        for(int i = buffer.length - 1; i >= 0; i--)
        {
            byte b = buffer[i];
            //System.out.printf("%02X\n", b);//https://stackoverflow.com/a/1748044//TEST
            temp += String.format("%02X", b);
        }
        return temp;
    }

    /**
     * Prints out the following info (in both hex and base 10 - saved in fields as base 10)
     * BPB_BytesPerSec (512, 1024, 2048 or 4096 - offset 11 bytes, size 2 bytes)
     * BPB_SecPerClus (legal values are 1, 2, 4, 8, 16, 32, 64, and 128 - offset 13 bytes, size 1 bytes)
     * BPB_RsvdSecCnt (typically 32, cannot be 0 - offset 14 bytes, size 2 bytes)
     * BPB_NumFATS (standard value is 2 - offset 16 bytes, size 1 bytes)
     * BPB_FATSz32 (offset 36 bytes, size 4 bytes)
     */
    private void printInfo()
    {
        System.out.println("BPB_BytesPerSec is 0x" + Integer.toHexString(this.boot.getBPB_BytesPerSec()) + ", " + this.boot.getBPB_BytesPerSec());
        System.out.println("BPB_SecPerClus is 0x" +  Integer.toHexString(this.boot.getBPB_SecPerClus()) + ", " + this.boot.getBPB_SecPerClus());
        System.out.println("BPB_RsvdSecCnt is 0x" + Integer.toHexString(this.boot.getBPB_RsvdSecCnt()) + ", " + this.boot.getBPB_RsvdSecCnt());
        System.out.println("BPB_NumFATS is 0x" + Integer.toHexString(this.boot.getBPB_NumFATS()) + ", " + this.boot.getBPB_NumFATS());
        System.out.println("BPB_FATSz32 is 0x" + Integer.toHexString(this.boot.getBPB_FATSz32()) + ", " + this.boot.getBPB_FATSz32());
    }

    /**
     * adds a file named FILE_NAME in the present working directory to an open-file table in your program.
     * A file may only be read from if it is opened first.
     * Log an error to a file fat32.log if the file is already open, or if the file does not exist.
     * @param fName
     */
    private void open(String fName)
    {
        if(!exists(fName))
        {
            LOGGER.log(Level.WARNING, "File " + fName + " does not exist.");
            System.out.println("Error: does not exist");
        }
        else if(isOpen(fName))
        {
            LOGGER.log(Level.WARNING, "File " + fName + " is already open.");
            System.out.println("Error: already open");
        }
        else if(isDirectory(fName))
        {
            LOGGER.log(Level.WARNING, "File " + fName + " is a directory and cannot be opened.");
            System.out.println("Error: cannot open a directory");
        }
        else //exists and is not yet open nor is directory
        {
            //open file
        }

    }



    /**
     * removes a file from the open-file table.
     * Log an error if the file is not open.
     * @param fName
     */
    private void close(String fName)
    {
        if(isOpen(fName))
        {
            //close file
        }
        else
        {
            LOGGER.log(Level.WARNING,"File " + fName + " not in open file table.");
            System.out.println("Error: file not in open file table");
        }
    }

    /**
     * prints the size of file FILE_NAME in the present working directory.
     * Log an error if FILE_NAME does not exist.
     * @param fName
     */
    private void size(String fName)
    {
        if(exists(fName))
        {
            //print size
        }
        else
        {
            LOGGER.log(Level.WARNING,"File " + fName + " does not exist.");
            System.out.println("Error: file does not exist");
        }
    }

    /**
     * changes the present working directory to DIR_NAME.
     * Log an error if the directory does not exist.
     * DIR_NAME may be “.” (here) and “..” (up one directory).
     * @param dName
     */
    private void cd(String dName)
    {
        if(!exists(dName))
        {
            LOGGER.log(Level.WARNING, dName + " does not exist.");
            System.out.println("Error: does not exist");
        }
        else if(isDirectory(dName))
        {
            setHeader("/" + dName + getHeader());
            //change wd to fName
        }
        else
        {
            LOGGER.log(Level.WARNING, dName + " is not a directory.");
            System.out.println("Error: not a directory");
        }
    }

    /**
    * lists the contents of DIR_NAME, including “.” and “..”
    * @param dName
     */
    private void ls(String dName)
    {
        if(dName.equals("."))
        {
            //list current directory contents
            for(Directory dir : fs.files)//TODO: change this - test
            {
                System.out.print(dir.name + "\t");
            }
            System.out.println();
        }
        else if(dName.equals(".."))
        {
            //list contents from one directory up
        }
        else
        {
            //list directory contents

        }
    }

    /**
     * reads from a file named FILE_NAME, starting at POSITION, and prints NUM_BYTES.
     * Return an error when trying to read an unopened file.
     * @param fName
     * @param position
     * @param num_bytes
     */
    private void read(String fName, String position, String num_bytes)
    {
        if(isOpen(fName))
        {
            //read file
            try
            {
                //TODO: take file contents and print them out
//                RandomAccessFile raf = new RandomAccessFile(getFile(fName), "r");
//                raf.read();
            }
            catch(Exception E)
            {
                LOGGER.log(Level.WARNING,"File " + fName + " error: attempt to read beyond EoF.");
                System.out.println("Error: attempt to read beyond EoF");
            }
        }
        else
        {
            LOGGER.log(Level.WARNING,"File " + fName + " is closed.");
            System.out.println("Error: file not in open file table");
        }
    }

    /**
     * Check to see if file exists or not
     * @param fName
     * @return
     */
    private boolean exists(String fName)
    {
        return false;
    }

    /**
     * Check to see if file is open already or not
     * @param fName
     * @return
     */
    private boolean isOpen(String fName)
    {
       return false;
    }

    /**
     * sees if file name is a directory or not
     * @param fName
     * @return
     */
    private boolean isDirectory(String fName) {
        return false;
    }

    private void stat(String fName)
    {
        System.out.println("Retrieving stats.");//TEST
        if(fName.toLowerCase().equals(this.volumeName.toLowerCase()))
        {
            System.out.println("Size is " + fs.size);
            System.out.println("Attributes " + fs.attributes);
            System.out.println("Next cluster number is 0x" + Integer.toHexString(fs.nextClusterNumber));
        }
    }

    private void volume()
    {
        System.out.println("Retrieving volume.");//TEST
        System.out.println("Volume name: " + this.volumeName);
    }

    private int getFirstSectorOfCluster(int n)
    {
        return (((n - 2) * this.boot.getBPB_SecPerClus()) + this.boot.getRootDirAddress());
    }

}
