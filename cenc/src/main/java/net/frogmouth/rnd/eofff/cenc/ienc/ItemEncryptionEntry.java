package net.frogmouth.rnd.eofff.cenc.ienc;

import java.util.HexFormat;

public class ItemEncryptionEntry {
    private int perSampleIVSize;
    private byte[] keyIdentifier;
    private byte[] constantIV = null;

    public int getPerSampleIVSize() {
        return perSampleIVSize;
    }

    public void setPerSampleIVSize(int perSampleIVSize) {
        this.perSampleIVSize = perSampleIVSize;
    }

    public byte[] getKeyIdentifier() {
        return keyIdentifier;
    }

    public void setKeyIdentifier(byte[] keyIdentifier) {
        this.keyIdentifier = keyIdentifier;
    }

    public byte[] getConstantIV() {
        return constantIV;
    }

    public void setConstantIV(byte[] constantIV) {
        this.constantIV = constantIV;
    }

    public void append_to(StringBuilder sb) {
        sb.append(", Per_Sample_IV_Size=");
        sb.append(this.perSampleIVSize);
        sb.append(", KID=0x");
        sb.append(HexFormat.of().formatHex(keyIdentifier));
        if (this.perSampleIVSize == 0) {
            sb.append(", constant_IV_size=");
            sb.append(this.constantIV.length);
            sb.append(", constant_IV=");
            sb.append(HexFormat.of().withDelimiter(",").withPrefix("0x").formatHex(constantIV));
        }
    }
}
