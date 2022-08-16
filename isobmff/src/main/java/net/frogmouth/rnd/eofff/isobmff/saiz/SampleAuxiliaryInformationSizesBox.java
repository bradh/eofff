package net.frogmouth.rnd.eofff.isobmff.saiz;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Auxiliary Information Sizes Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.7.8.
 */
public class SampleAuxiliaryInformationSizesBox extends FullBox {

    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private String theURI;
    private long defaultSampleInfoSize;
    private long sampleCount;

    public SampleAuxiliaryInformationSizesBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "SampleAuxiliaryInformationSizesBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if ((getFlags() & 0x01) == 0x01) {
            size += FourCC.BYTES;
            size += Integer.BYTES;
        }
        if (auxInfoType.equals(new FourCC("misb"))) {
            size += theURI.getBytes(StandardCharsets.UTF_8).length;
            size += Byte.BYTES; // null terminator
            // TODO: per parameters
            size += Byte.BYTES;
            size += Integer.BYTES;
            // TODO: sample_info_size array
        }
        return size;
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
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        if ((getFlags() & 0x01) == 0x01) {
            stream.writeFourCC(auxInfoType);
            stream.writeInt((int) auxInfoTypeParameter);
        }
        if (((getFlags() & 0x01) == 0x01) && (auxInfoType.equals(new FourCC("misb")))) {
            stream.writeNullTerminatedString(theURI);
            // TODO: larger sizes
            stream.writeByte((int) defaultSampleInfoSize);
            stream.writeInt((int) sampleCount);
            // TODO: sample_info_size array
        } else {
            // TODO: larger sizes
            stream.writeByte((int) defaultSampleInfoSize);
            stream.writeInt((int) sampleCount);
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
