package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        List<Box> nestedBoxes = parseContext.parseNestedBoxes(initialOffset + boxSize);
        List<SampleEntry> sampleEntries = new ArrayList<>();
        for (Box nestedBox : nestedBoxes) {
            if (nestedBox instanceof SampleEntry) {
                sampleEntries.add((SampleEntry) nestedBox);
            } else {
                String s = nestedBox.getFourCC().toString();
                System.out.println("Unimplemented SampleEntry box: " + s);
                LOG.warn(
                        "expected nested box to be a SampleEntry: "
                                + nestedBox.getFullName()
                                + ", "
                                + nestedBox.getFourCC().toString());
            }
        }
        box.addSampleEntries(sampleEntries);
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return version == 0x00;
    }
}
