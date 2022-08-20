package net.frogmouth.rnd.eofff.isobmff.cslg;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositionToDecodeBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(CompositionToDecodeBoxParser.class);

    public CompositionToDecodeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return CompositionToDecodeBox.CSLG_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CompositionToDecodeBox box = new CompositionToDecodeBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 0x00) {
            box.setCompositionToDTSShift(parseContext.readInt32());
            box.setLeastDecodeToDisplayDelta(parseContext.readInt32());
            box.setGreatestDecodeToDisplayDelta(parseContext.readInt32());
            box.setCompositionStartTime(parseContext.readInt32());
            box.setCompositionEndTime(parseContext.readInt32());
        } else {
            box.setCompositionToDTSShift(parseContext.readInt64());
            box.setLeastDecodeToDisplayDelta(parseContext.readInt64());
            box.setGreatestDecodeToDisplayDelta(parseContext.readInt64());
            box.setCompositionStartTime(parseContext.readInt64());
            box.setCompositionEndTime(parseContext.readInt64());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
