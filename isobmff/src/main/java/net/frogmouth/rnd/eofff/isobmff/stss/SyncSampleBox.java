package net.frogmouth.rnd.eofff.isobmff.stss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sync Sample Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.2.
 */
public class SyncSampleBox extends FullBox {

    public static final FourCC STSS_ATOM = new FourCC("stss");

    private final List<Long> entries = new ArrayList<>();

    public SyncSampleBox() {
        super(STSS_ATOM);
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        size += Integer.BYTES * entries.size();
        return size;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES + (Integer.BYTES * entries.size());
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
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (long entry : entries) {
            stream.writeInt((int) entry);
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
