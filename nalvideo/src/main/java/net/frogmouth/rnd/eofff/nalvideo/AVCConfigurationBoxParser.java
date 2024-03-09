package net.frogmouth.rnd.eofff.nalvideo;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class AVCConfigurationBoxParser extends BaseBoxParser {
    public AVCConfigurationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("avcC");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AVCConfigurationBox box = new AVCConfigurationBox(boxName);
        AVCDecoderConfigurationRecord decoderRecord =
                AVCDecoderConfigurationRecord.parseFrom(parseContext, initialOffset + boxSize);
        box.setAvcConfig(decoderRecord);
        return box;
    }
}
