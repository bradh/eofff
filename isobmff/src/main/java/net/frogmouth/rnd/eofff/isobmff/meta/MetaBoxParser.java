package net.frogmouth.rnd.eofff.isobmff.meta;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MetaBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(MetaBoxParser.class);

    public MetaBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("meta");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MetaBox box = new MetaBox();
        // This is a workaround for an android bug
        long maybeVersionAndFlags = parseContext.peekUnsignedInt32();
        if (maybeVersionAndFlags == 0x00000000) {
            // This has the version and flags (i.e. is correct)
            parseContext.skipBytes(Integer.BYTES);
        }
        box.setVersion(0);
        box.setFlags(0);
        box.addNestedBoxes(parseContext.parseNestedBoxes(initialOffset + boxSize));
        return box;
    }
}
