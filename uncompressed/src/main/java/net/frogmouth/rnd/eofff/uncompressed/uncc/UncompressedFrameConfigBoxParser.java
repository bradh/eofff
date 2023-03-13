package net.frogmouth.rnd.eofff.uncompressed.uncc;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullPropertyParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UncompressedFrameConfigBoxParser extends ItemFullPropertyParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(UncompressedFrameConfigBoxParser.class);

    public UncompressedFrameConfigBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return UncompressedFrameConfigBox.UNCC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UncompressedFrameConfigBox box = new UncompressedFrameConfigBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsUnknownProperty(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        FourCC profile = parseContext.readFourCC();
        box.setProfile(profile);
        int component_count = parseContext.readUnsignedInt16();
        for (int i = 0; i < component_count; i++) {
            int component_index = parseContext.readUnsignedInt16();
            int component_bit_depth_minus_one = parseContext.readUnsignedInt8();
            int component_format = parseContext.readUnsignedInt8();
            int component_align_size = parseContext.readUnsignedInt8();
            Component component =
                    new Component(
                            component_index,
                            component_bit_depth_minus_one,
                            component_format,
                            component_align_size);
            box.addComponent(component);
        }
        box.setSamplingType(parseContext.readUnsignedInt8());
        box.setInterleaveType(parseContext.readUnsignedInt8());
        box.setBlockSize(parseContext.readUnsignedInt8());
        int bitMask = parseContext.readUnsignedInt8();
        box.setComponentLittleEndian((bitMask & 0x80) == 0x80);
        box.setBlockPadLSB((bitMask & 0x40) == 0x40);
        box.setBlockLittleEndian((bitMask & 0x20) == 0x20);
        box.setBlockReversed((bitMask & 0x10) == 0x10);
        box.setPadUnknown((bitMask & 0x08) == 0x08);
        box.setPixelSize(parseContext.readUnsignedInt8());
        box.setRowAlignSize(parseContext.readUnsignedInt32());
        box.setTileAlignSize(parseContext.readUnsignedInt32());
        box.setNumTileColumnsMinusOne(parseContext.readUnsignedInt32());
        box.setNumTileRowsMinusOne(parseContext.readUnsignedInt32());
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
