package net.frogmouth.rnd.eofff.uncompressed.itai;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TAITimeStampBoxParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(TAITimeStampBoxParser.class);

    public TAITimeStampBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TAITimeStampBox.ITAI_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TAITimeStampBox box = new TAITimeStampBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        TAITimeStampPacket timeStampPacket = new TAITimeStampPacket();
        timeStampPacket.setTAITimeStamp(parseContext.readUnsignedInt64());
        timeStampPacket.setStatusBits(parseContext.readByte());
        box.setTimeStampPacket(timeStampPacket);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
