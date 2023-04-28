package net.frogmouth.rnd.eofff.uncompressed.cpal;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentPaletteBoxParser implements PropertyParser {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentPaletteBoxParser.class);

    public ComponentPaletteBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ComponentPaletteBox.CPAL_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ComponentPaletteBox box = new ComponentPaletteBox();
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
            int component_bit_depth_minus_one = parseContext.readUnsignedInt8();
            int component_format = parseContext.readUnsignedInt8();
            PaletteComponent component =
                    new PaletteComponent(
                            component_index, component_bit_depth_minus_one, component_format);
            box.addComponent(component);
        }
        long values_count = parseContext.readUnsignedInt32();
        byte[][] componentValues = new byte[(int) values_count][component_count];
        for (int i = 0; i < values_count; i++) {
            for (int j = 0; j < component_count; j++) {
                componentValues[i][j] = parseContext.readByte();
            }
        }
        box.setComponentValues(componentValues);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    protected int parseFlags(ParseContext parseContext) {
        byte[] flags = new byte[3];
        parseContext.readBytes(flags);
        return ((flags[0] & 0xFF) << 16) | ((flags[1] & 0xFF) << 8) | (flags[2] & 0xFF);
    }
}
