package net.frogmouth.rnd.eofff.isobmff.sgpd;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            if (version >= 1) {
                if (default_length == 0) {
                    long description_length = parseContext.readUnsignedInt32();
                }
            }
            SampleGroupDescriptionEntry entry = parseSampleGroupDescriptionEntry(parseContext);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01) || (version == 0x02));
    }

    private SampleGroupDescriptionEntry parseSampleGroupDescriptionEntry(
            ParseContext parseContext) {
        // TODO: implement
        return null;
    }
}
