package net.frogmouth.rnd.eofff.isobmff.smhd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SoundMediaHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SoundMediaHeaderBoxParser.class);

    public SoundMediaHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SoundMediaHeaderBox.SMHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SoundMediaHeaderBox box = new SoundMediaHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setBalance(parseContext.readUnsignedInt16());
        parseContext.readUnsignedInt16();
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
