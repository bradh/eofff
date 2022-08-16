package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': angle=");
        sb.append(90 * getAngle());
        sb.append(";");
        return sb.toString();
    }
}
