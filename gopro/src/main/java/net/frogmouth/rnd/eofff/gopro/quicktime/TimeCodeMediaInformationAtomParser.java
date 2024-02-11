package net.frogmouth.rnd.eofff.gopro.quicktime;

import com.google.auto.service.AutoService;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TimeCodeMediaInformationAtomParser extends FullBoxParser {

    private static final Logger LOG =
            LoggerFactory.getLogger(TimeCodeMediaInformationAtomParser.class);

    public TimeCodeMediaInformationAtomParser() {}

    @Override
    public FourCC getFourCC() {
        return TimeCodeMediaInformationAtom.TCMI_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TimeCodeMediaInformationAtom box = new TimeCodeMediaInformationAtom();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setTextFont((short) parseContext.readInt16());
        box.setTextFace((short) parseContext.readInt16());
        box.setTextSize((short) parseContext.readInt16());
        parseContext.skipBytes(Short.BYTES);
        box.setTextcolorRed((short) parseContext.readInt16());
        box.setTextcolorGreen((short) parseContext.readInt16());
        box.setTextcolorBlue((short) parseContext.readInt16());
        box.setBackgroundColorRed((short) parseContext.readInt16());
        box.setBackgroundColorGreen((short) parseContext.readInt16());
        box.setBackgroundColorBlue((short) parseContext.readInt16());
        int remainingBytes = (int) (boxSize - (parseContext.getCursorPosition() - initialOffset));
        int length = parseContext.readByte();
        length = Math.min(length, remainingBytes);
        byte[] fontNameBytes = parseContext.getBytes(length);
        box.setFontName(new String(fontNameBytes, StandardCharsets.US_ASCII));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
