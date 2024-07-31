package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ModelTiePointsProperty extends AbstractUUIDProperty {

    private final List<TiePoint> tiePoints = new ArrayList<>();

    public ModelTiePointsProperty() {
        setFlags(0x01);
    }

    @Override
    protected UUID getUUID() {
        return UUID.fromString("c683364f-d6a4-48b8-a76b-17a30af40c10");
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        writeUUIDHeaderTo(writer);
        writer.writeUnsignedInt16(tiePoints.size());
        for (TiePoint tiePoint : tiePoints) {
            tiePoint.writeTo(writer);
        }
    }

    public void addTiePoint(TiePoint tiePoint) {
        tiePoints.add(tiePoint);
    }

    public List<TiePoint> getTiePoints() {
        return tiePoints;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        // TODO: output
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "ModelTiePoints";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += 16;
        size += Short.BYTES;
        size += (tiePoints.size() * TiePoint.BYTES);
        return size;
    }
}
