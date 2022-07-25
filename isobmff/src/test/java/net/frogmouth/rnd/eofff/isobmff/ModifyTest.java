package net.frogmouth.rnd.eofff.isobmff;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HdlrBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemDataBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.meta.ItemInfoEntryBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.meta.PitmBox;
import net.frogmouth.rnd.eofff.isobmff.meta.PrimaryItemBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.moov.MediaBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MediaInformationBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBox;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.moov.MovieHeaderBox;
import net.frogmouth.rnd.eofff.isobmff.moov.SampleTableBox;
import net.frogmouth.rnd.eofff.isobmff.moov.TrackBox;
import net.frogmouth.rnd.eofff.isobmff.moov.TrackBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.saiz.SampleAuxiliaryInformationSizesBox;
import net.frogmouth.rnd.eofff.isobmff.saiz.SampleAuxiliaryInformationSizesBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.stco.ChunkOffsetBox;
import org.jmisb.api.common.KlvParseException;
import org.jmisb.api.klv.BerDecoder;
import org.jmisb.api.klv.BerField;
import org.jmisb.api.klv.IKlvKey;
import org.jmisb.api.klv.IKlvValue;
import org.jmisb.api.klv.IMisbMessage;
import org.jmisb.api.klv.INestedKlvValue;
import org.jmisb.api.klv.UniversalLabel;
import org.jmisb.core.klv.ArrayUtils;
import org.jmisb.mimd.st1902.MimdId;
import org.jmisb.mimd.st1902.MimdIdReference;
import org.jmisb.mimd.st1903.MIMD;
import org.jmisb.mimd.st1903.MIMD_Platforms;
import org.jmisb.mimd.st1903.MIMD_SecurityOptions;
import org.jmisb.mimd.st1903.MIMD_Version;
import org.jmisb.mimd.st1903.Security;
import org.jmisb.mimd.st1903.Security_Classification;
import org.jmisb.mimd.st1903.Security_ClassifyingMethod;
import org.jmisb.mimd.st1905.Platform;
import org.jmisb.mimd.st1905.PlatformType;
import org.jmisb.mimd.st1905.Platform_Name;
import org.jmisb.mimd.st1905.Platform_Payloads;
import org.jmisb.mimd.st1905.Platform_Stages;
import org.jmisb.mimd.st1906.AbsEnu;
import org.jmisb.mimd.st1906.AbsGeodetic;
import org.jmisb.mimd.st1906.AbsGeodetic_Hae;
import org.jmisb.mimd.st1906.AbsGeodetic_Lat;
import org.jmisb.mimd.st1906.AbsGeodetic_Lon;
import org.jmisb.mimd.st1906.Orientation;
import org.jmisb.mimd.st1906.Position;
import org.jmisb.mimd.st1906.RelPosition;
import org.jmisb.mimd.st1906.RelPosition_X;
import org.jmisb.mimd.st1906.RelPosition_Y;
import org.jmisb.mimd.st1906.RelPosition_Z;
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

    public ModifyTest() {}

    public List<Box> parseFile() throws IOException {
        File file = new File("/home/bradh/meva/2018-03-07.16-55-00.17-00-00.bus.G340.recoded.mp4");
        Path testFile = file.toPath();
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
            ftyp.setMajorBrand(new FourCC("iso6"));
            ftyp.setMinorVersion(0);
            // ftyp.removeCompatibleBrand(new FourCC("iso2"));
            ftyp.appendCompatibleBrand(new FourCC("misb"));
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
            MetaBox metaBox = buildMetaBox();
            moov.appendNestedBox(metaBox);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Box box : boxes) {
            box.writeTo(baos);
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
        MediaBox mdia = (MediaBox) findBox(motionImageryTrack, "mdia");
        MediaInformationBox minf = (MediaInformationBox) findBox(mdia, "minf");
        SampleTableBox stbl = (SampleTableBox) findBox(minf, "stbl");
        ChunkOffsetBox stco = (ChunkOffsetBox) findBox(stbl, "stco");
        if (ftyp != null) {
            ftyp.setMajorBrand(new FourCC("iso6"));
            ftyp.setMinorVersion(0);
            ftyp.removeCompatibleBrand(new FourCC("iso2"));
            stco.shiftChunks(-1 * FourCC.BYTES);
            ftyp.appendCompatibleBrand(new FourCC("misb"));
            stco.shiftChunks(FourCC.BYTES);
        }
        FreeBox free = (FreeBox) getTopLevelBoxByFourCC(boxes, "free");
        long freeBoxSize = free.getSize();
        boxes.remove(free);
        stco.shiftChunks(-1 * freeBoxSize);
        MetaBox metaBox = buildMetaBox();
        // TODO variable length sizes
        SampleAuxiliaryInformationSizesBox saiz =
                new SampleAuxiliaryInformationSizesBoxBuilder()
                        .withVersion(0)
                        .withFlags(1)
                        .withAuxInfoType(new FourCC("misb"))
                        .withAuxInfoTypeParameter(0)
                        .withURI("urn:misb.KLV.ul:060E2B34.02050101.0E010505.00000000")
                        .withDefaultSampleInfoSize(8)
                        .withSampleCount(5 * 60 * 30)
                        .build();
        stbl.appendNestedBox(saiz);
        minf.adjustSize(saiz.getSize());
        mdia.adjustSize(saiz.getSize());
        motionImageryTrack.adjustSize(saiz.getSize());
        // TODO: saio
        // TODO: write time stamps
        TrackBox metadataTrack = new TrackBoxBuilder().build();
        MovieBox moov =
                new MovieBoxBuilder()
                        .withNestedBox(mvhd)
                        .withNestedBox(motionImageryTrack)
                        // .withNestedBox(metadataTrack)
                        .withNestedBox(metaBox)
                        .build();
        boxes.add(moov);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Box box : boxes) {
            box.writeTo(baos);
        }
        File testOut = new File("G340_to_NGA.STND.0076_0.1_MIFF_aux_info.mp4");
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
    }

    private MetaBox buildMetaBox() throws IOException {
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
                        .withItemUriType("urn:misb.KLV.ul.060E2B34.02050101.0E010503.00000000")
                        .build();
        PitmBox pitm =
                new PrimaryItemBoxBuilder().withVersion(0).withFlags(0).withItemId(1).build();
        ItemInfoBox iinf =
                new ItemInfoBoxBuilder().withVersion(0).withFlags(0).withItemInfo(infe0).build();
        byte[] mimdMessageWithoutKeyAndLength = buildSimpleMIMD();
        ItemDataBox idat =
                new ItemDataBoxBuilder().withData(mimdMessageWithoutKeyAndLength).build();
        MetaBox metaBox =
                new MetaBoxBuilder()
                        .withVersion(0)
                        .withFlags(0)
                        .withNesteBox(hdlr)
                        .withNesteBox(pitm)
                        .withNesteBox(iinf)
                        .withNesteBox(idat)
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
            mimd.setCompositeMetadataSecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeMetadataSecurity",
                            "Security"));
            mimd.setCompositeProductSecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeProductSecurity",
                            "Security"));
            mimd.setCompositeMotionImagerySecurity(
                    new MimdIdReference(
                            SECURITY_ID_SERIAL,
                            SECURITY_ID_GROUP,
                            "CompositeMotionImagerySecurity",
                            "Security"));
            mimd.setPlatforms(buildPlatforms());
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
        platform.setStages(buildStages());
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
}
