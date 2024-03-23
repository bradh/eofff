package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class AuxiliaryTypeProperty extends ItemFullProperty {

    public static final FourCC AUXC_ATOM = new FourCC("auxC");
    private String auxType;
    private byte[] auxSubtype;

    public AuxiliaryTypeProperty() {
        super(AUXC_ATOM);
    }

    @Override
    public String getFullName() {
        return "AuxiliaryTypeProperty";
    }

    public String getAuxType() {
        return auxType;
    }

    public void setAuxType(String auxType) {
        this.auxType = auxType;
    }

    public byte[] getAuxSubtype() {
        return auxSubtype;
    }

    public void setAuxSubtype(byte[] auxSubtype) {
        this.auxSubtype = auxSubtype;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("aux_type=");
        sb.append(getAuxType());
        sb.append(", aux_subtype=[");
        sb.append(HexFormat.of().withPrefix("0x").withDelimiter(" ").formatHex(getAuxSubtype()));
        sb.append("]");
        return sb.toString();
    }
}
