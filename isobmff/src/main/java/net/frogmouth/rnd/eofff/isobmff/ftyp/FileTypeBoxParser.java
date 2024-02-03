package net.frogmouth.rnd.eofff.isobmff.ftyp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class FileTypeBoxParser extends FileTypeLikeBoxParser {

    public FileTypeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ftyp");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FileTypeBox box = new FileTypeBox(boxName);
        doParse(box, parseContext, initialOffset, boxSize);
        return box;
    }
}
