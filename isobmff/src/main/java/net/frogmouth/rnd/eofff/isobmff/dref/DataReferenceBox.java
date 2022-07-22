package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Data Reference Box.
 *
 * <p>Declares source(s) of media data in track (in media information container - minf) or sources
 * of metadata items (in data information box - dinf).
 *
 * <p>See ISO/IEC 14496-12 Section 8.7.2.
 */
public class DataReferenceBox extends FullBox {

    public List<DataEntryBox> entries = new ArrayList<>();

    public DataReferenceBox(long size, FourCC name) {
        super(size, name);
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(entries.size()));
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
