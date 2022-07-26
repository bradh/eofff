package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Track Group Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.3.4.
 */
public class TrackGroupBox extends BaseBox {

    private final List<TrackGroupTypeBox> entries = new ArrayList<>();

    public TrackGroupBox(long size, FourCC name) {
        super(size, name);
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
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
