package net.frogmouth.rnd.eofff.isobmff.stsd;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Sample Description Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.5.2
 */
public class SampleDescriptionBox extends FullBox {

    private final List<Box> nestedBoxes = new ArrayList<>();

    public SampleDescriptionBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "SampleDescriptionBox";
    }

    public List<Box> getNestedBoxes() {
        return new ArrayList<>(nestedBoxes);
    }

    public void addNestedBoxes(List<Box> boxes) {
        nestedBoxes.addAll(boxes);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(nestedBoxes.size()));
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
