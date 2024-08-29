package net.frogmouth.rnd.eofff.uncompressed.uncc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Uncompressed Frame Config Box.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 5.2.2.
 */
public class UncompressedFrameConfigBox extends ItemFullProperty {

    public static final FourCC UNCC_ATOM = new FourCC("uncC");

    public static final int COMPONENTS_LITTLE_ENDIAN_FLAG = 0x80;
    public static final int BLOCK_PAD_LSB_FLAG = 0x40;
    public static final int BLOCK_LITTLE_ENDIAN = 0x20;
    public static final int BLOCK_REVERSED_FLAG = 0x10;
    public static final int PAD_UNKNOWN_FLAG = 0x08;

    private FourCC profile;
    private final List<Component> components = new ArrayList<>();
    private SamplingType samplingType;
    private Interleaving interleaveType;
    private int blockSize;
    private boolean componentLittleEndian;
    private boolean blockPadLSB;
    private boolean blockLittleEndian;
    private boolean blockReversed;
    private boolean padUnknown;
    private long pixelSize;
    private long rowAlignSize;
    private long tileAlignSize;
    private long numTileColumnsMinusOne;
    private long numTileRowsMinusOne;

    public UncompressedFrameConfigBox() {
        super(UNCC_ATOM);
    }

    @Override
    public String getFullName() {
        return "UncompressedFrameConfigBox";
    }

    public FourCC getProfile() {
        return profile;
    }

    public void setProfile(FourCC profile) {
        this.profile = profile;
    }

    public List<Component> getComponents() {
        return new ArrayList<>(this.components);
    }

    public void addComponent(final Component component) {
        this.components.add(component);
    }

    public SamplingType getSamplingType() {
        return samplingType;
    }

    public void setSamplingType(SamplingType samplingType) {
        this.samplingType = samplingType;
    }

    public Interleaving getInterleaveType() {
        return interleaveType;
    }

    public void setInterleaveType(Interleaving interleaveType) {
        this.interleaveType = interleaveType;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public boolean isComponentLittleEndian() {
        return componentLittleEndian;
    }

    public void setComponentLittleEndian(boolean componentLittleEndian) {
        this.componentLittleEndian = componentLittleEndian;
    }

    public boolean isBlockPadLSB() {
        return blockPadLSB;
    }

    public void setBlockPadLSB(boolean blockPadLSB) {
        this.blockPadLSB = blockPadLSB;
    }

    public boolean isBlockLittleEndian() {
        return blockLittleEndian;
    }

    public void setBlockLittleEndian(boolean blockLittleEndian) {
        this.blockLittleEndian = blockLittleEndian;
    }

    public boolean isBlockReversed() {
        return blockReversed;
    }

    public void setBlockReversed(boolean blockReversed) {
        this.blockReversed = blockReversed;
    }

    public boolean isPadUnknown() {
        return padUnknown;
    }

    public void setPadUnknown(boolean padUnknown) {
        this.padUnknown = padUnknown;
    }

    public long getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(long pixelSize) {
        this.pixelSize = pixelSize;
    }

    public long getRowAlignSize() {
        return rowAlignSize;
    }

    public void setRowAlignSize(long rowAlignSize) {
        this.rowAlignSize = rowAlignSize;
    }

    public long getTileAlignSize() {
        return tileAlignSize;
    }

    public void setTileAlignSize(long tileAlignSize) {
        this.tileAlignSize = tileAlignSize;
    }

    public long getNumTileColumnsMinusOne() {
        return numTileColumnsMinusOne;
    }

    public void setNumTileColumnsMinusOne(long numTileColumnsMinusOne) {
        this.numTileColumnsMinusOne = numTileColumnsMinusOne;
    }

    public long getNumTileRowsMinusOne() {
        return numTileRowsMinusOne;
    }

    public void setNumTileRowsMinusOne(long numTileRowsMinusOne) {
        this.numTileRowsMinusOne = numTileRowsMinusOne;
    }

    @Override
    public long getBodySize() {
        long count = 0;
        count += FourCC.BYTES;
        count += Integer.BYTES;
        count += components.size() * Component.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
        count += Integer.BYTES;
        count += Integer.BYTES;
        count += Integer.BYTES;
        count += Integer.BYTES;
        count += Integer.BYTES;
        return count;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeFourCC(profile);
        stream.writeUnsignedInt32(this.components.size());
        for (Component component : components) {
            component.writeTo(stream);
        }
        stream.writeByte(samplingType.getEncodedValue());
        stream.writeByte(interleaveType.getEncodedValue());
        stream.writeByte(blockSize);
        int bitMask = 0;
        if (this.isComponentLittleEndian()) {
            bitMask |= COMPONENTS_LITTLE_ENDIAN_FLAG;
        }
        if (this.isBlockPadLSB()) {
            bitMask |= BLOCK_PAD_LSB_FLAG;
        }
        if (this.isBlockLittleEndian()) {
            bitMask |= BLOCK_LITTLE_ENDIAN;
        }
        if (this.isBlockReversed()) {
            bitMask |= BLOCK_REVERSED_FLAG;
        }
        if (this.isPadUnknown()) {
            bitMask |= PAD_UNKNOWN_FLAG;
        }
        stream.writeByte(bitMask);
        stream.writeUnsignedInt32(pixelSize);
        stream.writeUnsignedInt32(this.rowAlignSize);
        stream.writeUnsignedInt32(this.tileAlignSize);
        stream.writeUnsignedInt32(this.numTileColumnsMinusOne);
        stream.writeUnsignedInt32(this.numTileRowsMinusOne);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("profile=");
        sb.append(profile.toString());
        sb.append(", ");
        List<String> comps = new ArrayList<>();
        this.components.forEach(
                definition -> {
                    comps.add(definition.toString());
                });
        sb.append(String.join(",", comps));
        sb.append(", sampling_type=");
        sb.append(this.samplingType);
        sb.append(", interleaveType=");
        sb.append(this.interleaveType);
        sb.append(", blockSize=");
        sb.append(this.blockSize);
        sb.append(", component_little_endian=");
        sb.append(this.componentLittleEndian);
        sb.append(", block_pad_LSB=");
        sb.append(this.blockPadLSB);
        sb.append(", block_little_endian=");
        sb.append(this.blockLittleEndian);
        sb.append(", block_reversed=");
        sb.append(this.blockReversed);
        sb.append(", pad_unknown=");
        sb.append(this.padUnknown);
        sb.append(", pixel_size=");
        sb.append(this.pixelSize);
        sb.append(", row_align_size=");
        sb.append(this.rowAlignSize);
        sb.append(", tile_align_size=");
        sb.append(this.tileAlignSize);
        sb.append(", num_tile_cols_minus_one=");
        sb.append(this.numTileColumnsMinusOne);
        sb.append(", num_tile_rows_minus_one=");
        sb.append(this.numTileRowsMinusOne);
        return sb.toString();
    }
}
