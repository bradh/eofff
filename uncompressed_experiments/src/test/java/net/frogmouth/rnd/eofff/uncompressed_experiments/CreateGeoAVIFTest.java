package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.av1isobmff.av1C.AV1CodecConfigurationBox;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.pixi.PixelInformationProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.uuid.UUIDProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.ogc.CoordinateReferenceSystemProperty;
import net.frogmouth.rnd.eofff.ogc.ModelTiePointsProperty;
import net.frogmouth.rnd.eofff.ogc.ModelTransformationProperty;
import net.frogmouth.rnd.eofff.ogc.TiePoint;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

public class CreateGeoAVIFTest extends GIMIValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CreateGeoAVIFTest.class);

    private static final long PRIMARY_ITEM_ID = 1100;

    private static final UUID PRIMARY_ITEM_CONTENT_ID_UUID = UUID.randomUUID();

    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("4a66efa7-e541-526c-9427-9e77617feb7d");

    private final LocalDateTime TIMESTAMP =
            LocalDateTime.of(LocalDate.of(2017, 5, 7), LocalTime.MIN);

    private final String path;
    private final FileDirectory directory;
    private final int imageHeight;
    private final int imageWidth;
    private final List<Double> pixelScale;
    private final List<Double> modelTiePoint;
    private final String heifFile;
    private MediaDataBox mdat;
    private List<Box> sourceBoxes;

    public CreateGeoAVIFTest() throws IOException, InterruptedException {
        path = "/home/bradh/testbed20/geoheif/test.tif";
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(path));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        directory = directories.get(0);
        imageHeight = (int) directory.getImageHeight();
        imageWidth = (int) directory.getImageWidth();
        pixelScale = directory.getModelPixelScale();
        modelTiePoint = directory.getModelTiepoint();
        heifFile = Files.createTempFile(null, ".avif").toString();
        ProcessBuilder builder =
                new ProcessBuilder(
                        "/usr/local/bin/heif-enc",
                        path,
                        "--avif",
                        "--no-alpha",
                        "--output",
                        heifFile);
        Process process = builder.start();
        process.waitFor();
        // TODO: better error checks and handling
        InputStream errorStream = process.getErrorStream();
        String errors = new String(errorStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println(errors);
        {
            FileParser fileParser = new FileParser();
            sourceBoxes = fileParser.parse(Path.of(heifFile));
            for (Box box : sourceBoxes) {
                if (box instanceof MediaDataBox mdatSource) {
                    mdat = mdatSource;
                }
            }
        }
    }

    @Test
    public void writeAVIF() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);

        MetaBox meta = createMetaBox();
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        for (Box box : meta.getNestedBoxes()) {
            if (box instanceof ItemLocationBox iloc) {
                for (var item : iloc.getItems()) {
                    item.setBaseOffset(lengthOfPreviousBoxes + 8);
                }
            }
        }
        boxes.add(meta);
        boxes.add(mdat);
        writeBoxes(boxes, "geo.avif");
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
        fileTypeBox.setMajorBrand(new Brand("avif"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("avif"));
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("miaf"));
        return fileTypeBox;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType(new FourCC("pict"));
        hdlr.setName("");
        return hdlr;
    }

    private PrimaryItemBox makePrimaryItemBox(long itemID) {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(itemID);
        return pitm;
    }

    private MetaBox createMetaBox() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox(PRIMARY_ITEM_ID));
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox());
        boxes.add(makeItemPropertiesBox());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemInfoBox makeItemInfoBox() {
        ItemInfoBox iinf = new ItemInfoBox();
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(PRIMARY_ITEM_ID);
            FourCC unci = new FourCC("av01");
            infe1.setItemType(unci.asUnsigned());
            infe1.setItemName("AV1 image");
            iinf.addItem(infe1);
        }
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox() throws IOException {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        {
            ILocItem primaryItemLocation = new ILocItem();
            primaryItemLocation.setConstructionMethod(0);
            primaryItemLocation.setItemId(PRIMARY_ITEM_ID);
            primaryItemLocation.setBaseOffset(0);
            ILocExtent extent = new ILocExtent();
            extent.setExtentIndex(0);
            extent.setExtentOffset(0);
            extent.setExtentLength(mdat.getData().length);
            primaryItemLocation.addExtent(extent);
            iloc.addItem(primaryItemLocation);
        }
        return iloc;
    }

    private Box makeItemPropertiesBox() throws IOException {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeImageSpatialExtentsProperty()); // 1
        ipco.addProperty(getAv1CodecConfigBox()); // 2
        ipco.addProperty(makeContentIdMainItem()); // 3
        ipco.addProperty(makeUserDescription_copyright()); // 4
        ipco.addProperty(makeClockInfoItemProperty()); // 5
        ipco.addProperty(makeTimeStampBox()); // 6
        ipco.addProperty(makeModelTransformationProperty()); // 7
        ipco.addProperty(makeModelTiePointsProperty()); // 8
        ipco.addProperty(makeWKT2Property()); // 9
        ipco.addProperty(getPixiBox()); // 10
        iprp.setItemProperties(ipco);

        {
            ItemPropertyAssociation ipma = new ItemPropertyAssociation();
            AssociationEntry entryImage = new AssociationEntry();
            entryImage.setItemId(PRIMARY_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(1);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entryImage.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToCompressionConfigBox = new PropertyAssociation();
                associationToCompressionConfigBox.setPropertyIndex(2);
                associationToCompressionConfigBox.setEssential(true);
                entryImage.addAssociation(associationToCompressionConfigBox);
            }
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(3);
                associationToContentId.setEssential(false);
                entryImage.addAssociation(associationToContentId);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(4);
                associationToCopyrightUserDescription.setEssential(false);
                entryImage.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(5);
                associationToClockInfo.setEssential(false);
                entryImage.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation associationToTimestampTAI = new PropertyAssociation();
                associationToTimestampTAI.setPropertyIndex(6);
                associationToTimestampTAI.setEssential(false);
                entryImage.addAssociation(associationToTimestampTAI);
            }
            {
                PropertyAssociation associationToModelTransformation = new PropertyAssociation();
                associationToModelTransformation.setPropertyIndex(7);
                associationToModelTransformation.setEssential(false);
                entryImage.addAssociation(associationToModelTransformation);
            }
            {
                PropertyAssociation associationToTiePoints = new PropertyAssociation();
                associationToTiePoints.setPropertyIndex(8);
                associationToTiePoints.setEssential(false);
                entryImage.addAssociation(associationToTiePoints);
            }
            {
                PropertyAssociation associationToWKT2 = new PropertyAssociation();
                associationToWKT2.setPropertyIndex(9);
                associationToWKT2.setEssential(false);
                entryImage.addAssociation(associationToWKT2);
            }
            {
                PropertyAssociation associationToPixi = new PropertyAssociation();
                associationToPixi.setPropertyIndex(10);
                associationToPixi.setEssential(false);
                entryImage.addAssociation(associationToPixi);
            }
            ipma.addEntry(entryImage);
            iprp.addItemPropertyAssociation(ipma);
        }
        return iprp;
    }

    private AbstractItemProperty makeUserDescription_copyright() {
        UserDescriptionProperty udes = new UserDescriptionProperty();
        udes.setLang("en-AU");
        udes.setDescriptiveName("Copyright Statement");
        udes.setDescription(
                "CCBY \"Jacobs Group (Australia) Pty Ltd and Australian Capital Territory\"");
        udes.setTags("copyright");
        return udes;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(imageHeight);
        ispe.setImageWidth(imageWidth);
        return ispe;
    }

    private PixelInformationProperty getPixiBox() throws IOException {
        for (Box box : sourceBoxes) {
            if (box instanceof MetaBox meta) {
                for (var metaChild : meta.getNestedBoxes()) {
                    if (metaChild instanceof ItemPropertiesBox iprp) {
                        ItemPropertyContainerBox ipco = iprp.getItemProperties();
                        for (var prop : ipco.getProperties()) {
                            if (prop instanceof PixelInformationProperty pixi) {
                                return pixi;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private AV1CodecConfigurationBox getAv1CodecConfigBox() throws IOException {
        for (Box box : sourceBoxes) {
            if (box instanceof MetaBox meta) {
                for (var metaChild : meta.getNestedBoxes()) {
                    if (metaChild instanceof ItemPropertiesBox iprp) {
                        ItemPropertyContainerBox ipco = iprp.getItemProperties();
                        for (var prop : ipco.getProperties()) {
                            if (prop instanceof AV1CodecConfigurationBox av1C) {
                                return av1C;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private UUIDProperty makeContentIdMainItem() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(PRIMARY_ITEM_CONTENT_ID_UUID.getMostSignificantBits());
        bb.putLong(PRIMARY_ITEM_CONTENT_ID_UUID.getLeastSignificantBits());
        contentIdProperty.setPayload(bb.array());
        return contentIdProperty;
    }

    private TAITimeStampBox makeTimeStampBox() {
        TAITimeStampBox itai = new TAITimeStampBox();
        TAITimeStampPacket time_stamp_packet = new TAITimeStampPacket();
        Instant instant = TIMESTAMP.toInstant(ZoneOffset.UTC);
        UtcInstant utcInstant = UtcInstant.of(instant);
        TaiInstant taiInstant = utcInstant.toTaiInstant();
        long timestamp =
                GIMIUtils.NANOS_PER_SECOND * taiInstant.getTaiSeconds() + taiInstant.getNano();
        time_stamp_packet.setTAITimeStamp(timestamp);
        time_stamp_packet.setStatusBits((byte) 0x02);
        itai.setTimeStampPacket(time_stamp_packet);
        return itai;
    }

    private TAIClockInfoItemProperty makeClockInfoItemProperty() {

        TAIClockInfoItemProperty taic = new TAIClockInfoItemProperty();
        taic.setTimeUncertainty(24L * 60L * 60L * GIMIUtils.NANOS_PER_SECOND);
        taic.setClock_resolution(TAIClockInfoItemProperty.CLOCK_RESOLUTION_MICROSECOND);
        taic.setClock_drift_rate(TAIClockInfoItemProperty.CLOCK_DRIFT_RATE_UNKNOWN);
        taic.setClock_type(TAIClockInfoItemProperty.CLOCK_TYPE_UNKNOWN);
        return taic;
    }

    private ModelTransformationProperty makeModelTransformationProperty() {
        ModelTransformationProperty modelTransformation = new ModelTransformationProperty();
        assertEquals(this.pixelScale.size(), 3);
        assertEquals(this.pixelScale.get(2), 0.0);
        double sx = this.pixelScale.get(0);
        double sy = this.pixelScale.get(1);
        assertEquals(this.modelTiePoint.size(), 6);
        double tx = this.modelTiePoint.get(3) + this.modelTiePoint.get(0) / sx;
        double ty = this.modelTiePoint.get(4) + this.modelTiePoint.get(1) / sy;
        modelTransformation.setM00(sx);
        modelTransformation.setM01(0.0);
        modelTransformation.setM03(tx);
        modelTransformation.setM10(0.0);
        modelTransformation.setM11(-1.0 * sy);
        modelTransformation.setM13(ty);
        return modelTransformation;
    }

    private ModelTiePointsProperty makeModelTiePointsProperty() {
        ModelTiePointsProperty prop = new ModelTiePointsProperty();
        int numPoints = this.modelTiePoint.size() / 6;
        for (int p = 0; p < numPoints; p += 6) {
            double i = this.modelTiePoint.get(p + 0);
            double j = this.modelTiePoint.get(p + 1);
            double k = this.modelTiePoint.get(p + 2);
            double x = this.modelTiePoint.get(p + 3);
            double y = this.modelTiePoint.get(p + 4);
            double z = this.modelTiePoint.get(p + 5);
            assertEquals(i % 1.0, 0.0);
            assertEquals(j % 1.0, 0.0);
            assertEquals(k, 0.0);
            assertEquals(z, 0.0);
            TiePoint tiePoint = new TiePoint((int) i, (int) j, x, y);
            prop.addTiePoint(tiePoint);
        }
        return prop;
    }

    private CoordinateReferenceSystemProperty makeWKT2Property() {
        CoordinateReferenceSystemProperty prop = new CoordinateReferenceSystemProperty();
        // TODO: make sure it is not hard coded
        prop.setCrsEncoding(new FourCC("wkt2"));
        prop.setCrs(
                "PROJCRS[\"GDA94 / MGA zone 55\",BASEGEOGCRS[\"GDA94\",DATUM[\"Geocentric Datum of Australia 1994\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101004,LENGTHUNIT[\"metre\",1]]],PRIMEM[\"Greenwich\",0,ANGLEUNIT[\"degree\",0.0174532925199433]],ID[\"EPSG\",4283]],CONVERSION[\"Transverse Mercator\",METHOD[\"Transverse Mercator\",ID[\"EPSG\",9807]],PARAMETER[\"Latitude of natural origin\",0,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8801]],PARAMETER[\"Longitude of natural origin\",147,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8802]],PARAMETER[\"Scale factor at natural origin\",0.9996,SCALEUNIT[\"unity\",1],ID[\"EPSG\",8805]],PARAMETER[\"False easting\",500000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8806]],PARAMETER[\"False northing\",10000000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8807]]],CS[Cartesian,2],AXIS[\"easting\",east,ORDER[1],LENGTHUNIT[\"metre\",1]],AXIS[\"northing\",north,ORDER[2],LENGTHUNIT[\"metre\",1]],ID[\"EPSG\",28355]]");
        return prop;
    }
}
