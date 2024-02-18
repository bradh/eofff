package net.frogmouth.rnd.eofff.isobmff.trgr;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(TrackGroupTypeParser.class)
public class MultiSourcePresentationTrackGroupParser extends BaseTrackGroupTypeParser
        implements TrackGroupTypeParser {

    @Override
    public FourCC getFourCC() {
        return MultiSourcePresentationTrackGroup.MSRC;
    }

    @Override
    public TrackGroupTypeBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC track_group_type) {
        MultiSourcePresentationTrackGroup msrc = new MultiSourcePresentationTrackGroup();
        if (this.parseVersionAndFlags(
                parseContext, msrc, initialOffset, boxSize, track_group_type)) {
            return this.parseAsBaseTrackGroupType(
                    parseContext, initialOffset, boxSize, track_group_type);
        }
        msrc.setTrackGroupId(parseContext.readUnsignedInt32());
        return msrc;
    }
}
