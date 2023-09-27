package net.frogmouth.rnd.eofff.uncompressed.pmdp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemFullProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ImagePyramidInformationProperty extends ItemFullProperty {

    public static final FourCC PMDP_ATOM = new FourCC("pmdp");

    private int tile_size_x;
    private int tile_size_y;
    private List<ImagePyramidLayer> layers = new ArrayList<>();

    public ImagePyramidInformationProperty() {
        super(PMDP_ATOM);
    }

    @Override
    public String getFullName() {
        return "ImagePyramidInformationProperty";
    }

    @Override
    public long getBodySize() {
        long size = Byte.BYTES + Short.BYTES + Short.BYTES;
        for (ImagePyramidLayer layer : layers) {
            size += layer.getNumberOfBytes();
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        // TODO: write body
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        sb.append("TODO");
        return sb.toString();
    }
}
