package net.frogmouth.rnd.eofff.isobmff.pasp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class PixelAspectRatioBoxParser extends BoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(PixelAspectRatioBoxParser.class);

    public PixelAspectRatioBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return PixelAspectRatioBox.PASP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PixelAspectRatioBox box = new PixelAspectRatioBox();
        box.setHorizontalSpacing(parseContext.readUnsignedInt32());
        box.setVerticalSpacing(parseContext.readUnsignedInt32());
        return box;
    }
}
