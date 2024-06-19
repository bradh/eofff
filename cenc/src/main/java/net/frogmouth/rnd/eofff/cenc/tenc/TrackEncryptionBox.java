package net.frogmouth.rnd.eofff.cenc.tenc;

import java.io.IOException;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class TrackEncryptionBox extends FullBox {
    public static final FourCC TENC_ATOM = new FourCC("tenc");

    private int defaultCryptByteBlock;
    private int defaultSkipByteBlock;
    private int defaultIsProtected;
    private int defaultPerSampleIVSize;
    private byte[] defaultKeyIdentifier;
    private byte[] defaultConstantIV = null;

    public TrackEncryptionBox() {
        super(TENC_ATOM);
    }

    public int getDefaultCryptByteBlock() {
        return defaultCryptByteBlock;
    }

    public void setDefaultCryptByteBlock(int defaultCryptByteBlock) {
        this.defaultCryptByteBlock = defaultCryptByteBlock;
    }

    public int getDefaultSkipByteBlock() {
        return defaultSkipByteBlock;
    }

    public void setDefaultSkipByteBlock(int defaultSkipByteBlock) {
        this.defaultSkipByteBlock = defaultSkipByteBlock;
    }

    public int getDefaultIsProtected() {
        return defaultIsProtected;
    }

    public void setDefaultIsProtected(int defaultIsProtected) {
        this.defaultIsProtected = defaultIsProtected;
    }

    public int getDefaultPerSampleIVSize() {
        return defaultPerSampleIVSize;
    }

    public void setDefaultPerSampleIVSize(int defaultPerSampleIVSize) {
        this.defaultPerSampleIVSize = defaultPerSampleIVSize;
    }

    public byte[] getDefaultKeyIdentifier() {
        return defaultKeyIdentifier;
    }

    public void setDefaultKeyIdentifier(byte[] defaultKeyIdentifier) {
        this.defaultKeyIdentifier = defaultKeyIdentifier;
    }

    public byte[] getDefaultConstantIV() {
        return defaultConstantIV;
    }

    public void setDefaultConstantIV(byte[] defaultConstantIV) {
        this.defaultConstantIV = defaultConstantIV;
    }

    @Override
    public long getBodySize() {
        int size = 0;
        // TODO
        return size;
    }

    @Override
    public String getFullName() {
        return "TrackEncryptionBox";
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        // TODO
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        if (getVersion() > 0) {
            sb.append("default_crypt_byte_block=");
            sb.append(this.defaultCryptByteBlock);
            sb.append(", default_skip_byte_block=");
            sb.append(this.defaultSkipByteBlock);
            sb.append(", ");
        }
        sb.append("default_isProtected=");
        sb.append(this.defaultIsProtected);
        sb.append(", default_Per_Sample_IV_Size=");
        sb.append(this.defaultPerSampleIVSize);
        sb.append(", default_KID=");
        sb.append(
                HexFormat.of().withDelimiter(",").withPrefix("0x").formatHex(defaultKeyIdentifier));
        if ((this.defaultIsProtected == 1) && (this.defaultPerSampleIVSize == 0)) {
            sb.append(", default_constant_IV_size=");
            sb.append(this.defaultConstantIV.length);
            sb.append(", default_constant_IV=");
            sb.append(
                    HexFormat.of()
                            .withDelimiter(",")
                            .withPrefix("0x")
                            .formatHex(this.defaultConstantIV));
        }
        return sb.toString();
    }
}
