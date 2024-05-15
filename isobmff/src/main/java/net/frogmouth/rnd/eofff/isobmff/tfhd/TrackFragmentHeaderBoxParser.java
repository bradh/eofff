package net.frogmouth.rnd.eofff.isobmff.tfhd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TrackFragmentHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackFragmentHeaderBoxParser.class);

    public TrackFragmentHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackFragmentHeaderBox.TFHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackFragmentHeaderBox box = new TrackFragmentHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setTrackID(parseContext.readUnsignedInt32());
        if (box.isFlagSet(TrackFragmentHeaderBox.BASE_DATA_OFFSET_PRESENT_FLAG)) {
            box.setBaseDataOffset(parseContext.readUnsignedInt64());
        }
        if (box.isFlagSet(TrackFragmentHeaderBox.SAMPLE_DESCRIPTION_INDEX_PRESENT_FLAG)) {
            box.setSampleDescriptionIndex(parseContext.readUnsignedInt32());
        }
        if (box.isFlagSet(TrackFragmentHeaderBox.DEFAULT_SAMPLE_DURATION_PRESENT_FLAG)) {
            box.setDefaultSampleDuration(parseContext.readUnsignedInt32());
        }
        if (box.isFlagSet(TrackFragmentHeaderBox.DEFAULT_SAMPLE_SIZE_PRESENT_FLAG)) {
            box.setDefaultSampleSize(parseContext.readUnsignedInt32());
        }
        if (box.isFlagSet(TrackFragmentHeaderBox.DEFAULT_SAMPLE_FLAGS_PRESENT_FLAG)) {
            box.setDefaultSampleFlags(parseContext.readUnsignedInt32());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
