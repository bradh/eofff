package net.frogmouth.rnd.eofff.mpeg4.esds;

import com.google.auto.service.AutoService;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class ESDBoxParser extends FullBoxParser {

    private static final Logger LOG = LoggerFactory.getLogger(ESDBoxParser.class);

    public ESDBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ESDBox.ESDS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ESDBox box = new ESDBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        box.setTag(parseContext.readByte());
        box.setLength(parseContext.readLengthVariable());
        box.setES_ID(parseContext.readUnsignedInt16());
        byte flags = parseContext.readByte();
        if (flags != 0) {
            throw new UnsupportedOperationException("flags parsing is not yet handled");
        }
        while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
            byte tag = parseContext.readByte();
            if (tag == 0x04) {
                DecoderConfigurationDescriptor decConfigDesc = new DecoderConfigurationDescriptor();
                // TODO: we should set these on the descriptor
                int descrLen = parseContext.readLengthVariable();
                long offset = parseContext.getCursorPosition();
                int objectTypeIndication = parseContext.readByte();
                byte b = parseContext.readByte();
                int streamType = b >> 2;
                boolean upStream = ((b & 0x02) == 0x02);
                int reserved = (b & 0x01);
                long bufferSizeDB =
                        (parseContext.readUnsignedInt8() << 16)
                                + (parseContext.readUnsignedInt16());
                long maxBitRate = parseContext.readUnsignedInt32();
                long avgBitRate = parseContext.readUnsignedInt32();
                if (descrLen > 13) {
                    int decSpecificInfoTag = parseContext.readByte();
                    int decSpecificInfoLen = parseContext.readLengthVariable();
                    byte[] decSpecificInfoBytes = parseContext.getBytes(decSpecificInfoLen);
                    System.out.println(
                            HexFormat.of()
                                    .withPrefix("0x")
                                    .withDelimiter(" ")
                                    .formatHex(decSpecificInfoBytes));
                }
                if (parseContext.hasRemainingUntil(offset + descrLen)) {
                    throw new UnsupportedOperationException(
                            "No support for extended ES Descr profiles");
                }
                box.setDecConfigDescr(decConfigDesc);
            } else if (tag == 0x06) {
                int descrLen = parseContext.readLengthVariable();
                byte[] slBytes = parseContext.getBytes(descrLen);
                // TODO: set on descr
                System.out.println(
                        HexFormat.of().withPrefix("0x").withDelimiter(" ").formatHex(slBytes));
            } else {
                throw new UnsupportedOperationException("No support for ES Descr: " + tag);
            }
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
