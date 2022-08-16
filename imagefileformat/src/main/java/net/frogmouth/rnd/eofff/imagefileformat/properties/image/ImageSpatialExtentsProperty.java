package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class ImageSpatialExtentsProperty extends ItemFullProperty {

    private long imageWidth;
    private long imageHeight;

    public ImageSpatialExtentsProperty(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ImageSpatialExtentsProperty";
    }

    public long getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(long imageWidth) {
        this.imageWidth = imageWidth;
    }

    public long getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(long imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': image_width=");
        sb.append(getImageWidth());
        sb.append(", image_height=");
        sb.append(getImageHeight());
        sb.append(";");
        return sb.toString();
    }
}
