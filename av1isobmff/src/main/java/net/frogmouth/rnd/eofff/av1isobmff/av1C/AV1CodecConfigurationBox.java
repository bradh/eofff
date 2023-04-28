package net.frogmouth.rnd.eofff.av1isobmff.av1C;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AV1CodecConfigurationBox extends ItemProperty {
    private int version;
    private int seq_profile;
    private int seq_level_idx_0;
    private boolean seq_tier_0;
    private boolean high_bitdepth;
    private boolean twelve_bit;
    private boolean monochrome;
    private boolean chroma_subsampling_x;
    private boolean chroma_subsampling_y;
    private int chroma_sample_position;
    private int intial_presentation_delay;
    private byte[] obuBytes;

    public static final FourCC AV1C_ATOM = new FourCC("av1C");

    public AV1CodecConfigurationBox() {
        super(AV1C_ATOM);
    }

    @Override
    public String getFullName() {
        return "AV1CodecConfigurationBox";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getSeq_profile() {
        return seq_profile;
    }

    public void setSeq_profile(int seq_profile) {
        this.seq_profile = seq_profile;
    }

    public int getSeq_level_idx_0() {
        return seq_level_idx_0;
    }

    public void setSeq_level_idx_0(int seq_level_idx_0) {
        this.seq_level_idx_0 = seq_level_idx_0;
    }

    public boolean isSeq_tier_0() {
        return seq_tier_0;
    }

    public void setSeq_tier_0(boolean seq_tier_0) {
        this.seq_tier_0 = seq_tier_0;
    }

    public boolean isHigh_bitdepth() {
        return high_bitdepth;
    }

    public void setHigh_bitdepth(boolean high_bitdepth) {
        this.high_bitdepth = high_bitdepth;
    }

    public boolean isTwelve_bit() {
        return twelve_bit;
    }

    public void setTwelve_bit(boolean twelve_bit) {
        this.twelve_bit = twelve_bit;
    }

    public boolean isMonochrome() {
        return monochrome;
    }

    public void setMonochrome(boolean monochrome) {
        this.monochrome = monochrome;
    }

    public boolean isChroma_subsampling_x() {
        return chroma_subsampling_x;
    }

    public void setChroma_subsampling_x(boolean chroma_subsampling_x) {
        this.chroma_subsampling_x = chroma_subsampling_x;
    }

    public boolean isChroma_subsampling_y() {
        return chroma_subsampling_y;
    }

    public void setChroma_subsampling_y(boolean chroma_subsampling_y) {
        this.chroma_subsampling_y = chroma_subsampling_y;
    }

    public int getChroma_sample_position() {
        return chroma_sample_position;
    }

    public void setChroma_sample_position(int chroma_sample_position) {
        this.chroma_sample_position = chroma_sample_position;
    }

    public int getIntial_presentation_delay() {
        return intial_presentation_delay;
    }

    public void setIntial_presentation_delay(int intial_presentation_delay) {
        this.intial_presentation_delay = intial_presentation_delay;
    }

    public byte[] getObuBytes() {
        return obuBytes;
    }

    public void setObuBytes(byte[] obuBytes) {
        this.obuBytes = obuBytes;
    }
}
