package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Visual Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.1.3.
 */
public abstract class VisualSampleEntry extends BaseSampleEntry implements SampleEntry {

    private int width;
    private int height;
    private long horizontalResolution;
    private long verticalResolution;
    private int frameCount;
    private String compressorName;
    private int depth;

    public VisualSampleEntry(FourCC format) {
        super(format);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getHorizontalResolution() {
        return horizontalResolution;
    }

    public void setHorizontalResolution(long horizontalResolution) {
        this.horizontalResolution = horizontalResolution;
    }

    public long getVerticalResolution() {
        return verticalResolution;
    }

    public void setVerticalResolution(long verticalResolution) {
        this.verticalResolution = verticalResolution;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public String getCompressorName() {
        return compressorName;
    }

    public void setCompressorName(String compressorName) {
        this.compressorName = compressorName;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        stream.writeShort((short) 0); // pre_defined
        stream.writeShort((short) 0); // reserved
        for (int i = 0; i < 3; i++) {
            stream.writeInt(0); // pre_defined
        }
        stream.writeShort((short) width);
        stream.writeShort((short) height);
        stream.writeInt((int) horizontalResolution);
        stream.writeInt((int) verticalResolution);
        stream.writeInt(0); // reserved
        stream.writeShort((short) frameCount);
        byte[] compressorNameBytes = compressorName.getBytes(StandardCharsets.US_ASCII);
        stream.writeByte(compressorNameBytes.length);
        stream.write(compressorNameBytes);
        for (int i = 0; i < (32 - (compressorNameBytes.length + Byte.BYTES)); i++) {
            stream.writeByte(0);
        }
        stream.writeShort((short) depth);
        stream.writeShort((short) -1); // pre_defined
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", width: ");
        sb.append(width);
        sb.append(", height: ");
        sb.append(height);
        sb.append(", horizresolution: ");
        sb.append(horizontalResolution);
        sb.append(", vertresolution: ");
        sb.append(verticalResolution);
        sb.append(", frame_count: ");
        sb.append(frameCount);
        sb.append(", compressorname: ");
        if (compressorName.isBlank()) {
            sb.append("[None]");
        } else {
            sb.append(compressorName);
        }
        sb.append(", depth: ");
        sb.append(depth);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += Short.BYTES;
        size += Short.BYTES;
        size += (3 * Integer.BYTES);
        size += Short.BYTES;
        size += Short.BYTES;
        size += Integer.BYTES; // horizresolution
        size += Integer.BYTES; // vertresolution
        size += Integer.BYTES; // reserved
        size += Short.BYTES; // frame_count
        size += (32 * Byte.BYTES);
        size += Short.BYTES; // depth
        size += Short.BYTES; // pre_defined
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }
}
