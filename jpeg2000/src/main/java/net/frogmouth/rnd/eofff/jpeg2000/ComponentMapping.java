package net.frogmouth.rnd.eofff.jpeg2000;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ComponentMapping {
    public static final int BYTES = Short.BYTES + Byte.BYTES + Byte.BYTES;

    private final int cmp;
    private final int mtyp;
    private final int pcol;

    public ComponentMapping(int cmp, int mtyp, int pcol) {
        this.cmp = cmp;
        this.mtyp = mtyp;
        this.pcol = pcol;
    }

    public int getCmp() {
        return cmp;
    }

    public int getMtyp() {
        return mtyp;
    }

    public int getPcol() {
        return pcol;
    }

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt16(cmp);
        writer.writeByte(mtyp);
        writer.writeByte(pcol);
    }
}
