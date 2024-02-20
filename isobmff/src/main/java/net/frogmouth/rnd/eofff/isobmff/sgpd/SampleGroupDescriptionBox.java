package net.frogmouth.rnd.eofff.isobmff.sgpd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Group Description Box Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.9.3.
 */
// TODO: this needs to treat sample group description entries as a namespace
public class SampleGroupDescriptionBox extends FullBox {

    public static final FourCC SGPD_ATOM = new FourCC("sgpd");

    private final List<SampleGroupDescriptionEntry> entries = new ArrayList<>();

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

    public List<SampleGroupDescriptionEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(SampleGroupDescriptionEntry entry) {
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
        sb.append("TODO");
        return sb.toString();
    }
}
