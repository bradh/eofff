package net.frogmouth.rnd.eofff.imagefileformat.properties.mski;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaskConfigurationPropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(MaskConfigurationPropertyParser.class);

    public MaskConfigurationPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return MaskConfigurationProperty.MSKC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MaskConfigurationProperty box = new MaskConfigurationProperty();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as unknown property.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setBitDepth(parseContext.readUnsignedInt8());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
