package net.frogmouth.rnd.eofff.isobmff.ftyp;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class FileTypeBoxParser extends FileTypeLikeBoxParser {

    public FileTypeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ftyp");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FileTypeBox box = new FileTypeBox(boxSize, boxName);
        doParse(box, parseContext, initialOffset, boxSize);
        return box;
    }
}
