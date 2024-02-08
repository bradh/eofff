package net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class HEVCConfigurationItemPropertyParser implements PropertyParser {

    public HEVCConfigurationItemPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return HEVCConfigurationItemProperty.HVCC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCConfigurationItemProperty box = new HEVCConfigurationItemProperty();
        box.setConfigurationVersion(parseContext.readByte());
        int temp = parseContext.readByte();
        box.setGeneral_profile_space((temp & 0b11000000) >> 6);
        box.setGeneral_tier_flag((temp & 0b00100000) >> 5);
        box.setGeneral_profile_idc(temp & 0b00011111);
        byte[] general_profile_compatibility_flags = new byte[4];
        parseContext.readBytes(general_profile_compatibility_flags);
        box.setGeneral_profile_compatibility_flags(general_profile_compatibility_flags);
        byte[] general_constraint_indicator_flags = new byte[6];
        parseContext.readBytes(general_constraint_indicator_flags);
        box.setGeneral_constraint_indicator_flags(general_constraint_indicator_flags);
        box.setGeneral_level_idc(parseContext.readByte());
        temp = parseContext.readUnsignedInt16();
        box.setMin_spatial_segmentation_idc(temp & 0x0FFF);
        temp = parseContext.readByte();
        box.setParallelismType(temp & 0x03);
        temp = parseContext.readByte();
        box.setChromaFormat(temp & 0x03);
        temp = parseContext.readByte();
        box.setBitDepthLumaMinus8(temp & 0x07);
        temp = parseContext.readByte();
        box.setBitDepthChromaMinus8(temp & 0x07);
        box.setAvgFrameRate(parseContext.readUnsignedInt16());
        temp = parseContext.readByte();
        box.setConstantFrameRate((temp & 0b11000000) >> 6);
        box.setNumTemporalLayers((temp & 0b00111000) >> 3);
        box.setTemporalIdNested((temp & 0b00000100) >> 2);
        box.setLengthSizeMinusOne(temp & 0b00000011);
        box.setNumOfArrays(parseContext.readUnsignedInt8());
        for (int i = 0; i < box.getNumOfArrays(); i++) {
            HEVCDecoderConfigurationArray array = new HEVCDecoderConfigurationArray();
            temp = parseContext.readByte();
            array.setArray_completion(((temp & 0b10000000) != 0));
            array.setNal_unit_type(temp & 0b00111111);
            int numNalus = parseContext.readUnsignedInt16();
            for (int j = 0; j < numNalus; j++) {
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
}
