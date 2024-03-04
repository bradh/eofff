package net.frogmouth.rnd.eofff.goprotools;

import java.io.File;
import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.gopro.gpmf.GPMF;
import net.frogmouth.rnd.eofff.gopro.gpmf.GPMFParser;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBox;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBox;
import net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBox;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBox;

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
}
