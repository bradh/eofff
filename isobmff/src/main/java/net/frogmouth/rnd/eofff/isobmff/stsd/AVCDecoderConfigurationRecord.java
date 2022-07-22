package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class AVCDecoderConfigurationRecord {

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

    public void writeTo(OutputStream stream) throws IOException {
        stream.write(configurationVersion);
        stream.write(avcProfileIndication);
        stream.write(profileCompatibility);
        stream.write(avcLevelIndication);
        stream.write(0b11111100 | lengthSizeMinusOne);
        stream.write(0b11100000 | sps.size());
        for (SequenceParameterSetNALUnit nalu : sps) {
            nalu.writeTo(stream);
        }
        stream.write(pps.size());
        for (PictureParameterSetNALUnit nalu : pps) {
            nalu.writeTo(stream);
        }
    }

    /*
            aligned(8) class AVCDecoderConfigurationRecord {
    	unsigned int(8) configurationVersion = 1;
    	unsigned int(8) AVCProfileIndication;
    	unsigned int(8) profile_compatibility;
    	unsigned int(8) AVCLevelIndication;
    	bit(6) reserved = ‘111111’b;
    	unsigned int(2) lengthSizeMinusOne;
    	bit(3) reserved = ‘111’b;
    	unsigned int(5) numOfSequenceParameterSets;
    	for (i=0; i< numOfSequenceParameterSets;  i++) {
    		unsigned int(16) sequenceParameterSetLength ;
    		bit(8*sequenceParameterSetLength) sequenceParameterSetNALUnit;
    	}
    	unsigned int(8) numOfPictureParameterSets;
    	for (i=0; i< numOfPictureParameterSets;  i++) {
    		unsigned int(16) pictureParameterSetLength;
    		bit(8*pictureParameterSetLength) pictureParameterSetNALUnit;
    	}
    	if( profile_idc  ==  100  ||  profile_idc  ==  110  ||
    	    profile_idc  ==  122  ||  profile_idc  ==  144 )
    	{
    		bit(6) reserved = ‘111111’b;
    		unsigned int(2) chroma_format;
    		bit(5) reserved = ‘11111’b;
    		unsigned int(3) bit_depth_luma_minus8;
    		bit(5) reserved = ‘11111’b;
    		unsigned int(3) bit_depth_chroma_minus8;
    		unsigned int(8) numOfSequenceParameterSetExt;
    		for (i=0; i< numOfSequenceParameterSetExt; i++) {
    			unsigned int(16) sequenceParameterSetExtLength;
    			bit(8*sequenceParameterSetExtLength) sequenceParameterSetExtNALUnit;
    		}
    	}
    }*/

}
