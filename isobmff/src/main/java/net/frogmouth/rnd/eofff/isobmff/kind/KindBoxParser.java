package net.frogmouth.rnd.eofff.isobmff.kind;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class KindBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(KindBoxParser.class);

    public KindBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return KindBox.KIND_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        KindBox box = new KindBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setSchemeURI(parseContext.readNullDelimitedString(boxSize));
        box.setValue(parseContext.readNullDelimitedString(boxSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
