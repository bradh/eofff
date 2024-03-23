package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemProperty;
import net.frogmouth.rnd.eofff.nalvideo.lhevc.LHEVCDecoderConfigurationRecord;

public class LHEVCConfigurationItemProperty extends ItemProperty {

    private LHEVCDecoderConfigurationRecord lhevcConfig;
    public static final FourCC LHVC_ATOM = new FourCC("lhvC");

    public LHEVCConfigurationItemProperty() {
        super(LHVC_ATOM);
    }

    @Override
    public String getFullName() {
        return "LHEVCConfigurationItemProperty";
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
