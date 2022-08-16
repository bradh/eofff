package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ImageMirrorParser extends PropertyParser {

    public ImageMirrorParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("imir");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImageMirror box = new ImageMirror(boxName);
        int temp = parseContext.readUnsignedInt8();
        box.setAxis(temp & 0x01);
        return box;
    }
}
