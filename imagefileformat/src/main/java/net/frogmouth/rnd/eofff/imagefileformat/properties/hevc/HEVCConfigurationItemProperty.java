package net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;

// TODO: rebuild this using nalvideo implementation
public class HEVCConfigurationItemProperty extends ItemProperty {
    public static final FourCC HVCC_ATOM = new FourCC("hvcC");

    private int configurationVersion;
    private int general_profile_space;
    private int general_tier_flag;
    private int general_profile_idc;
    private byte[] general_profile_compatibility_flags;
    private byte[] general_constraint_indicator_flags;
    private int general_level_idc;
    private int min_spatial_segmentation_idc;
    private int parallelismType;
    private int chromaFormat;
    private int bitDepthLumaMinus8;
    private int bitDepthChromaMinus8;
    private int avgFrameRate;
    private int constantFrameRate;
    private int numTemporalLayers;
    private int temporalIdNested;
    private int lengthSizeMinusOne;
    private int numOfArrays; // TODO: remove
    private final List<HEVCDecoderConfigurationArray> arrays = new ArrayList<>();

    @Override
    public long getBodySize() {
        int count = 23;
        for (HEVCDecoderConfigurationArray array : arrays) {
            count += (array.getNumBytes());
        }
        return count;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeByte(configurationVersion);
        int flagBits1 = ((general_profile_space & 0b11) << 6);
        flagBits1 |= ((general_tier_flag & 0b1) << 5);
        flagBits1 |= (general_profile_idc & 0b11111);
        writer.writeByte(flagBits1);
        writer.write(general_profile_compatibility_flags);
        writer.write(general_constraint_indicator_flags);
        writer.writeByte(general_level_idc);
        writer.writeUnsignedInt16((0b1111 << 12) | min_spatial_segmentation_idc);
        writer.writeByte((0b111111 << 2) | parallelismType);
        writer.writeByte((0b111111 << 2) | chromaFormat);
        writer.writeByte((0b11111 << 3) | bitDepthLumaMinus8);
        writer.writeByte((0b11111 << 3) | bitDepthChromaMinus8);
        writer.writeUnsignedInt16(avgFrameRate);
        int flagBits2 = ((constantFrameRate & 0b11) << 6);
        flagBits2 |= ((numTemporalLayers & 0b111) << 3);
        flagBits2 |= ((temporalIdNested & 0b1) << 2);
        flagBits2 |= (this.lengthSizeMinusOne & 0b11);
        writer.writeByte(flagBits2);
        writer.writeByte(numOfArrays);
        for (HEVCDecoderConfigurationArray array : arrays) {
            array.writeTo(writer);
        }
    }

    public HEVCConfigurationItemProperty() {
        super(HVCC_ATOM);
    }

    public int getConfigurationVersion() {
        return configurationVersion;
    }

    public void setConfigurationVersion(int configurationVersion) {
        this.configurationVersion = configurationVersion;
    }

    public int getGeneral_profile_space() {
        return general_profile_space;
    }

    public void setGeneral_profile_space(int general_profile_space) {
        this.general_profile_space = general_profile_space;
    }

    public int getGeneral_tier_flag() {
        return general_tier_flag;
    }

    public void setGeneral_tier_flag(int general_tier_flag) {
        this.general_tier_flag = general_tier_flag;
    }

    public int getGeneral_profile_idc() {
        return general_profile_idc;
    }

    public void setGeneral_profile_idc(int general_profile_idc) {
        this.general_profile_idc = general_profile_idc;
    }

    public byte[] getGeneral_profile_compatibility_flags() {
        return general_profile_compatibility_flags;
    }

    public void setGeneral_profile_compatibility_flags(byte[] general_profile_compatibility_flags) {
        this.general_profile_compatibility_flags = general_profile_compatibility_flags;
    }

    public byte[] getGeneral_constraint_indicator_flags() {
        return general_constraint_indicator_flags;
    }

    public void setGeneral_constraint_indicator_flags(byte[] general_constraint_indicator_flags) {
        this.general_constraint_indicator_flags = general_constraint_indicator_flags;
    }

    public int getGeneral_level_idc() {
        return general_level_idc;
    }

