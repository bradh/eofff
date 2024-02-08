package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

public class PixelAspectRatioPropertyParser implements PropertyParser {

    @Override
    public FourCC getFourCC() {
        return PixelAspectRatioProperty.PASP_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PixelAspectRatioProperty property = new PixelAspectRatioProperty();
        property.setHorizontalSpacing(parseContext.readUnsignedInt32());
        property.setVerticalSpacing(parseContext.readUnsignedInt32());
        return property;
    }
}
