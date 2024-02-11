package net.frogmouth.rnd.eofff.goprotools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.gopro.gpmf.GPMF;
import net.frogmouth.rnd.eofff.gopro.gpmf.GPMFParser;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;
import net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBox;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBox;
import net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBox;
import net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBox;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBox;
import net.frogmouth.rnd.eofff.mpeg4.esds.ESDBox;

class GoProParser {

    private final List<Box> boxes = new ArrayList<>();

    public GoProParser(String filename) throws IOException {
        boxes.addAll(parseFile(filename));
    }

    private List<Box> parseFile(String inputPath) throws IOException {
        Path testFile = Paths.get(inputPath);
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    public void dumpBoxes() {
        for (Box box : boxes) {
            System.out.println(box.toString(0));
        }
    }

    private static Box findChildBox(AbstractContainerBox parent, FourCC fourCC) {
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(fourCC)) {
                return box;
            }
        }
        return null;
    }

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    void findMetadataTrack() throws IOException {
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC(boxes, "moov");
        if (moov == null) {
            throw new IOException("Missing essential moov box");
        }
        for (Box box : moov.getNestedBoxes()) {
            if (box instanceof TrackBox trak) {
                MediaBox mdia = (MediaBox) findChildBox(trak, new FourCC("mdia"));
                if (mdia != null) {
                    HandlerBox hdlr = (HandlerBox) findChildBox(mdia, new FourCC("hdlr"));
                    if (hdlr != null) {
                        if (hdlr.getHandlerType().equals("meta")) {}
                    }
                }
            }
        }
    }

    void dumpH265Files() throws IOException {
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(boxes, "mdat");
        if (mdat == null) {
            throw new IOException("Missing essential mdat box");
        }
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC(boxes, "moov");
        if (moov == null) {
            throw new IOException("Missing essential moov box");
        }
        for (Box box : moov.getNestedBoxes()) {
            if (box instanceof TrackBox trak) {
                MediaBox mdia = (MediaBox) findChildBox(trak, new FourCC("mdia"));
                if (mdia != null) {
                    HandlerBox hdlr = (HandlerBox) findChildBox(mdia, new FourCC("hdlr"));
                    if (hdlr != null) {
                        if (hdlr.getHandlerType().equals("vide")) {
                            MediaInformationBox minf =
                                    (MediaInformationBox) findChildBox(mdia, new FourCC("minf"));
                            if (minf != null) {
                                SampleTableBox stbl =
                                        (SampleTableBox) findChildBox(minf, new FourCC("stbl"));
                                if (stbl != null) {
                                    dumpSamples(stbl, mdat, "h265");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void dumpGPMF() throws IOException {
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(boxes, "mdat");
        if (mdat == null) {
            throw new IOException("Missing essential mdat box");
        }
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC(boxes, "moov");
        if (moov == null) {
            throw new IOException("Missing essential moov box");
        }
        for (Box box : moov.getNestedBoxes()) {
            if (box instanceof TrackBox trak) {
                MediaBox mdia = (MediaBox) findChildBox(trak, new FourCC("mdia"));
                if (mdia != null) {
                    HandlerBox hdlr = (HandlerBox) findChildBox(mdia, new FourCC("hdlr"));
                    if (hdlr != null) {
                        if (hdlr.getHandlerType().equals("meta")) {
                            MediaInformationBox minf =
                                    (MediaInformationBox) findChildBox(mdia, new FourCC("minf"));
                            if (minf != null) {
                                SampleTableBox stbl =
                                        (SampleTableBox) findChildBox(minf, new FourCC("stbl"));
                                if (stbl != null) {
                                    dumpSamples(stbl, mdat, "meta");
                                    debugDumpGPMF(stbl, mdat);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void dumpSamples(SampleTableBox stbl, MediaDataBox mdat, String suffix)
            throws IOException {
        SampleSizeBox stsz = (SampleSizeBox) findChildBox(stbl, new FourCC("stsz"));
        if (stsz == null) {
            throw new IOException("Missing essential stsz box");
        }
        ChunkOffsetBox stco = (ChunkOffsetBox) findChildBox(stbl, new FourCC("stco"));
        if (stco == null) {
            throw new IOException("Missing essential stco box");
        }
        // SyncSampleBox stss = (SyncSampleBox) findChildBox(stbl, new FourCC("stss"));
        List<Long> offsets = stco.getEntries();
        List<Long> sizes = stsz.getEntries();
        assert (offsets.size() == sizes.size());
        for (int i = 0; i < offsets.size(); i++) {
            Long offset = offsets.get(i);
            Long size = sizes.get(i);
            byte[] chunk = mdat.getDataAt(offset, size);
            if (chunk.length == 4) {
                ByteBuffer bb = ByteBuffer.wrap(chunk);
                System.out.println("Four bytes: " + bb.asIntBuffer().get(0));
            }
            File f = new File(String.format("chunk%03d.%s", i, suffix));
            Files.write(f.toPath(), chunk);
        }
    }

    private void debugDumpGPMF(SampleTableBox stbl, MediaDataBox mdat) throws IOException {
        SampleSizeBox stsz = (SampleSizeBox) findChildBox(stbl, new FourCC("stsz"));
        if (stsz == null) {
            throw new IOException("Missing essential stsz box");
        }
        ChunkOffsetBox stco = (ChunkOffsetBox) findChildBox(stbl, new FourCC("stco"));
        if (stco == null) {
            throw new IOException("Missing essential stco box");
        }
        List<Long> offsets = stco.getEntries();
        List<Long> sizes = stsz.getEntries();
        assert (offsets.size() == sizes.size());
        System.out.println("----------------");
        for (int i = 0; i < offsets.size(); i++) {
            Long offset = offsets.get(i);
            Long size = sizes.get(i);
            byte[] chunk = mdat.getDataAt(offset, size);
            MemorySegment segment = MemorySegment.ofArray(chunk);
            ParseContext ctx = new ParseContext(segment);
            GPMFParser gpmfParser = new GPMFParser();
            GPMF items = gpmfParser.parse(ctx, 0, size, new FourCC("gpmf"));
            System.out.println("GPMF chunk: " + i);
            System.out.println(items.toString(0));
            System.out.println("----------------");
        }
    }

    void dumpTimingTrack() throws IOException {
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(boxes, "mdat");
        if (mdat == null) {
            throw new IOException("Missing essential mdat box");
        }
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC(boxes, "moov");
        if (moov == null) {
            throw new IOException("Missing essential moov box");
        }
        for (Box box : moov.getNestedBoxes()) {
            if (box instanceof TrackBox trak) {
                MediaBox mdia = (MediaBox) findChildBox(trak, new FourCC("mdia"));
                if (mdia != null) {
                    HandlerBox hdlr = (HandlerBox) findChildBox(mdia, new FourCC("hdlr"));
                    if (hdlr != null) {
                        if (hdlr.getHandlerType().equals("tmcd")) {
                            MediaInformationBox minf =
                                    (MediaInformationBox) findChildBox(mdia, new FourCC("minf"));
                            if (minf != null) {
                                SampleTableBox stbl =
                                        (SampleTableBox) findChildBox(minf, new FourCC("stbl"));
                                if (stbl != null) {
                                    dumpSamples(stbl, mdat, "tmcd");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void writeOutFile(String filename) throws IOException {
        List<Box> cleanBoxes = new ArrayList<>();
        for (Box box : boxes) {
            Box cleanBox;
            switch (box.getFourCC().toString()) {
                case "moov":
                    cleanBox = cleanMoov((MovieBox) box);
                default:
                    cleanBox = box;
            }
            cleanBoxes.add(cleanBox);
        }
        writeBoxes(cleanBoxes, filename);
    }

    protected void writeBoxes(List<Box> cleanBoxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : cleanBoxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private Box cleanMoov(MovieBox moov) {
        MovieBox cleanMoov = new MovieBox();
        for (Box box : moov.getNestedBoxes()) {
            if (box.getFourCC().toString().equals("iods")) {
                continue;
            }
            if (box.getFourCC().toString().equals("udta")) {
                // TODO: we do want udta
                continue;
            }
            if (box.getFourCC().toString().equals("trak")) {
                // TODO: we do want trak
                continue;
            }
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "trak" -> cleanTrak((TrackBox) box);
                        default -> box;
                    };
            cleanMoov.appendNestedBox(cleanBox);
        }
        return cleanMoov;
    }

    private Box cleanTrak(TrackBox trak) {
        TrackBox cleanTrak = new TrackBox();
        for (Box box : trak.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "mdia" -> cleanMdia((MediaBox) box);
                        default -> box;
                    };
            cleanTrak.appendNestedBox(cleanBox);
        }
        return cleanTrak;
    }

    private Box cleanMdia(MediaBox mdia) {
        MediaBox cleanMdia = new MediaBox();
        for (Box box : mdia.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "minf" -> cleanMinf((MediaInformationBox) box);
                        default -> box;
                    };
            cleanMdia.appendNestedBox(cleanBox);
        }
        return cleanMdia;
    }

    private Box cleanMinf(MediaInformationBox minf) {
        MediaInformationBox cleanMinf = new MediaInformationBox();
        for (Box box : minf.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                            // TODO
                            // case "dinf" -> cleanDinf((DataInformationBox)box);
                        case "stbl" -> cleanStbl((SampleTableBox) box);
                        default -> box;
                    };
            cleanMinf.appendNestedBox(cleanBox);
        }
        return cleanMinf;
    }

    private Box cleanStbl(SampleTableBox stbl) {
        SampleTableBox cleanStbl = new SampleTableBox();
        for (Box box : stbl.getNestedBoxes()) {
            Box cleanBox =
                    switch (box.getFourCC().toString()) {
                        case "stsd" -> cleanStsd((SampleDescriptionBox) box);
                        default -> box;
                    };
            cleanStbl.appendNestedBox(cleanBox);
        }
        return cleanStbl;
    }

    private Box cleanStsd(SampleDescriptionBox dirty) {
        var clean = new SampleDescriptionBox();
        for (SampleEntry entry : dirty.getSampleEntries()) {
            SampleEntry cleanEntry = cleanSampleEntry(entry);
            clean.appendSampleEntry(cleanEntry);
        }
        return clean;
    }

    private SampleEntry cleanSampleEntry(SampleEntry dirty) {
        if (dirty instanceof AudioSampleEntry audioDirty) {
            AudioSampleEntry clean = audioDirty;
            for (Box child : clean.getNestedBoxes()) {
                if (child instanceof ESDBox ugly) {
                    clean.removeNestedBox(ugly);
                }
            }
            return clean;
        } else {
            return dirty;
        }
    }
}
