package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AuxiliaryTypeProperty extends ItemFullProperty {

    private String auxType;
    private byte[] auxSubtype;

    public AuxiliaryTypeProperty(FourCC name) {
        super(name);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': aux_typ=");
        sb.append(getAuxType());
        sb.append(", aux_subtype=");
        sb.append(getAuxSubtype());
        sb.append(";");
        return sb.toString();
    }
}
