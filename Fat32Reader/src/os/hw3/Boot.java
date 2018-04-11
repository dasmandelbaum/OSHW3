package os.hw3;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class contains important fields from the boot sector of the FAT32 file system.
 * Author: Jeffrey Hagler, David Mandelbaum
 */
class Boot
{
    //boot fields
    private int BPB_BytesPerSec;
    private int BPB_SecPerClus;
    private int BPB_RsvdSecCnt;
    private int BPB_NumFATS;
    private int BPB_FATSz32;
    private int rootDirAddress;

    /**
     * Set the BPB data fields
     * @param fis
     * @throws IOException
     */
    protected void setInfo(FileInputStream fis) throws IOException
    {
        BPB_BytesPerSec = setValue(fis, 11, 2, 11);
        BPB_SecPerClus = setValue(fis, 13, 1, 0);
        BPB_RsvdSecCnt = setValue(fis, 14, 2, 0);
        BPB_NumFATS = setValue(fis, 16, 1, 0);
        BPB_FATSz32 = setValue(fis, 36, 4, 19);
    }

    /**
     * given a file stream, length of the field, and amount to skip (can be zero), return the integer value of the field
     * @param fis
     * @param offset
     * @param length
     * @param skip
     * @return
     * @throws IOException
     */
    private int setValue(FileInputStream fis, int offset, int length, int skip) throws IOException
    {
        byte[] buffer = new byte[length];
        //System.out.println("Buffer size: " + buffer.length);//TEST
        fis.skip(skip);
        fis.read(buffer, 0, length);
        String temp = "";
        //turn into hex string
        for(int i = buffer.length - 1; i >= 0; i--)
        {
            byte b = buffer[i];
            //System.out.printf("0x%02X\n", b);//https://stackoverflow.com/a/1748044//TEST
            temp += String.format("%02X", b);
        }
        //System.out.println("0x" + temp);//TEST
        int value = Integer.parseInt(temp, 16);
        //System.out.println("integer: " + value);//TEST
        //String value2 = Integer.toHexString(value);
        //value2 = "0x" + value2;
        //System.out.println("hex string: " + value2);//TEST
        return value;
    }

    public int getRootDirAddress() {
        return rootDirAddress;
    }

    public void setRootDirAddress(int rootDirAddress) {
        this.rootDirAddress = rootDirAddress;
    }

    public int getBPB_FATSz32() {
        return BPB_FATSz32;
    }

    public void setBPB_FATSz32(int BPB_FATSz32) {
        this.BPB_FATSz32 = BPB_FATSz32;
    }

    public int getBPB_NumFATS() {
        return BPB_NumFATS;
    }

    public void setBPB_NumFATS(int BPB_NumFATS) {
        this.BPB_NumFATS = BPB_NumFATS;
    }

    public int getBPB_RsvdSecCnt() {
        return BPB_RsvdSecCnt;
    }

    public void setBPB_RsvdSecCnt(int BPB_RsvdSecCnt) {
        this.BPB_RsvdSecCnt = BPB_RsvdSecCnt;
    }

    public int getBPB_SecPerClus() {
        return BPB_SecPerClus;
    }

    public void setBPB_SecPerClus(int BPB_SecPerClus) {
        this.BPB_SecPerClus = BPB_SecPerClus;
    }

    public int getBPB_BytesPerSec() {
        return BPB_BytesPerSec;
    }

    public void setBPB_BytesPerSec(int BPB_BytesPerSec) {
        this.BPB_BytesPerSec = BPB_BytesPerSec;
    }
}
