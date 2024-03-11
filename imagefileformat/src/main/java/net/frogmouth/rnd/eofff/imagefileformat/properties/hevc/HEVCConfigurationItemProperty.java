package net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;
import net.frogmouth.rnd.eofff.nalvideo.HEVCDecoderConfigurationRecord;

// TODO: rebuild this using nalvideo implementation
public class HEVCConfigurationItemProperty extends ItemProperty {
    public static final FourCC HVCC_ATOM = new FourCC("hvcC");

    private HEVCDecoderConfigurationRecord hevcConfig;

    public HEVCConfigurationItemProperty() {
        super(HVCC_ATOM);
    }

    @Override
    public long getBodySize() {
        return hevcConfig.getSize();
    }

    public HEVCDecoderConfigurationRecord getHevcConfig() {
        return hevcConfig;
    }

    public void setHevcConfig(HEVCDecoderConfigurationRecord config) {
        this.hevcConfig = config;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        hevcConfig.writeTo(stream);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        hevcConfig.addToStringBuilder(sb, nestingLevel + 1);
        return sb.toString();
    }
}
