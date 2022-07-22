package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/*
 * TODO: This needs to be in an ISO 14496:15 related package
 * TODO: This need to have the VisualSampleEntry stuff refactored out
 */
public class AVCSampleEntry extends AbstractContainerBox {

    private int dataReferenceIndex;
    private int width;
    private int height;
    private long horizontalResolution;
    private long verticalResolution;
    private int frameCount;
    private String compressorName;
    private int depth;

    public AVCSampleEntry(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "AVCSampleEntry";
    }

    public int getDataReferenceIndex() {
        return dataReferenceIndex;
    }

    public void setDataReferenceIndex(int dataReferenceIndex) {
        this.dataReferenceIndex = dataReferenceIndex;
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        for (int i = 0; i < 6; i++) {
            stream.write(0);
        }
        stream.write(shortToBytes((short) dataReferenceIndex));
        stream.write(shortToBytes((short) 0)); // pre_defined
        stream.write(shortToBytes((short) 0)); // reserved
        for (int i = 0; i < 3; i++) {
            stream.write(intToBytes(0)); // pre_defined
        }
        stream.write(shortToBytes((short) width));
        stream.write(shortToBytes((short) height));
        stream.write(intToBytes((int) horizontalResolution));
        stream.write(intToBytes((int) verticalResolution));
        stream.write(intToBytes(0)); // reserved
        stream.write(shortToBytes((short) frameCount));
        byte[] compressorNameBytes = compressorName.getBytes(StandardCharsets.US_ASCII);
        stream.write(compressorNameBytes.length);
        stream.write(compressorNameBytes);
        for (int i = 0; i < (32 - (compressorNameBytes.length + Byte.BYTES)); i++) {
            stream.write(0);
        }
        stream.write(shortToBytes((short) depth));
        stream.write(shortToBytes((short) -1)); // pre_defined
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    // TODO: add toString() override
}
