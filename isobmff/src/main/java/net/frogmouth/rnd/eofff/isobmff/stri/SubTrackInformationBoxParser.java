package net.frogmouth.rnd.eofff.isobmff.stri;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SubTrackInformationBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SubTrackInformationBoxParser.class);

    public SubTrackInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SubTrackInformationBox.STRI_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SubTrackInformationBox box = new SubTrackInformationBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setSwitchGroup(parseContext.readUnsignedInt16());
        box.setAlternateGroup(parseContext.readUnsignedInt16());
        box.setSubTrackId(parseContext.readUnsignedInt32());
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            box.addAttribute(parseContext.readFourCC());
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
