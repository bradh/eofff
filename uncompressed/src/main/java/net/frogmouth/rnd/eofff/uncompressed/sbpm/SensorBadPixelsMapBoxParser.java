package net.frogmouth.rnd.eofff.uncompressed.sbpm;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SensorBadPixelsMapBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SensorBadPixelsMapBoxParser.class);

    public SensorBadPixelsMapBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SensorBadPixelsMapBox.SBPM_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SensorBadPixelsMapBox box = new SensorBadPixelsMapBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        int component_count = parseContext.readUnsignedInt16();
        for (int i = 0; i < component_count; i++) {
            box.addComponentIndex(parseContext.readUnsignedInt16());
        }
        // TODO: correction_applied onwards
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
