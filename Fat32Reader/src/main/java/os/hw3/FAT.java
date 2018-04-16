package os.hw3;

import java.io.FileInputStream;
import java.io.IOException;

class FAT {
    byte[] fatTable;
    int fatSize;

    FAT(FileInputStream fis, Fat32Reader fr, int address) throws IOException
    {
        fis.skip(address - fr.currentLocation);
        fatSize = fr.boot.getBPB_BytesPerSec();
        fatTable = new byte[fatSize];
        fis.read(fatTable, 0, fatSize);
        fr.currentLocation = address + fatSize;
    }

    int getNextCluster(int fatSecNum, int fatEntOffset)
    {
        byte nextClus = fatTable[fatSecNum * 32];
        String nextClusString = String.format("%8s", Integer.toBinaryString(nextClus & 0xFF)).replace(' ', '0');//https://stackoverflow.com/questions/12310017/how-to-convert-a-byte-to-its-binary-string-representation
        return Integer.parseInt(nextClusString, 16);
    }


}
