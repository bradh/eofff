package net.frogmouth.rnd.eofff.isobmff.stsh;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ShadowSyncSampleBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(ShadowSyncSampleBoxParser.class);

    public ShadowSyncSampleBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ShadowSyncSampleBox.STSH_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ShadowSyncSampleBox box = new ShadowSyncSampleBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (long i = 0; i < entryCount; i++) {
            ShadowSyncSampleEntry entry = parseShadowSampleEntry(parseContext);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    private ShadowSyncSampleEntry parseShadowSampleEntry(ParseContext parseContext) {
        long shadowedSampleNumber = parseContext.readUnsignedInt32();
        long syncSampleNumber = parseContext.readUnsignedInt32();
        return new ShadowSyncSampleEntry(shadowedSampleNumber, syncSampleNumber);
    }
}
