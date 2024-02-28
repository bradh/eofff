package net.frogmouth.rnd.eofff.isobmff.hinf;

import com.google.auto.service.AutoService;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.BoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class HintPayloadIDParser extends BoxParser {

    public HintPayloadIDParser() {}

    @Override
    public FourCC getFourCC() {
        return HintPayloadID.PAYT_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        HintPayloadID box = new HintPayloadID();
        box.setPayloadID(parseContext.readUnsignedInt32());
        int count = parseContext.readUnsignedInt8();
        box.setRtpmap(new String(parseContext.getBytes(count), StandardCharsets.UTF_8));
        return box;
    }
}
