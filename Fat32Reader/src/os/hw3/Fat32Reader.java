package os.hw3;

import java.io.*;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/************************************************************
 *   Name of program: Fat32Reader
 *   Authors: David Mandelbaum
 *   Description: a program that supports file system commands (from specs)
 **********************************************************/
public class Fat32Reader {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    FileHandler fh;

    private String header;

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
//        byte[] buffer = new byte[5];
//        //int result = fis.read(buffer);
//       if(result != -1)
//      {
//        System.out.println("Buffer: " + buffer);
//        fis.read(buffer, 11, 2);
//        String bytesPerSec = new String(buffer);
//        System.out.println("BPB_BytesPerSec: " + bytesPerSec);//TEST
//        }

        /* Get root directory address */
            //printf("Root addr is 0x%x\n", root_addr);

        /* Main loop.  You probably want to create a helper function for each command besides quit. */
        Scanner s = new Scanner(System.in);
        String input;
        String[] inputParts;
        while(true) {
            //bzero(cmd_line, MAX_CMD);
            //System.out.println("/]");
            System.out.print(fr.getHeader());//print prompt
            //fgets(cmd_line, MAX_CMD, stdin);
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
                    System.out.println("Going to ls.");
                    fr.ls(fName);
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
                    System.out.println("Going to display info.");
                    fr.getInfo();
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

    /**
     * Prints out the following info (in both hex and base 10)
     * BPB_BytesPerSec (512, 1024, 2048 or 4096 - offset 11 bytes, size 2 bytes)
     * BPB_SecPerClus (legal values are 1, 2, 4, 8, 16, 32, 64, and 128 - offset 13 bytes, size 1 bytes)
     * BPB_RsvdSecCnt (typically 32, cannot be 0 - offset 14 bytes, size 2 bytes)
     * BPB_NumFATS (standard value is 2 - offset 16 bytes, size 1 bytes)
     * BPB_FATSz32 (offset 36 bytes, size 4 bytes)
     */
    private void getInfo()
    {
//        BPB_BytesPerSec
//        BPB_SecPerClus
//        BPB_RsvdSecCnt
//        BPB_NumFATS
//        BPB_FATSz32
        //convert to hex: Integer.toHexString();

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
            //list current directory
        }
        else if(dName.equals(".."))
        {
            //list from one directory up
        }
        else
        {
            //list directory
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
                //TODO:
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
}
