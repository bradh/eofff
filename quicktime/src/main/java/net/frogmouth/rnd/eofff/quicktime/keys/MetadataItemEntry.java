package net.frogmouth.rnd.eofff.quicktime.keys;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record MetadataItemEntry(FourCC namespace, String value) {
    public void writeTo(OutputStreamWriter writer) throws IOException {
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        int keySize = Integer.BYTES + FourCC.BYTES + valueBytes.length;
        writer.writeUnsignedInt32(keySize);
        writer.writeFourCC(namespace);
        writer.write(valueBytes);
    }

    public long getSize() {
        byte[] valueBytes = value.getBytes(StandardCharsets.UTF_8);
        int size = Integer.BYTES + FourCC.BYTES + valueBytes.length;
        return size;
    }
}
