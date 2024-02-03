package net.frogmouth.rnd.eofff.mpeg4.iods;

import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectDescriptorBoxParser extends FullBoxParser {

    private static final int MP4_IOD_TAG = 0x10;
    private static final Logger LOG = LoggerFactory.getLogger(ObjectDescriptorBoxParser.class);

    public ObjectDescriptorBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return ObjectDescriptorBox.IODS_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ObjectDescriptorBox box = new ObjectDescriptorBox();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return parseAsBaseBox(parseContext, initialOffset, boxSize, boxName);
        }
        box.setFlags(parseFlags(parseContext));
        byte tag = parseContext.readByte();
        if (tag == MP4_IOD_TAG) {
            parseBytesAsIOD(box, parseContext, initialOffset, boxSize);
        }
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }

    private void parseBytesAsIOD(
            ObjectDescriptorBox box, ParseContext parseContext, long initialOffset, long boxSize) {
        byte b = parseContext.readByte();
        int length = b & 0x7F;
        if ((b & 0x80) == 0x80) {
            // TODO: we should handle this in the parseContext
            length = length << 7;
            byte b1 = parseContext.readByte();
            length = length + (b1 & 0x7f);
            if ((b1 & 0x80) == 0x80) {
                // throw new UnsupportedOperationException("We need to do extended length
                // handling");
            }
        }
        System.out.println("length: " + length);
        int packed = parseContext.readUnsignedInt16();
        int objectDescriptorId = packed >> 6;
        System.out.println("object descriptor id: " + objectDescriptorId);
        int urlFlag = (packed & 0x20) >> 5;
        int inlineProfilesFlag = (packed & 0x10) >> 4;
        int reserved = (packed & 0x0F);
        if (urlFlag != 0) {
            throw new UnsupportedOperationException("We need to do URL handling");
        } else {
            int odProfileLevelIndication = parseContext.readUnsignedInt8();
            int sceneProfileLevelIndication = parseContext.readUnsignedInt8();
            int audioProfileLevelIndication = parseContext.readUnsignedInt8();
            int visualProfileLevelIndication = parseContext.readUnsignedInt8();
            int graphicsProfileLevelIndication = parseContext.readUnsignedInt8();
            while (parseContext.hasRemainingUntil(initialOffset + boxSize)) {
                int tag = parseContext.readUnsignedInt8();
                int esLength = parseContext.readUnsignedInt8();
                if ((esLength & 0x80) == 0x80) {
                    int esLength1 = parseContext.readUnsignedInt8();
                    if ((esLength1 & 0x80) == 0x80) {
                        // throw new UnsupportedOperationException(
                        //        "We need to do extended length handling");
                    }
                    esLength = ((esLength & 0x7f) << 7) + (esLength1 & 0x7f);
                }
                if (tag == 0x0E) {
                    long track_ID = parseContext.readUnsignedInt32();
                    System.out.println("track_ID: " + track_ID);
                }
            }
        }
        // TODO: extDesc?
    }
}
