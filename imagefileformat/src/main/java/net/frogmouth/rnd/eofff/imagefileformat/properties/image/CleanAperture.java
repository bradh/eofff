package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;

public class CleanAperture extends ItemProperty {

    private long cleanApertureWidthN;
    private long cleanApertureWidthD;
    private long cleanApertureHeightN;
    private long cleanApertureHeightD;
    private long horizOffN;
    private long horizOffD;
    private long vertOffN;
    private long vertOffD;

    public static final FourCC CLAP_ATOM = new FourCC("clap");

    public CleanAperture() {
        super(CLAP_ATOM);
    }

    @Override
    public String getFullName() {
        return "CleanAperture";
    }

    public long getCleanApertureWidthN() {
        return cleanApertureWidthN;
    }

    public void setCleanApertureWidthN(long cleanApertureWidthN) {
        this.cleanApertureWidthN = cleanApertureWidthN;
    }

    public long getCleanApertureWidthD() {
        return cleanApertureWidthD;
    }

    public void setCleanApertureWidthD(long cleanApertureWidthD) {
        this.cleanApertureWidthD = cleanApertureWidthD;
    }

    public long getCleanApertureHeightN() {
        return cleanApertureHeightN;
    }

    public void setCleanApertureHeightN(long cleanApertureHeightN) {
        this.cleanApertureHeightN = cleanApertureHeightN;
    }

    public long getCleanApertureHeightD() {
        return cleanApertureHeightD;
    }

    public void setCleanApertureHeightD(long cleanApertureHeightD) {
        this.cleanApertureHeightD = cleanApertureHeightD;
    }

    public long getHorizOffN() {
        return horizOffN;
    }

    public void setHorizOffN(long horizOffN) {
        this.horizOffN = horizOffN;
    }

    public long getHorizOffD() {
        return horizOffD;
    }

    public void setHorizOffD(long horizOffD) {
        this.horizOffD = horizOffD;
    }

    public long getVertOffN() {
        return vertOffN;
    }

    public void setVertOffN(long vertOffN) {
        this.vertOffN = vertOffN;
    }

    public long getVertOffD() {
        return vertOffD;
    }

    public void setVertOffD(long vertOffD) {
        this.vertOffD = vertOffD;
    }

    @Override
    public long getBodySize() {
        return 8 * Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUnsignedInt32(this.cleanApertureWidthN);
        writer.writeUnsignedInt32(this.cleanApertureWidthD);
        writer.writeUnsignedInt32(this.cleanApertureHeightN);
        writer.writeUnsignedInt32(this.cleanApertureHeightD);
        writer.writeUnsignedInt32(this.getHorizOffN());
        writer.writeUnsignedInt32(this.getHorizOffD());
        writer.writeUnsignedInt32(this.getVertOffN());
        writer.writeUnsignedInt32(this.getVertOffD());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': cleanApertureWidthN=");
        sb.append(this.getCleanApertureWidthN());
        sb.append(", cleanApertureWidthD=");
        sb.append(this.getCleanApertureWidthD());
        sb.append(", cleanApertureHeightN=");
        sb.append(this.getCleanApertureHeightN());
        sb.append(", cleanApertureHeightD=");
        sb.append(this.getCleanApertureHeightD());
        sb.append(", horizOffN=");
        sb.append(this.getHorizOffN());
        sb.append(", horizOffD=");
        sb.append(this.getHorizOffD());
        sb.append(", vertOffN=");
        sb.append(this.getVertOffN());
        sb.append(", vertOffD=");
        sb.append(this.getVertOffD());
        return sb.toString();
    }
}
