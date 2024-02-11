package net.frogmouth.rnd.eofff.gopro.quicktime;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Base media info atom.
 *
 * <p>See https://developer.apple.com/documentation/quicktime-file-format/base_media_info_atom
 */
public class BaseMediaInfoAtom extends FullBox {
    public static final FourCC GMIN_ATOM = new FourCC("gmin");
    private int graphicsmode = 0;
    private int[] opcolor;
    private int balance;

    public BaseMediaInfoAtom() {
        super(GMIN_ATOM);
    }

    @Override
    public String getFullName() {
        return "Base media info atom";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Short.BYTES;
        size += (opcolor.length * Short.BYTES);
        size += Short.BYTES; // balance
        size += Short.BYTES; // reserved
        return size;
    }

    public int getGraphicsmode() {
        return graphicsmode;
    }

    public void setGraphicsmode(int graphicsmode) {
        this.graphicsmode = graphicsmode;
    }

    public int[] getOpcolor() {
        return opcolor;
    }

    public void setOpcolor(int[] opcolor) {
        this.opcolor = opcolor;
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
        stream.writeShort((short) graphicsmode);
        for (int i = 0; i < opcolor.length; i++) {
            stream.writeShort((short) opcolor[i]);
        }
        stream.writeShort((short) balance);
        stream.writeShort((short) 0);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("graphicsmode=");
        sb.append(getGraphicsmode());
        sb.append(", opcolor=");
        sb.append(
                String.format(
                        "rgb(%d, %d, %d)", getOpcolor()[0], getOpcolor()[1], getOpcolor()[2]));
        sb.append(", balance=");
        sb.append(balance);
        return sb.toString();
    }
}
