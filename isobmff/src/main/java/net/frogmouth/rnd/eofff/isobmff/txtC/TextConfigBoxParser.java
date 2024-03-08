package net.frogmouth.rnd.eofff.isobmff.txtC;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class TextConfigBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(TextConfigBoxParser.class);

    public TextConfigBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return TextConfigBox.TXTC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        TextConfigBox box = new TextConfigBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setTextConfig(parseContext.readNullDelimitedString(boxSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
