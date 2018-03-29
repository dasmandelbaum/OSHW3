package os.hw3;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/************************************************************
 *   Name of program: Fat32Reader
 *   Authors: David Mandelbaum
 *   Description: a program that supports file system commands (from specs)
 *   logging help from: http://www.vogella.com/tutorials/Logging/article.html
 *   **********************************************************/
public class Fat32Reader {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Fat32Reader()
    {
        LOGGER.setLevel(Level.INFO);
    }

    public static void main(String[] args) {
        Fat32Reader fr = new Fat32Reader();
        /* Parse args and open our image file */


        /* Parse boot sector and get information */


        /* Get root directory address */
            //printf("Root addr is 0x%x\n", root_addr);

        /* Main loop.  You probably want to create a helper function for each command besides quit. */
        Scanner s = new Scanner(System.in);
        String input;
        String[] inputParts;
        while(true) {
            //bzero(cmd_line, MAX_CMD);
            //System.out.println("/]");
            System.out.print("/]");
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
        //convert to hex: Integer.toHexString();//https://stackoverflow.com/a/26738067

    }

    /**
     * adds a file named FILE_NAME in the present working directory to an open-file table in your program.
     * A file may only be read from if it is opened first.
     * Log an error to a file fat32.log if the file is already open, or if the file does not exist.
     * @param fName
     */
    private void open(String fName)
    {
        if(!isOpen(fName) && exists(fName))
        {
            //open file
        }
        else
        {
            LOGGER.warning("File " + fName + " is closed or does not exist.");
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
            LOGGER.warning("File " + fName + " is already closed.");
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
            LOGGER.warning("File " + fName + " does not exist.");
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
        if(exists(dName))
        {
            //change wd to fName
        }
        else
        {
            LOGGER.warning("Directory " + dName + " does not exist.");
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
        }
        else
        {
            LOGGER.warning("File " + fName + " is closed.");
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

}
