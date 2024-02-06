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
 * <p>See ISO/IEC 14496-12:2022 Section 8.6.2.
 */
public class SyncSampleBox extends FullBox {

    public static final FourCC STSS_ATOM = new FourCC("stss");

    private final List<Long> entries = new ArrayList<>();

    public SyncSampleBox() {
        super(STSS_ATOM);
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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("item_count=");
        sb.append(getEntries().size());
        for (Long item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append("sample_number=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
