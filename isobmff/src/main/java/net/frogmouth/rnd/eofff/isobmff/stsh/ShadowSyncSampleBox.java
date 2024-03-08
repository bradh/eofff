package net.frogmouth.rnd.eofff.isobmff.stsh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Shadow Sync Sample Box (stsh)
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.6.3.2
 */
public class ShadowSyncSampleBox extends FullBox {
    public static final FourCC STSH_ATOM = new FourCC("stsh");

    private final List<ShadowSyncSampleEntry> entries = new ArrayList<>();

    public ShadowSyncSampleBox() {
        super(STSH_ATOM);
    }

    @Override
    public String getFullName() {
        return "ShadowSyncSampleBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (ShadowSyncSampleEntry entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    public List<ShadowSyncSampleEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(ShadowSyncSampleEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (ShadowSyncSampleEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (ShadowSyncSampleEntry item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
