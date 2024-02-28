package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ImageSpatialExtentsProperty extends ItemFullProperty {
    public static final FourCC ISPE_ATOM = new FourCC("ispe");
    private long imageWidth;
    private long imageHeight;

    public ImageSpatialExtentsProperty() {
        super(ISPE_ATOM);
    }

    @Override
    public long getBodySize() {
        return 2 * Integer.BYTES;
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
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt32(imageWidth);
        writer.writeUnsignedInt32(imageHeight);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("image_width=");
        sb.append(getImageWidth());
        sb.append(", image_height=");
        sb.append(getImageHeight());
        return sb.toString();
    }
}
