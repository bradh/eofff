package net.frogmouth.rnd.eofff.imagefileformat.properties.hevc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import net.frogmouth.rnd.eofff.nalvideo.HEVCDecoderConfigurationRecord;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class HEVCConfigurationItemPropertyParser implements PropertyParser {

    public HEVCConfigurationItemPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return HEVCConfigurationItemProperty.HVCC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCConfigurationItemProperty box = new HEVCConfigurationItemProperty();
        HEVCDecoderConfigurationRecord decoderRecord =
                HEVCDecoderConfigurationRecord.parseFrom(parseContext);
        box.setHevcConfig(decoderRecord);
        return box;
    }
}
