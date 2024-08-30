package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import mil.nga.tiff.FileDirectory;
import net.frogmouth.rnd.eofff.imagefileformat.properties.hevc.HEVCConfigurationItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
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
import net.frogmouth.rnd.eofff.ogc.ModelTransformationProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class CreateTildHevcTest extends GIMIValidator {
    private static final Logger LOG = LoggerFactory.getLogger(CreateTildHevcTest.class);

    private static final long ITEM_ID = 5;

    private static final int NUM_BYTES_PER_PIXEL_RGB = 3;

    private final int tileWidth;
    private final int tileHeight;
    private final int imageHeight;
    private final int imageWidth;
    private static final int MDAT_HEADER_BYTES = 8;
    private final int tileSizeBytes;

    private final FileDirectory directory;

    private final String path;
    private int num_tile_columns;
    private int num_tile_rows;
    List<Double> pixelScale;
    List<Double> modelTiePoint;

    public CreateTildHevcTest() throws IOException {
        path = "/home/bradh/testbed20/geoheif/tiled.tif";
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
    public void writeFile_hevc_tild() throws IOException {
        MediaDataBox mdat = createMediaDataBox_tild();
        List<Box> boxes = new ArrayList<>();
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("heic"));
        boxes.add(fileTypeBox);
        MetaBox meta = new MetaBox();

        meta.addNestedBox(makeHandlerBox());
        meta.addNestedBox(makePrimaryItemBox(ITEM_ID));
        meta.addNestedBox(makeItemInfoBoxTile());
        ItemLocationBox iloc = new ItemLocationBox();
        iloc.setOffsetSize(4);
        iloc.setLengthSize(4);
        iloc.setBaseOffsetSize(4);
        iloc.setIndexSize(4);
        iloc.setVersion(1);
        ILocItem alternateItemLocation = new ILocItem();
        alternateItemLocation.setConstructionMethod(0);
        alternateItemLocation.setItemId(ITEM_ID);
        alternateItemLocation.setBaseOffset(0);
        ILocExtent alternateItemExtent = new ILocExtent();
        alternateItemExtent.setExtentIndex(0);
        alternateItemExtent.setExtentOffset(0);
        alternateItemExtent.setExtentLength(meta.getBodySize());
        alternateItemLocation.addExtent(alternateItemExtent);
        iloc.addItem(alternateItemLocation);
        meta.addNestedBox(iloc);
        meta.addNestedBox(makeItemPropertiesBox());
        boxes.add(meta);
        long lengthOfPreviousBoxes = fileTypeBox.getSize() + meta.getSize();
        for (var item : iloc.getItems()) {
            if (item.getConstructionMethod() == 0) {
                item.setBaseOffset(lengthOfPreviousBoxes + MDAT_HEADER_BYTES);
            }
        }
        boxes.add(mdat);
        writeBoxes(boxes, "tild_hevc_geo.heif");
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

    private ItemInfoBox makeItemInfoBoxTile() {
        ItemInfoBox iinf = new ItemInfoBox();
        {
            ItemInfoEntry infe1 = new ItemInfoEntry();
            infe1.setVersion(2);
            infe1.setItemID(ITEM_ID);
            FourCC tild = new FourCC("tild");
            infe1.setItemType(tild.asUnsigned());
            infe1.setItemName("tild HEVC experimental");
            iinf.addItem(infe1);
        }
        return iinf;
    }

    private Box makeItemPropertiesBox() throws IOException {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeImageSpatialExtentsProperty()); // 1
        ipco.addProperty(makeHevcCodecBox()); // 2
        ipco.addProperty(makeUserDescription_copyright()); // 3
        ipco.addProperty(makeModelTransformationProperty()); // 4
        ipco.addProperty(makeWKT2Property()); // 5
        iprp.setItemProperties(ipco);

        {
            ItemPropertyAssociation ipma = new ItemPropertyAssociation();
            AssociationEntry entryImage = new AssociationEntry();
            entryImage.setItemId(ITEM_ID);
            {
                PropertyAssociation associationToImageSpatialExtentsProperty =
                        new PropertyAssociation();
                associationToImageSpatialExtentsProperty.setPropertyIndex(1);
                associationToImageSpatialExtentsProperty.setEssential(false);
                entryImage.addAssociation(associationToImageSpatialExtentsProperty);
            }
            {
                PropertyAssociation associationToHevcCodec = new PropertyAssociation();
                associationToHevcCodec.setPropertyIndex(2);
                associationToHevcCodec.setEssential(true);
                entryImage.addAssociation(associationToHevcCodec);
            }
            {
                PropertyAssociation associationToCopyrightUserDescription =
                        new PropertyAssociation();
                associationToCopyrightUserDescription.setPropertyIndex(3);
                associationToCopyrightUserDescription.setEssential(false);
                entryImage.addAssociation(associationToCopyrightUserDescription);
            }
            {
                PropertyAssociation associationToModelTransformation = new PropertyAssociation();
                associationToModelTransformation.setPropertyIndex(4);
                associationToModelTransformation.setEssential(false);
                entryImage.addAssociation(associationToModelTransformation);
            }
            {
                PropertyAssociation associationToWKT2 = new PropertyAssociation();
                associationToWKT2.setPropertyIndex(5);
                associationToWKT2.setEssential(false);
                entryImage.addAssociation(associationToWKT2);
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

    private MediaDataBox createMediaDataBox_tild() throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        int numTilesX =
                (directory.getImageWidth().intValue() + directory.getTileWidth().intValue() - 1)
                        / directory.getTileWidth().intValue();
        int numTilesY =
                (directory.getImageHeight().intValue() + directory.getTileHeight().intValue() - 1)
                        / directory.getTileHeight().intValue();

        TiledItem tild = new TiledItem();
        tild.setFlags(0x04);
        tild.setOutput_width(directory.getImageWidth().intValue());
        tild.setOutput_height(directory.getImageHeight().intValue());
        tild.setTile_width(directory.getTileWidth().intValue());
        tild.setTile_height(directory.getTileWidth().intValue());
        tild.setTile_compression_type(new FourCC("hvc1"));
        for (int y = 0; y < numTilesY; y++) {
            for (int x = 0; x < numTilesX; x++) {
                byte[] tileBytes = get_bytes_for_tile(x, y);
                tild.addExtents(tileBytes);
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        tild.writeTo(streamWriter);
        mdat.appendData(baos.toByteArray());
        return mdat;
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

    private CoordinateReferenceSystemProperty makeWKT2Property() {
        CoordinateReferenceSystemProperty prop = new CoordinateReferenceSystemProperty();
        // TODO: make sure it is not hard coded
        prop.setCrsEncoding(new FourCC("wkt2"));
        prop.setCrs(
                "PROJCRS[\"GDA94 / MGA zone 55\",BASEGEOGCRS[\"GDA94\",DATUM[\"Geocentric Datum of Australia 1994\",ELLIPSOID[\"GRS 1980\",6378137,298.257222101004,LENGTHUNIT[\"metre\",1]]],PRIMEM[\"Greenwich\",0,ANGLEUNIT[\"degree\",0.0174532925199433]],ID[\"EPSG\",4283]],CONVERSION[\"Transverse Mercator\",METHOD[\"Transverse Mercator\",ID[\"EPSG\",9807]],PARAMETER[\"Latitude of natural origin\",0,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8801]],PARAMETER[\"Longitude of natural origin\",147,ANGLEUNIT[\"degree\",0.0174532925199433],ID[\"EPSG\",8802]],PARAMETER[\"Scale factor at natural origin\",0.9996,SCALEUNIT[\"unity\",1],ID[\"EPSG\",8805]],PARAMETER[\"False easting\",500000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8806]],PARAMETER[\"False northing\",10000000,LENGTHUNIT[\"metre\",1],ID[\"EPSG\",8807]]],CS[Cartesian,2],AXIS[\"easting\",east,ORDER[1],LENGTHUNIT[\"metre\",1]],AXIS[\"northing\",north,ORDER[2],LENGTHUNIT[\"metre\",1]],ID[\"EPSG\",28355]]");
        return prop;
    }

    private byte[] get_bytes_for_tile(int x, int y) throws IOException {
        Path inputPath = Path.of(String.format("/home/bradh/testbed20/tild/tile_%d_%d.hif", x, y));
        FileParser fileParser = new FileParser();
        List<Box> boxes = fileParser.parse(inputPath);
        // Hack
        for (Box box : boxes) {
            if (box instanceof MediaDataBox mdat) {
                return mdat.getData();
            }
        }
        throw new IOException("How can there be no mdat?.");
    }

    private AbstractItemProperty makeHevcCodecBox() throws IOException {
        Path inputPath = Path.of(String.format("/home/bradh/testbed20/tild/tile_%d_%d.hif", 0, 0));
        FileParser fileParser = new FileParser();
        List<Box> boxes = fileParser.parse(inputPath);
        // Hack
        for (Box box : boxes) {
            if (box instanceof MetaBox meta) {
                for (Box metaChild : meta.getNestedBoxes())
                    if (metaChild instanceof ItemPropertiesBox iprp) {
                        ItemPropertyContainerBox ipco = iprp.getItemProperties();
                        for (AbstractItemProperty prop : ipco.getProperties()) {
                            if (prop instanceof HEVCConfigurationItemProperty hvcC) {
                                return hvcC;
                            }
                        }
                    }
            }
        }
        throw new IOException("How can there be no hvcC?.");
    }
}
