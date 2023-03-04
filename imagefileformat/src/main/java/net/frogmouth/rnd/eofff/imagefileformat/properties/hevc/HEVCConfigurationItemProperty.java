package net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

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
    private int numOfArrays;
    /*
    for (j=0; j < numOfArrays; j++) {
    	bit(1) array_completeness;
    	unsigned int(1) reserved = 0;
    	unsigned int(6) NAL_unit_type;
    	unsigned int(16) numNalus;
    	for (i=0; i< numNalus; i++) {
    		unsigned int(16) nalUnitLength;
    		bit(8*nalUnitLength) nalUnit;
    	}
    }
       */

    @Override
    public long getBodySize() {
        // TODO: incomplete
        return 0;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        // TODO: rest of box
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

    @Override
    public String getFullName() {
        return "HEVCConfigurationItemProperty";
    }
}
