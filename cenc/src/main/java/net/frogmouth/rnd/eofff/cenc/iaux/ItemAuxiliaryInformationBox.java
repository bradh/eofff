package net.frogmouth.rnd.eofff.cenc.iaux;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ItemAuxiliaryInformationBox extends ItemFullProperty {
    public static final FourCC IAUX_ATOM = new FourCC("iaux");

    private FourCC auxInfoType;
    private long auxInfoTypeParameter;

    public ItemAuxiliaryInformationBox() {
        super(IAUX_ATOM);
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

    @Override
    public long getBodySize() {
        int size = 0;
        size += FourCC.BYTES;
        size += Integer.BYTES;
        return size;
    }

    @Override
    public String getFullName() {
        return "ItemAuxiliaryInformationBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeFourCC(auxInfoType);
        writer.writeUnsignedInt32(auxInfoTypeParameter);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("aux_info_type=");
        sb.append(auxInfoType.toString());
        sb.append(", aux_info_type_parameter=");
        sb.append(auxInfoTypeParameter);
        return sb.toString();
    }
}
