package net.frogmouth.rnd.eofff.isobmff.saio;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Auxiliary Information Offsets Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.7.9.
 */
public class SampleAuxiliaryInformationOffsetsBox extends FullBox {

    private FourCC auxInfoType;
    private long auxInfoTypeParameter;
    private long entryCount;
    private long[] offsets;

    public SampleAuxiliaryInformationOffsetsBox(FourCC name) {
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
        size += Integer.BYTES;
        if (getVersion() == 0) {
            size += (offsets.length * Integer.BYTES);
        } else {
            size += (offsets.length * Long.BYTES);
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

    public long getEntryCount() {
        return entryCount;
    }

    public void setEntryCount(long entryCount) {
        this.entryCount = entryCount;
    }

    public long[] getOffsets() {
        return offsets.clone();
    }

    public void setOffsets(long[] offsets) {
        this.offsets = offsets;
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
        stream.writeInt((int) entryCount);
        if (this.getVersion() == 0) {
            for (long l : offsets) {
                stream.writeInt((int) l);
            }
        } else {
            for (long l : offsets) {
                stream.writeLong(l);
            }
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
        // TODO: entry_count and offset[]
        return sb.toString();
    }
}
