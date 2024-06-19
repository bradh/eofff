package net.frogmouth.rnd.eofff.cenc.pssh;

import static net.frogmouth.rnd.eofff.cenc.CommonEncryptionConstants.KEY_IDENTIFIER_BYTES;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Protection System Specific Header Box (pssh).
 *
 * <p>See ISO/IEC 23001-7:2023 Section 8.1.2.
 */
public class ProtectionSystemSpecificHeaderBox extends FullBox {
    public static final FourCC PSSH_ATOM = new FourCC("pssh");

    private UUID systemID;
    private final List<byte[]> keyIdentifiers = new ArrayList<>();
    private byte[] data;

    public ProtectionSystemSpecificHeaderBox() {
        super(PSSH_ATOM);
    }

    @Override
    public String getFullName() {
        return "ProtectionSystemSpecificHeaderBox";
    }

    public UUID getSystemID() {
        return systemID;
    }

    public void setSystemID(UUID systemID) {
        this.systemID = systemID;
    }

    public List<byte[]> getKeyIdentifiers() {
        return new ArrayList<>(keyIdentifiers);
    }

    public void addKeyIdentifier(byte[] kid) {
        keyIdentifiers.add(kid);
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += 16;
        if (getVersion() > 0) {
            size += Integer.BYTES;
            size += (keyIdentifiers.size() * KEY_IDENTIFIER_BYTES);
        }
        size += Integer.BYTES;
        size += data.length;
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUUID(systemID);
        if (getVersion() > 0) {
            stream.writeUnsignedInt32(keyIdentifiers.size());
            for (int i = 0; i < keyIdentifiers.size(); i++) {
                stream.write(keyIdentifiers.get(i));
            }
        }
        stream.writeUnsignedInt32(data.length);
        stream.write(data);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("SystemID=");
        sb.append(systemID);
        if (getVersion() > 0) {
            sb.append(", KID_count=");
            sb.append(this.keyIdentifiers.size());
            sb.append(", KID=[");
            for (byte[] identifier : this.keyIdentifiers) {
                sb.append("0x");
                sb.append(HexFormat.of().formatHex(identifier));
                sb.append(" ");
            }
            sb.append("]");
        }
        sb.append(", DataSize=");
        sb.append(data.length);
        sb.append(", Data=");
        sb.append(HexFormat.of().withDelimiter(",").withPrefix("0x").formatHex(data));
        return sb.toString();
    }
}
