package net.frogmouth.rnd.eofff.isobmff.schm;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SchemeTypeBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SchemeTypeBoxParser.class);

    public SchemeTypeBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SchemeTypeBox.SCHM_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SchemeTypeBox box = new SchemeTypeBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setSchemeType(parseContext.readFourCC());
        box.setSchemeVersion(parseContext.readUnsignedInt32());
        if (box.getFlags() == 1) {
            box.setSchemeUri(parseContext.readNullDelimitedString(boxSize));
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
