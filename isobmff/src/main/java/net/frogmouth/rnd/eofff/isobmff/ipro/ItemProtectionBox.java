package net.frogmouth.rnd.eofff.isobmff.ipro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.sinf.ProtectionSchemeInfoBox;

/**
 * Item Protection Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.11.5.
 */
public class ItemProtectionBox extends FullBox {
    public static final FourCC IPRO_ATOM = new FourCC("ipro");

    private final List<ProtectionSchemeInfoBox> protectionSchemeInfoBoxes = new ArrayList<>();

    public ItemProtectionBox() {
        super(IPRO_ATOM);
    }

    @Override
    public String getFullName() {
        return "ItemProtectionBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        // TODO
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (ProtectionSchemeInfoBox sinf : this.protectionSchemeInfoBoxes) {
            sb.append("\n");
            sb.append(sinf.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    public void appendProtectionSchemeInfoBox(ProtectionSchemeInfoBox sinf) {
        this.protectionSchemeInfoBoxes.add(sinf);
    }
}
