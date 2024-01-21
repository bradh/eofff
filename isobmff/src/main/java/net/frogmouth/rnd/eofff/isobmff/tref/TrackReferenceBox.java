package net.frogmouth.rnd.eofff.isobmff.tref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Reference Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.3.3.
 */
public class TrackReferenceBox extends BaseBox {
    public static final FourCC TREF_FOURCC = new FourCC("tref");
    private final List<TrackReferenceTypeBox> entries = new ArrayList<>();

    public TrackReferenceBox() {
        super(TREF_FOURCC);
    }

    @Override
    public String getFullName() {
        return "TrackReferenceBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        for (TrackReferenceTypeBox reference : entries) {
            size += reference.getSize();
        }
        return size;
    }

    public List<TrackReferenceTypeBox> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(TrackReferenceTypeBox item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        for (TrackReferenceTypeBox entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("reference count=");
        sb.append(getEntries().size());
        for (TrackReferenceTypeBox item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
