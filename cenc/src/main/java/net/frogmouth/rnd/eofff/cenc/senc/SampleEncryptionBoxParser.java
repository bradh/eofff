package net.frogmouth.rnd.eofff.cenc.senc;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SampleEncryptionBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(SampleEncryptionBoxParser.class);

    public SampleEncryptionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return SampleEncryptionBox.SENC_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleEncryptionBox box = new SampleEncryptionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long sampleCount = parseContext.readUnsignedInt32();
        // TODO: stash number of samples and byte array since we can't parse this from here
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return ((version == 0x00) || (version == 0x01) || (version == 0x02));
    }
}
