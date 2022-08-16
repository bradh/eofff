package net.frogmouth.rnd.eofff.isobmff.tref;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackReferenceBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackReferenceBoxParser.class);

    public TrackReferenceBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackReferenceBox.TREF_FOURCC;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackReferenceBox box = new TrackReferenceBox();
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            long refBoxSize = parseContext.readUnsignedInt32();
            TrackReference refBoxName = parseContext.readTrackReference();
            int numTrackIds = (int) ((refBoxSize - (Integer.BYTES + FourCC.BYTES)) / Integer.BYTES);
            long[] trackIds = new long[numTrackIds];
            for (int i = 0; i < numTrackIds; i++) {
                trackIds[i] = parseContext.readUnsignedInt32();
            }
            TrackReferenceTypeBox refBox = new TrackReferenceTypeBox(refBoxName, trackIds);
            box.addEntry(refBox);
        }
        return box;
    }
}
