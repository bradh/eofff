package net.frogmouth.rnd.eofff.isobmff.nmhd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class NullMediaHeaderBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(NullMediaHeaderBoxParser.class);

    public NullMediaHeaderBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return NullMediaHeaderBox.NMHD_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        NullMediaHeaderBox box = new NullMediaHeaderBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
