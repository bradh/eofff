package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class AVCConfigurationBox extends BaseBox {

    private AVCDecoderConfigurationRecord avcConfig;

    public AVCConfigurationBox(long size, FourCC name) {
        super(size, name);
    }

    public AVCDecoderConfigurationRecord getAvcConfig() {
        return avcConfig;
    }

    public void setAvcConfig(AVCDecoderConfigurationRecord avcConfig) {
        this.avcConfig = avcConfig;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        avcConfig.writeTo(stream);
    }
}
