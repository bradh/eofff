package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PixelInformationPropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(PixelInformationPropertyParser.class);

    public PixelInformationPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("pixi");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PixelInformationProperty box = new PixelInformationProperty(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as unknown property.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int numChannels = parseContext.readUnsignedInt8();
        for (int i = 0; i < numChannels; i++) {
            box.addChannel(parseContext.readUnsignedInt8());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
