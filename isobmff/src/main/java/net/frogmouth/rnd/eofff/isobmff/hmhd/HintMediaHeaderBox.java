package net.frogmouth.rnd.eofff.isobmff.hmhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.4.3.
 */
public class HintMediaHeaderBox extends FullBox {

    public static final FourCC HMHD_ATOM = new FourCC("hmhd");

    private int maxPDUsize;
    private int avgPDUsize;
    private long maxbitrate;
    private long avgbitrate;

    public HintMediaHeaderBox() {
        super(HMHD_ATOM);
    }

    public int getMaxPDUsize() {
        return maxPDUsize;
    }

    public void setMaxPDUsize(int maxPDUsize) {
        this.maxPDUsize = maxPDUsize;
    }

    public int getAvgPDUsize() {
        return avgPDUsize;
    }

    public void setAvgPDUsize(int avgPDUsize) {
        this.avgPDUsize = avgPDUsize;
    }

    public long getMaxbitrate() {
        return maxbitrate;
    }

    public void setMaxbitrate(long maxbitrate) {
        this.maxbitrate = maxbitrate;
    }

    public long getAvgbitrate() {
        return avgbitrate;
    }

    public void setAvgbitrate(long avgbitrate) {
        this.avgbitrate = avgbitrate;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Integer.BYTES;
        size += Integer.BYTES;
        size += Integer.BYTES; // reserved
        return size;
    }

    @Override
    public String getFullName() {
        return "HintMediaHeaderBox";
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt16(maxPDUsize);
        stream.writeUnsignedInt16(avgPDUsize);
        stream.writeUnsignedInt32(maxbitrate);
        stream.writeUnsignedInt32(avgbitrate);
        stream.writeUnsignedInt32(0); // reserved
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("maxPDUsize=");
        sb.append(maxPDUsize);
        sb.append(", avgPDUsize=");
        sb.append(avgPDUsize);
        sb.append(", maxbitrate=");
        sb.append(maxbitrate);
        sb.append(", avgbitrate=");
        sb.append(avgbitrate);
        return sb.toString();
    }
}
