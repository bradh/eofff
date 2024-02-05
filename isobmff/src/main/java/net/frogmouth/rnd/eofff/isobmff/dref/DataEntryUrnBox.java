package net.frogmouth.rnd.eofff.isobmff.dref;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class DataEntryUrnBox extends DataEntryBaseBox {

    public static final FourCC URN_ATOM = new FourCC("urn ");
    private String name;
    private String location;

    public DataEntryUrnBox() {
        super(URN_ATOM);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String getFullName() {
        return "DataEntryUrnBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += (name.getBytes(StandardCharsets.UTF_8).length);
        size += 1; // null terminator
        size += (location.getBytes(StandardCharsets.UTF_8).length);
        size += 1; // null terminator
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeNullTerminatedString(name);
        stream.writeNullTerminatedString(location);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("name: ");
        sb.append(name);
        sb.append(", location: ");
        sb.append(location);
        return sb.toString();
    }
}
