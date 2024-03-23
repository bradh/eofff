package net.frogmouth.rnd.eofff.nalvideo.lhevc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class LHEVCConfigurationBoxParser extends BaseBoxParser {
    public LHEVCConfigurationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return LHEVCConfigurationBox.LHVC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        LHEVCConfigurationBox box = new LHEVCConfigurationBox();
        LHEVCDecoderConfigurationRecord decoderRecord =
                LHEVCDecoderConfigurationRecord.parseFrom(parseContext, initialOffset + boxSize);
        box.setLhevcConfig(decoderRecord);
        return box;
    }
}
