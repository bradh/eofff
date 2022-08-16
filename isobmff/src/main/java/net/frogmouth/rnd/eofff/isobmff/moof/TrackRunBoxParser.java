package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackRunBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackRunBoxParser.class);
    private static final int DATA_OFFSET_PRESENT_FLAG = 0x000001;
    private static final int FIRST_SAMPLE_FLAGS_PRESENT_FLAG = 0x000004;
    private static final int SAMPLE_DURATION_PRESENT_FLAG = 0x000100;
    private static final int SAMPLE_SIZE_PRESENT_FLAG = 0x000200;
    private static final int SAMPLE_FLAGS_PRESENT_FLAG = 0x000400;
    private static final int SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG = 0x000800;

    public TrackRunBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("trun");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackRunBox box = new TrackRunBox(boxName);
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setSampleCount(parseContext.readUnsignedInt32());
        if (box.isFlagSet(DATA_OFFSET_PRESENT_FLAG)) {
            box.setDataOffset(parseContext.readInt32());
        }
        if (box.isFlagSet(FIRST_SAMPLE_FLAGS_PRESENT_FLAG)) {
            box.setFirstSampleFlags(parseContext.readUnsignedInt32());
        }
        for (int i = 0; i < box.getSampleCount(); i++) {
            TrackRunSample sample = new TrackRunSample();
            if (box.isFlagSet(SAMPLE_DURATION_PRESENT_FLAG)) {
                sample.setSampleDuration(parseContext.readUnsignedInt32());
            }
            if (box.isFlagSet(SAMPLE_SIZE_PRESENT_FLAG)) {
                sample.setSampleSize(parseContext.readUnsignedInt32());
            }
            if (box.isFlagSet(SAMPLE_FLAGS_PRESENT_FLAG)) {
                sample.setSampleFlags(parseContext.readUnsignedInt32());
            }
            if (box.isFlagSet(SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG)) {
                if (box.getVersion() == 0x00) {
                    sample.setSampleCompositionTimeOffset(parseContext.readUnsignedInt32());
                } else {
                    sample.setSampleCompositionTimeOffset(parseContext.readInt32());
                }
            }
            box.addSample(sample);
        }

        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
