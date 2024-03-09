package net.frogmouth.rnd.eofff.imagefileformat.properties.avcC;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser;
import net.frogmouth.rnd.eofff.nalvideo.AVCDecoderConfigurationRecord;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class AVCConfigurationItemPropertyParser implements PropertyParser {
    public AVCConfigurationItemPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return AVCConfigurationItemProperty.AVCC_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AVCConfigurationItemProperty box = new AVCConfigurationItemProperty();
        AVCDecoderConfigurationRecord decoderRecord =
                AVCDecoderConfigurationRecord.parseFrom(parseContext, initialOffset + boxSize);
        box.setAvcConfig(decoderRecord);
        return box;
    }
}
