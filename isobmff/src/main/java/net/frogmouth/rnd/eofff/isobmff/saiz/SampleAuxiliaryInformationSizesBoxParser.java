package net.frogmouth.rnd.eofff.isobmff.saiz;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SampleAuxiliaryInformationSizesBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(SampleAuxiliaryInformationSizesBoxParser.class);

    public SampleAuxiliaryInformationSizesBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SampleAuxiliaryInformationSizesBox.SAIZ_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleAuxiliaryInformationSizesBox box = new SampleAuxiliaryInformationSizesBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        if ((box.getFlags() & 0x01) == 0x01) {
            box.setAuxInfoType(parseContext.readFourCC());
            box.setAuxInfoTypeParameter(parseContext.readUnsignedInt32());
        }
        box.setDefaultSampleInfoSize(parseContext.readUnsignedInt8());
        long sample_count = parseContext.readUnsignedInt32();
        if (box.getDefaultSampleInfoSize() == 0) {
            for (int i = 0; i < sample_count; i++) {
                box.appendSampleInfoSize(parseContext.readUnsignedInt8());
            }
        } else {
            box.setSampleCount(sample_count);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
