package net.frogmouth.rnd.eofff.isobmff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;

public class KrtdConverter {

    private double[][] kMatrix = null;
    private double[][] rMatrix = null;
    private double[] tVector = null;
    private double[] dVector = null;

    public KrtdConverter(File file) {
        try (FileReader reader = new FileReader(file);
                BufferedReader br = new BufferedReader(reader)) {
            kMatrix = parseMatrix(br);
            for (double[] row : kMatrix) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
            br.readLine();
            rMatrix = parseMatrix(br);
            for (double[] row : rMatrix) {
                System.out.println(Arrays.toString(row));
            }
            System.out.println();
            br.readLine();
            tVector = parseVector(br);
            System.out.println(Arrays.toString(tVector));
            System.out.println();
            br.readLine();
            dVector = parseVector(br);
            System.out.println(Arrays.toString(dVector));
            System.out.println();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private double[] parseVector(final BufferedReader br)
            throws IOException, NumberFormatException {
        String[] line = br.readLine().trim().split(" ");
        double[] vector = new double[line.length];
        for (int j = 0; j < line.length; j++) {
            vector[j] = Double.parseDouble(line[j]);
        }
        return vector;
    }

    private double[][] parseMatrix(final BufferedReader br)
            throws NumberFormatException, IOException {
        double[][] matrix = new double[3][3];
        for (int i = 0; i < matrix.length; i++) {
            String[] line = br.readLine().trim().split(" ");
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = Double.parseDouble(line[j]);
            }
        }
        return matrix;
    }

    public double getAbsGeodeticLat() {
        double originLat = 39.04977294;
        return Math.toRadians(originLat);
    }

    public double getAbsGeodeticLon() {
        double originLon = -85.52924953;
        return Math.toRadians(originLon);
    }

    public double getAbsGeodeticHae() {
        return 205;
    }

    public double getRelPositionX() {
        return tVector[1];
    }

    public double getRelPositionY() {
        return tVector[0];
    }

    public double getRelPositionZ() {
        return tVector[2];
    }

    private double[] getRotationAngles() {
        Rotation rotation = new Rotation(rMatrix, 0.000001);
        return rotation.getAngles(RotationOrder.XYZ, RotationConvention.VECTOR_OPERATOR);
    }

    public static void main(String argc[]) {
        KrtdConverter krtd =
                new KrtdConverter(
                        new File(
                                "/home/bradh/meva/meva-data-repo/metadata/camera-models/krtd/2018-03-07.11-45-09.11-50-09.bus.G340.krtd"));
        System.out.println(
                String.format(
                        "Absolute stage: [%f, %f, %f]",
                        krtd.getAbsGeodeticLat(),
                        krtd.getAbsGeodeticLon(),
                        krtd.getAbsGeodeticHae()));
        System.out.println(
                String.format(
                        "Relative stage: [%f, %f, %f]",
                        krtd.getRelPositionX(), krtd.getRelPositionY(), krtd.getRelPositionZ()));
        double[] rotationAngles = krtd.getRotationAngles();
        System.out.println(
                String.format(
                        "Rotation stage: [%f, %f, %f]",
                        rotationAngles[0], rotationAngles[1], rotationAngles[2]));
    }
}
