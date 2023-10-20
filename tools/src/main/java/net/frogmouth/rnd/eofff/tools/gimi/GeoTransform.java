package net.frogmouth.rnd.eofff.tools.gimi;

import java.util.List;

public class GeoTransform {
    double p0 = 0.0;
    double p1 = 1.0;
    double p2 = 0.0;
    double p3 = 0.0;
    double p4 = 0.0;
    double p5 = 1.0;
    boolean validScale = false;
    boolean validTiePoints = false;

    public GeoTransform(List<Double> modelTiePoint, List<Double> pixelScale) {
        if ((pixelScale != null) && (pixelScale.size() >= 2)) {
            p1 = pixelScale.get(0);
            p5 = -1.0 * pixelScale.get(1);
            validScale = true;
        }
        if ((modelTiePoint != null) && (modelTiePoint.size() >= 6)) {
            p0 = modelTiePoint.get(3) - modelTiePoint.get(0) * p1;
            p3 = modelTiePoint.get(4) - modelTiePoint.get(1) * p5;
            validTiePoints = true;
        }
        // TODO: adjust if pixelIsPoint
    }

    public boolean isValid() {
        return validScale && validTiePoints;
    }

    public double getLatitude(int x, int y) {
        return p3 + p4 * x + p5 * y;
    }

    public double getLongitude(int x, int y) {
        return p0 + p1 * x + p2 * y;
    }
}
