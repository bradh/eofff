package net.frogmouth.rnd.eofff.isobmff.stss;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Sync Sample Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.2.
 */
public class SyncSampleBox extends FullBox {
    private final List<Long> entries = new ArrayList<>();

    public SyncSampleBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "SyncSampleBox";
    }

    public List<Long> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(Long item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(entries.size()));
        for (long entry : entries) {
            stream.write(intToBytes((int) entry));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_count=");
        sb.append(getEntries().size());
        for (Long item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  sample_number=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
