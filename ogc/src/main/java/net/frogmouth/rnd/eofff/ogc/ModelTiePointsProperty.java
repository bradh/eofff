package net.frogmouth.rnd.eofff.ogc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ModelTiePointsProperty extends ItemFullProperty {

    private final List<TiePoint> tiePoints = new ArrayList<>();
    public static final FourCC TIEP_ATOM = new FourCC("tiep");

    public ModelTiePointsProperty() {
        super(TIEP_ATOM);
        setFlags(0x01);
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
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
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("num_points: ");
        sb.append(this.tiePoints.size());
        for (TiePoint tiepoint : this.tiePoints) {
            sb.append("\n");
            addIndent(nestingLevel + 1, sb);
            tiepoint.addToStringBuilder(sb);
        }
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "ModelTiePoints";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += (tiePoints.size() * TiePoint.BYTES);
        return size;
    }
}
