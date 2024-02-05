package net.frogmouth.rnd.eofff.isobmff.dref;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class DataReferenceBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(DataReferenceBoxParser.class);

    public DataReferenceBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return new FourCC("dref");
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        DataReferenceBox box = new DataReferenceBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (int i = 0; i < entryCount; i++) {
            DataEntryBaseBox dataEntryBox = parseDataEntry(parseContext);
            box.addDataReference(dataEntryBox);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }

    private DataEntryBaseBox parseDataEntry(ParseContext parseContext) {
        long offset = parseContext.getCursorPosition();
        long boxSize = parseContext.readUnsignedInt32();
        FourCC entry_type = parseContext.readFourCC();
        DataReferenceParser parser = DataReferenceFactoryManager.getParser(entry_type);
        DataEntryBaseBox dataReference = parser.parse(parseContext, offset, boxSize, entry_type);
        parseContext.setCursorPosition(offset + boxSize);
        return dataReference;
    }
}
