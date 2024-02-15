package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class DataEntryUrlBox extends DataEntryBaseBox {

    public static final FourCC URL_ATOM = new FourCC("url ");
    private String location;

    public DataEntryUrlBox() {
        super(URL_ATOM);
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String getFullName() {
        return "DataEntryUrlBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (!this.isFlagSet(DataEntryBaseBox.MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            size += (location.getBytes(StandardCharsets.UTF_8).length);
            size += 1; // null terminator
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if (!this.isFlagSet(DataEntryBaseBox.MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            stream.writeNullTerminatedString(location);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("location: ");
        if (this.isFlagSet(MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            sb.append("[in same file]");
        } else {
            sb.append(location);
        }
        return sb.toString();
    }
}
