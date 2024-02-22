package net.frogmouth.rnd.eofff.quicktime.keys;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MetadataItemKeysAtomParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataItemKeysAtomParser.class);

    public MetadataItemKeysAtomParser() {}

    @Override
    public FourCC getFourCC() {
        return MetadataItemKeysAtom.KEYS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MetadataItemKeysAtom box = new MetadataItemKeysAtom();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        long entryCount = parseContext.readUnsignedInt32();
        for (int i = 0; i < entryCount; i++) {
            long keySize = parseContext.readUnsignedInt32();
            FourCC keyNamespace = parseContext.readFourCC();
            long keyValueLength = keySize - (Integer.BYTES + FourCC.BYTES);
            String keyValue = parseContext.readNullDelimitedString(keyValueLength);
            System.out.println("namespace: " + keyNamespace.toString() + ", keyValue: " + keyValue);
            MetadataItemEntry entry = new MetadataItemEntry(keyNamespace, keyValue);
            box.addEntry(entry);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
