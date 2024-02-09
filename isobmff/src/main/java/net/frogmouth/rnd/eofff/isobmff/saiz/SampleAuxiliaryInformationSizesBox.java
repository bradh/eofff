package net.frogmouth.rnd.eofff.isobmff.saiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Auxiliary Information Sizes Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.8.
 */
public class SampleAuxiliaryInformationSizesBox extends FullBox {

    public static final FourCC SAIZ_ATOM = new FourCC("saiz");

    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private int defaultSampleInfoSize;
    private long sampleCount;
    private final List<Integer> sampleInfoSizes = new ArrayList<>();

    public SampleAuxiliaryInformationSizesBox() {
        super(SAIZ_ATOM);
    }

    @Override
    public String getFullName() {
        return "SampleAuxiliaryInformationSizesBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if ((getFlags() & 0x01) == 0x01) {
            size += FourCC.BYTES;
            size += Integer.BYTES;
        }
        size += Byte.BYTES; // default_sample_info_size
        size += Integer.BYTES; // sample_count
        if (defaultSampleInfoSize == 0) {
            size += (Byte.BYTES * sampleInfoSizes.size());
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

    public int getDefaultSampleInfoSize() {
        return defaultSampleInfoSize;
    }

    public void setDefaultSampleInfoSize(int defaultSampleInfoSize) {
        this.defaultSampleInfoSize = defaultSampleInfoSize;
    }

    public List<Integer> getSampleInfoSizes() {
        return sampleInfoSizes;
    }

    public void appendSampleInfoSize(int size) {
        this.sampleInfoSizes.add(size);
    }

    public long getSampleCount() {
        if (defaultSampleInfoSize == 0) {
            return sampleInfoSizes.size();
        } else {
            return sampleCount;
        }
    }

    public void setSampleCount(long count) {
        this.sampleCount = count;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if ((getFlags() & 0x01) == 0x01) {
            stream.writeFourCC(auxInfoType);
            stream.writeInt((int) auxInfoTypeParameter);
        }
        stream.writeUnsignedInt8(this.defaultSampleInfoSize);
        stream.writeUnsignedInt32(getSampleCount());
        if (this.defaultSampleInfoSize == 0) {
            for (int size : sampleInfoSizes) {
                stream.writeUnsignedInt8(size);
            }
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        if ((getFlags() & 0x01) == 0x01) {
            sb.append("aux_info_type=");
            sb.append(auxInfoType.toString());
            sb.append(", aux_info_type_parameter=");
            sb.append(auxInfoTypeParameter);
            sb.append(", ");
        }
        sb.append("default_sample_info_size=");
        sb.append(defaultSampleInfoSize);
        sb.append(", sample_count=");
        sb.append(getSampleCount());
        if (defaultSampleInfoSize == 0) {
            sb.append(", sample sizes=");
            for (int i = 0; i < this.sampleInfoSizes.size(); i++) {
                sb.append("\n");
                this.addIndent(nestingLevel + 1, sb);
                sb.append(sampleInfoSizes.get(i));
            }
        }
        return sb.toString();
    }
}
