package net.frogmouth.rnd.eofff.nalvideo;

import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class HEVCConfigurationBoxParser extends BaseBoxParser {
    public HEVCConfigurationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("hvcC");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCConfigurationBox box = new HEVCConfigurationBox(boxName);
        HEVCDecoderConfigurationRecord decoderRecord =
                parseDecoderConfigurationRecord(parseContext);
        box.setHevcConfig(decoderRecord);
        return box;
    }

    private HEVCDecoderConfigurationRecord parseDecoderConfigurationRecord(
            ParseContext parseContext) {
        HEVCDecoderConfigurationRecord record = new HEVCDecoderConfigurationRecord();
        record.setConfigurationVersion(parseContext.readByte());
        record.setAvcProfileIndication(parseContext.readByte());
        record.setProfileCompatibility(parseContext.readByte());
        record.setAvcLevelIndication(parseContext.readByte());
        record.setLengthSizeMinusOne((byte) (parseContext.readByte() & 0x03));
        int numberOfSequenceParameterSets = (parseContext.readByte() & 0x1F);
        for (int i = 0; i < numberOfSequenceParameterSets; i++) {
            record.addSequenceParameterSet(parseSPSNALU(parseContext));
        }
        int numberOfPictureParameterSets = parseContext.readByte();
        for (int i = 0; i < numberOfPictureParameterSets; i++) {
            record.addPictureParameterSet(parsePPSNALU(parseContext));
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
