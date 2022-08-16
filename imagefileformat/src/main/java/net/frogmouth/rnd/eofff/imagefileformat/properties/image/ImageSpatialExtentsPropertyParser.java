package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageSpatialExtentsPropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(ImageSpatialExtentsPropertyParser.class);

    public ImageSpatialExtentsPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("ispe");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImageSpatialExtentsProperty box = new ImageSpatialExtentsProperty(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as unknown property.", version);
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
