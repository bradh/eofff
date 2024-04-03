package net.frogmouth.rnd.eofff.isobmff.stsg;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SubTrackSampleGroupBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SubTrackSampleGroupBoxParser.class);

    public SubTrackSampleGroupBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SubTrackSampleGroupBox.STSG_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SubTrackSampleGroupBox box = new SubTrackSampleGroupBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setGroupingType(parseContext.readFourCC());
        int itemCount = parseContext.readUnsignedInt16();
        for (int i = 0; i < itemCount; i++) {
            box.addGroupDescriptionIndex(parseContext.readUnsignedInt32());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
