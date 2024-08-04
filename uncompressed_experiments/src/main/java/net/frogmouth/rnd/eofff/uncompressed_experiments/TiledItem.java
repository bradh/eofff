package net.frogmouth.rnd.eofff.uncompressed_experiments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Tiled image item.
 *
 * <p>Experiment from OGC Testbed 20
 */
public class TiledItem {
    private static final int version = 0;
    private int flags;
    private int output_width;
    private int output_height;
    private int tile_width;
    private int tile_height;
    private FourCC tile_compression_type;
    private List<byte[]> extents = new ArrayList<>();

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public int getOutput_width() {
        return output_width;
    }

    public void setOutput_width(int output_width) {
        this.output_width = output_width;
    }

    public int getOutput_height() {
        return output_height;
    }

    public void setOutput_height(int output_height) {
        this.output_height = output_height;
    }

    public int getTile_width() {
        return tile_width;
    }

    public void setTile_width(int tile_width) {
        this.tile_width = tile_width;
    }

    public int getTile_height() {
        return tile_height;
    }

    public void setTile_height(int tile_height) {
        this.tile_height = tile_height;
    }

    public FourCC getTile_compression_type() {
        return tile_compression_type;
    }

    public void setTile_compression_type(FourCC tile_compression_type) {
        this.tile_compression_type = tile_compression_type;
    }

    public List<byte[]> getExtents() {
        return extents;
    }

    public long getLengthOfExtent(int i) {
        return extents.get(i).length;
    }

    public void addExtents(byte[] extent) {
        this.extents.add(extent);
    }

    public void writeTo(OutputStreamWriter stream) throws IOException {
        int headerSize = 0;
        stream.writeByte(version);
        headerSize += Byte.BYTES;
        stream.writeByte(flags);
        headerSize += Byte.BYTES;
        if ((flags & 0x01) == 0x01) {
            stream.writeLong(this.output_width);
            headerSize += Long.BYTES;
            stream.writeLong((this.output_height));
            headerSize += Long.BYTES;
        } else {
            stream.writeUnsignedInt32(this.output_width);
            headerSize += Integer.BYTES;
            stream.writeUnsignedInt32(this.output_height);
            headerSize += Integer.BYTES;
        }
        stream.writeUnsignedInt32(this.tile_width);
        headerSize += Integer.BYTES;
        stream.writeUnsignedInt32(this.tile_height);
        headerSize += Integer.BYTES;
        stream.writeFourCC(tile_compression_type);
        headerSize += FourCC.BYTES;
        int tileColumns = (this.output_width + this.tile_width - 1) / this.tile_width;
        int tileRows = (this.output_height + this.tile_height - 1) / this.tile_height;
        int numEntries = tileColumns * tileRows;
        if ((flags & 0x02) == 0x02) {
            headerSize += numEntries * Long.BYTES;
            long tile_start_offset = headerSize;
            for (int i = 0; i < numEntries; i++) {
                stream.writeLong(tile_start_offset);
                tile_start_offset += this.getLengthOfExtent(i);
            }
        } else {
            headerSize += numEntries * Integer.BYTES;
            long tile_start_offset = headerSize;
            for (int i = 0; i < numEntries; i++) {
                stream.writeUnsignedInt32(tile_start_offset);
                tile_start_offset += this.getLengthOfExtent(i);
            }
        }
        for (byte[] extent : extents) {
            stream.write(extent);
        }
    }
}
