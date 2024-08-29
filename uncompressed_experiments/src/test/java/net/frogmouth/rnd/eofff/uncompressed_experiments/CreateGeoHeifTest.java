package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.itemreferences.ContentDescribes;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.uuid.UUIDProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
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
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.ogc.CoordinateReferenceSystemProperty;
import net.frogmouth.rnd.eofff.ogc.ModelTiePointsProperty;
import net.frogmouth.rnd.eofff.ogc.ModelTransformationProperty;
import net.frogmouth.rnd.eofff.ogc.TiePoint;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampBox;
import net.frogmouth.rnd.eofff.uncompressed.itai.TAITimeStampPacket;
import net.frogmouth.rnd.eofff.uncompressed.taic.TAIClockInfoItemProperty;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

public class CreateGeoHeifTest extends GIMIValidator {

    private static final Logger LOG = LoggerFactory.getLogger(CreateGeoHeifTest.class);

    private static final long PRIMARY_ITEM_ID = 1100;
    private static final long SECURITY_ITEM_ID = 1200;

    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private final int tileWidth;
    private final int tileHeight;
    private final int imageHeight;
    private final int imageWidth;
    private static final int MDAT_START_GRID = 2030 + 896;
    private static final int MDAT_START_DATA = MDAT_START_GRID + 2 * Integer.BYTES;
    private final int tileSizeBytes;

