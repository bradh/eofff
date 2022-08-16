package net.frogmouth.rnd.eofff.isobmff.stts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class TimeToSampleBox extends FullBox {
    private final List<TimeToSampleEntry> entries = new ArrayList<>();

    public TimeToSampleBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "TimeToSampleBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (TimeToSampleEntry entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    public List<TimeToSampleEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(TimeToSampleEntry item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeInt(entries.size());
        for (TimeToSampleEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_count=");
        sb.append(getEntries().size());
        for (TimeToSampleEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
