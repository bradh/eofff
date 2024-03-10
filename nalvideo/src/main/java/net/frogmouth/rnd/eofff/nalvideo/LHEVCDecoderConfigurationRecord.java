package net.frogmouth.rnd.eofff.nalvideo;

// import static net.frogmouth.rnd.eofff.nalvideo.FormatUtils.addByteArrayAsHex;
import static net.frogmouth.rnd.eofff.nalvideo.FormatUtils.addIndent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

/**
 * L-HEVC decoder configuration record.
 *
 * <p>See ISO/IEC 14496-15 Section 9.4.3
 */
public class LHEVCDecoderConfigurationRecord {

    static LHEVCDecoderConfigurationRecord parseFrom(ParseContext parseContext, long l) {
        LHEVCDecoderConfigurationRecord box = new LHEVCDecoderConfigurationRecord();
        box.setConfigurationVersion(parseContext.readUnsignedInt8());
        box.setMinSpatialSegmentationIDC(parseContext.readUnsignedInt16() & 0x7FF);
        box.setParallelismType(parseContext.readUnsignedInt8() & 0x03);
        byte bits = parseContext.readByte();
        box.setNumTemporalLayers((bits >> 3) & 0x07);
        box.setTemporalIdNested((bits >> 2) & 0x01);
        box.setLengthSizeMinusOne(bits & 0x03);
        int numOfArrays = parseContext.readUnsignedInt8();
        for (int j = 0; j < numOfArrays; j++) {
            // TODO: consider moving this to the array, and use that in
            // HEVCDecoderConfigurationArray
            HEVCDecoderConfigurationArray array = new HEVCDecoderConfigurationArray();
            byte temp = parseContext.readByte();
            array.setArray_completion(((temp & 0b10000000) != 0));
            array.setNal_unit_type(temp & 0b00111111);
            int numNalus = parseContext.readUnsignedInt16();
            for (int i = 0; i < numNalus; i++) {
                int nalUnitLength = parseContext.readUnsignedInt16();
                byte[] nalUnit = new byte[nalUnitLength];
                parseContext.readBytes(nalUnit);
                NALU nalu = new NALU();
                nalu.setNalUnit(nalUnit);
                array.addNALU(nalu);
            }
            box.addArray(array);
        }
        return box;
    }

    private int configurationVersion;
    private int minSpatialSegmentationIDC;
    private int parallelismType;
    private int numTemporalLayers;
    private int temporalIdNested;
    private int lengthSizeMinusOne;
    private final List<HEVCDecoderConfigurationArray> arrays = new ArrayList<>();

    public long getSize() {
        int size = 6;
        for (HEVCDecoderConfigurationArray array : arrays) {
            size += (array.getNumBytes());
        }
        return size;
    }

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeByte(configurationVersion);
        writer.writeUnsignedInt16((0b1111 << 12) | minSpatialSegmentationIDC);
        writer.writeByte((0b111111 << 2) | parallelismType);
        int bits =
                (0b11 << 6)
                        | ((numTemporalLayers & 0b111) << 3)
                        | ((temporalIdNested & 0b1) << 2)
                        | (lengthSizeMinusOne & 0b11);
        writer.writeUnsignedInt8(bits);
        writer.writeUnsignedInt8(arrays.size());
        for (HEVCDecoderConfigurationArray array : arrays) {
            array.writeTo(writer);
        }
    }

    public int getConfigurationVersion() {
        return configurationVersion;
    }

    public void setConfigurationVersion(int configurationVersion) {
        this.configurationVersion = configurationVersion;
    }

    public int getMinSpatialSegmentationIDC() {
        return minSpatialSegmentationIDC;
    }

    public void setMinSpatialSegmentationIDC(int minSpatialSegmentationIDC) {
        this.minSpatialSegmentationIDC = minSpatialSegmentationIDC;
    }

    public int getParallelismType() {
        return parallelismType;
    }

    public void setParallelismType(int parallelismType) {
        this.parallelismType = parallelismType;
    }

    public int getNumTemporalLayers() {
        return numTemporalLayers;
    }

    public void setNumTemporalLayers(int numTemporalLayers) {
        this.numTemporalLayers = numTemporalLayers;
    }

    public int getTemporalIdNested() {
        return temporalIdNested;
    }

    public void setTemporalIdNested(int temporalIdNested) {
        this.temporalIdNested = temporalIdNested;
    }

    public int getLengthSizeMinusOne() {
        return lengthSizeMinusOne;
    }

    public void setLengthSizeMinusOne(int lengthSizeMinusOne) {
        this.lengthSizeMinusOne = lengthSizeMinusOne;
    }

    public List<HEVCDecoderConfigurationArray> getArrays() {
        return new ArrayList<>(arrays);
    }

    public void addArray(HEVCDecoderConfigurationArray array) {
        this.arrays.add(array);
    }

    void addToStringBuilder(StringBuilder sb, int nestingLevel) {
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("configurationVersion=");
        sb.append(configurationVersion);
        sb.append(", min_spatial_segmentation_idc=");
        sb.append(minSpatialSegmentationIDC);
        sb.append(", parallelismType=");
        sb.append(parallelismType);
        sb.append(", numTemporalLayers=");
        sb.append(numTemporalLayers);
        sb.append(", temporalIdNested=");
        sb.append(temporalIdNested);
        sb.append(", lengthSizeMinusOne=");
        sb.append(lengthSizeMinusOne);
        sb.append(", numOfArrays: ");
        sb.append(arrays.size());
        for (HEVCDecoderConfigurationArray array : arrays) {
            array.addToStringBuilder(nestingLevel, sb);
        }
    }
}
