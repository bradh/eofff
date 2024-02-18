package net.frogmouth.rnd.eofff.isobmff.trgr;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(TrackGroupTypeParser.class)
public class StereoVideoGroupBoxParser extends BaseTrackGroupTypeParser
        implements TrackGroupTypeParser {

    @Override
    public FourCC getFourCC() {
        return StereoVideoGroupBox.STER;
    }

    @Override
    public TrackGroupTypeBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC track_group_type) {
        StereoVideoGroupBox ster = new StereoVideoGroupBox();
        if (this.parseVersionAndFlags(
                parseContext, ster, initialOffset, boxSize, track_group_type)) {
            return this.parseAsBaseTrackGroupType(
                    parseContext, initialOffset, boxSize, track_group_type);
        }
        ster.setTrackGroupId(parseContext.readUnsignedInt32());
        long v = parseContext.readUnsignedInt32();
        boolean left_view =
                ((v & StereoVideoGroupBox.HIGH_BIT_FLAG) == (StereoVideoGroupBox.HIGH_BIT_FLAG));
        ster.setLeftViewFlag(left_view);
        return ster;
    }
}
