package net.frogmouth.rnd.eofff.isobmff.saio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Auxiliary Information Offsets Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.9.
 */
public class SampleAuxiliaryInformationOffsetsBox extends FullBox {
    public static final FourCC SAIO_ATOM = new FourCC("saio");

    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private List<Long> offsets = new ArrayList<>();

    public SampleAuxiliaryInformationOffsetsBox() {
        super(SAIO_ATOM);
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
        size += Integer.BYTES;
        if (getVersion() == 0) {
            size += (offsets.size() * Integer.BYTES);
        } else {
            size += (offsets.size() * Long.BYTES);
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

    public List<Long> getOffsets() {
        return offsets;
    }

    public void addOffset(Long offset) {
        offsets.add(offset);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if ((getFlags() & 0x01) == 0x01) {
            stream.writeFourCC(auxInfoType);
            stream.writeInt((int) auxInfoTypeParameter);
        }
        stream.writeUnsignedInt32(offsets.size());
        if (this.getVersion() == 0) {
            for (long l : offsets) {
                stream.writeUnsignedInt32((int) l);
            }
        } else {
            for (long l : offsets) {
                stream.writeLong(l);
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
        sb.append("entry_count=");
        sb.append(offsets.size());
        if (offsets.size() == 1) {
            sb.append(", offset=");
            sb.append(offsets.get(0));
        } else {
            sb.append(", offsets=");
            for (Long offset : offsets) {
                sb.append("\n");
                addIndent(nestingLevel + 1, sb);
                sb.append(offset);
            }
        }
        return sb.toString();
    }
}
