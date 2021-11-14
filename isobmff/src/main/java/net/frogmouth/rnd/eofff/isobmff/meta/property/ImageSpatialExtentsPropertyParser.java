package net.frogmouth.rnd.eofff.isobmff.meta.property;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ImageSpatialExtentsPropertyParser extends ItemFullPropertyParser {

    public ImageSpatialExtentsPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ispe");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImageSpatialExtentsProperty box = new ImageSpatialExtentsProperty(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            // TODO: LOG
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setImageWidth(parseContext.readUnsignedInt32());
        box.setImageHeight(parseContext.readUnsignedInt32());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