    public void setGeneral_level_idc(int general_level_idc) {
        this.general_level_idc = general_level_idc;
    }

    public int getMin_spatial_segmentation_idc() {
        return min_spatial_segmentation_idc;
    }

    public void setMin_spatial_segmentation_idc(int min_spatial_segmentation_idc) {
        this.min_spatial_segmentation_idc = min_spatial_segmentation_idc;
    }

    public int getParallelismType() {
        return parallelismType;
    }

    public void setParallelismType(int parallelismType) {
        this.parallelismType = parallelismType;
    }

    public int getChromaFormat() {
        return chromaFormat;
    }

    public void setChromaFormat(int chromaFormat) {
        this.chromaFormat = chromaFormat;
    }

    public int getBitDepthLumaMinus8() {
        return bitDepthLumaMinus8;
    }

    public void setBitDepthLumaMinus8(int bitDepthLumaMinus8) {
        this.bitDepthLumaMinus8 = bitDepthLumaMinus8;
    }

    public int getBitDepthChromaMinus8() {
        return bitDepthChromaMinus8;
    }

    public void setBitDepthChromaMinus8(int bitDepthChromaMinus8) {
        this.bitDepthChromaMinus8 = bitDepthChromaMinus8;
    }

    public int getAvgFrameRate() {
        return avgFrameRate;
    }

    public void setAvgFrameRate(int avgFrameRate) {
        this.avgFrameRate = avgFrameRate;
    }

    public int getConstantFrameRate() {
        return constantFrameRate;
    }

    public void setConstantFrameRate(int constantFrameRate) {
        this.constantFrameRate = constantFrameRate;
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

    public int getNumOfArrays() {
        return numOfArrays;
    }

    public void setNumOfArrays(int numOfArrays) {
        this.numOfArrays = numOfArrays;
    }

    public List<HEVCDecoderConfigurationArray> getArrays() {
        return new ArrayList<>(arrays);
    }

    public void addArray(HEVCDecoderConfigurationArray array) {
        this.arrays.add(array);
    }

    @Override
    public String getFullName() {
        return "HEVCConfigurationItemProperty";
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        this.addToStringBuilder(sb, nestingLevel + 1);
        return sb.toString();
    }

    // TODO: we may not need this after alignment with nalvideo impl.

    private static final String INDENT = "    ";

    public static void addByteArrayAsHex(byte[] bytes, StringBuilder sb) {
        String hexString = HexFormat.ofDelimiter(" ").withPrefix("0x").formatHex(bytes);
        sb.append(hexString);
    }

    void addToStringBuilder(StringBuilder sb, int nestingLevel) {
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("configurationVersion: ");
        sb.append(configurationVersion);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("general_profile_space: ");
        sb.append(general_profile_space);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("general_tier_flag: ");
        sb.append(general_tier_flag);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("general_profile_idc: ");
        sb.append(general_profile_idc);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("general_profile_compatibility_flags: ");
        addByteArrayAsHex(general_profile_compatibility_flags, sb);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("general_constraint_indicator_flags: ");
        addByteArrayAsHex(general_constraint_indicator_flags, sb);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("general_level_idc: ");
        sb.append(general_level_idc);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("min_spatial_segmentation_idc: ");
        sb.append(min_spatial_segmentation_idc);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("parallelismType: ");
        sb.append(parallelismType);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("chroma_format_idc: ");
        sb.append(chromaFormat);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("bitDepthLumaMinus8: ");
        sb.append(bitDepthLumaMinus8);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("bitDepthChromaMinus8: ");
        sb.append(bitDepthChromaMinus8);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("avgFrameRate: ");
        sb.append(avgFrameRate);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("constantFrameRate: ");
        sb.append(constantFrameRate);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("numTemporalLayers: ");
        sb.append(numTemporalLayers);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("temporalIdNested: ");
        sb.append(temporalIdNested);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("lengthSizeMinusOne: ");
        sb.append(lengthSizeMinusOne);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("numOfArrays: ");
        sb.append(arrays.size());
        for (HEVCDecoderConfigurationArray array : arrays) {
            array.addToStringBuilder(nestingLevel, sb);
        }
    }
}
