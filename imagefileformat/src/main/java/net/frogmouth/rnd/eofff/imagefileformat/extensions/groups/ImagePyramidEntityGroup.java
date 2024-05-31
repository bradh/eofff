package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.grpl.AbstractEntityToGroupBox;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroup;

public class ImagePyramidEntityGroup extends AbstractEntityToGroupBox implements EntityToGroup {

    private int tileSizeX;
    private int tileSizeY;
    private final List<PyramidEntityEntry> entities = new ArrayList<>();

    public static final FourCC PYMD_ATOM = new FourCC("pymd");

    public ImagePyramidEntityGroup() {
        super(PYMD_ATOM);
    }

    @Override
    public String getFullName() {
        return "ImagePyramid";
    }

    public int getTileSizeX() {
        return tileSizeX;
    }

    public void setTileSizeX(int tileSizeX) {
        this.tileSizeX = tileSizeX;
    }

    public int getTileSizeY() {
        return tileSizeY;
    }

    public void setTileSizeY(int tileSizeY) {
        this.tileSizeY = tileSizeY;
    }

    public List<PyramidEntityEntry> getPyramidEntries() {
        return new ArrayList<>(entities);
    }

    public void addPyramidEntry(PyramidEntityEntry entry) {
        this.entities.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        super.writeTo(writer);
        writer.writeUnsignedInt16(this.tileSizeX);
        writer.writeUnsignedInt16(this.tileSizeY);
        for (PyramidEntityEntry entry : this.entities) {
            entry.writeTo(writer);
        }
    }

    @Override
    public long getBodySize() {
        long size = super.getBodySize();
        size += Short.BYTES;
        size += Short.BYTES;
        size += (this.entities.size() * PyramidEntityEntry.BYTES);
        return size;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        sb.append(", tile_size_x=");
        sb.append(this.tileSizeX);
        sb.append(", tile_size_y=");
        sb.append(this.tileSizeY);
        sb.append(", entities:");
        for (PyramidEntityEntry entry : this.entities) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            entry.addTo(sb);
        }
        return sb.toString();
    }
}
