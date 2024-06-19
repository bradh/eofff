package net.frogmouth.rnd.eofff.isobmff.schm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Scheme Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.12.6.
 */
public class SchemeTypeBox extends FullBox {
    public static final FourCC SCHM_ATOM = new FourCC("schm");
    private FourCC schemeType;
    private long schemeVersion;
    private String schemeUri = null;

    public SchemeTypeBox() {
        super(SCHM_ATOM);
    }

    @Override
    public String getFullName() {
        return "SchemeTypeBox";
    }

    public FourCC getSchemeType() {
        return schemeType;
    }

    public void setSchemeType(FourCC schemeType) {
        this.schemeType = schemeType;
    }

    public long getSchemeVersion() {
        return schemeVersion;
    }

    public void setSchemeVersion(long schemeVersion) {
        this.schemeVersion = schemeVersion;
    }

    public String getSchemeUri() {
        return schemeUri;
    }

    public void setSchemeUri(String schemeUri) {
        this.schemeUri = schemeUri;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += FourCC.BYTES;
        size += Integer.BYTES;
        if (schemeUri != null) {
            size += schemeUri.getBytes(StandardCharsets.UTF_8).length;
            size += 1;
        }
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        if (schemeUri != null) {
            setFlags(1);
        }
        this.writeBoxHeader(stream);
        stream.writeFourCC(schemeType);
        stream.writeUnsignedInt32(schemeVersion);
        if (schemeUri != null) {
            stream.writeNullTerminatedString(schemeUri);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("scheme_type=");
        sb.append(this.schemeType.toString());
        sb.append(", scheme_version=");
        sb.append(this.schemeVersion >> 16);
        sb.append(".");
        sb.append(this.schemeVersion & 0xFFFF);
        sb.append(" (0x");
        sb.append(HexFormat.of().toHexDigits((int) this.schemeVersion));
        sb.append(") ");
        if (schemeUri != null) {
            sb.append(", scheme_uri=");
            sb.append(this.schemeUri);
        }
        return sb.toString();
    }
}
