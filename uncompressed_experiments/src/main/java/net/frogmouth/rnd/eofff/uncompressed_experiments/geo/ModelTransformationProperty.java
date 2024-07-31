package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ModelTransformationProperty extends AbstractUUIDProperty {

    private double m00;
    private double m01;
    private double m03;
    private double m10;
    private double m11;
    private double m13;

    // TODO: 3D case

    public ModelTransformationProperty() {
        setFlags(0x01);
    }

    public double getM00() {
        return m00;
    }

    public void setM00(double m00) {
        this.m00 = m00;
    }

    public double getM01() {
        return m01;
    }

    public void setM01(double m01) {
        this.m01 = m01;
    }

    public double getM03() {
        return m03;
    }

    public void setM03(double m03) {
        this.m03 = m03;
    }

    public double getM10() {
        return m10;
    }

    public void setM10(double m10) {
        this.m10 = m10;
    }

    public double getM11() {
        return m11;
    }

    public void setM11(double m11) {
        this.m11 = m11;
    }

    public double getM13() {
        return m13;
    }

    public void setM13(double m13) {
        this.m13 = m13;
    }

    @Override
    protected UUID getUUID() {
        return UUID.fromString("763cf838-b630-440b-84f8-be44bf9910af");
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        writeUUIDHeaderTo(writer);
        writer.writeDouble64(m00);
        writer.writeDouble64(m01);
        writer.writeDouble64(m03);
        writer.writeDouble64(m10);
        writer.writeDouble64(m11);
        writer.writeDouble64(m13);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        // TODO: output
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "ModelTransformation";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += 16;
        if ((this.getFlags() & 0x01) == 0x01) {
            size += 6 * Double.BYTES;
        } else {
            size += 12 * Double.BYTES;
        }
        return size;
    }
}
