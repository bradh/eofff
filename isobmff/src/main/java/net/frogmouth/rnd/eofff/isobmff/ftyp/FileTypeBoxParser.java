package net.frogmouth.rnd.eofff.isobmff.ftyp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class FileTypeBoxParser extends GeneralTypeBoxParser {

    public FileTypeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return FileTypeBox.FTYP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        FileTypeBox box = new FileTypeBox();
        doParse(box, parseContext, initialOffset, boxSize);
        return box;
    }
}
