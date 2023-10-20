package net.frogmouth.rnd.eofff.imagefileformat.properties.uuid;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemProperty;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class UUIDProperty extends ItemProperty {
    public static final FourCC UUID_ATOM = new FourCC("uuid");

    private UUID extendedType;
    private byte[] payload;

    public UUIDProperty() {
        super(UUID_ATOM);
    }

    public UUID getExtendedType() {
        return extendedType;
    }

    public void setExtendedType(UUID extendedType) {
        this.extendedType = extendedType;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }

    @Override
    public long getBodySize() {
        int size = 0;
        size += 16;
        size += payload.length;
        return size;
    }

    @Override
    public String getFullName() {
        return "UUID";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writer.writeUUID(extendedType);
        writer.write(payload);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append(" - ");
        sb.append(extendedType.toString());
        sb.append(": ");
        for (byte b : this.payload) {
            sb.append(String.format("0x%02x ", b));
        }
        sb.append("[");
        sb.append(new String(payload, StandardCharsets.UTF_8));
        sb.append("]");
        return sb.toString();
    }
}
