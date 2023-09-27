package net.frogmouth.rnd.eofff.uncompressed.pmdp;

public class ImagePyramidLayer {

    private final byte layer_num;
    private final int item_ID;
    private final int layer_binning;
    private final int tiles_in_layer_row;
    private final int tiles_in_layer_column;

    public ImagePyramidLayer(
            byte layer_num,
            int item_ID,
            int layer_binning,
            int tiles_in_layer_row,
            int tiles_in_layer_column) {
        this.layer_num = layer_num;
        this.item_ID = item_ID;
        this.layer_binning = layer_binning;
        this.tiles_in_layer_row = tiles_in_layer_row;
        this.tiles_in_layer_column = tiles_in_layer_column;
    }

    public byte getLayer_num() {
        return layer_num;
    }

    public int getItem_ID() {
        return item_ID;
    }

    public int getLayer_binning() {
        return layer_binning;
    }

    public int getTiles_in_layer_row() {
        return tiles_in_layer_row;
    }

    public int getTiles_in_layer_column() {
        return tiles_in_layer_column;
    }

    public long getNumberOfBytes() {
        // TODO: work out really how
        return 0;
    }
}
