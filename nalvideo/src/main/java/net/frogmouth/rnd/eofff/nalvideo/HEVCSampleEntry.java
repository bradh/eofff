package net.frogmouth.rnd.eofff.nalvideo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/*
 * TODO: This needs to be in an ISO 14496:15 related package
 * TODO: This needs to be changed to match HEVC.
 * TODO: This need to have the VisualSampleEntry stuff refactored out
 */
public class HEVCSampleEntry extends AbstractContainerBox {

    private int dataReferenceIndex;
    private int width;
    private int height;
    private long horizontalResolution;
    private long verticalResolution;
    private int frameCount;
    private String compressorName;
    private int depth;

    public HEVCSampleEntry(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "HEVCSampleEntry";
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
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        for (int i = 0; i < 6; i++) {
            stream.writeByte(0);
        }
        stream.writeShort((short) dataReferenceIndex);
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

    // TODO: add toString() override
}
