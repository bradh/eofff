package net.frogmouth.rnd.eofff.ogc;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullProperty;

public class CoordinateReferenceSystemProperty extends ItemFullProperty {

    public FourCC crsEncoding;
    private String crs;
    private float epoch;
    public static final FourCC MCRS_ATOM = new FourCC("mcrs");

    public CoordinateReferenceSystemProperty() {
        super(MCRS_ATOM);
    }

    public FourCC getCrsEncoding() {
        return crsEncoding;
    }

    public void setCrsEncoding(FourCC crsEncoding) {
        this.crsEncoding = crsEncoding;
    }

    public String getCrs() {
        return crs;
    }

    public void setCrs(String crs) {
        this.crs = crs;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeFourCC(crsEncoding);
        writer.writeNullTerminatedString(crs);
        if ((this.getFlags() & 0x01) == 0x01) {
            writer.writeDouble32(epoch);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("crsEncoding=");
        sb.append(crsEncoding.toString());
        sb.append(", crs=");
        sb.append(crs);
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "CoordinateReferenceSystem";
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += FourCC.BYTES;
        size += crs.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        if ((this.getFlags() & 0x01) == 0x01) {
            size += Float.BYTES;
        }
        return size;
    }

    public void setEpoch(double d) {
        this.setFlags(1);
        this.epoch = (float) d;
    }
}
