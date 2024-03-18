package net.frogmouth.rnd.eofff.isobmff.trep;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Extension Properties Box (trep).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.15.
 */
public class TrackExtensionPropertiesBox extends FullBox {
    public static final FourCC TREP_ATOM = new FourCC("trep");

    private long trackID;
    private final List<Box> nestedBoxes = new ArrayList<>();

    public TrackExtensionPropertiesBox() {
        super(TREP_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackExtensionPropertiesBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
    }

    public List<Box> getNestedBoxes() {
        return new ArrayList<>(nestedBoxes);
    }

    public void addNestedBoxes(List<Box> boxes) {
        nestedBoxes.addAll(boxes);
    }

    public void addNestedBox(Box box) {
        nestedBoxes.add(box);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(trackID);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append(", track_ID=");
        sb.append(trackID);
        for (Box nestedBox : getNestedBoxes()) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }
}
