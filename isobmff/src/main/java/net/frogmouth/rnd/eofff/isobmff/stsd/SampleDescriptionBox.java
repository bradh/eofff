package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Description Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.5.2
 */
public class SampleDescriptionBox extends FullBox {

    private final List<Box> nestedBoxes = new ArrayList<>();

    public SampleDescriptionBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "SampleDescriptionBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    public List<Box> getNestedBoxes() {
        return new ArrayList<>(nestedBoxes);
    }

    public void addNestedBoxes(List<Box> boxes) {
        nestedBoxes.addAll(boxes);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeInt(nestedBoxes.size());
        for (Box entry : nestedBoxes) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        for (Box item : nestedBoxes) {
            sb.append("\n");
            sb.append("\t\t\t\t\t SampleEntry=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
