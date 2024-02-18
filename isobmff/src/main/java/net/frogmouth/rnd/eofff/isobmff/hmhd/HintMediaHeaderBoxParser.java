package net.frogmouth.rnd.eofff.isobmff.hmhd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintMediaHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(HintMediaHeaderBoxParser.class);

    public HintMediaHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return HintMediaHeaderBox.HMHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintMediaHeaderBox box = new HintMediaHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setMaxPDUsize(parseContext.readUnsignedInt16());
        box.setAvgPDUsize(parseContext.readUnsignedInt16());
        box.setMaxbitrate(parseContext.readUnsignedInt32());
        box.setAvgbitrate(parseContext.readUnsignedInt32());
        parseContext.skipBytes(Integer.BYTES); // reserved
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
