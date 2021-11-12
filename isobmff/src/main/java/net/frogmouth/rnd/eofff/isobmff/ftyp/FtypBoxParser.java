package net.frogmouth.rnd.eofff.isobmff.ftyp;

import java.nio.ByteBuffer;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;

public class FtypBoxParser extends BoxParser {

    public FtypBoxParser() {}
    
    @Override
    public String getFourCC() {
        return "ftyp";
    }

    @Override
    public Box parse(ByteBuffer byteBuffer, long initialOffset, long boxSize, String boxName) {
        FtypBox box = new FtypBox(boxSize, boxName);
        // TODO: validate minimum box size
        box.setMajorBrand(readFourCC(byteBuffer));
        box.setMinorVersion(readInteger(byteBuffer));
        while (byteBuffer.position() < (initialOffset + boxSize)) {
            String compatibleBrand = readFourCC(byteBuffer);
            box.addCompatibleBrand(compatibleBrand);
        }
        return box;
    }
}
