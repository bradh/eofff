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
 * <p>Declares source(s) of media data in track (in media information container - minf) or sources
 * of metadata items (in data information box - dinf).
 *
 * <p>See ISO/IEC 14496-12 Section 8.7.2.
 */
public class DataReferenceBox extends FullBox {
    public static final FourCC DREF_ATOM = new FourCC("dref");
    private final List<DataEntryBox> entries = new ArrayList<>();

    public DataReferenceBox() {
        super(DREF_ATOM);
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (DataEntryBox entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (DataEntryBox entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    @Override
    public String getFullName() {
        return "DataReferenceBox";
    }

    void addDataEntryBox(DataEntryBox dataEntryBox) {
        entries.add(dataEntryBox);
    }

    public List<DataEntryBox> getEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (DataEntryBox box : entries) {
            box.writeTo(stream);
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
        sb.append("): ");
        sb.append(entries.size());
        sb.append(" entry/entries:");
        for (DataEntryBox entry : entries) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  entry=");
            sb.append(entry.toString());
        }
        return sb.toString();
    }
}
