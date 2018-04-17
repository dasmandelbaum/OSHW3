package os.hw3;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

class FAT {
    byte[] fatTable;
    int fatSize;

    FAT(RandomAccessFile raf, Fat32Reader fr, int address) throws IOException
    {
  //      fis.skip(address - fr.currentLocation);
      	raf.seek(address);
        fatSize = fr.boot.getBPB_BytesPerSec();
        fatTable = new byte[fatSize];
        raf.read(fatTable, 0, fatSize);
        fr.currentLocation = address + fatSize;

    }

    ArrayList<Integer> getClusters(int fatSecNum, int fatEntOffset, int address)
    {
        ArrayList<Integer> clusters = new ArrayList<Integer>();
        //go to beginning of fat
        //raf.seek(address);
        //for current file/directory, get arraylist of cluster numbers related to that file/directory
        byte nextClus = fatTable[fatSecNum * 32];
        String nextClusString = String.format("%8s", Integer.toBinaryString(nextClus & 0xFF)).replace(' ', '0');//https://stackoverflow.com/questions/12310017/how-to-convert-a-byte-to-its-binary-string-representation
        //return Integer.parseInt(nextClusString, 16);
        return clusters;
    }


}