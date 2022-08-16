package net.frogmouth.rnd.eofff.isobmff.vmhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Video Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 12.1.2.
 */
public class VideoMediaHeaderBox extends FullBox {
    private int graphicsmode = 0;
    private int[] opcolor;

    public VideoMediaHeaderBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "VideoMediaHeaderBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Short.BYTES;
        size += (opcolor.length * Short.BYTES);
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

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeShort((short) graphicsmode);
        for (int i = 0; i < opcolor.length; i++) {
            stream.writeShort((short) opcolor[i]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': graphicsmode=");
        sb.append(getGraphicsmode());
        sb.append(", opcolor=");
        sb.append(
                String.format(
                        "rgb(%d, %d, %d)", getOpcolor()[0], getOpcolor()[1], getOpcolor()[2]));
        return sb.toString();
    }
}
