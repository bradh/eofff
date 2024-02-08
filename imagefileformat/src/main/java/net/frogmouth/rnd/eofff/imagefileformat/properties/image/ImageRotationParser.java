package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class ImageRotationParser implements PropertyParser {

    public ImageRotationParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("irot");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImageRotation box = new ImageRotation(boxName);
        int temp = parseContext.readUnsignedInt8();
        box.setAngle(temp & 0x03);
        return box;
    }
}
