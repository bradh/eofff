package net.frogmouth.rnd.eofff.isobmff.padb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Padding Bits Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.6.
 */
public class PaddingBitsBox extends FullBox {

    public static final FourCC PADB_ATOM = new FourCC("padb");

    List<Integer> padding = new ArrayList<>();

    public PaddingBitsBox() {
        super(PADB_ATOM);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES; // sample_count
        size += ((padding.size() + 1) / 2);
        return size;
    }

    @Override
    public String getFullName() {
        return "PaddingBitsBox";
    }

    public List<Integer> getPadding() {
        return padding;
    }

    public void addPadding(int v) {
        this.padding.add(v);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(padding.size());
        byte[] packedPadding = new byte[(padding.size() + 1) / 2];
        for (int i = 0; i < padding.size(); i++) {
            int offset = i / 2;
            if ((i % 2) == 0) {
                byte pad1 = (byte) ((padding.get(i) & 0x7) << 4);
                packedPadding[offset] |= pad1;
            } else {
                byte pad2 = (byte) (padding.get(i) & 0x7);
                packedPadding[offset] |= pad2;
            }
        }
        stream.write(packedPadding);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("padding=");
        sb.append(padding.toString());
        return sb.toString();
    }
}
