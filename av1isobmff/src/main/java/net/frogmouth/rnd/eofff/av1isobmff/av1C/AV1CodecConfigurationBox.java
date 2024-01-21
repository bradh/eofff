package net.frogmouth.rnd.eofff.av1isobmff.av1C;

import java.io.IOException;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

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
    private int initial_presentation_delay;
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

    public int getInitial_presentation_delay() {
        return initial_presentation_delay;
    }

    public void setInitial_presentation_delay(int initial_presentation_delay) {
        this.initial_presentation_delay = initial_presentation_delay;
    }

    public byte[] getObuBytes() {
        return obuBytes;
    }

    public void setObuBytes(byte[] obuBytes) {
        this.obuBytes = obuBytes;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeByte(version | 0x80);
        writer.writeByte((seq_profile << 5) | seq_level_idx_0);
        int flags = (seq_tier_0 ? 0x80 : 0);
        flags |= (high_bitdepth ? 0x40 : 0);
        flags |= (twelve_bit ? 0x20 : 0);
        flags |= (monochrome ? 0x10 : 0);
        flags |= (chroma_subsampling_x ? 0x08 : 0);
        flags |= (chroma_subsampling_y ? 0x04 : 0);
        flags |= (chroma_sample_position & 0x03);
        writer.writeByte(flags);
        if (initial_presentation_delay == 0) {
            writer.writeByte(0x00);
        } else {
            int value = 0x10;
            value |= ((initial_presentation_delay - 1) & 0x0F);
            writer.writeByte(value);
        }
        writer.write(obuBytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += 4;
        size += obuBytes.length;
        return size;
    }
}
