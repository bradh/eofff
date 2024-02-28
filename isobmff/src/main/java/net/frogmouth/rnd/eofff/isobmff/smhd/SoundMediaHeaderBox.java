package net.frogmouth.rnd.eofff.isobmff.smhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sound Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.2.2.
 */
public class SoundMediaHeaderBox extends FullBox {
    public static final FourCC SMHD_ATOM = new FourCC("smhd");
    private int balance = 0;

    public SoundMediaHeaderBox() {
        super(SMHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "SoundMediaHeaderBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += Short.BYTES;
        return size;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeShort((short) balance);
        stream.writeShort((short) 0);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("balance=");
        sb.append(balance);
        return sb.toString();
    }
}
