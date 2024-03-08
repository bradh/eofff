package net.frogmouth.rnd.eofff.nalvideo;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * AVC Configuration Box(avcC).
 *
 * <p>See ISO/IEC 14496-15:2022 Section 5.3.1 and 5.4.2.
 */
public class AVCConfigurationBox extends BaseBox {

    private AVCDecoderConfigurationRecord avcConfig;

    public AVCConfigurationBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "AVCConfigurationBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES;
        size += avcConfig.getSize();
        return size;
    }

    public AVCDecoderConfigurationRecord getAvcConfig() {
        return avcConfig;
    }

    public void setAvcConfig(AVCDecoderConfigurationRecord avcConfig) {
        this.avcConfig = avcConfig;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        avcConfig.writeTo(stream);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        avcConfig.addToStringBuilder(sb, nestingLevel + 1);
        return sb.toString();
    }
}
