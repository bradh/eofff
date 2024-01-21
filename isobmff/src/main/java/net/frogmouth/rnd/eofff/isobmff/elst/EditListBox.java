package net.frogmouth.rnd.eofff.isobmff.elst;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Edit List Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.6.
 */
public class EditListBox extends FullBox {

    public static final FourCC ELST_ATOM = new FourCC("elst");

    private final List<EditListBoxEntry> entries = new ArrayList<>();

    public EditListBox() {
        super(ELST_ATOM);
    }

    @Override
    public String getFullName() {
        return "EditListBox";
    }

    // @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (EditListBoxEntry entry : entries) {
            size += entry.getSize(getVersion());
        }
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (EditListBoxEntry entry : entries) {
            size += entry.getSize(getVersion());
        }
        return size;
    }

    public List<EditListBoxEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(EditListBoxEntry item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (EditListBoxEntry entry : entries) {
            entry.writeTo(stream, this.getVersion());
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("(version=");
        sb.append(getVersion());
        sb.append("): item_count=");
        sb.append(getEntries().size());
        for (EditListBoxEntry item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
