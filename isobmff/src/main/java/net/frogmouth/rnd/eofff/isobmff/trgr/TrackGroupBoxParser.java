package net.frogmouth.rnd.eofff.isobmff.trgr;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackGroupBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackGroupBoxParser.class);

    public TrackGroupBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackGroupBox.TRGR_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackGroupBox box = new TrackGroupBox();
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            long groupBoxSize = parseContext.readUnsignedInt32();
            TrackGroupType groupBoxName = parseContext.readTrackGroupType();
            int version = parseContext.readByte();
            if (!isSupportedVersion(version)) {
                LOG.warn("Got unsupported version {}.", version);
                // TODO: we need to bail here.
            }
            int flags = parseFlags(parseContext);
            long groupId = parseContext.readUnsignedInt32();
            TrackGroupTypeBox groupTypeBox =
                    new TrackGroupTypeBox(groupBoxName, groupId, version, flags);
            box.addEntry(groupTypeBox);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
