package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static net.frogmouth.rnd.eofff.uncompressed_experiments.GIMIUtils.makeRandomContentId;
import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
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
import net.frogmouth.rnd.eofff.ogc.ModelTiePoints3DProperty;
import net.frogmouth.rnd.eofff.ogc.TiePoint3D;
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
import org.testng.annotations.Test;
import org.threeten.extra.scale.TaiInstant;
import org.threeten.extra.scale.UtcInstant;

/** Write file. */
public class WriteRotterdamSentinel1Test {

    private static final long MAIN_ITEM_ID = 10;
    private static final int LENGTH_OF_FREEBOX_HEADER = 8;
    private int imageWidth = 0;
    private int imageHeight = 0;
    private static final int MDAT_START = 512 + 2071 + 5460;
    // TODO: fix
    private LocalDateTime TIMESTAMP = LocalDateTime.of(LocalDate.of(2020, 2, 13), LocalTime.MIN);

    private static final int IMAGE_DATA_START = MDAT_START + 8; // assumes mdat header is 8 bytes.

    private FileDirectory directory;
    private ByteOrder source_endian;
    private int numTilesX, numTilesY;

    public WriteRotterdamSentinel1Test() throws IOException {
        // TODO: we should expose this in the file
        String sourcePath =
                "/home/bradh/testbed20/Rotterdam/S1A_IW_GRDH_1SDV_20240704T055846_20240704T055911_054607_06A5A1__COG.SAFE/measurement/s1a-iw-grd-vh-20240704t055846-20240704t055911-054607-06a5a1-002-cog.tiff";
        source_endian = ByteOrder.LITTLE_ENDIAN;
        mil.nga.tiff.TIFFImage tiffImage = mil.nga.tiff.TiffReader.readTiff(new File(sourcePath));
        List<mil.nga.tiff.FileDirectory> directories = tiffImage.getFileDirectories();
        directory = directories.get(0);
        // List<Double> pixelScale = directory.getModelPixelScale();

        imageHeight = (int) directory.getImageHeight();
        imageWidth = (int) directory.getImageWidth();
        List<Integer> sampleFormats = directory.getSampleFormat();
        // TODO: generalise this
        assertEquals(sampleFormats.size(), 1);
        assertEquals(sampleFormats.get(0).intValue(), 1);
        assertEquals(directory.getBitsPerSample().size(), 1);
        assertEquals(directory.getBitsPerSample().get(0), 16);
        // GeoTransform transform = new GeoTransform(modelTiePoint, pixelScale);
        System.out.println(
                String.format("Image width: %d, image height: %d", imageWidth, imageHeight));
        // TODO: just copy out of source file
        System.out.println(
                String.format(
                        "Tile width: %d, tile height: %d",
                        directory.getTileWidth(), directory.getTileHeight()));
        // System.out.println(directory.getTileByteCounts());
        numTilesX =
                (directory.getImageWidth().intValue() + directory.getTileWidth().intValue() - 1)
                        / directory.getTileWidth().intValue();
        numTilesY =
                (directory.getImageHeight().intValue() + directory.getTileHeight().intValue() - 1)
                        / directory.getTileHeight().intValue();
    }

    @Test
    public void vh_convert() throws IOException {
        MediaDataBox mdat = createMediaDataBox(ByteOrder.BIG_ENDIAN);

        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox(false);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        boxes.add(mdat);
        writeBoxes(
                boxes,
                "/home/bradh/testbed20/Rotterdam/s1a-iw-grd-vh-20240704t055846-20240704t055911-054607-06a5a1-002-cog.hif");
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(Brand.MIF2);
        fileTypeBox.addCompatibleBrand(Brand.MIF1);
        fileTypeBox.addCompatibleBrand(new Brand("geo1"));
        fileTypeBox.addCompatibleBrand(Brand.UNIF);
        return fileTypeBox;
    }

