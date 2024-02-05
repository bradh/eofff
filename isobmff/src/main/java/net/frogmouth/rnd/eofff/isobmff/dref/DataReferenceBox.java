package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Data Reference Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.2.
 */
public class DataReferenceBox extends FullBox {
    public static final FourCC DREF_ATOM = new FourCC("dref");
    private final List<DataEntryBaseBox> entries = new ArrayList<>();

    public DataReferenceBox() {
        super(DREF_ATOM);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (DataEntryBaseBox entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    @Override
    public String getFullName() {
        return "DataReferenceBox";
    }

    void addDataReference(DataEntryBaseBox dataEntryBox) {
        entries.add(dataEntryBox);
    }

    public List<DataEntryBaseBox> getEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (DataEntryBaseBox box : entries) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("(version=");
        sb.append(getVersion());
        sb.append("): ");
        sb.append(entries.size());
        sb.append(" entry/entries:");
        for (DataEntryBaseBox entry : entries) {
            sb.append("\n");
            sb.append(entry.toString(nestingLevel + 1));
        }
        return sb.toString();
    }
}
