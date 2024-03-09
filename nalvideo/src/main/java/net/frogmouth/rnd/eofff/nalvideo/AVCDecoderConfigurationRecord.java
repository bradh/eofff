package net.frogmouth.rnd.eofff.nalvideo;

import static net.frogmouth.rnd.eofff.nalvideo.FormatUtils.addIndent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

/**
 * AVC decoder configuration record.
 *
 * <p>See ISO/IEC 14496-15:2022 Section 5.3.1.
 */
public class AVCDecoderConfigurationRecord {

    public static AVCDecoderConfigurationRecord parseFrom(ParseContext parseContext, long limit) {
        AVCDecoderConfigurationRecord record = new AVCDecoderConfigurationRecord();
        record.setConfigurationVersion(parseContext.readByte());
        byte avcProfileIndication = parseContext.readByte();
        record.setAvcProfileIndication(avcProfileIndication);
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
        if (parseContext.hasRemainingUntil(limit)) {
            if ((avcProfileIndication != 66)
                    && (avcProfileIndication != 77)
                    && (avcProfileIndication != 88)) {
                byte chromaFormat = (byte) (parseContext.readByte() & 0x03);
                byte bitDepthLumaMinus8 = (byte) (parseContext.readByte() & 0x07);
                byte bitDepthChromaMinus8 = (byte) (parseContext.readByte() & 0x07);
                int numOfSequenceParameterSetExt = parseContext.readUnsignedInt8();
                for (int i = 0; i < numOfSequenceParameterSetExt; i++) {
                    int sequenceParameterSetExtLength = parseContext.readUnsignedInt16();
                    byte[] sequenceParameterSetExtNALUnit =
                            parseContext.getBytes(sequenceParameterSetExtLength);
                }
            }
        }
        return record;
    }

    private static SequenceParameterSetNALUnit parseSPSNALU(ParseContext parseContext) {
        SequenceParameterSetNALUnit nalu = new SequenceParameterSetNALUnit();
        int sequenceParameterSetLength = parseContext.readUnsignedInt16();
        nalu.setNaluBytes(parseContext.getBytes(sequenceParameterSetLength));
        return nalu;
    }

    private static PictureParameterSetNALUnit parsePPSNALU(ParseContext parseContext) {
        PictureParameterSetNALUnit nalu = new PictureParameterSetNALUnit();
        int pictureParameterSetLength = parseContext.readUnsignedInt16();
        nalu.setNaluBytes(parseContext.getBytes(pictureParameterSetLength));
        return nalu;
    }

    private short configurationVersion;
    private short avcProfileIndication;
    private short profileCompatibility;
    private short avcLevelIndication;
    private byte lengthSizeMinusOne;
    private final List<SequenceParameterSetNALUnit> sps = new ArrayList<>();
    private final List<PictureParameterSetNALUnit> pps = new ArrayList<>();

    public short getConfigurationVersion() {
        return configurationVersion;
    }

    public void setConfigurationVersion(short configurationVersion) {
        this.configurationVersion = configurationVersion;
    }

    public short getAvcProfileIndication() {
        return avcProfileIndication;
    }

    public void setAvcProfileIndication(short avcProfileIndication) {
        this.avcProfileIndication = avcProfileIndication;
    }

    public short getProfileCompatibility() {
        return profileCompatibility;
    }

    public void setProfileCompatibility(short profileCompatibility) {
        this.profileCompatibility = profileCompatibility;
    }

    public short getAvcLevelIndication() {
        return avcLevelIndication;
    }

    public void setAvcLevelIndication(short avcLevelIndication) {
        this.avcLevelIndication = avcLevelIndication;
    }

    public byte getLengthSizeMinusOne() {
        return lengthSizeMinusOne;
    }

    public void setLengthSizeMinusOne(byte lengthSizeMinusOne) {
        this.lengthSizeMinusOne = lengthSizeMinusOne;
    }

    public void addSequenceParameterSet(SequenceParameterSetNALUnit nalu) {
        sps.add(nalu);
    }

    public void addPictureParameterSet(PictureParameterSetNALUnit nalu) {
        pps.add(nalu);
    }

    public long getSize() {
        long size = 6 * Byte.BYTES;
        for (SequenceParameterSetNALUnit nalu : sps) {
            size += nalu.getSize();
        }
        size += Byte.BYTES;
        for (PictureParameterSetNALUnit nalu : pps) {
            size += nalu.getSize();
        }
        return size;
    }

    public void addToStringBuilder(StringBuilder sb, int nestingLevel) {
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("configurationVersion=");
        sb.append(configurationVersion);
        sb.append(", AVCProfileIndication=");
        sb.append(avcProfileIndication);
        sb.append(", profile_compatibility=");
        sb.append(profileCompatibility);
        sb.append(", AVCLevelIndication=");
        sb.append(avcLevelIndication);
        sb.append(", lengthSizeMinusOne=");
        sb.append(lengthSizeMinusOne);
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("numOfSequenceParameterSets=");
        sb.append(sps.size());
        for (SequenceParameterSetNALUnit nalu : sps) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            nalu.addToStringBuffer(sb);
        }
        sb.append("\n");
        addIndent(nestingLevel, sb);
        sb.append("numOfPictureParameterSets=");
        sb.append(pps.size());
        for (PictureParameterSetNALUnit nalu : pps) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            nalu.addToStringBuffer(sb);
        }
        // TODO: special case for if (AVCProfileIndication...
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeByte(configurationVersion);
        stream.writeByte(avcProfileIndication);
        stream.writeByte(profileCompatibility);
        stream.writeByte(avcLevelIndication);
        stream.writeByte(0b11111100 | lengthSizeMinusOne);
        stream.writeByte(0b11100000 | sps.size());
        for (SequenceParameterSetNALUnit nalu : sps) {
            nalu.writeTo(stream);
        }
        stream.writeByte(pps.size());
        for (PictureParameterSetNALUnit nalu : pps) {
            nalu.writeTo(stream);
        }
    }
}
