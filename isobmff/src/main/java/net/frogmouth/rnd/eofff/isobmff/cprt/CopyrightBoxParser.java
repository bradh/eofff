package net.frogmouth.rnd.eofff.isobmff.cprt;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class CopyrightBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(CopyrightBoxParser.class);

    public CopyrightBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return CopyrightBox.CPRT_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        CopyrightBox box = new CopyrightBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setLanguage(parseContext.readPackedLanguageCode());
        box.setNotice(
                parseContext.readNullDelimitedString(
                        boxSize - (parseContext.getCursorPosition() - initialOffset)));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
