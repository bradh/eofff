package net.frogmouth.rnd.eofff.isobmff.trgr;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
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
            TrackGroupTypeBox trackGroup = parseTrackGroup(parseContext);
            box.addEntry(trackGroup);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    private TrackGroupTypeBox parseTrackGroup(ParseContext parseContext) {
        long offset = parseContext.getCursorPosition();
        long boxSize = parseContext.readUnsignedInt32();
        FourCC entry_type = parseContext.readFourCC();
        TrackGroupTypeParser parser = TrackGroupTypeFactoryManager.getParser(entry_type);
        TrackGroupTypeBox trackGroup = parser.parse(parseContext, offset, boxSize, entry_type);
        parseContext.setCursorPosition(offset + boxSize);
        return trackGroup;
    }
}
