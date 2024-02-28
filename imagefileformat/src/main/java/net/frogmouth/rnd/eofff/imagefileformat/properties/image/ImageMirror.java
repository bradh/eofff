package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;

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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("axis=");
        sb.append(getAxis());
        if (getAxis() == 0x01) {
            sb.append(" (horizontal)");
        } else {
            sb.append(" (vertical)");
        }
        return sb.toString();
    }
}
