package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class WellKnownText2Property extends AbstractUUIDProperty {

    private String wkt2;

    public WellKnownText2Property() {}

    @Override
    protected UUID getUUID() {
        return UUID.fromString("137a1742-75ac-4747-82bc-659576e8675b");
    }

    public String getWkt2() {
        return wkt2;
    }

    public void setWkt2(String wkt2) {
        this.wkt2 = wkt2;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        writeUUIDHeaderTo(writer);
        writer.writeNullTerminatedString(wkt2);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        // TODO: output
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "WellKnownText2";
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += 16;
        size += wkt2.getBytes(StandardCharsets.UTF_8).length;
        size += 1;
        return size;
    }
}
