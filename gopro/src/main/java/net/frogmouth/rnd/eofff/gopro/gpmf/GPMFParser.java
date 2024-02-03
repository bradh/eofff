package net.frogmouth.rnd.eofff.gopro.gpmf;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class GPMFParser extends BaseBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(GPMFParser.class);

    public GPMFParser() {}

    @Override
    public FourCC getFourCC() {
        return GPMF.GPMF_ATOM;
    }

    @Override
    public GPMF parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        GPMF box = new GPMF();
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            FourCC fourCC = parseContext.readFourCC();
            if (fourCC.asUnsigned() == 0) {
                // this is an end-of-structure marker (GPMF_KEY_END)
                parseContext.setCursorPosition(initialOffset + boxSize);
                break;
            }
            byte type = parseContext.readByte();
            int sampleSize = parseContext.readByte();
            int repeat = parseContext.readInt16();
            GPMFItem item = GPMFItemFactory.getItem(fourCC, type, sampleSize, repeat);
            item.parse(parseContext);
            box.addItem(item);
        }
        return box;
    }
}
