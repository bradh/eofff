package net.frogmouth.rnd.eofff.uncompressed.disi;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisparityInformationBoxParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(DisparityInformationBoxParser.class);

    public DisparityInformationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DisparityInformationBox.DISI_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        DisparityInformationBox box = new DisparityInformationBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        int component_count = parseContext.readUnsignedInt16();
        for (int i = 0; i < component_count; i++) {
            int component_index = parseContext.readUnsignedInt16();
            box.addComponentIndex(component_index);
        }
        box.setParallax_zero(parseContext.readUnsignedInt16());
        box.setParallax_scale(parseContext.readUnsignedInt16());
        box.setDref(parseContext.readUnsignedInt16());
        box.setWref(parseContext.readUnsignedInt16());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
