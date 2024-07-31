package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class GeoKeyProperty extends AbstractUUIDProperty {

    private int modelType;
    private int rasterType;
    private int projectedCRS;

    // TODO: other CRS keys

    public GeoKeyProperty() {}

    @Override
    protected UUID getUUID() {
        return UUID.fromString("b42ba770-439d-4622-8b9e-b3d38a039922");
    }

    public int getModelType() {
        return modelType;
    }

    public void setModelType(int modelType) {
        this.modelType = modelType;
    }

    public int getRasterType() {
        return rasterType;
    }

    public void setRasterType(int rasterType) {
        this.rasterType = rasterType;
    }

    public int getProjectedCRS() {
        return projectedCRS;
    }

    public void setProjectedCRS(int projectedCRS) {
        this.projectedCRS = projectedCRS;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        writeUUIDHeaderTo(writer);
        writer.writeUnsignedInt16(modelType);
        writer.writeUnsignedInt16(rasterType);
        writer.writeUnsignedInt16(projectedCRS);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        // TODO: output
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "GeoKey";
    }

    @Override
    public long getBodySize() {
        return 16 + 1 + 1 + 2;
    }
}