    private MetaBox createMetaBox(boolean littleEndian) throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox());
        boxes.add(makeItemDataBox());
        boxes.add(makeItemPropertiesBox(littleEndian));
        boxes.add(makeItemReferenceBox());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType(new FourCC("pict"));
        hdlr.setName("");
        return hdlr;
    }

    private PrimaryItemBox makePrimaryItemBox() {
        PrimaryItemBox pitm = new PrimaryItemBox();
        pitm.setItemID(MAIN_ITEM_ID);
        return pitm;
    }

    private ItemInfoBox makeItemInfoBox() {
        ItemInfoBox iinf = new ItemInfoBox();
        ItemInfoEntry infe0 = new ItemInfoEntry();
        infe0.setVersion(2);
        infe0.setItemID(MAIN_ITEM_ID);
        FourCC unci = new FourCC("unci");
        infe0.setItemType(unci.asUnsigned());
        infe0.setItemName("");
        iinf.addItem(infe0);
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox() throws IOException {
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        ILocItem mainItemLocation = new ILocItem();
        mainItemLocation.setConstructionMethod(0);
        mainItemLocation.setItemId(MAIN_ITEM_ID);
        ILocExtent mainItemExtent = new ILocExtent();
        mainItemExtent.setExtentIndex(0);
        mainItemExtent.setExtentOffset(IMAGE_DATA_START);
        mainItemExtent.setExtentLength(imageHeight * imageWidth * 2);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private ItemDataBox makeItemDataBox() throws IOException {
        ItemDataBoxBuilder itemDataBoxBuilder = new ItemDataBoxBuilder();
        return itemDataBoxBuilder.build();
    }

    private Box makeItemPropertiesBox(boolean littleEndian) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox());
        ipco.addProperty(makeUncompressedFrameConfigBox(littleEndian));
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeRandomContentId()); // 4
        ipco.addProperty(makeClockInfoItemProperty()); // 5
        ipco.addProperty(makeTimeStampBox()); // 6
        ipco.addProperty(makeModelTiePointsProperty()); // 7
        ipco.addProperty(makeWKT2Property()); // 8
        iprp.setItemProperties(ipco);

        ItemPropertyAssociation itemPropertyAssociation = new ItemPropertyAssociation();
        {
            AssociationEntry mainItemAssociations = new AssociationEntry();
            mainItemAssociations.setItemId(MAIN_ITEM_ID);

            PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
            associationToComponentDefinitionBox.setPropertyIndex(1);
            associationToComponentDefinitionBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToComponentDefinitionBox);

            PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
            associationToUncompressedFrameConfigBox.setPropertyIndex(2);
            associationToUncompressedFrameConfigBox.setEssential(true);
            mainItemAssociations.addAssociation(associationToUncompressedFrameConfigBox);

            PropertyAssociation associationToImageSpatialExtentsProperty =
                    new PropertyAssociation();
            associationToImageSpatialExtentsProperty.setPropertyIndex(3);
            associationToImageSpatialExtentsProperty.setEssential(true);
            mainItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);

            PropertyAssociation associationToContentIDProperty = new PropertyAssociation();
            associationToContentIDProperty.setPropertyIndex(4);
            associationToContentIDProperty.setEssential(false);
            mainItemAssociations.addAssociation(associationToContentIDProperty);

            {
                PropertyAssociation associationToClockInfo = new PropertyAssociation();
                associationToClockInfo.setPropertyIndex(5);
                associationToClockInfo.setEssential(false);
                mainItemAssociations.addAssociation(associationToClockInfo);
            }
            {
                PropertyAssociation assocationToTimestampTAI = new PropertyAssociation();
                assocationToTimestampTAI.setPropertyIndex(6);
                assocationToTimestampTAI.setEssential(false);
                mainItemAssociations.addAssociation(assocationToTimestampTAI);
            }
            {
                PropertyAssociation associationToTiePoints = new PropertyAssociation();
                associationToTiePoints.setPropertyIndex(7);
                associationToTiePoints.setEssential(false);
                mainItemAssociations.addAssociation(associationToTiePoints);
            }
            {
                PropertyAssociation associationToWKT2 = new PropertyAssociation();
                associationToWKT2.setPropertyIndex(8);
                associationToWKT2.setEssential(false);
                mainItemAssociations.addAssociation(associationToWKT2);
            }

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        iprp.addItemPropertyAssociation(itemPropertyAssociation);

        return iprp;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition monoComponent = new ComponentDefinition(0, null);
        cmpd.addComponentDefinition(monoComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox(boolean littleEndian) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC(0));
        uncc.addComponent(new Component(0, 15, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
        uncc.setBlockSize(0);
        uncc.setComponentLittleEndian(littleEndian);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(false);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(this.numTilesX - 1);
        uncc.setNumTileRowsMinusOne(this.numTilesY - 1);
        return uncc;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty() {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(imageHeight);
        ispe.setImageWidth(imageWidth);
        return ispe;
    }

    private Box makeItemReferenceBox() {
        ItemReferenceBox iref = new ItemReferenceBox();
        return iref;
    }

    private MediaDataBox createMediaDataBox(ByteOrder targetEndian) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                byte[] tileBytes = directory.getTileOrStrip(x, y, 0);
                // System.out.println("x: " + x + " y: " + y + ", bytes: " + tileBytes.length);
                ByteBuffer buf = ByteBuffer.wrap(tileBytes);
                buf.order(source_endian);
                ShortBuffer values = buf.asShortBuffer();
                for (int i = 0; i < values.limit(); i++) {
                    int value = values.get(i);
                    ByteBuffer bb = ByteBuffer.allocate(Short.BYTES);
                    bb.order(targetEndian);
                    bb.putShort((short) value);
                    byte[] bigEndian = bb.array();
                    baos.writeBytes(bigEndian);
                }
            }
        }
        mdat.setData(baos.toByteArray());
        return mdat;
    }

    private void writeBoxes(List<Box> boxes, String outputPathName) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        for (Box box : boxes) {
            // consider validating here
            box.writeTo(streamWriter);
        }
        File testOut = new File(outputPathName);
        Files.write(testOut.toPath(), baos.toByteArray(), StandardOpenOption.CREATE);
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
        taic.setTimeUncertainty(24 * 60 * 60 * GIMIUtils.NANOS_PER_SECOND);
        taic.setClock_resolution(TAIClockInfoItemProperty.CLOCK_RESOLUTION_MICROSECOND);
        taic.setClock_drift_rate(TAIClockInfoItemProperty.CLOCK_DRIFT_RATE_UNKNOWN);
        taic.setClock_type(TAIClockInfoItemProperty.CLOCK_TYPE_UNKNOWN);
        return taic;
    }

    private ModelTiePoints3DProperty makeModelTiePointsProperty() {
        ModelTiePoints3DProperty prop = new ModelTiePoints3DProperty();
        List<Double> modelTiePoint = directory.getModelTiepoint();
        int numPoints = modelTiePoint.size() / 6;
        for (int p = 0; p < numPoints; p += 1) {
            double i = modelTiePoint.get(6 * p + 0);
            double j = modelTiePoint.get(6 * p + 1);
            double k = modelTiePoint.get(6 * p + 2);
            double x = modelTiePoint.get(6 * p + 3);
            double y = modelTiePoint.get(6 * p + 4);
            double z = modelTiePoint.get(6 * p + 5);
            assertEquals(i % 1.0, 0.0);
            assertEquals(j % 1.0, 0.0);
            assertEquals(k, 0.0);
            TiePoint3D tiePoint = new TiePoint3D((int) i, (int) j, x, y, z);
            prop.addTiePoint3D(tiePoint);
        }
        return prop;
    }

    private CoordinateReferenceSystemProperty makeWKT2Property() {
        CoordinateReferenceSystemProperty prop = new CoordinateReferenceSystemProperty();
        // TODO: make sure it is not hard coded
        prop.setCrsEncoding(new FourCC("wkt2"));
        prop.setCrs(
                "GEOGCRS[\"WGS 84\",ENSEMBLE[\"World Geodetic System 1984 ensemble\",MEMBER[\"World Geodetic System 1984 (Transit)\"],MEMBER[\"World Geodetic System 1984 (G730)\"],MEMBER[\"World Geodetic System 1984 (G873)\"],MEMBER[\"World Geodetic System 1984 (G1150)\"],MEMBER[\"World Geodetic System 1984 (G1674)\"],MEMBER[\"World Geodetic System 1984 (G1762)\"],MEMBER[\"World Geodetic System 1984 (G2139)\"],ELLIPSOID[\"WGS 84\",6378137,298.257223563,LENGTHUNIT[\"metre\",1]],ENSEMBLEACCURACY[2.0]],PRIMEM[\"Greenwich\",0,ANGLEUNIT[\"degree\",0.0174532925199433]],CS[ellipsoidal,2],AXIS[\"geodetic latitude (Lat)\",north,ORDER[1],ANGLEUNIT[\"degree\",0.0174532925199433]],AXIS[\"geodetic longitude (Lon)\",east,ORDER[2],ANGLEUNIT[\"degree\",0.0174532925199433]],USAGE[SCOPE[\"Horizontal component of 3D system.\"],AREA[\"World.\"],BBOX[-90,-180,90,180]],ID[\"EPSG\",4326]]");
        return prop;
    }
}
