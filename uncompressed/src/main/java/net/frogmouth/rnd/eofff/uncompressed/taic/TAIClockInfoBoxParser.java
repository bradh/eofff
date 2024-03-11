package net.frogmouth.rnd.eofff.uncompressed.taic;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TAIClockInfoBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TAIClockInfoBoxParser.class);

    public TAIClockInfoBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TAIClockInfoBox.TAIC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TAIClockInfoBox box = new TAIClockInfoBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setTimeUncertainty(parseContext.readUnsignedInt64());
        box.setCorrectionOffset(parseContext.readInt64());
        box.setClockDriftRate(parseContext.readDouble32());
        box.setReferenceSourceType((byte) parseContext.readUnsignedInt8());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
