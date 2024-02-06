package net.frogmouth.rnd.eofff.isobmff.stts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Description Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.6.1.2.
 */
public class TimeToSampleBox extends FullBox {
    public static final FourCC STTS_ATOM = new FourCC("stts");
    private final List<TimeToSampleEntry> entries = new ArrayList<>();

    public TimeToSampleBox() {
        super(STTS_ATOM);
    }

    @Override
    public String getFullName() {
        return "TimeToSampleBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (TimeToSampleEntry entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    public List<TimeToSampleEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(TimeToSampleEntry item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (TimeToSampleEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("item_count=");
        sb.append(getEntries().size());
        for (TimeToSampleEntry item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
