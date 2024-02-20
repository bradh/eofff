package net.frogmouth.rnd.eofff.isobmff.padb;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class PaddingBitsBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(PaddingBitsBoxParser.class);

    public PaddingBitsBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return PaddingBitsBox.PADB_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        PaddingBitsBox box = new PaddingBitsBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long numSamples = parseContext.readUnsignedInt32();
        int i = 0;
        byte b = 0;
        while (i < numSamples) {
            if ((i % 2) == 0) {
                b = parseContext.readByte();
                box.addPadding((b >> 4) & 0x7);
            } else {
                box.addPadding(b & 0x7);
            }
            i++;
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
