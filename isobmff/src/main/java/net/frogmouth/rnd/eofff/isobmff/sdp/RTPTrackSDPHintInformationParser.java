package net.frogmouth.rnd.eofff.isobmff.sdp;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class RTPTrackSDPHintInformationParser extends FullBoxParser {
    private static final Logger LOG =
            LoggerFactory.getLogger(RTPTrackSDPHintInformationParser.class);

    public RTPTrackSDPHintInformationParser() {}

    @Override
    public FourCC getFourCC() {
        return RTPTrackSDPHintInformation.SDP_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        RTPTrackSDPHintInformation box = new RTPTrackSDPHintInformation();
        box.setSdpText(
                parseContext.readNullDelimitedString(
                        (initialOffset + boxSize) - parseContext.getCursorPosition()));
        return box;
    }
}
