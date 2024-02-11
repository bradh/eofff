package net.frogmouth.rnd.eofff.isobmff.stsd;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryFactoryManager;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntryParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class SampleDescriptionBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(SampleDescriptionBoxParser.class);

    public SampleDescriptionBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("stsd");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        SampleDescriptionBox box = new SampleDescriptionBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (int i = 0; i < entryCount; i++) {
            SampleEntry sampleEntry = parseSampleEntry(parseContext);
            box.appendSampleEntry(sampleEntry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    private SampleEntry parseSampleEntry(ParseContext parseContext) {
        long offset = parseContext.getCursorPosition();
        long boxSize = parseContext.readUnsignedInt32();
        FourCC entry_type = parseContext.readFourCC();
        SampleEntryParser parser = SampleEntryFactoryManager.getParser(entry_type);
        SampleEntry sampleEntry = parser.parse(parseContext, offset, boxSize, entry_type);
        parseContext.setCursorPosition(offset + boxSize);
        return sampleEntry;
    }
}
