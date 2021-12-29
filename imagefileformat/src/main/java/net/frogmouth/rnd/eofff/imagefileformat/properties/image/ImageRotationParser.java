package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ImageRotationParser extends PropertyParser {

    public ImageRotationParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("irot");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImageRotation box = new ImageRotation(boxSize, boxName);
        int temp = parseContext.readUnsignedInt8();
        box.setAngle(temp & 0x03);
        return box;
    }
}
