package net.frogmouth.rnd.eofff.uncompressed.uncc;

import static net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox.BLOCK_LITTLE_ENDIAN;
import static net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox.BLOCK_PAD_LSB_FLAG;
import static net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox.BLOCK_REVERSED_FLAG;
import static net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox.COMPONENTS_LITTLE_ENDIAN_FLAG;
import static net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox.PAD_UNKNOWN_FLAG;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
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
        long component_count = parseContext.readUnsignedInt32();
        for (long i = 0; i < component_count; i++) {
            int component_index = parseContext.readUnsignedInt16();
            int component_bit_depth_minus_one = parseContext.readUnsignedInt8();
            int component_format = parseContext.readUnsignedInt8();
            int component_align_size = parseContext.readUnsignedInt8();
            Component component =
                    new Component(
                            component_index,
                            component_bit_depth_minus_one,
                            ComponentFormat.lookup(component_format),
                            component_align_size);
            box.addComponent(component);
        }
        box.setSamplingType(SamplingType.lookup(parseContext.readUnsignedInt8()));
        box.setInterleaveType(Interleaving.lookup(parseContext.readUnsignedInt8()));
        box.setBlockSize(parseContext.readUnsignedInt8());
        int bitMask = parseContext.readUnsignedInt8();
        box.setComponentLittleEndian(
                (bitMask & COMPONENTS_LITTLE_ENDIAN_FLAG) == COMPONENTS_LITTLE_ENDIAN_FLAG);
        box.setBlockPadLSB((bitMask & BLOCK_PAD_LSB_FLAG) == BLOCK_PAD_LSB_FLAG);
        box.setBlockLittleEndian((bitMask & BLOCK_LITTLE_ENDIAN) == BLOCK_LITTLE_ENDIAN);
        box.setBlockReversed((bitMask & BLOCK_REVERSED_FLAG) == BLOCK_REVERSED_FLAG);
        box.setPadUnknown((bitMask & PAD_UNKNOWN_FLAG) == PAD_UNKNOWN_FLAG);
        box.setPixelSize(parseContext.readUnsignedInt32());
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
