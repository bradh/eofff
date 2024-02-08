package net.frogmouth.rnd.eofff.nalvideo;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HEVCConfigurationBoxParser extends BaseBoxParser {
    public HEVCConfigurationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return HEVCConfigurationBox.HVCC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCConfigurationBox box = new HEVCConfigurationBox();
        HEVCDecoderConfigurationRecord decoderRecord =
                parseDecoderConfigurationRecord(parseContext);
        box.setHevcConfig(decoderRecord);
        return box;
    }

    private HEVCDecoderConfigurationRecord parseDecoderConfigurationRecord(
            ParseContext parseContext) {
        HEVCDecoderConfigurationRecord record = new HEVCDecoderConfigurationRecord();
        record.setConfigurationVersion(parseContext.readByte());
        int temp = parseContext.readByte();
        record.setGeneral_profile_space((temp & 0b11000000) >> 6);
        record.setGeneral_tier_flag((temp & 0b00100000) >> 5);
        record.setGeneral_profile_idc(temp & 0b00011111);
        byte[] general_profile_compatibility_flags = new byte[4];
        parseContext.readBytes(general_profile_compatibility_flags);
        record.setGeneral_profile_compatibility_flags(general_profile_compatibility_flags);
        byte[] general_constraint_indicator_flags = new byte[6];
        parseContext.readBytes(general_constraint_indicator_flags);
        record.setGeneral_constraint_indicator_flags(general_constraint_indicator_flags);
        record.setGeneral_level_idc(parseContext.readByte());
        temp = parseContext.readUnsignedInt16();
        record.setMin_spatial_segmentation_idc(temp & 0x0FFF);
        temp = parseContext.readByte();
        record.setParallelismType(temp & 0x03);
        temp = parseContext.readByte();
        record.setChromaFormat(temp & 0x03);
        temp = parseContext.readByte();
        record.setBitDepthLumaMinus8(temp & 0x07);
        temp = parseContext.readByte();
        record.setBitDepthChromaMinus8(temp & 0x07);
        record.setAvgFrameRate(parseContext.readUnsignedInt16());
        temp = parseContext.readByte();
        record.setConstantFrameRate((temp & 0b11000000) >> 6);
        record.setNumTemporalLayers((temp & 0b00111000) >> 3);
        record.setTemporalIdNested((temp & 0b00000100) >> 2);
        record.setLengthSizeMinusOne(temp & 0b00000011);
        int numArrays = parseContext.readUnsignedInt8();
        for (int i = 0; i < numArrays; i++) {
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
            record.addArray(array);
        }

        // TODO: whatever profile_idc shows
        return record;
    }

    private SequenceParameterSetNALUnit parseSPSNALU(ParseContext parseContext) {
        SequenceParameterSetNALUnit nalu = new SequenceParameterSetNALUnit();
        int sequenceParameterSetLength = parseContext.readUnsignedInt16();
        nalu.setNaluBytes(parseContext.getBytes(sequenceParameterSetLength));
        return nalu;
    }

    private PictureParameterSetNALUnit parsePPSNALU(ParseContext parseContext) {
        PictureParameterSetNALUnit nalu = new PictureParameterSetNALUnit();
        int pictureParameterSetLength = parseContext.readUnsignedInt16();
        nalu.setNaluBytes(parseContext.getBytes(pictureParameterSetLength));
        return nalu;
    }
}
