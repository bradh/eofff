package net.frogmouth.rnd.eofff.uncompressed.uncc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Uncompressed Frame Config Box.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 5.2.2.
 */
public class UncompressedFrameConfigBox extends ItemFullProperty {

    public static final FourCC UNCC_ATOM = new FourCC("uncC");

    private FourCC profile;
    private final List<Component> components = new ArrayList<>();
    private SamplingType samplingType;
    private int interleaveType;
    private int blockSize;
    private boolean componentLittleEndian;
    private boolean blockPadLSB;
    private boolean blockLittleEndian;
    private boolean blockReversed;
    private boolean padUnknown;
    private int pixelSize;
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

    public int getInterleaveType() {
        return interleaveType;
    }

    public void setInterleaveType(int interleaveType) {
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

    public int getPixelSize() {
        return pixelSize;
    }

    public void setPixelSize(int pixelSize) {
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
        count += Short.BYTES;
        count += components.size() * Component.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
        count += Byte.BYTES;
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
        stream.writeUnsignedInt16(this.components.size());
        for (Component component : components) {
            component.writeTo(stream);
        }
        stream.writeByte(samplingType.getEncodedValue());
        stream.writeByte(interleaveType);
        stream.writeByte(blockSize);
        int bitMask = 0;
        // TODO: define constants for these bit flag masks
        if (this.isComponentLittleEndian()) {
            bitMask |= 0x80;
        }
        if (this.isBlockPadLSB()) {
            bitMask |= 0x40;
        }
        if (this.isBlockLittleEndian()) {
            bitMask |= 0x20;
        }
        if (this.isBlockReversed()) {
            bitMask |= 0x10;
        }
        if (this.isPadUnknown()) {
            bitMask |= 0x08;
        }
        stream.writeByte(bitMask);
        stream.writeByte(pixelSize);
        stream.writeUnsignedInt32(this.rowAlignSize);
        stream.writeUnsignedInt32(this.tileAlignSize);
        stream.writeUnsignedInt32(this.numTileColumnsMinusOne);
        stream.writeUnsignedInt32(this.numTileRowsMinusOne);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        // TODO: more details
        return sb.toString();
    }
}
