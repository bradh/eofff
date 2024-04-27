package net.frogmouth.rnd.eofff.isobmff.trun;

import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.DATA_OFFSET_PRESENT_FLAG;
import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.FIRST_SAMPLE_FLAGS_PRESENT_FLAG;
import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG;
import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.SAMPLE_DURATION_PRESENT_FLAG;
import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.SAMPLE_FLAGS_PRESENT_FLAG;
import static net.frogmouth.rnd.eofff.isobmff.trun.TrackRunBox.SAMPLE_SIZE_PRESENT_FLAG;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TrackRunBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TrackRunBoxParser.class);

    public TrackRunBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TrackRunBox.TRUN_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TrackRunBox box = new TrackRunBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long sampleCount = parseContext.readUnsignedInt32();
        if (box.isFlagSet(DATA_OFFSET_PRESENT_FLAG)) {
            box.setDataOffset(parseContext.readInt32());
        }
        if (box.isFlagSet(FIRST_SAMPLE_FLAGS_PRESENT_FLAG)) {
            box.setFirstSampleFlags(parseContext.readUnsignedInt32());
        }
        for (int i = 0; i < sampleCount; i++) {
            long sampleDuration = 0;
            long sampleSize = 0;
            long sampleFlags = 0;
            long sampleCompositionTimeOffset = 0;
            if (box.isFlagSet(SAMPLE_DURATION_PRESENT_FLAG)) {
                sampleDuration = parseContext.readUnsignedInt32();
            }
            if (box.isFlagSet(SAMPLE_SIZE_PRESENT_FLAG)) {
                sampleSize = parseContext.readUnsignedInt32();
            }
            if (box.isFlagSet(SAMPLE_FLAGS_PRESENT_FLAG)) {
                sampleFlags = parseContext.readUnsignedInt32();
            }
            if (box.isFlagSet(SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG)) {
                if (box.getVersion() == 0x00) {
                    sampleCompositionTimeOffset = parseContext.readUnsignedInt32();
                } else {
                    sampleCompositionTimeOffset = parseContext.readInt32();
                }
            }
            TrackRunSample sample =
                    new TrackRunSample(
                            sampleDuration, sampleSize, sampleFlags, sampleCompositionTimeOffset);
            box.addSample(sample);
        }

        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01));
    }
}
