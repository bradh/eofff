package net.frogmouth.rnd.eofff.isobmff.tref;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Track Reference Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.3.3.
 */
public class TrackReferenceBox extends BaseBox {

    private final List<TrackReferenceTypeBox> entries = new ArrayList<>();

    public TrackReferenceBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "TrackReferenceBox";
    }

    public List<TrackReferenceTypeBox> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(TrackReferenceTypeBox item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        for (TrackReferenceTypeBox entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("' : reference count=");
        sb.append(getEntries().size());
        for (TrackReferenceTypeBox item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
