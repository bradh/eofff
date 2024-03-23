package net.frogmouth.rnd.eofff.nalvideo.lhevc.oinf;

import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class ProfileTierLevel {
    private int general_profile_space;
    private int general_tier_flag;
    private int general_profile_idc;
    private byte[] general_profile_compatibility_flags;
    private byte[] general_constraint_indicator_flags;
    private int general_level_idc;

    void parseFrom(ParseContext parseContext) {
        int temp = parseContext.readByte();
        general_profile_space = ((temp & 0b11000000) >> 6);
        general_tier_flag = ((temp & 0b00100000) >> 5);
        general_profile_idc = (temp & 0b00011111);
        general_profile_compatibility_flags = parseContext.getBytes(4);
        general_constraint_indicator_flags = parseContext.getBytes(6);
        general_level_idc = parseContext.readUnsignedInt8();
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
}
