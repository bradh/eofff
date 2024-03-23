package net.frogmouth.rnd.eofff.imagefileformat.properties.image;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import net.frogmouth.rnd.eofff.nalvideo.lhevc.LHEVCDecoderConfigurationRecord;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class LHEVCConfigurationItemPropertyParser implements PropertyParser {
    public LHEVCConfigurationItemPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return LHEVCConfigurationItemProperty.LHVC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        LHEVCConfigurationItemProperty box = new LHEVCConfigurationItemProperty();
        LHEVCDecoderConfigurationRecord decoderRecord =
                LHEVCDecoderConfigurationRecord.parseFrom(parseContext, initialOffset + boxSize);
        box.setLhevcConfig(decoderRecord);
        return box;
    }
}
