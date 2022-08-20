package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Group Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.3.4.
 */
public class TrackGroupBox extends BaseBox {

    public static final FourCC TRGR_ATOM = new FourCC("trgr");
    private final List<TrackGroupTypeBox> entries = new ArrayList<>();

    public TrackGroupBox() {
        super(TRGR_ATOM);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        for (TrackGroupTypeBox group : entries) {
            size += group.getSize();
        }
        return size;
    }

    @Override
    public String getFullName() {
        return "TrackGroupBox";
    }

    public List<TrackGroupTypeBox> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(TrackGroupTypeBox item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBoxHeader(stream);
        for (TrackGroupTypeBox entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("' : group count=");
        sb.append(getEntries().size());
        for (TrackGroupTypeBox item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}