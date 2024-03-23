package net.frogmouth.rnd.eofff.nalvideo.lhevc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class LHEVCConfigurationBox extends BaseBox {

    private LHEVCDecoderConfigurationRecord lhevcConfig;
    public static final FourCC LHVC_ATOM = new FourCC("lhvC");

    public LHEVCConfigurationBox() {
        super(LHVC_ATOM);
    }

    @Override
    public String getFullName() {
        return "LHEVCConfigurationBox";
    }

    @Override
    public long getBodySize() {
        return lhevcConfig.getSize();
    }

    public LHEVCDecoderConfigurationRecord getLhevcConfig() {
        return lhevcConfig;
    }

    public void setLhevcConfig(LHEVCDecoderConfigurationRecord lhevcConfig) {
        this.lhevcConfig = lhevcConfig;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        lhevcConfig.writeTo(stream);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        lhevcConfig.addToStringBuilder(sb, nestingLevel + 1);
        return sb.toString();
    }
}
