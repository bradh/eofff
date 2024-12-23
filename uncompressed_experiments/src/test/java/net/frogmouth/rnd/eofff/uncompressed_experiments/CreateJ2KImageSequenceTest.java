package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.ccst.CodingConstraintsBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ISO639Language;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.dinf.DataInformationBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataEntryUrlBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.mdhd.MediaHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.mvhd.MovieHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBox;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBox;
import net.frogmouth.rnd.eofff.isobmff.stsc.SampleToChunkBox;
import net.frogmouth.rnd.eofff.isobmff.stsc.SampleToChunkEntry;
import net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBox;
import net.frogmouth.rnd.eofff.isobmff.stss.SyncSampleBox;
import net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBox;
import net.frogmouth.rnd.eofff.isobmff.stts.TimeToSampleBox;
import net.frogmouth.rnd.eofff.isobmff.stts.TimeToSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.tkhd.TrackHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBox;
import net.frogmouth.rnd.eofff.isobmff.vmhd.VideoMediaHeaderBox;
import net.frogmouth.rnd.eofff.jpeg2000.J2KHeaderInfo;
import net.frogmouth.rnd.eofff.jpeg2000.J2KHeaderItemProperty;
import net.frogmouth.rnd.eofff.jpeg2000.sampleentry.J2KSampleEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateJ2KImageSequenceTest extends GIMIValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CreateJ2KImageSequenceTest.class);

    private final int imageInterval = 3 * 1000;
    private final String sourceDirectory;
    private final List<FileDirectory> tiffSourceFiles = new ArrayList<>();
    private final List<String> j2kFiles = new ArrayList<>();
    private final int LENGTH_MDAT_HEADER = 8;

    public CreateJ2KImageSequenceTest() throws IOException {
        sourceDirectory =
                "/home/bradh/testbed20/temporal/train/L15-1848E-0793N_7394_5018_13/images";
        Files.walk(Paths.get(sourceDirectory))
                .filter(Files::isRegularFile)
                .sorted()
                .forEach(filePath -> processFile(filePath.toFile()));
    }

    private void processFile(File file) {
        System.out.println(file.toString());
        try {
            mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(file);
            List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
            FileDirectory directory = directories.get(0);
            tiffSourceFiles.add(directory);
            String heifFile = Files.createTempFile(null, ".hej2").toString();
            ProcessBuilder builder =
                    new ProcessBuilder(
                            "/usr/local/bin/heif-enc",
                            file.getPath(),
                            "--no-alpha",
                            "--jpeg2000",
                            "--output",
                            heifFile);
            Process process = builder.start();
            process.waitFor();
            // TODO: better error checks and handling
            InputStream errorStream = process.getErrorStream();
            String errors = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println(errors);
            j2kFiles.add(heifFile);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex);
        }
    }

    @Test
    public void writeImageSegmentBasic() throws IOException {
        MediaDataBox mdat = makeMediaDataBox();

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        ChunkOffsetBox stco = new ChunkOffsetBox();
        stco.addEntry(0L);
        MovieBox moov = createMovieBoxBasicImageSequence(stco);
        boxes.add(moov);
        long lengthOfBoxesBeforeMdat = ftyp.getSize() + moov.getSize();
        stco.shiftChunks(lengthOfBoxesBeforeMdat + LENGTH_MDAT_HEADER);
        boxes.add(mdat);

        writeBoxes(boxes, "image_sequence_j2k.j2is");
    }

    private void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(
                testOut.toPath(),
                baos.toByteArray(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("msf1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("msf1"));
        fileTypeBox.addCompatibleBrand(new Brand("j2is"));
        fileTypeBox.addCompatibleBrand(new Brand("isoa"));
        fileTypeBox.addCompatibleBrand(new Brand("geo1"));
        return fileTypeBox;
    }

    private MovieBox createMovieBoxBasicImageSequence(ChunkOffsetBox stco) throws IOException {
        MovieBox moov = new MovieBox();
        MovieHeaderBox mvhd = new MovieHeaderBox();
        mvhd.setDuration((this.j2kFiles.size() - 1) * imageInterval);
        moov.appendNestedBox(mvhd);
        TrackBox trak = new TrackBox();
        TrackHeaderBox tkhd = new TrackHeaderBox();
        tkhd.setFlags(0x01 | 0x02);
        tkhd.setCreationTime(mvhd.getCreationTime());
        tkhd.setModificationTime(mvhd.getModificationTime());
        tkhd.setDuration(mvhd.getDuration());
        // Assume all the files are the same
        tkhd.setWidth(tiffSourceFiles.get(0).getImageWidth().intValue());
        tkhd.setHeight(tiffSourceFiles.get(0).getImageHeight().intValue());
        trak.appendNestedBox(tkhd);
        MediaBox mdia = new MediaBox();
        MediaHeaderBox mdhd = new MediaHeaderBox();
        mdhd.setCreationTime(mvhd.getCreationTime());
        mdhd.setModificationTime(mvhd.getModificationTime());
        mdhd.setDuration(mvhd.getDuration());
        mdhd.setLanguage(new ISO639Language("eng"));
        mdia.appendNestedBox(mdhd);
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType(new FourCC("pict"));
        mdia.appendNestedBox(hdlr);
        MediaInformationBox minf = new MediaInformationBox();
        VideoMediaHeaderBox vmhd = new VideoMediaHeaderBox();
        minf.appendNestedBox(vmhd);
        DataInformationBox dinf = new DataInformationBox();
        DataReferenceBox dref = new DataReferenceBox();
        DataEntryUrlBox url = new DataEntryUrlBox();
        url.setFlags(0x01);
        dref.addDataReference(url);
        dinf.appendNestedBox(dref);
        minf.appendNestedBox(dinf);
        SampleTableBox stbl = new SampleTableBox();
        SampleDescriptionBox stsd = new SampleDescriptionBox();
        VisualSampleEntry j2kiSampleEntry = new J2KSampleEntry();
        j2kiSampleEntry.setWidth(tiffSourceFiles.get(0).getImageWidth().intValue());
        j2kiSampleEntry.setHeight(tiffSourceFiles.get(0).getImageHeight().intValue());
        j2kiSampleEntry.setDataReferenceIndex(1);
        {
            FileParser fileParser = new FileParser();
            List<Box> boxes = fileParser.parse(Path.of(this.j2kFiles.get(0)));
            for (Box box : boxes) {
                if (box instanceof MetaBox meta) {
                    for (var metaChild : meta.getNestedBoxes()) {
                        if (metaChild instanceof ItemPropertiesBox iprp) {
                            ItemPropertyContainerBox ipco = iprp.getItemProperties();
                            for (var prop : ipco.getProperties()) {
                                if (prop instanceof J2KHeaderItemProperty j2kHProp) {
                                    J2KHeaderInfo j2kHBox = new J2KHeaderInfo();
                                    j2kHBox.setChannels(j2kHProp.getChannels());
                                    j2kHBox.setComponents(j2kHProp.getComponents());
                                    j2kiSampleEntry.appendNestedBox(j2kHBox);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        CodingConstraintsBox ccst = new CodingConstraintsBox();
        ccst.setAllRefPicsIntra(true);
        ccst.setIntraPredUsed(true);
        ccst.setMaxRefPerPic((byte) 1);
        j2kiSampleEntry.appendNestedBox(ccst);
        stsd.appendSampleEntry(j2kiSampleEntry);
        stbl.appendNestedBox(stsd);
        TimeToSampleBox stts = new TimeToSampleBox();
        TimeToSampleEntry timeToSampleEntry =
                new TimeToSampleEntry(j2kFiles.size(), this.imageInterval);
        stts.addEntry(timeToSampleEntry);
        stbl.appendNestedBox(stts);
        SampleToChunkBox stsc = new SampleToChunkBox();
        // Only one chunk with all the samples
        SampleToChunkEntry sampleToChunkEntry = new SampleToChunkEntry(1, j2kFiles.size(), 1);
        stsc.addEntry(sampleToChunkEntry);
        stbl.appendNestedBox(stsc);
        stbl.appendNestedBox(stco);
        SampleSizeBox stsz = new SampleSizeBox();
        for (String j2k : this.j2kFiles) {
            FileParser fileParser = new FileParser();
            List<Box> boxes = fileParser.parse(Path.of(j2k));
            for (Box box : boxes) {
                if (box instanceof MediaDataBox mdat) {
                    stsz.addEntry((long) mdat.getData().length);
                }
            }
        }
        stbl.appendNestedBox(stsz);
        SyncSampleBox stss = new SyncSampleBox();
        for (int i = 0; i < this.j2kFiles.size(); i++) {
            stss.addEntry((long) (i + 1));
        }
        stbl.appendNestedBox(stss);
        // SampleGroupDescriptionBox sgpd = new SampleGroupDescriptionBox();
        // TODO: samples
        // stbl.appendNestedBox(sgpd);
        // SampleToGroupBox sbgp = new SampleToGroupBox();
        // TODO entries
        // stbl.appendNestedBox(sbgp);
        minf.appendNestedBox(stbl);
        mdia.appendNestedBox(minf);
        trak.appendNestedBox(mdia);
        moov.appendNestedBox(trak);
        return moov;
    }

    private MediaDataBox makeMediaDataBox() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        mdat.setData(makeData());
        return mdat;
    }

    private byte[] makeData() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (String heif : this.j2kFiles) {
            FileParser fileParser = new FileParser();
            List<Box> boxes = fileParser.parse(Path.of(heif));
            for (Box box : boxes) {
                if (box instanceof MediaDataBox mdat) {
                    baos.write(mdat.getData());
                }
            }
        }
        return baos.toByteArray();
    }
}
