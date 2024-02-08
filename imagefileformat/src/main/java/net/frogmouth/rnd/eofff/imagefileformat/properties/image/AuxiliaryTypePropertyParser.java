package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuxiliaryTypePropertyParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(AuxiliaryTypePropertyParser.class);

    public AuxiliaryTypePropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("auxC");
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AuxiliaryTypeProperty box = new AuxiliaryTypeProperty(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as unknown property.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setAuxType(parseContext.readNullDelimitedString(boxSize));
        long subtypeLen = initialOffset + boxSize - parseContext.getCursorPosition();
        byte[] subType = new byte[(int) subtypeLen];
        parseContext.readBytes(subType);
        box.setAuxSubtype(subType);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
