package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class FtypBoxParser extends BoxParser {

    public FtypBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ftyp");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, String boxName) {
        FtypBox box = new FtypBox(boxSize, boxName);
        // TODO: validate minimum box size
        box.setMajorBrand(parseContext.readFourCC().toString());
        box.setMinorVersion(parseContext.getInteger());
        while (parseContext.getCursorPosition() < (initialOffset + boxSize)) {
            String compatibleBrand = parseContext.readFourCC().toString();
            box.addCompatibleBrand(compatibleBrand);
        }
        return box;
    }
}
