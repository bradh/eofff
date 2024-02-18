package net.frogmouth.rnd.eofff.isobmff.elng;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ExtendedLanguageBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ExtendedLanguageBoxParser.class);

    public ExtendedLanguageBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ExtendedLanguageBox.ELNG_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ExtendedLanguageBox box = new ExtendedLanguageBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setLanguage(parseContext.readNullDelimitedString(boxSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
