package net.frogmouth.rnd.eofff.imagefileformat.items.grid;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Image Grid Item.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 6.6.2.3.
 */
public class GridItem {
    private static final int version = 0;
    private int rows;
    private int columns;
    private long output_width;
    private long output_height;

    static final long MAX_UNSIGNED_16_BITS = 65535;
    private static final int LONG_FORMAT_FLAG = 0x01;

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public long getOutput_width() {
        return output_width;
    }

    public void setOutput_width(long output_width) {
        this.output_width = output_width;
    }

    public long getOutput_height() {
        return output_height;
    }

    public void setOutput_height(long output_height) {
        this.output_height = output_height;
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeByte(version);
        int flags = 0;
        if (needsLongFormat()) {
            flags |= LONG_FORMAT_FLAG;
        }
        stream.writeByte(flags);
        stream.writeByte(rows - 1);
        stream.writeByte(columns - 1);
        boolean longForm = ((flags & LONG_FORMAT_FLAG) == LONG_FORMAT_FLAG);

        if (longForm) {
            stream.writeUnsignedInt32(this.output_width);
            stream.writeUnsignedInt32(this.output_height);
        } else {
            stream.writeUnsignedInt16(this.output_width);
            stream.writeUnsignedInt16(this.output_height);
        }
    }

    private boolean needsLongFormat() {
        return (output_width > MAX_UNSIGNED_16_BITS) || (output_height > MAX_UNSIGNED_16_BITS);
    }
}
