/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public abstract class Poly implements Region {

    protected final List<Point> points = new ArrayList<>();

    public Poly() {}

    public List<Point> getPoints() {
        return points;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    @Override
    public abstract GeometryType getGeometryType();

    @Override
    public boolean needsLongFormat() {
        if (points.size() > RegionItem.MAX_UNSIGNED_16_BITS) {
            return true;
        }
        boolean needsLong = false;
        for (Point point : points) {
            if (point.needsLongFormat()) {
                needsLong = true;
                break;
            }
        }
        return needsLong;
    }

    @Override
    public void writeTo(OutputStreamWriter stream, boolean useLongForm) throws IOException {
        if (useLongForm) {
            stream.writeUnsignedInt32(points.size());
        } else {
            stream.writeUnsignedInt16(points.size());
        }
        for (Point point : points) {
            point.writeTo(stream, useLongForm);
        }
    }
}
