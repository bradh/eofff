package net.frogmouth.rnd.eofff.gopro;

import com.google.auto.service.AutoService;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class XYZPositionParser extends BaseBoxParser {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

    public XYZPositionParser() {}

    @Override
    public FourCC getFourCC() {
        return XYZPosition.XYZPosition_ATOM;
    }

    @Override
    public XYZPosition parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        XYZPosition box = new XYZPosition();
        int length = parseContext.readUnsignedInt16();
        box.setLanguage(parseContext.readPackedLanguageCode());
        byte[] bytes = parseContext.getBytes(length);
        String value = new String(bytes, StandardCharsets.ISO_8859_1);
        box.setValue(value);
        return box;
    }
}
