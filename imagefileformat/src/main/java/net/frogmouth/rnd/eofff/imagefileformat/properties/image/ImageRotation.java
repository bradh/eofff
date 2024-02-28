package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;

public class ImageRotation extends ItemProperty {

    private int angle;

    public ImageRotation(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ImageRotation";
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("angle=");
        sb.append(90 * getAngle());
        sb.append(";");
        return sb.toString();
    }
}
