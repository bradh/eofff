package net.frogmouth.rnd.eofff.uncompressed.depi;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class DepthInfoBoxParser extends ItemFullPropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(DepthInfoBoxParser.class);

    public DepthInfoBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return DepthInfoBox.DEPI_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        DepthInfoBox box = new DepthInfoBox();
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
        box.setNknear(parseContext.readUnsignedInt8());
        box.setNkfar(parseContext.readUnsignedInt8());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
