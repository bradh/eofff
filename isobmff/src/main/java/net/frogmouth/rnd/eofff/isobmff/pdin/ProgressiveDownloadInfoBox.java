package net.frogmouth.rnd.eofff.isobmff.pdin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Progressive Download Info Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.3.
 */
public class ProgressiveDownloadInfoBox extends FullBox {
    public static final FourCC PDIN_ATOM = new FourCC("pdin");

    private List<ProgressiveDownloadInfoBoxEntry> entries = new ArrayList<>();

    public ProgressiveDownloadInfoBox() {
        super(PDIN_ATOM);
    }

    @Override
    public String getFullName() {
        return "ProgressiveDownloadInfoBox";
    }

    @Override
    public long getBodySize() {
        return entries.size() * ProgressiveDownloadInfoBoxEntry.BYTES;
    }

    public List<ProgressiveDownloadInfoBoxEntry> getEntries() {
        return entries;
    }

    public void addEntry(ProgressiveDownloadInfoBoxEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        for (ProgressiveDownloadInfoBoxEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (ProgressiveDownloadInfoBoxEntry entry : entries) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(entry.toString());
        }
        return sb.toString();
    }
}
