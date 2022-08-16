package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ImageMirror extends ItemProperty {

    private int axis;

    public ImageMirror(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ImageRotation";
    }

    public int getAxis() {
        return axis;
    }

    public void setAxis(int axis) {
        this.axis = axis;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': axis=");
        sb.append(getAxis());
        if (getAxis() == 0x01) {
            sb.append(" (horizontal)");
        } else {
            sb.append(" (vertical)");
        }
        sb.append(";");
        return sb.toString();
    }
}
