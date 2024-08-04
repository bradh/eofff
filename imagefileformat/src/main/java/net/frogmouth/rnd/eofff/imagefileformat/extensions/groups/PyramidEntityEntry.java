package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record PyramidEntityEntry(
        int layerBinning, int tilesInLayerRowMinus1, int tilesInLayerColumnMinus1) {

    public static final int BYTES = Short.BYTES + Short.BYTES + Short.BYTES;

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt16(layerBinning);
        writer.writeUnsignedInt16(tilesInLayerRowMinus1);
        writer.writeUnsignedInt16(tilesInLayerColumnMinus1);
    }

    public void addTo(StringBuilder sb) {
        sb.append("layer_binning=");
        sb.append(this.layerBinning);
        sb.append(", tiles_in_layer_row_minus1=");
        sb.append(this.tilesInLayerRowMinus1);
        sb.append(", tiles_in_layer_column_minus1=");
        sb.append(this.tilesInLayerColumnMinus1);
    }
}
