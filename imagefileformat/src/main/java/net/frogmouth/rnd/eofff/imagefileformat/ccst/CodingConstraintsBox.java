package net.frogmouth.rnd.eofff.imagefileformat.ccst;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Coding constraints box.
 *
 * <p>See ISO/IEC 23008-12:2022 Section 7.2.3.
 */
public class CodingConstraintsBox extends FullBox {

    public static final FourCC CCST_ATOM = new FourCC("ccst");

    private boolean allRefPicsIntra;
    private boolean intraPredUsed;
    private byte maxRefPerPic;

    public CodingConstraintsBox() {
        super(CCST_ATOM);
    }

    public CodingConstraintsBox(FourCC name) {
        super(name);
    }

    public boolean isAllRefPicsIntra() {
        return allRefPicsIntra;
    }

    public void setAllRefPicsIntra(boolean allRefPicsIntra) {
        this.allRefPicsIntra = allRefPicsIntra;
    }

    public boolean isIntraPredUsed() {
        return intraPredUsed;
    }

    public void setIntraPredUsed(boolean intraPredUsed) {
        this.intraPredUsed = intraPredUsed;
    }

    public byte getMaxRefPerPic() {
        return maxRefPerPic;
    }

    public void setMaxRefPerPic(byte maxRefPerPic) {
        this.maxRefPerPic = maxRefPerPic;
    }

    @Override
    public String getFullName() {
        return "CodingConstraintsBox";
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        int flags = (maxRefPerPic & 0x0F) << 26;
        if (intraPredUsed) {
            flags |= (1 << 30);
        }
        if (allRefPicsIntra) {
            flags |= (1 << 31);
        }
        stream.writeInt(flags);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("all_ref_pics_intra=");
        sb.append(allRefPicsIntra);
        sb.append(", intra_pred_used=");
        sb.append(intraPredUsed);
        sb.append(", max_ref_per_pic=");
        sb.append(maxRefPerPic);
        return sb.toString();
    }
}
