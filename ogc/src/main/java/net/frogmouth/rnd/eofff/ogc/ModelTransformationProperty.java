package net.frogmouth.rnd.eofff.ogc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class ModelTransformationProperty extends ItemFullProperty {

    public static final FourCC MTXF_ATOM = new FourCC("mtxf");
    private double m00;
    private double m01;
    private double m03;
    private double m10;
    private double m11;
    private double m13;

    // TODO: 3D case

    public ModelTransformationProperty() {
        super(MTXF_ATOM);
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
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeDouble64(m00);
        writer.writeDouble64(m01);
        writer.writeDouble64(m03);
        writer.writeDouble64(m10);
        writer.writeDouble64(m11);
        writer.writeDouble64(m13);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        if ((getFlags() & 0x01) == 0x01) {
            sb.append("2D, ");
        } else {
            sb.append("3D, ");
        }
        sb.append("m00=");
        sb.append(m00);
        sb.append(", m01=");
        sb.append(m01);
        sb.append(", m03=");
        sb.append(m03);
        sb.append(", m10=");
        sb.append(m10);
        sb.append(", m11=");
        sb.append(m11);
        sb.append(", m13=");
        sb.append(m13);

        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "ModelTransformation";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if ((this.getFlags() & 0x01) == 0x01) {
            size += 6 * Double.BYTES;
        } else {
            size += 12 * Double.BYTES;
        }
        return size;
    }
}
