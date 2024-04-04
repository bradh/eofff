package net.frogmouth.rnd.eofff.isobmff.subs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sub track information box (subs)
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.7
 */
public class SubSampleInformationBox extends FullBox {
    public static final FourCC SUBS_ATOM = new FourCC("subs");

    private final List<SubSampleEntry> entries = new ArrayList<>();

    public SubSampleInformationBox() {
        super(SUBS_ATOM);
    }

    @Override
    public String getFullName() {
        return "SubSampleInformationBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (SubSampleEntry entry : entries) {
            size += entry.getSize(this.getVersion());
        }
        return size;
    }

    public List<SubSampleEntry> getEntries() {
        return entries;
    }

    public void addEntry(SubSampleEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(entries.size());
        for (SubSampleEntry entry : entries) {
            entry.writeTo(stream, this.getVersion());
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("entry_count=");
        sb.append(this.entries.size());
        for (SubSampleEntry entry : entries) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            entry.writeTo(sb, nestingLevel + 1);
        }
        return sb.toString();
    }
}
