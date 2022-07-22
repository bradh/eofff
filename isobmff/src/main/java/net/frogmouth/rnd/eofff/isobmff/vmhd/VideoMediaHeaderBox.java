package net.frogmouth.rnd.eofff.isobmff.vmhd;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Video Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 12.1.2.
 */
public class VideoMediaHeaderBox extends FullBox {
    private int graphicsmode = 0;
    private int[] opcolor;

    public VideoMediaHeaderBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "VideoMediaHeaderBox";
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(shortToBytes((short) graphicsmode));
        for (int i = 0; i < opcolor.length; i++) {
            stream.write(shortToBytes((short) opcolor[i]));
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
