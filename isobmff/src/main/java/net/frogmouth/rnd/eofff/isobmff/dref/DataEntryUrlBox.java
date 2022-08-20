package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class DataEntryUrlBox extends DataEntryBox {

    private String location;

    public DataEntryUrlBox() {
        super(new FourCC("url "));
    }

    @Override
    public boolean isSupportedVersion(int version) {
        return (version == 0);
    }

    void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    // @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if (!this.isFlagSet(DataEntryBox.MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            size += (location.getBytes(StandardCharsets.US_ASCII).length);
        }
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (!this.isFlagSet(DataEntryBox.MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            size += (location.getBytes(StandardCharsets.US_ASCII).length);
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if (!this.isFlagSet(DataEntryBox.MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            stream.write(location.getBytes(StandardCharsets.US_ASCII));
        }
    }

    @Override
    public String toString() {
        if (this.isFlagSet(MEDIA_DATA_IN_SAME_FILE_FLAG)) {
            return ("[in same file]");
        } else {
            return location;
        }
    }
}
