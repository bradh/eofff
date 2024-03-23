package net.frogmouth.rnd.eofff.isobmff.sgpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.samplegroup.SampleGroupEntry;

/**
 * Sample Group Description Box Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.3.
 */
// TODO: this needs to treat sample group description entries as a namespace
public class SampleGroupDescriptionBox extends FullBox {

    public static final FourCC SGPD_ATOM = new FourCC("sgpd");

    private final List<SampleGroupEntry> entries = new ArrayList<>();

    public SampleGroupDescriptionBox() {
        super(SGPD_ATOM);
    }

    @Override
    public long getBodySize() {
        // TODO
        return 0;
    }

    @Override
    public String getFullName() {
        return "SampleGroupDescriptionBox";
    }

    public List<SampleGroupEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(SampleGroupEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (var entry : entries) {
            sb.append("\n");
            if (entry == null) {
                sb.append("[null box - need to add]");
            } else {
                sb.append(entry.toString(nestingLevel + 1));
            }
        }
        return sb.toString();
    }
}
