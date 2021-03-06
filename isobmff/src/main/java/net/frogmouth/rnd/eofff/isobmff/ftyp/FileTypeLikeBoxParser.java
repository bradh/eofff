package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

/** Parser for boxes that are like File Type Box. */
public abstract class FileTypeLikeBoxParser extends BoxParser {

    public FileTypeLikeBoxParser() {}

    protected void doParse(
            FileTypeLikeBox box, ParseContext parseContext, long initialOffset, long boxSize) {
        box.setMajorBrand(parseContext.readFourCC());
        box.setMinorVersion(parseContext.readInt32());
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            FourCC compatibleBrand = parseContext.readFourCC();
            box.addCompatibleBrand(compatibleBrand);
        }
    }
}
