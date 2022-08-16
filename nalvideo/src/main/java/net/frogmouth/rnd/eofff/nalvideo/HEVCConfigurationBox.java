package net.frogmouth.rnd.eofff.nalvideo;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class HEVCConfigurationBox extends BaseBox {

    private HEVCDecoderConfigurationRecord hevcConfig;

    public HEVCConfigurationBox(FourCC name) {
        super(name);
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES;
        size += hevcConfig.getSize();
        return size;
    }

    public HEVCDecoderConfigurationRecord getAvcConfig() {
        return hevcConfig;
    }

    public void setHevcConfig(HEVCDecoderConfigurationRecord avcConfig) {
        this.hevcConfig = avcConfig;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        hevcConfig.writeTo(stream);
    }
}
