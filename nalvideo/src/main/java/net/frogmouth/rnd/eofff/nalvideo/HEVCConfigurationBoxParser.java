package net.frogmouth.rnd.eofff.nalvideo;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HEVCConfigurationBoxParser extends BaseBoxParser {
    public HEVCConfigurationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return HEVCConfigurationBox.HVCC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HEVCConfigurationBox box = new HEVCConfigurationBox();
        HEVCDecoderConfigurationRecord decoderRecord =
                HEVCDecoderConfigurationRecord.parseFrom(parseContext);
        box.setHevcConfig(decoderRecord);
        return box;
    }
}
