package net.frogmouth.rnd.eofff.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.dinf.DataInformationBox;
import net.frogmouth.rnd.eofff.isobmff.dinf.DataInformationBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.dref.DataReferenceBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.mdhd.MediaHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.mdhd.MediaHeaderBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.mdia.MediaBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ILocBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ILocBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.meta.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.PitmBox;
import net.frogmouth.rnd.eofff.isobmff.meta.PrimaryItemBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBox;
import net.frogmouth.rnd.eofff.isobmff.minf.MediaInformationBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.mvhd.MovieHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.nmhd.NullMediaHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.nmhd.NullMediaHeaderBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.saio.SampleAuxiliaryInformationOffsetsBox;
import net.frogmouth.rnd.eofff.isobmff.saio.SampleAuxiliaryInformationOffsetsBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.saiz.SampleAuxiliaryInformationSizesBox;
import net.frogmouth.rnd.eofff.isobmff.saiz.SampleAuxiliaryInformationSizesBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBox;
import net.frogmouth.rnd.eofff.isobmff.stbl.SampleTableBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBox;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stsc.SampleToChunkBox;
import net.frogmouth.rnd.eofff.isobmff.stsc.SampleToChunkBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stsc.SampleToChunkEntry;
import net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBox;
import net.frogmouth.rnd.eofff.isobmff.stsd.SampleDescriptionBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stsd.URIBox;
import net.frogmouth.rnd.eofff.isobmff.stsd.URIBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stsd.URIMetaSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.stsd.URIMetaSampleEntryBuilder;
import net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBox;
import net.frogmouth.rnd.eofff.isobmff.stsz.SampleSizeBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stts.TimeToSampleBox;
import net.frogmouth.rnd.eofff.isobmff.stts.TimeToSampleBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stts.TimeToSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.tkhd.TrackHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.tkhd.TrackHeaderBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBox;
import net.frogmouth.rnd.eofff.isobmff.trak.TrackBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.tref.TrackReference;
import net.frogmouth.rnd.eofff.isobmff.tref.TrackReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.tref.TrackReferenceBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.tref.TrackReferenceTypeBox;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupBox;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupType;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupTypeBox;
import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerEncoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.IKlvKey;
import org.jmisb.api.klv.IKlvValue;
import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.INestedKlvValue;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.api.klv.st1204.CoreIdentifier;
import org.jmisb.core.klv.ArrayUtils;
import org.jmisb.mimd.st1902.MimdId;
import org.jmisb.mimd.st1902.MimdIdReference;
import org.jmisb.mimd.st1903.MIMD;
import org.jmisb.mimd.st1903.MIMD_Platforms;
import org.jmisb.mimd.st1903.MIMD_SecurityOptions;
import org.jmisb.mimd.st1903.MIMD_Timers;
import org.jmisb.mimd.st1903.MIMD_Version;
import org.jmisb.mimd.st1903.ReferenceSource;
import org.jmisb.mimd.st1903.Security;
import org.jmisb.mimd.st1903.Security_Classification;
import org.jmisb.mimd.st1903.Security_ClassifyingMethod;
import org.jmisb.mimd.st1903.TimeTransferMethod;
import org.jmisb.mimd.st1903.Timer;
import org.jmisb.mimd.st1903.Timer_UtcLeapSeconds;
import org.jmisb.mimd.st1905.Platform;
import org.jmisb.mimd.st1905.PlatformType;
import org.jmisb.mimd.st1905.Platform_Name;
import org.jmisb.mimd.st1905.Platform_Payloads;
import org.jmisb.mimd.st1906.Stage;
import org.jmisb.mimd.st1907.GISensorType;
import org.jmisb.mimd.st1907.GeoIntelligenceSensor;
import org.jmisb.mimd.st1907.GeoIntelligenceSensor_NCols;
import org.jmisb.mimd.st1907.GeoIntelligenceSensor_NRows;
import org.jmisb.mimd.st1907.GeoIntelligenceSensor_Stages;
import org.jmisb.mimd.st1907.Payload;
import org.jmisb.mimd.st1907.Payload_GeoIntelligenceSensors;
import org.jmisb.mimd.st1908.ImagerSystem;
import org.jmisb.mimd.st1908.ImagerSystem_Name;
import org.jmisb.mimd.st1908.MIIS;
import org.jmisb.mimd.st1908.MIIS_Version;
import org.jmisb.mimd.st1908.MinorCoreId;
import org.jmisb.mimd.st1908.MinorCoreId_Uuid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class ModifyTest {
    private static final Logger LOG = LoggerFactory.getLogger(ModifyTest.class);
    private static final int SECURITY_ID_GROUP = 1;
    private static final int SECURITY_ID_SERIAL = 2;
    private static final int STAGES_ID_GROUP = 2;
    private static final int TIMER_ID_GROUP = 3;
    private static final int TRACK_GROUP_ID = 16;
    /** Timescale is in inverse units. */
    private static final long TIMESCALE = 15360;

    private static final long NANOS_PER_SECOND = 1000 * 1000 * 1000;

    private static final int DURATION_SECONDS = 5 * 60;
    private static final int NUM_METADATA_CHUNKS = DURATION_SECONDS; // one per second
    private static final int FRAME_RATE = 30;
    private static final int NUM_FRAMES = DURATION_SECONDS * FRAME_RATE;
    private static final ZonedDateTime BASE_DATE =
            ZonedDateTime.of(1904, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    private static final ZonedDateTime SAMPLE_START_DATE =
            ZonedDateTime.of(2018, 3, 7, 16, 55, 00, 0, ZoneOffset.UTC);
    private static long SAMPLE_START_DATE_AS_NANOS =
            SAMPLE_START_DATE.toEpochSecond() * NANOS_PER_SECOND;
    private static final String MIMD_URI = "urn:misb.KLV.ul.060E2B34.02050101.0E010503.00000000";
    private static final String CORE_ID_URI =
            "urn:misb.KLV.ul.060E2B34.01010101.OE.010405.03000000";
    private static final byte[] MIIS_UUID_BYTES =
            new byte[] {
                0x43,
                (byte) 0xbb,
                0x3a,
                (byte) 0x8d,
                0x48,
                (byte) 0x99,
                0x4d,
                (byte) 0xec,
                (byte) 0x80,
                (byte) 0x93,
                (byte) 0xa9,
                0x09,
                0x52,
                0x48,
                (byte) 0xe5,
                (byte) 0xe6
            };

    private final UUID miisUuid;

    public ModifyTest() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(MIIS_UUID_BYTES);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();
        miisUuid = new UUID(high, low);
    }

    public List<Box> parseFile() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        Path testFile = Paths.get(classLoader.getResource("ex000-abandon-package.mp4").getPath());
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    @Test
    public void hackBoxes() throws IOException {
        List<Box> boxes = new ArrayList<>();
        boxes.addAll(parseFile());
        FileTypeBox ftyp = (FileTypeBox) getTopLevelBoxByFourCC(boxes, "ftyp");
        MovieBox moov = (MovieBox) getTopLevelBoxByFourCC(boxes, "moov");
        ChunkOffsetBox stco =
                (ChunkOffsetBox) findBox(moov, "trak", "mdia", "minf", "stbl", "stco");
        if (ftyp != null) {
            ftyp.setMajorBrand(new Brand("iso6"));
            ftyp.setMinorVersion(0);
            // ftyp.removeCompatibleBrand(new Brand("iso2"));
            ftyp.appendCompatibleBrand(new Brand("misb"));
            stco.shiftChunks(FourCC.BYTES);
        }
        FreeBox free = (FreeBox) getTopLevelBoxByFourCC(boxes, "free");
        long freeBoxSize = free.getSize();
        boxes.remove(free);
        stco.shiftChunks(-1 * freeBoxSize);
        if (moov != null) {
            Box udta = findChildBox(moov, "udta");
            if (udta != null) {
                moov.removeNestedBox(udta);
            }
            MetaBox metaBox = buildPresentationLevelMetaBox();
            moov.appendNestedBox(metaBox);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File("G340_to_NGA.STND.0076_0.1_MIFF_static.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    @Test
    public void addAuxiliaryInformation() throws IOException {
        List<Box> boxes = new ArrayList<>();
        List<Box> originalBoxes = parseFile();
        for (Box originalBox : originalBoxes) {
            if (originalBox instanceof FileTypeBox) {
                boxes.add(originalBox);
            }
            if (originalBox instanceof FreeBox) {
                boxes.add(originalBox);
            }
            if (originalBox instanceof MediaDataBox) {
                boxes.add(originalBox);
            }
        }
        FileTypeBox ftyp = (FileTypeBox) getTopLevelBoxByFourCC(boxes, "ftyp");
        MovieBox originalMoov = (MovieBox) getTopLevelBoxByFourCC(originalBoxes, "moov");
        MovieHeaderBox mvhd = (MovieHeaderBox) findBox(originalMoov, "mvhd");
        TrackBox motionImageryTrack = (TrackBox) findBox(originalMoov, "trak");
        TrackGroupBox motionImageryTrackGroup =
                new TrackGroupBoxBuilder()
                        .withGroup(new TrackGroupTypeBox(TrackGroupType.MSRC, TRACK_GROUP_ID))
                        .build();
        motionImageryTrack.appendNestedBox(motionImageryTrackGroup);
        motionImageryTrack.appendNestedBox(buildStaticMotionImageryTrackLevelMetaBox());
        TrackHeaderBox motionImageryTrackHeader =
                (TrackHeaderBox) findBox(motionImageryTrack, "tkhd");
        MediaBox mdia = (MediaBox) findBox(motionImageryTrack, "mdia");
        MediaInformationBox minf = (MediaInformationBox) findBox(mdia, "minf");
        SampleTableBox stbl = (SampleTableBox) findBox(minf, "stbl");
        ChunkOffsetBox stco = (ChunkOffsetBox) findBox(stbl, "stco");
        if (ftyp != null) {
            ftyp.setMajorBrand(new Brand("iso6"));
            ftyp.setMinorVersion(0);
            ftyp.removeCompatibleBrand(new Brand("iso2"));
            stco.shiftChunks(-1 * FourCC.BYTES);
            ftyp.appendCompatibleBrand(new Brand("misb"));
            stco.shiftChunks(FourCC.BYTES);
        }
        FreeBox free = (FreeBox) getTopLevelBoxByFourCC(boxes, "free");
        long freeBoxSize = free.getSize();
        boxes.remove(free);
        stco.shiftChunks(-1 * freeBoxSize);
        long startOfAuxInfoOffset = findEndOfMdat(stbl);
        MetaBox metaBox = buildPresentationLevelMetaBox();
        MediaDataBox mdat = (MediaDataBox) getTopLevelBoxByFourCC(boxes, "mdat");
        byte[] miisCoreIdAsBytes = getMiisCoreIdAsBytes();
        byte[] miisCoreLengthIdAsBytes = BerEncoder.encode(miisCoreIdAsBytes.length);
        long auxInfoSampleSize =
                Long.BYTES + miisCoreLengthIdAsBytes.length + miisCoreIdAsBytes.length;
        SampleAuxiliaryInformationSizesBox saiz =
                new SampleAuxiliaryInformationSizesBoxBuilder()
                        .withVersion(0)
                        .withFlags(1)
                        .withAuxInfoType(new FourCC("misb"))
                        .withAuxInfoTypeParameter(0)
                        .withURI("urn:misb.KLV.ul:060E2B34.02050101.0E010505.00000000")
                        .withDefaultSampleInfoSize(auxInfoSampleSize)
                        .withSampleCount(NUM_FRAMES)
                        .build();
        stbl.appendNestedBox(saiz);
        SampleAuxiliaryInformationOffsetsBoxBuilder saioBuilder =
                new SampleAuxiliaryInformationOffsetsBoxBuilder()
                        .withVersion(0)
                        .withFlags(1)
                        .withAuxInfoType(new FourCC("misb"))
                        .withAuxInfoTypeParameter(0);
        ByteArrayOutputStream auxInfoBaos = new ByteArrayOutputStream();
        for (int i = 0; i < NUM_FRAMES; i++) {
            long nanos = SAMPLE_START_DATE_AS_NANOS + (1000 * 1000 * 1000 * (long) i / 30);
            org.jmisb.api.klv.st0603.NanoPrecisionTimeStamp timeStamp =
                    new org.jmisb.api.klv.st0603.NanoPrecisionTimeStamp(nanos);
            byte[] nanoTimeStampBytes = timeStamp.getBytes();
            saioBuilder.addOffset(startOfAuxInfoOffset + i * auxInfoSampleSize);
            auxInfoBaos.writeBytes(nanoTimeStampBytes);
            auxInfoBaos.writeBytes(miisCoreLengthIdAsBytes);
            auxInfoBaos.writeBytes(miisCoreIdAsBytes);
        }
        long endOfAuxInfoOffset = startOfAuxInfoOffset + NUM_FRAMES * auxInfoSampleSize;
        byte[] auxInfoBytes = auxInfoBaos.toByteArray();
        byte[] mdatBytes = mdat.getData();
        byte[] originalBytesWithAuxInfo = new byte[mdatBytes.length + auxInfoBytes.length];
        System.arraycopy(mdatBytes, 0, originalBytesWithAuxInfo, 0, mdatBytes.length);
        System.arraycopy(
                auxInfoBytes, 0, originalBytesWithAuxInfo, mdatBytes.length, auxInfoBytes.length);
        mdat.setData(originalBytesWithAuxInfo);
        SampleAuxiliaryInformationOffsetsBox saio = saioBuilder.build();
        stbl.appendNestedBox(saio);
        TrackBox metadataTrack =
                makeMetadataTrak(
                        motionImageryTrackHeader.getDuration(),
                        saio,
                        saiz,
                        endOfAuxInfoOffset,
                        mdat);
        MovieBox moov =
                new MovieBoxBuilder()
                        .withNestedBox(mvhd)
                        .withNestedBox(motionImageryTrack)
                        .withNestedBox(metadataTrack)
                        .withNestedBox(metaBox)
                        .build();
        boxes.add(moov);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            box.writeTo(streamWriter);
        }
        File testOut = new File("G340_to_NGA.STND.0076_0.1_MIFF_aux_info.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private byte[] getMiisCoreIdAsBytes() {
        CoreIdentifier miisCoreId = new CoreIdentifier();
        miisCoreId.setMinorUUID(miisUuid);
        miisCoreId.setVersion(1);
        byte[] miisCoreIdAsBytes = miisCoreId.getRawBytesRepresentation();
        return miisCoreIdAsBytes;
    }

    private long findEndOfMdat(SampleTableBox stbl) {
        ChunkOffsetBox stco = (ChunkOffsetBox) findBox(stbl, "stco");
        int numberOfChunks = stco.getEntries().size();
        long lastChunkOffset = stco.getEntries().get(numberOfChunks - 1);
        SampleToChunkBox stsc = (SampleToChunkBox) findBox(stbl, "stsc");
        long sampleNumberForStartOfLastChunk = 0;
        for (int i = 0; i < stsc.getEntries().size() - 1; i++) {
            SampleToChunkEntry entry = stsc.getEntries().get(i);
            SampleToChunkEntry nextEntry = stsc.getEntries().get(i + 1);
            long numChunks = (nextEntry.firstChunk() - entry.firstChunk());
            sampleNumberForStartOfLastChunk += (numChunks * entry.samplesPerChunk());
        }
        SampleSizeBox stsz = (SampleSizeBox) findBox(stbl, "stsz");
        long bytesInLastChunk = 0;
        for (int sampleIndex = (int) sampleNumberForStartOfLastChunk;
                sampleIndex < stsz.getEntries().size();
                sampleIndex++) {
            bytesInLastChunk += stsz.getEntries().get(sampleIndex);
        }
        long startOfAuxInfoOffset = lastChunkOffset + bytesInLastChunk;
        return startOfAuxInfoOffset;
    }

    private TrackBox makeMetadataTrak(
            long motionImageryTrackHeaderDuration,
            SampleAuxiliaryInformationOffsetsBox saio,
            SampleAuxiliaryInformationSizesBox saiz,
            long endOfAuxInfoOffset,
            MediaDataBox mdat)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] timeVariableMetadataTrackData = makeTimeVaryingMIMD();
        TrackHeaderBox metadataTrackHeader =
                new TrackHeaderBoxBuilder()
                        .withVersion(0)
                        .withFlags(3)
                        .withTrackID(2)
                        .withDuration(motionImageryTrackHeaderDuration)
                        .build();
        HdlrBox metadataHandlerBox =
                new HdlrBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withHandlerType("meta")
                        .withName("MIMD timed metadata")
                        .build();
        TrackReferenceBox metadataTrackRefToMotionImagery =
                new TrackReferenceBoxBuilder()
                        .withReference(
                                new TrackReferenceTypeBox(TrackReference.CDSC, new long[] {1}))
                        .build();
        TrackGroupBox metadataTrackGroup =
                new TrackGroupBoxBuilder()
                        .withGroup(new TrackGroupTypeBox(TrackGroupType.MSRC, TRACK_GROUP_ID))
                        .build();
        NullMediaHeaderBox nmhd = new NullMediaHeaderBoxBuilder().build();
        DataReferenceBox metadataDref =
                new DataReferenceBoxBuilder().withLocalFileReference().build();
        DataInformationBox metadataDinf =
                new DataInformationBoxBuilder().withNestedBox(metadataDref).build();
        URIBox metadataUriBox =
                new URIBoxBuilder().withVersion(0).withFlags(0).withURI(MIMD_URI).build();
        URIMetaSampleEntry metadataTrackUrim =
                new URIMetaSampleEntryBuilder()
                        .withDataReferenceIndex(1)
                        .withNestedBox(metadataUriBox)
                        .build();
        SampleDescriptionBox metadataSampleDescriptionBox =
                new SampleDescriptionBoxBuilder().withNestedBox(metadataTrackUrim).build();
        TimeToSampleBox stts =
                new TimeToSampleBoxBuilder()
                        .withReference(new TimeToSampleEntry(NUM_FRAMES, TIMESCALE / FRAME_RATE))
                        .build();
        SampleToChunkBox stsc =
                new SampleToChunkBoxBuilder()
                        .addEntry(new SampleToChunkEntry(1, FRAME_RATE, 1))
                        .addEntry(new SampleToChunkEntry(NUM_METADATA_CHUNKS, FRAME_RATE, 1))
                        .build();
        SampleSizeBox stsz =
                new SampleSizeBoxBuilder()
                        .withSampleSize(timeVariableMetadataTrackData.length)
                        .withSampleCount(NUM_FRAMES)
                        .build();
        long firstChunkOffset = endOfAuxInfoOffset;
        ChunkOffsetBoxBuilder stcoBuilder = new ChunkOffsetBoxBuilder();
        for (int i = 0; i < NUM_METADATA_CHUNKS; i++) {
            baos.writeBytes(timeVariableMetadataTrackData);
            stcoBuilder.addOffset(firstChunkOffset + i * timeVariableMetadataTrackData.length);
        }
        ChunkOffsetBox stco = stcoBuilder.build();
        SampleTableBox metadataStbl =
                new SampleTableBoxBuilder()
                        .withNestedBox(metadataSampleDescriptionBox)
                        .withNestedBox(stts)
                        .withNestedBox(stsc)
                        .withNestedBox(stsz)
                        .withNestedBox(stco)
                        .withNestedBox(saiz)
                        .withNestedBox(saio)
                        .build();
        MediaInformationBox metadataMediaInformation =
                new MediaInformationBoxBuilder()
                        .withNestedBox(nmhd)
                        .withNestedBox(metadataDinf)
                        .withNestedBox(metadataStbl)
                        .build();
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        long secondsOffset = ChronoUnit.SECONDS.between(BASE_DATE, now);
        MediaHeaderBox metadataMediaHeaderBox =
                new MediaHeaderBoxBuilder()
                        .withCreationTime(secondsOffset)
                        .withModificationTime(secondsOffset)
                        .withTimeScale(TIMESCALE)
                        .withDuration(DURATION_SECONDS * TIMESCALE)
                        .withLanguage("eng")
                        .build();
        MediaBox metadataMediaBox =
                new MediaBoxBuilder()
                        .withNestedBox(metadataMediaHeaderBox)
                        .withNestedBox(metadataHandlerBox)
                        .withNestedBox(metadataMediaInformation)
                        .build();
        TrackBox metadataTrack =
                new TrackBoxBuilder()
                        .withNestedBox(metadataTrackHeader)
                        .withNestedBox(buildStaticMetadataTrackLevelMetaBox())
                        .withNestedBox(metadataTrackGroup)
                        .withNestedBox(metadataTrackRefToMotionImagery)
                        .withNestedBox(metadataMediaBox)
                        .build();
        byte[] mdatBytes = mdat.getData();
        byte[] timeVaryingMetadata = baos.toByteArray();
        byte[] mdatBytesWithTimeVaryingMetadata =
                new byte[mdatBytes.length + timeVaryingMetadata.length];
        System.arraycopy(mdatBytes, 0, mdatBytesWithTimeVaryingMetadata, 0, mdatBytes.length);
        System.arraycopy(
                timeVaryingMetadata,
                0,
                mdatBytesWithTimeVaryingMetadata,
                mdatBytes.length,
                timeVaryingMetadata.length);
        mdat.setData(mdatBytesWithTimeVaryingMetadata);
        return metadataTrack;
    }

    private MetaBox buildPresentationLevelMetaBox() throws IOException {
        HdlrBox hdlr =
                new HdlrBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withHandlerType("meta")
                        .withName("MIMD static metadata")
                        .build();
        ItemInfoEntry infe0 =
                new ItemInfoEntryBuilder()
                        .withVersion(2)
                        .withItemId(1)
                        .withItemType("uri ")
                        .withItemUriType(MIMD_URI)
                        .build();
        PitmBox pitm =
                new PrimaryItemBoxBuilder().withVersion(0).withFlags(0).withItemId(1).build();
        ItemInfoBox iinf =
                new ItemInfoBoxBuilder().withVersion(0).withFlags(0).withItemInfo(infe0).build();
        byte[] mimdMessageWithoutKeyAndLength = buildSimpleMIMD();
        ItemDataBox idat = new ItemDataBoxBuilder().addData(mimdMessageWithoutKeyAndLength).build();
        ILocItem ilocItem = new ILocItem();
        ilocItem.setItemId(1);
        ilocItem.setBaseOffset(0);
        ilocItem.setDataReferenceIndex(0);
        ilocItem.setConstructionMethod(1);
        ILocExtent ilocItemExtent = new ILocExtent();
        ilocItemExtent.setExtentOffset(0);
        ilocItemExtent.setExtentLength(mimdMessageWithoutKeyAndLength.length);
        ilocItem.addExtent(ilocItemExtent);
        ILocBox iloc = new ILocBoxBuilder().withVersion(1).addItem(ilocItem).build();
        MetaBox metaBox =
                new MetaBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withNestedBox(hdlr)
                        .withNestedBox(pitm)
                        .withNestedBox(iinf)
                        .withNestedBox(iloc)
                        .withNestedBox(idat)
                        .build();
        return metaBox;
    }

    private MetaBox buildStaticMotionImageryTrackLevelMetaBox() throws IOException {
        HdlrBox hdlr =
                new HdlrBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withHandlerType("meta")
                        .withName("Motion Imagery Track Static Metadata")
                        .build();
        ItemInfoEntry infeMimd =
                new ItemInfoEntryBuilder()
                        .withVersion(2)
                        .withItemId(2)
                        .withItemType("uri ")
                        .withItemUriType(MIMD_URI)
                        .build();
        ItemInfoEntry infeCoreId =
                new ItemInfoEntryBuilder()
                        .withVersion(2)
                        .withItemId(17)
                        .withItemType("uri ")
                        .withItemUriType(CORE_ID_URI)
                        .build();
        PitmBox pitm =
                new PrimaryItemBoxBuilder().withVersion(0).withFlags(0).withItemId(2).build();
        ItemInfoBox iinf =
                new ItemInfoBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withItemInfo(infeMimd)
                        .withItemInfo(infeCoreId)
                        .build();
        ILocItem ilocItemMIMD = new ILocItem();
        ilocItemMIMD.setItemId(2);
        ilocItemMIMD.setBaseOffset(0);
        ilocItemMIMD.setDataReferenceIndex(0);
        ilocItemMIMD.setConstructionMethod(1);
        byte[] mimdMessageWithoutKeyAndLength = buildStaticMotionImageryTrackMIMD();
        ILocExtent mimdExtent = new ILocExtent();
        mimdExtent.setExtentOffset(0);
        mimdExtent.setExtentLength(mimdMessageWithoutKeyAndLength.length);
        ilocItemMIMD.addExtent(mimdExtent);
        ILocItem ilocItemCoreId = new ILocItem();
        ilocItemCoreId.setItemId(17);
        ilocItemCoreId.setBaseOffset(0);
        ilocItemCoreId.setDataReferenceIndex(0);
        ilocItemCoreId.setConstructionMethod(1);
        byte[] miisCoreIdAsBytes = getMiisCoreIdAsBytes();
        ILocExtent coreIdExtent = new ILocExtent();
        coreIdExtent.setExtentOffset(mimdExtent.getExtentLength());
        coreIdExtent.setExtentLength(miisCoreIdAsBytes.length);
        ilocItemCoreId.addExtent(coreIdExtent);
        ILocBox iloc =
                new ILocBoxBuilder()
                        .withVersion(1)
                        .addItem(ilocItemMIMD)
                        .addItem(ilocItemCoreId)
                        .build();
        ItemDataBox idat =
                new ItemDataBoxBuilder()
                        .addData(mimdMessageWithoutKeyAndLength)
                        .addData(miisCoreIdAsBytes)
                        .build();
        MetaBox metaBox =
                new MetaBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withNestedBox(hdlr)
                        .withNestedBox(pitm)
                        .withNestedBox(iinf)
                        .withNestedBox(iloc)
                        .withNestedBox(idat)
                        .build();
        return metaBox;
    }

    private MetaBox buildStaticMetadataTrackLevelMetaBox() throws IOException {
        HdlrBox hdlr =
                new HdlrBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withHandlerType("meta")
                        .withName("Metadata Track Static Metadata")
                        .build();
        ItemInfoEntry infe =
                new ItemInfoEntryBuilder()
                        .withVersion(2)
                        .withItemId(3)
                        .withItemType("uri ")
                        .withItemUriType(MIMD_URI)
                        .build();
        PitmBox pitm =
                new PrimaryItemBoxBuilder().withVersion(0).withFlags(0).withItemId(3).build();
        ItemInfoBox iinf =
                new ItemInfoBoxBuilder().withVersion(0).withFlags(0).withItemInfo(infe).build();
        byte[] mimdMessageWithoutKeyAndLength = buildStaticMetadataTrackMIMD();
        ItemDataBox idat = new ItemDataBoxBuilder().addData(mimdMessageWithoutKeyAndLength).build();
        ILocItem ilocItem = new ILocItem();
        ilocItem.setItemId(3);
        ilocItem.setBaseOffset(0);
        ilocItem.setDataReferenceIndex(0);
        ilocItem.setConstructionMethod(1);
        ILocExtent ilocItemExtent = new ILocExtent();
        ilocItemExtent.setExtentOffset(0);
        ilocItemExtent.setExtentLength(mimdMessageWithoutKeyAndLength.length);
        ilocItem.addExtent(ilocItemExtent);
        ILocBox iloc = new ILocBoxBuilder().withVersion(1).addItem(ilocItem).build();
        MetaBox metaBox =
                new MetaBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withNestedBox(hdlr)
                        .withNestedBox(pitm)
                        .withNestedBox(iinf)
                        .withNestedBox(iloc)
                        .withNestedBox(idat)
                        .build();
        return metaBox;
    }

    private static Box findBox(AbstractContainerBox parent, String... fourCCs) {
        Box child = null;
        for (String fourCC : fourCCs) {
            child = findChildBox(parent, fourCC);
            if (child instanceof AbstractContainerBox abstractContainerBox) {
                parent = abstractContainerBox;
            }
        }
        return child;
    }

    private static Box findChildBox(AbstractContainerBox parent, String fourCC) {
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
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

    private static byte[] buildSimpleMIMD() throws IOException {
        try {
            MIMD mimd = new MIMD();
            mimd.setVersion(new MIMD_Version(1));
            mimd.setSecurityOptions(buildSecurityOptions());
            mimd.setCompositeProductSecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeProductSecurity",
                            "Security"));
            List<Timer> timerList = new ArrayList<>();
            Timer timer = new Timer();
            timer.setMimdId(new MimdId(1, TIMER_ID_GROUP));
            Timer_UtcLeapSeconds timerUtcLeapSeconds = new Timer_UtcLeapSeconds(37);
            timer.setUtcLeapSeconds(timerUtcLeapSeconds);
            timerList.add(timer);
            MIMD_Timers timers = new MIMD_Timers(timerList);
            mimd.setTimers(timers);
            mimd.setPlatforms(buildPlatforms());
            System.out.println("Static metadata for timed presentation");
            dumpMimd(mimd);
            byte[] mimdBytes = mimd.frameMessage(false);
            BerField ber = BerDecoder.decode(mimdBytes, UniversalLabel.LENGTH, false);
            int lengthOfLength = ber.getLength();
            byte[] mimdValueBytes = new byte[ber.getValue()];
            System.arraycopy(
                    mimdBytes,
                    UniversalLabel.LENGTH + lengthOfLength,
                    mimdValueBytes,
                    0,
                    mimdValueBytes.length);
            return mimdValueBytes;
        } catch (KlvParseException ex) {
            throw new IOException(ex.toString());
        }
    }

    private static byte[] buildStaticMotionImageryTrackMIMD() throws IOException {
        try {
            MIMD mimd = new MIMD();
            mimd.setVersion(new MIMD_Version(1));
            mimd.setCompositeMotionImagerySecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeMotionImagerySecurity",
                            "Security"));
            System.out.println("Static metadata for Motion Imagery track");
            dumpMimd(mimd);
            byte[] mimdBytes = mimd.frameMessage(false);
            BerField ber = BerDecoder.decode(mimdBytes, UniversalLabel.LENGTH, false);
            int lengthOfLength = ber.getLength();
            byte[] mimdValueBytes = new byte[ber.getValue()];
            System.arraycopy(
                    mimdBytes,
                    UniversalLabel.LENGTH + lengthOfLength,
                    mimdValueBytes,
                    0,
                    mimdValueBytes.length);
            return mimdValueBytes;
        } catch (KlvParseException ex) {
            throw new IOException(ex.toString());
        }
    }

    private static byte[] buildStaticMetadataTrackMIMD() throws IOException {
        try {
            MIMD mimd = new MIMD();
            mimd.setVersion(new MIMD_Version(1));
            mimd.setCompositeMetadataSecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeMetadataSecurity",
                            "Security"));
            System.out.println("Static metadata for Metadata track");
            dumpMimd(mimd);
            byte[] mimdBytes = mimd.frameMessage(false);
            BerField ber = BerDecoder.decode(mimdBytes, UniversalLabel.LENGTH, false);
            int lengthOfLength = ber.getLength();
            byte[] mimdValueBytes = new byte[ber.getValue()];
            System.arraycopy(
                    mimdBytes,
                    UniversalLabel.LENGTH + lengthOfLength,
                    mimdValueBytes,
                    0,
                    mimdValueBytes.length);
            return mimdValueBytes;
        } catch (KlvParseException ex) {
            throw new IOException(ex.toString());
        }
    }

    private static MIMD_SecurityOptions buildSecurityOptions() throws KlvParseException {
        Security security = new Security();
        MimdId securityId = new MimdId(SECURITY_ID_SERIAL, SECURITY_ID_GROUP);
        security.setMimdId(securityId);
        security.setClassification(new Security_Classification("UNCLASSIFIED//"));
        security.setClassifyingMethod(new Security_ClassifyingMethod("US-1"));
        List<Security> securities = new ArrayList<>();
        securities.add(security);
        MIMD_SecurityOptions securityOptions = new MIMD_SecurityOptions(securities);
        return securityOptions;
    }

    private static MIMD_Platforms buildPlatforms() throws KlvParseException {
        Platform platform = new Platform();
        Platform_Name platformName = new Platform_Name("MEVA Camera G340");
        platform.setName(platformName);
        platform.setType(PlatformType.Structure);
        // platform.setStages(buildStages());
        platform.setPayloads(buildPayloads());
        List<Platform> platformsList = new ArrayList<>();
        platformsList.add(platform);
        MIMD_Platforms platforms = new MIMD_Platforms(platformsList);
        return platforms;
    }

    private static Platform_Payloads buildPayloads() throws KlvParseException {
        Payload payload = new Payload();
        payload.setGeoIntelligenceSensors(buildGeoIntelligenceSensors());
        List<Payload> payloadsList = new ArrayList<>();
        payloadsList.add(payload);
        Platform_Payloads payloads = new Platform_Payloads(payloadsList);
        return payloads;
    }

    private static Payload_GeoIntelligenceSensors buildGeoIntelligenceSensors()
            throws KlvParseException {
        List<GeoIntelligenceSensor> sensorList = new ArrayList<>();
        GeoIntelligenceSensor geoIntelligenceSensor = new GeoIntelligenceSensor();
        GeoIntelligenceSensor_NCols numColumns = new GeoIntelligenceSensor_NCols(1920);
        geoIntelligenceSensor.setNCols(numColumns);
        GeoIntelligenceSensor_NRows numRows = new GeoIntelligenceSensor_NRows(1080);
        geoIntelligenceSensor.setNRows(numRows);
        geoIntelligenceSensor.setType(GISensorType.EO);
        geoIntelligenceSensor.setImagerSystem(buildImagerSystem());
        List<Stage> giSensorStagesList = new ArrayList<>();
        Stage geoIntelligenceSensorStage = new Stage();
        geoIntelligenceSensorStage.setParentStage(
                new MimdIdReference(3, STAGES_ID_GROUP, "ParentStage", "Stage"));
        giSensorStagesList.add(geoIntelligenceSensorStage);
        GeoIntelligenceSensor_Stages giSensorStages =
                new GeoIntelligenceSensor_Stages(giSensorStagesList);
        geoIntelligenceSensor.setStages(giSensorStages);
        sensorList.add(geoIntelligenceSensor);
        Payload_GeoIntelligenceSensors geoIntelligenceSensors =
                new Payload_GeoIntelligenceSensors(sensorList);
        return geoIntelligenceSensors;
    }

    private static ImagerSystem buildImagerSystem() throws KlvParseException {
        ImagerSystem imagerSystem = new ImagerSystem();
        ImagerSystem_Name imagerSystemName = new ImagerSystem_Name("Shopkeeper Mini PTZ");
        imagerSystem.setName(imagerSystemName);
        imagerSystem.setMiis(buildMIIS());
        // TODO: need FOV horizontal and vertical
        return imagerSystem;
    }

    private static MIIS buildMIIS() throws KlvParseException {
        MIIS miis = new MIIS();
        MinorCoreId minorCoreId = new MinorCoreId();
        MinorCoreId_Uuid minorCoreUUID =
                new MinorCoreId_Uuid(
                        new long[] {
                            0x43, 0xbb, 0x3a, 0x8d, 0x48, 0x99, 0x4d, 0xec, 0x80, 0x93, 0xa9, 0x09,
                            0x52, 0x48, 0xe5, 0xe6
                        });
        minorCoreId.setUuid(minorCoreUUID);
        miis.setMinorCoreId(minorCoreId);
        MIIS_Version miisVersion = new MIIS_Version(1);
        miis.setVersion(miisVersion);
        return miis;
    }

    /*
    private static Platform_Stages buildStages() throws KlvParseException {
        KrtdConverter krtd =
                new KrtdConverter(
                        new File(
                                "/home/bradh/meva/meva-data-repo/metadata/camera-models/krtd/2018-03-07.11-45-09.11-50-09.bus.G340.krtd"));
        List<Stage> stagesList = new ArrayList<>();
        stagesList.add(buildRootStage(krtd));
        stagesList.add(buildCameraOffsetStage(krtd));
        stagesList.add(buildCameraOrientationStage(krtd));
        Platform_Stages stages = new Platform_Stages(stagesList);
        return stages;
    }

    private static Stage buildRootStage(KrtdConverter krtd) throws KlvParseException {
        Stage rootStage = new Stage();
        rootStage.setMimdId(new MimdId(1, STAGES_ID_GROUP));
        rootStage.setParentStage(new MimdIdReference(0, STAGES_ID_GROUP, "ParentStage", "Stage"));
        Position rootStagePosition = new Position();
        AbsGeodetic absGeodeticRootStage = new AbsGeodetic();
        absGeodeticRootStage.setLat(new AbsGeodetic_Lat(krtd.getAbsGeodeticLat()));
        absGeodeticRootStage.setLon(new AbsGeodetic_Lon(krtd.getAbsGeodeticLon()));
        absGeodeticRootStage.setHae(new AbsGeodetic_Hae(krtd.getAbsGeodeticHae()));
        rootStagePosition.setAbsGeodetic(absGeodeticRootStage);
        rootStage.setPosition(rootStagePosition);
        return rootStage;
    }

    private static Stage buildCameraOffsetStage(KrtdConverter krtd) throws KlvParseException {
        Stage cameraPositionStage = new Stage();
        cameraPositionStage.setMimdId(new MimdId(2, STAGES_ID_GROUP));
        cameraPositionStage.setParentStage(
                new MimdIdReference(1, STAGES_ID_GROUP, "ParentStage", "Stage"));
        Position cameraPositionStagePosition = new Position();
        RelPosition relPosition = new RelPosition();
        relPosition.setX(new RelPosition_X(krtd.getRelPositionX()));
        relPosition.setY(new RelPosition_Y(krtd.getRelPositionY()));
        relPosition.setZ(new RelPosition_Z(krtd.getRelPositionZ()));
        cameraPositionStagePosition.setRelPosition(relPosition);
        cameraPositionStage.setPosition(cameraPositionStagePosition);
        return cameraPositionStage;
    }

    private static Stage buildCameraOrientationStage(KrtdConverter krtd) throws KlvParseException {
        Stage cameraOrientationStage = new Stage();
        cameraOrientationStage.setMimdId(new MimdId(3, STAGES_ID_GROUP));
        cameraOrientationStage.setParentStage(
                new MimdIdReference(2, STAGES_ID_GROUP, "ParentStage", "Stage"));
        Orientation cameraOrientationStageStage = new Orientation();
        AbsEnu absEnu = new AbsEnu();
        // TODO: add orientation stage values
        cameraOrientationStageStage.setAbsEnu(absEnu);
        cameraOrientationStage.setOrientation(cameraOrientationStageStage);
        return cameraOrientationStage;
    }
    */

    private static void dumpMimd(MIMD mimd) {
        outputTopLevelMessageHeader(mimd);
        outputNestedKlvValue(mimd, 1);
    }

    private static void outputTopLevelMessageHeader(IMisbMessage misbMessage) {
        String displayHeader = misbMessage.displayHeader();
        if (displayHeader.equalsIgnoreCase("Unknown")) {
            System.out.println(
                    displayHeader
                            + " ["
                            + ArrayUtils.toHexString(misbMessage.getUniversalLabel().getBytes())
                                    .trim()
                            + "]");
            outputUnknownMessageContent(misbMessage.frameMessage(true));
        } else {
            System.out.println(displayHeader);
        }
    }

    private static void outputUnknownMessageContent(byte[] frameMessage) {
        System.out.println(ArrayUtils.toHexString(frameMessage, 16, true));
    }

    private static void outputNestedKlvValue(INestedKlvValue nestedKlvValue, int indentationLevel) {
        for (IKlvKey identifier : nestedKlvValue.getIdentifiers()) {
            IKlvValue value = nestedKlvValue.getField(identifier);
            outputValueWithIndentation(value, indentationLevel);
            // if this has nested content, output that at the next indentation level
            if (value instanceof INestedKlvValue) {
                outputNestedKlvValue((INestedKlvValue) value, indentationLevel + 1);
            }
        }
    }

    private static void outputValueWithIndentation(IKlvValue value, int indentationLevel) {
        for (int i = 0; i < indentationLevel; ++i) {
            System.out.print("\t");
        }
        System.out.println(value.getDisplayName() + ": " + value.getDisplayableValue());
    }

    private byte[] makeTimeVaryingMIMD() throws IOException {
        try {
            MIMD mimd = new MIMD();

            List<Timer> timerList = new ArrayList<>();
            Timer timer = new Timer();
            timer.setMimdId(new MimdId(1, TIMER_ID_GROUP));
            Timer_UtcLeapSeconds timerUtcLeapSeconds = new Timer_UtcLeapSeconds(37);
            TimeTransferMethod timeTransferMethod = TimeTransferMethod.Unknown;
            timer.setTimeTransferMethod(timeTransferMethod);
            ReferenceSource referenceSource = ReferenceSource.Unknown;
            timer.setReferenceSource(referenceSource);
            timerList.add(timer);
            MIMD_Timers timers = new MIMD_Timers(timerList);
            mimd.setTimers(timers);
            mimd.setPlatforms(buildPlatforms());
            System.out.println("Static metadata for timed presentation");
            dumpMimd(mimd);
            byte[] mimdBytes = mimd.frameMessage(false);
            BerField ber = BerDecoder.decode(mimdBytes, UniversalLabel.LENGTH, false);
            int lengthOfLength = ber.getLength();
            byte[] mimdValueBytes = new byte[ber.getValue()];
            System.arraycopy(
                    mimdBytes,
                    UniversalLabel.LENGTH + lengthOfLength,
                    mimdValueBytes,
                    0,
                    mimdValueBytes.length);
            return mimdValueBytes;
        } catch (KlvParseException ex) {
            throw new IOException(ex.toString());
        }
    }
}
