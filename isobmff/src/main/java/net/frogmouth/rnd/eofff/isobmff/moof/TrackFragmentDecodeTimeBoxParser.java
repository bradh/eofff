package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackFragmentDecodeTimeBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(TrackFragmentDecodeTimeBoxParser.class);

    public TrackFragmentDecodeTimeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("tfdt");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackFragmentDecodeTimeBox box = new TrackFragmentDecodeTimeBox(boxSize, boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if (version == 0x01) {
            box.setBaseMediaDecodeTime(parseContext.readUnsignedInt64());
        } else {
            box.setBaseMediaDecodeTime(parseContext.readUnsignedInt32());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
