package net.frogmouth.rnd.eofff.isobmff.elst;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Edit List Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.6.
 */
public class EditListBox extends FullBox {
    private final List<EditListBoxEntry> entries = new ArrayList<>();

    public EditListBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "EditListBox";
    }

    public List<EditListBoxEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(EditListBoxEntry item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(entries.size()));
        for (EditListBoxEntry entry : entries) {
            entry.writeTo(stream, this.getVersion());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("' (version=");
        sb.append(getVersion());
        sb.append("): item_count=");
        sb.append(getEntries().size());
        for (EditListBoxEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
