package net.frogmouth.rnd.eofff.ogc;

import static net.frogmouth.rnd.eofff.ogc.ModelTiePointsProperty.TIEP_ATOM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ModelTiePoints3DProperty extends ItemFullProperty {

    private final List<TiePoint3D> tiePoints = new ArrayList<>();

    public ModelTiePoints3DProperty() {
        super(TIEP_ATOM);
        setFlags(0x00);
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt16(tiePoints.size());
        for (TiePoint3D tiePoint : tiePoints) {
            tiePoint.writeTo(writer);
        }
    }

    public void addTiePoint3D(TiePoint3D tiePoint) {
        tiePoints.add(tiePoint);
    }

    public List<TiePoint3D> getTiePoint3Ds() {
        return tiePoints;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("num_points: ");
        sb.append(this.tiePoints.size());
        for (TiePoint3D tiepoint : this.tiePoints) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            tiepoint.addToStringBuilder(sb);
        }
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "ModelTiePoints3D";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += (tiePoints.size() * TiePoint3D.BYTES);
        return size;
    }
}
