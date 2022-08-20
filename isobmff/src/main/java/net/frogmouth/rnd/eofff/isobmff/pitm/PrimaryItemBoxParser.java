package net.frogmouth.rnd.eofff.isobmff.pitm;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrimaryItemBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(PrimaryItemBoxParser.class);

    public PrimaryItemBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return PrimaryItemBox.PITM_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PrimaryItemBox box = new PrimaryItemBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 0) {
            box.setItemID(parseContext.readUnsignedInt16());
        } else {
            box.setItemID(parseContext.readUnsignedInt32());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
