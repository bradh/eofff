package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class CleanApertureParser implements PropertyParser {

    public CleanApertureParser() {}

    @Override
    public FourCC getFourCC() {
        return CleanAperture.CLAP_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CleanAperture box = new CleanAperture();
        box.setCleanApertureWidthN(parseContext.readUnsignedInt32());
        box.setCleanApertureWidthD(parseContext.readUnsignedInt32());
        box.setCleanApertureHeightN(parseContext.readUnsignedInt32());
        box.setCleanApertureHeightD(parseContext.readUnsignedInt32());
        box.setHorizOffN(parseContext.readUnsignedInt32());
        box.setHorizOffD(parseContext.readUnsignedInt32());
        box.setVertOffN(parseContext.readUnsignedInt32());
        box.setVertOffD(parseContext.readUnsignedInt32());
        return box;
    }
}
