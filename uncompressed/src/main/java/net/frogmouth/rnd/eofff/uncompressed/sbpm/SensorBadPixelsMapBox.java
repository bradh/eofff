package net.frogmouth.rnd.eofff.uncompressed.sbpm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

/**
 * Sensor Bad Pixel Map Box.
 *
 * <p>See ISO/IEC 23001-17 (DIS) Section 6.1.7.
 */
public class SensorBadPixelsMapBox extends ItemFullProperty {

    public static final FourCC SBPM_ATOM = new FourCC("sbpm");
    private final List<Integer> componentIndexes = new ArrayList<>();
    private boolean correctionApplied;
    private List<Long> badRows = new ArrayList<>();
    private List<Long> badColumns = new ArrayList<>();
    private List<PixelCoordinate> badPixels = new ArrayList<>();

    public SensorBadPixelsMapBox() {
        super(SBPM_ATOM);
    }

    @Override
    public String getFullName() {
        return "SensorBadPixelsMap";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': corrections applied: ");
        sb.append(this.correctionApplied ? "true" : "false");
        sb.append(", bad rows: ");
        sb.append(this.badRows.toString());
        sb.append(", bad columns: ");
        sb.append(this.badColumns.toString());
        sb.append(", bad pixels: ");
        sb.append(this.badPixels.toString());
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long count = 0;
        count += Short.BYTES;
        count += (this.componentIndexes.size() * Short.BYTES);
        count += Byte.BYTES;
        count += Integer.BYTES;
        count += Integer.BYTES;
        count += Integer.BYTES;
        count += (Integer.BYTES * this.badRows.size());
        count += (Integer.BYTES * this.badColumns.size());
        count += (2 * Integer.BYTES * this.badPixels.size());
        return count;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(this.componentIndexes.size());
        for (int i = 0; i < this.componentIndexes.size(); i++) {
            stream.writeUnsignedInt16(this.componentIndexes.get(i));
        }
        stream.writeByte(correctionApplied ? (byte) 0x80 : 0x00);
        stream.writeUnsignedInt32(badRows.size());
        stream.writeUnsignedInt32(badColumns.size());
        stream.writeUnsignedInt32(badPixels.size());
        for (Long row : badRows) {
            stream.writeUnsignedInt32(row);
        }
        for (Long column : badColumns) {
            stream.writeUnsignedInt32(column);
        }
        for (PixelCoordinate pixelCoordinate : badPixels) {
            stream.writeUnsignedInt32(pixelCoordinate.row());
            stream.writeUnsignedInt32(pixelCoordinate.column());
        }
    }

    public List<Integer> getComponentIndexes() {
        return new ArrayList<>(componentIndexes);
    }

    public void addComponentIndex(int component_index) {
        this.componentIndexes.add(component_index);
    }

    public boolean isCorrectionApplied() {
        return correctionApplied;
    }

    public void setCorrectionApplied(boolean correctionApplied) {
        this.correctionApplied = correctionApplied;
    }

    public List<Long> getBadRows() {
        return badRows;
    }

    public void addBadRow(long badRow) {
        this.badRows.add(badRow);
    }

    public List<Long> getBadColumns() {
        return badColumns;
    }

    public void addBadColumn(long badColumn) {
        this.badColumns.add(badColumn);
    }

    public List<PixelCoordinate> getBadPixels() {
        return badPixels;
    }

    public void addBadPixel(PixelCoordinate badPixel) {
        this.badPixels.add(badPixel);
    }
}
