package net.frogmouth.rnd.eofff.isobmff.saiz;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class SampleAuxiliaryInformationSizesBox extends FullBox {

    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private String theURI;
    private long defaultSampleInfoSize;
    private long sampleCount;

    public SampleAuxiliaryInformationSizesBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "SampleAuxiliaryInformationSizesBox";
    }

    public FourCC getAuxInfoType() {
        return auxInfoType;
    }

    public void setAuxInfoType(FourCC auxInfoType) {
        this.auxInfoType = auxInfoType;
    }

    public long getAuxInfoTypeParameter() {
        return auxInfoTypeParameter;
    }

    public void setAuxInfoTypeParameter(long auxInfoTypeParameter) {
        this.auxInfoTypeParameter = auxInfoTypeParameter;
    }

    public String getTheURI() {
        return theURI;
    }

    public void setTheURI(String theURI) {
        this.theURI = theURI;
    }

    public long getDefaultSampleInfoSize() {
        return defaultSampleInfoSize;
    }

    public void setDefaultSampleInfoSize(long defaultSampleInfoSize) {
        this.defaultSampleInfoSize = defaultSampleInfoSize;
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(long sampleCount) {
        this.sampleCount = sampleCount;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        if ((getFlags() & 0x01) == 0x01) {
            stream.write(auxInfoType.toBytes());
            stream.write(BaseBox.intToBytes((int) auxInfoTypeParameter));
        }
        if (((getFlags() & 0x01) == 0x01) && (auxInfoType.equals(new FourCC("misb")))) {
            stream.write(theURI.getBytes(StandardCharsets.UTF_8));
            stream.write(0); // String null terminator
            // TODO: larger sizes
            stream.write((int) defaultSampleInfoSize);
            stream.write(BaseBox.intToBytes((int) sampleCount));
            // TODO: sample_info_size array
        } else {
            // TODO: larger sizes
            stream.write((int) defaultSampleInfoSize);
            stream.write(BaseBox.intToBytes((int) sampleCount));
            // TODO: sample_info_size array
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        if ((getFlags() & 0x01) == 0x01) {
            sb.append("aux_info_type=");
            sb.append(auxInfoType.toString());
            sb.append(", aux_info_type_parameter=");
            sb.append(auxInfoTypeParameter);
        }
        return sb.toString();
    }
}
