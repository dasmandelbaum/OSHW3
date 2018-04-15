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

}
