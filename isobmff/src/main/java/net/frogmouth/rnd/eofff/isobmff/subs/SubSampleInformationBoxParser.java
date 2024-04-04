package net.frogmouth.rnd.eofff.isobmff.subs;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SubSampleInformationBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SubSampleInformationBoxParser.class);

    public SubSampleInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SubSampleInformationBox.SUBS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SubSampleInformationBox box = new SubSampleInformationBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            SubSampleEntry entry = parseSubSampleEntry(parseContext, version);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    private SubSampleEntry parseSubSampleEntry(ParseContext parseContext, int version) {
        SubSampleEntry subSampleEntry = new SubSampleEntry();
        subSampleEntry.setSampleDelta(parseContext.readUnsignedInt32());
        long subsampleCount = parseContext.readUnsignedInt16();
        for (int i = 0; i < subsampleCount; i++) {
            SubSample subSample = new SubSample();
            if (version == 1) {
                subSample.setSubsampleSize(parseContext.readUnsignedInt32());
            } else {
                subSample.setSubsampleSize(parseContext.readUnsignedInt16());
            }
            subSample.setSubsamplePriority(parseContext.readUnsignedInt8());
            subSample.setDiscardable(parseContext.readUnsignedInt8());
            subSample.setCodecSpecificParameters(parseContext.readUnsignedInt32());
            subSampleEntry.addSubSample(subSample);
        }
        return subSampleEntry;
    }
}
