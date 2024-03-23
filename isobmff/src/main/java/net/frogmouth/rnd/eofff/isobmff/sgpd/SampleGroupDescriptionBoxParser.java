package net.frogmouth.rnd.eofff.isobmff.sgpd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.*;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntry;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntryFactoryManager;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
// TODO: incomplete - needs a custom factory
public class SampleGroupDescriptionBoxParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(SampleGroupDescriptionBoxParser.class);

    public SampleGroupDescriptionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SampleGroupDescriptionBox.SGPD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleGroupDescriptionBox box = new SampleGroupDescriptionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        FourCC grouping_type = parseContext.readFourCC();
        String s = grouping_type.toString();
        long default_length = 0;
        if (version >= 1) {
            default_length = parseContext.readUnsignedInt32();
        }
        if (version >= 2) {
            long default_group_description_index = parseContext.readUnsignedInt32();
        }
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            long description_length = default_length;
            if (version >= 1) {
                if (default_length == 0) {
                    description_length = parseContext.readUnsignedInt32();
                }
            }
            SampleGroupEntry entry =
                    parseSampleGroupDescriptionEntry(
                            parseContext, grouping_type, description_length);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01) || (version == 0x02));
    }

    private SampleGroupEntry parseSampleGroupDescriptionEntry(
            ParseContext parseContext, FourCC grouping_type, long description_length) {
        SampleGroupEntryParser parser = SampleGroupEntryFactoryManager.getParser(grouping_type);
        return parser.parse(parseContext, description_length);
    }
}
