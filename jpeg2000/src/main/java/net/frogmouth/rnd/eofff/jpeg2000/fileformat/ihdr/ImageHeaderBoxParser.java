package net.frogmouth.rnd.eofff.jpeg2000.fileformat.ihdr;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ImageHeaderBoxParser extends BoxParser {

    public ImageHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ImageHeaderBox.IHDR_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImageHeaderBox box = new ImageHeaderBox();
        box.setWidth(parseContext.readUnsignedInt32());
        box.setHeight(parseContext.readUnsignedInt32());
        box.setNumberOfComponents(parseContext.readUnsignedInt16());
        box.setBitsPerComponent((short) parseContext.readUnsignedInt8());
        box.setCompression((short) parseContext.readUnsignedInt8());
        box.setColourspaceUnknown((short) parseContext.readUnsignedInt8());
        box.setIPR((short) parseContext.readUnsignedInt8());
        return box;
    }
}
