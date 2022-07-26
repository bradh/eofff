package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class DataEntryUrlBox extends DataEntryBox {

    private String location;

    public DataEntryUrlBox(long size) {
        super(size, new FourCC("url "));
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

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
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
