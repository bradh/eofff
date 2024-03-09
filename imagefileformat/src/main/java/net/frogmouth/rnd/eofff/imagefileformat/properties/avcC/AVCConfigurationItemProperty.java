package net.frogmouth.rnd.eofff.imagefileformat.properties.avcC;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;
import net.frogmouth.rnd.eofff.nalvideo.AVCDecoderConfigurationRecord;

public class AVCConfigurationItemProperty extends ItemProperty {
    public static final FourCC AVCC_ATOM = new FourCC("avcC");

    private AVCDecoderConfigurationRecord avcConfig;

    public AVCConfigurationItemProperty() {
        super(AVCC_ATOM);
    }

    @Override
    public long getBodySize() {
        return avcConfig.getSize();
    }

    public AVCDecoderConfigurationRecord getAvcConfig() {
        return avcConfig;
    }

    public void setAvcConfig(AVCDecoderConfigurationRecord avcConfig) {
        this.avcConfig = avcConfig;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        avcConfig.writeTo(stream);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        avcConfig.addToStringBuilder(sb, nestingLevel + 1);
        return sb.toString();
    }

    @Override
    public String getFullName() {
        return "AVCConfigurationItemProperty";
    }
}