    private static final UUID PRIMARY_ITEM_CONTENT_ID_UUID = UUID.randomUUID();
    private static final UUID SECURITY_CONTENT_ID_UUID = UUID.randomUUID();
    private final String ISM_SECURITY_MIME_TYPE = "application/dni-arh+xml";
    private final String ISM_SECURITY_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                    + "<GIMISecurity xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:us:mil:nga:stnd:0076:ism\" xmlns:ism=\"urn:us:gov:ic:ism\" xmlns:gimi=\"urn:us:mil:nga:stnd:0076:ism\" xmlns:arh=\"urn:us:gov:ic:arh\" xsi:schemaLocation=\"urn:us:mil:nga:stnd:0076:ism GIMI.xsd\" ism:DESVersion=\"202405\" ism:ISMCATCESVersion=\"202405\" gimi:GIMISecVer=\"1\">"
                    + "<File>"
                    + "<arh:Security ism:compliesWith=\"USGov USIC\" ism:resourceElement=\"true\" ism:createDate=\""
                    + ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE)
                    + "\" ism:classification=\"U\" ism:ownerProducer=\"USA\"/>"
                    + "<Items>"
                    + "<Item gimi:itemID=\""
                    + PRIMARY_ITEM_CONTENT_ID_UUID
                    + "\">"
                    + "<gimi:Security ism:classification=\"U\" ism:ownerProducer=\"USA\"/>"
                    + "</Item>"
                    + "</Items>"
                    + "</File>"
                    + "</GIMISecurity>";

    private final FileDirectory directory;
    private static final UUID CONTENT_ID_UUID =
            UUID.fromString("4a66efa7-e541-526c-9427-9e77617feb7d");

    private final LocalDateTime TIMESTAMP =
            LocalDateTime.of(LocalDate.of(2017, 5, 7), LocalTime.MIN);

    private final String path;
    private final int num_tile_columns;
    private final int num_tile_rows;
    List<Double> pixelScale;
    List<Double> modelTiePoint;

    public CreateGeoHeifTest() throws IOException {
        path = "/home/bradh/testbed20/geoheif/test.tif";
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(path));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        directory = directories.get(0);
        tileWidth = (int) directory.getTileWidth();
        tileHeight = (int) directory.getTileHeight();
        imageHeight = (int) directory.getImageHeight();
        imageWidth = (int) directory.getImageWidth();
        tileSizeBytes = tileWidth * tileHeight * NUM_BYTES_PER_PIXEL_RGB;
        pixelScale = directory.getModelPixelScale();
        modelTiePoint = directory.getModelTiepoint();
        num_tile_rows = (imageHeight + tileHeight - 1) / tileHeight;
        num_tile_columns = (imageWidth + tileWidth - 1) / tileWidth;
    }

    @Test
    public void writeFile_rgb_tile() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_tile();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START_GRID - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_grid();
        boxes.add(mdat);
        writeBoxes(boxes, "geo.heif");
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
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("heic"));
        return fileTypeBox;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
        hdlr.setName("");
        return hdlr;
    }

    private PrimaryItemBox makePrimaryItemBox(long itemID) {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(itemID);
        return pitm;
    }

    private MetaBox createMetaBox_rgb_tile() throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox(PRIMARY_ITEM_ID));
        boxes.add(makeItemInfoBoxTile());
        boxes.add(makeItemLocationBox_tile());
        boxes.add(makeItemDataBox_tile());
        boxes.add(makeItemPropertiesBox_tile());
        boxes.add(makeItemReferenceBoxTile());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemInfoBox makeItemInfoBoxTile() {
        ItemInfoBox iinf = new ItemInfoBox();
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(PRIMARY_ITEM_ID);
            FourCC unci = new FourCC("unci");
            infe1.setItemType(unci.asUnsigned());
            infe1.setItemName("Alternate view");
            iinf.addItem(infe1);
        }
        {
            ItemInfoEntry infe2 = new ItemInfoEntry();
            infe2.setVersion(2);
            infe2.setItemID(SECURITY_ITEM_ID);
            FourCC mime_fourcc = new FourCC("mime");
            infe2.setItemType(mime_fourcc.asUnsigned());
            infe2.setContentType(ISM_SECURITY_MIME_TYPE);
            infe2.setItemName("Security Marking (ISM.XML)");
            iinf.addItem(infe2);
        }
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox_tile() throws IOException {
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
            primaryItemLocation.setBaseOffset(MDAT_START_DATA);
            ILocExtent alternateItemExtent = new ILocExtent();
            alternateItemExtent.setExtentIndex(0);
            alternateItemExtent.setExtentOffset(0);
            alternateItemExtent.setExtentLength(tileSizeBytes * num_tile_columns * num_tile_rows);
            primaryItemLocation.addExtent(alternateItemExtent);
            iloc.addItem(primaryItemLocation);
        }
        {
            ILocItem securityItemLocation = new ILocItem();
            securityItemLocation.setConstructionMethod(1);
            securityItemLocation.setItemId(SECURITY_ITEM_ID);
            securityItemLocation.setBaseOffset(0);
            ILocExtent securityExtent = new ILocExtent();
            securityExtent.setExtentIndex(0);
            securityExtent.setExtentOffset(0);
            securityExtent.setExtentLength(getSecurityXMLBytes(false).length);
            securityItemLocation.addExtent(securityExtent);
            iloc.addItem(securityItemLocation);
        }
        return iloc;
    }

    private byte[] getSecurityXMLBytes(boolean dump) {
        byte[] securityXMLBytes = ISM_SECURITY_XML.getBytes(StandardCharsets.UTF_8);
        return securityXMLBytes;
    }

    private ItemDataBox makeItemDataBox_tile() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        itemDataBoxBuilder.addData(this.getSecurityXMLBytes(true));
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox_tile() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb3()); // 1
        ipco.addProperty(makeImageSpatialExtentsProperty_grid()); // 2
        ipco.addProperty(makeUncompressedFrameConfigBox_tiled_item()); // 3
        ipco.addProperty(makeContentIdPropertyGridItem()); // 4
        ipco.addProperty(makeContentIdPropertySecurity()); // 5
        ipco.addProperty(makeUserDescription_copyright()); // 6
        ipco.addProperty(makeClockInfoItemProperty()); // 7
        ipco.addProperty(makeTimeStampBox()); // 8
        ipco.addProperty(makeModelTransformationProperty()); // 9
        ipco.addProperty(makeModelTiePointsProperty()); // 10
        ipco.addProperty(makeWKT2Property()); // 11
        iprp.setItemProperties(ipco);

        {
            ItemPropertyAssociation ipma = new ItemPropertyAssociation();
            AssociationEntry entryImage = new AssociationEntry();
            entryImage.setItemId(PRIMARY_ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(2);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entryImage.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
                associationToComponentDefinitionBox.setPropertyIndex(1);
                associationToComponentDefinitionBox.setEssential(true);
                entryImage.addAssociation(associationToComponentDefinitionBox);

                PropertyAssociation associationToUncompressedFrameConfigBox =
                        new PropertyAssociation();
                associationToUncompressedFrameConfigBox.setPropertyIndex(3);
                associationToUncompressedFrameConfigBox.setEssential(true);
                entryImage.addAssociation(associationToUncompressedFrameConfigBox);
            }
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(4);
                associationToContentId.setEssential(false);
                entryImage.addAssociation(associationToContentId);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(6);
                associationToCopyrightUserDescription.setEssential(false);
                entryImage.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(7);
                associationToClockInfo.setEssential(false);
                entryImage.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation associationToTimestampTAI = new PropertyAssociation();
                associationToTimestampTAI.setPropertyIndex(8);
                associationToTimestampTAI.setEssential(false);
                entryImage.addAssociation(associationToTimestampTAI);
            }
            {
                PropertyAssociation associationToModelTransformation = new PropertyAssociation();
                associationToModelTransformation.setPropertyIndex(9);
                associationToModelTransformation.setEssential(false);
                entryImage.addAssociation(associationToModelTransformation);
            }
            {
                PropertyAssociation associationToTiePoints = new PropertyAssociation();
                associationToTiePoints.setPropertyIndex(10);
                associationToTiePoints.setEssential(false);
                entryImage.addAssociation(associationToTiePoints);
            }
            {
                PropertyAssociation associationToWKT2 = new PropertyAssociation();
                associationToWKT2.setPropertyIndex(11);
                associationToWKT2.setEssential(false);
                entryImage.addAssociation(associationToWKT2);
            }
            ipma.addEntry(entryImage);

            AssociationEntry entrySecurity = new AssociationEntry();
            entrySecurity.setItemId(SECURITY_ITEM_ID);
            {
                PropertyAssociation associationToContentId = new PropertyAssociation();
                associationToContentId.setPropertyIndex(5);
                associationToContentId.setEssential(false);
                entrySecurity.addAssociation(associationToContentId);
            }
            ipma.addEntry(entrySecurity);
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

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty_grid() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(imageHeight);
        ispe.setImageWidth(imageWidth);
        return ispe;
    }

    private Box makeItemReferenceBoxTile() {
        ItemReferenceBox iref = new ItemReferenceBox();
        {
            ContentDescribes cdsc = new ContentDescribes();
            cdsc.setFromItemId(SECURITY_ITEM_ID);
            cdsc.addReference(PRIMARY_ITEM_ID);
            iref.addItem(cdsc);
        }
        return iref;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgb3() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_tiled_item() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(1, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(2, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(num_tile_columns - 1);
        uncc.setNumTileRowsMinusOne(num_tile_rows - 1);
        return uncc;
    }

    private MediaDataBox createMediaDataBox_rgb_grid() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        System.out.println(
                String.format(
                        "Tile width: %d, tile height: %d",
                        directory.getTileWidth(), directory.getTileHeight()));
        System.out.println(directory.getTileByteCounts());
        int numTilesX =
                (directory.getImageWidth().intValue() + directory.getTileWidth().intValue() - 1)
                        / directory.getTileWidth().intValue();
        int numTilesY =
                (directory.getImageHeight().intValue() + directory.getTileHeight().intValue() - 1)
                        / directory.getTileHeight().intValue();

        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                byte[] tileBytes = directory.getTileOrStrip(x, y, 0);
                mdat.appendData(tileBytes);
            }
        }

        return mdat;
    }

    private UUIDProperty makeContentIdPropertyGridItem() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(PRIMARY_ITEM_CONTENT_ID_UUID.getMostSignificantBits());
        bb.putLong(PRIMARY_ITEM_CONTENT_ID_UUID.getLeastSignificantBits());
        contentIdProperty.setPayload(bb.array());
        return contentIdProperty;
    }

    private UUIDProperty makeContentIdPropertySecurity() {
        UUIDProperty contentIdProperty = new UUIDProperty();
        contentIdProperty.setExtendedType(CONTENT_ID_UUID);
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(SECURITY_CONTENT_ID_UUID.getMostSignificantBits());
        bb.putLong(SECURITY_CONTENT_ID_UUID.getLeastSignificantBits());
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
