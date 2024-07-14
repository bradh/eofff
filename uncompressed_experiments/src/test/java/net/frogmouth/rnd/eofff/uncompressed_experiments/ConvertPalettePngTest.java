package net.frogmouth.rnd.eofff.uncompressed_experiments;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.isobmff.BitReader;
import net.frogmouth.rnd.eofff.isobmff.Box;
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
import net.frogmouth.rnd.eofff.isobmff.iprp.AssociationEntry;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.isobmff.iprp.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteProperty;
import net.frogmouth.rnd.eofff.uncompressed.cpal.PaletteComponent;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import net.frogmouth.rnd.ngiis.png.ChunkIDAT;
import net.frogmouth.rnd.ngiis.png.ChunkIHDR;
import net.frogmouth.rnd.ngiis.png.ChunkPLTE;
import net.frogmouth.rnd.ngiis.png.PaletteEntry;
import net.frogmouth.rnd.ngiis.png.PngChunk;
import net.frogmouth.rnd.ngiis.png.PngFileParser;
import org.testng.annotations.Test;

/** Write file. */
public class ConvertPalettePngTest {

    private static final long MAIN_ITEM_ID = 10;
    private static final int MDAT_HEADER_BYTES = 8;

    public ConvertPalettePngTest() {}

    @Test
    public void convertSimple() throws IOException {
        String source = "/home/bradh/testbed20/silvereye/smalltiles_2024-06-29/simple_osm_tile.png";
        String target = "rgb_palette_simple.heif";
        convert(source, target);
    }

    @Test
    public void convertComplex() throws IOException {
        String source =
                "/home/bradh/testbed20/silvereye/smalltiles_2024-06-29/complex_osm_tile.png";
        String target = "rgb_palette_complex.heif";
        convert(source, target);
    }

    protected void convert(String source, String target) throws IOException {
        PngFileParser parser = new PngFileParser();
        List<PngChunk> chunks = parser.parse(Paths.get(source));
        assertEquals(chunks.size(), 4);
        ChunkIHDR ihdr = getIHDR(chunks);
        ChunkPLTE plte = getPLTE(chunks);
        byte[] data = getDataFromIDAT(chunks);
        MediaDataBox mdat = createMediaDataBox_palette(data, ihdr);

        ItemLocationBox iloc = makeItemLocationBox(mdat.getBodySize());
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_palette(iloc, ihdr, plte);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        for (var item : iloc.getItems()) {
            if (item.getConstructionMethod() == 0) {
                item.setBaseOffset(lengthOfPreviousBoxes + MDAT_HEADER_BYTES);
            }
        }
        boxes.add(mdat);
        writeBoxes(boxes, target);
    }

    private FileTypeBox createFileTypeBox() {
        FileTypeBox fileTypeBox = new FileTypeBox();
        fileTypeBox.setMajorBrand(new Brand("mif1"));
        fileTypeBox.setMinorVersion(0);
        fileTypeBox.addCompatibleBrand(new Brand("mif1"));
        fileTypeBox.addCompatibleBrand(new Brand("mif2"));
        fileTypeBox.addCompatibleBrand(new Brand("unif"));
        return fileTypeBox;
    }

    private MetaBox createMetaBox_palette(ItemLocationBox iloc, ChunkIHDR ihdr, ChunkPLTE plte)
            throws IOException {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(iloc);
        boxes.add(makeItemPropertiesBox_palette(ihdr, plte));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private HandlerBox makeHandlerBox() {
        HandlerBox hdlr = new HandlerBox();
        hdlr.setHandlerType("pict");
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
        infe0.setItemName("Palette Image");
        iinf.addItem(infe0);
        return iinf;
    }

    private ItemLocationBox makeItemLocationBox(long extentLength) {
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
        mainItemExtent.setExtentOffset(0);
        mainItemExtent.setExtentLength(extentLength);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private Box makeItemPropertiesBox_palette(ChunkIHDR ihdr, ChunkPLTE plte) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic()); // 1
        ipco.addProperty(makeUncompressedFrameConfigBox(ihdr)); // 2
        ipco.addProperty(makeComponentPaletteProperty(plte)); // 3
        ipco.addProperty(makeImageSpatialExtentsProperty(ihdr)); // 4
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

            PropertyAssociation associationToComponentPalette = new PropertyAssociation();
            associationToComponentPalette.setPropertyIndex(3);
            associationToComponentPalette.setEssential(true);
            mainItemAssociations.addAssociation(associationToComponentPalette);

            PropertyAssociation associationToImageSpatialExtentsProperty =
                    new PropertyAssociation();
            associationToImageSpatialExtentsProperty.setPropertyIndex(4);
            associationToImageSpatialExtentsProperty.setEssential(true);
            mainItemAssociations.addAssociation(associationToImageSpatialExtentsProperty);

            itemPropertyAssociation.addEntry(mainItemAssociations);
        }
        iprp.addItemPropertyAssociation(itemPropertyAssociation);

        return iprp;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgb_generic() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        ComponentDefinition paletteComponent = new ComponentDefinition(10, null);
        cmpd.addComponentDefinition(paletteComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox(ChunkIHDR ihdr) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(
                new Component(3, ihdr.getBitDepth() - 1, ComponentFormat.UnsignedInteger, 0));
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
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileRowsMinusOne(0);
        return uncc;
    }

    private ComponentPaletteProperty makeComponentPaletteProperty(ChunkPLTE plte) {
        ComponentPaletteProperty cpal = new ComponentPaletteProperty();
        PaletteComponent redComponent = new PaletteComponent(0, 7, 0);
        cpal.addComponent(redComponent);
        PaletteComponent greenComponent = new PaletteComponent(1, 7, 0);
        cpal.addComponent(greenComponent);
        PaletteComponent blueComponent = new PaletteComponent(2, 7, 0);
        cpal.addComponent(blueComponent);
        byte[][] componentValues = new byte[plte.getEntries().size()][3];
        for (int i = 0; i < plte.getEntries().size(); i++) {
            PaletteEntry entry = plte.getEntries().get(i);
            componentValues[i][0] = (byte) entry.red();
            componentValues[i][1] = (byte) entry.green();
            componentValues[i][2] = (byte) entry.blue();
        }
        cpal.setComponentValues(componentValues);
        return cpal;
    }

    private ImageSpatialExtentsProperty makeImageSpatialExtentsProperty(ChunkIHDR ihdr) {
        ImageSpatialExtentsProperty ispe = new ImageSpatialExtentsProperty();
        ispe.setImageHeight(ihdr.getHeight());
        ispe.setImageWidth(ihdr.getWidth());
        return ispe;
    }

    private MediaDataBox createMediaDataBox_palette(byte[] data, ChunkIHDR ihdr)
            throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitReader bitReader = new BitReader(data);
        // TODO: copy over bit-by-bit properly
        // HACK: currently assumes its always multiple of 8 bits
        for (int y = 0; y < ihdr.getHeight(); y++) {
            int filter = bitReader.readBits(Byte.SIZE);
            assertEquals(filter, 0); // safety check
            int bits = ihdr.getWidth() * ihdr.getBitDepth();
            assertEquals(bits % Byte.SIZE, 0);
            int bytes = bits / Byte.SIZE;
            for (int x = 0; x < bytes; x++) {
                int v = bitReader.readBits(Byte.SIZE);
                baos.write(v);
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

    private byte[] getDataFromIDAT(List<PngChunk> chunks) throws IOException {
        ChunkIHDR ihdr = null;
        ChunkPLTE plte = null;
        ChunkIDAT idat = null;
        for (PngChunk chunk : chunks) {
            if (chunk instanceof ChunkIHDR chunkIHDR) {
                ihdr = chunkIHDR;
            }
            if (chunk instanceof ChunkPLTE chunkPLTE) {
                plte = chunkPLTE;
            }
            if (chunk instanceof ChunkIDAT chunkIDAT) {
                idat = chunkIDAT;
            }
        }
        if (ihdr == null) {
            throw new IOException("missing IHDR from PNG file");
        }
        if (plte == null) {
            throw new IOException("missing PLTE from PNG file");
        }
        if (idat == null) {
            throw new IOException("missing IDAT from PNG file");
        }
        return idat.getDecompressedData();
    }

    private ChunkIHDR getIHDR(List<PngChunk> chunks) throws IOException {
        ChunkIHDR ihdr = null;
        for (PngChunk chunk : chunks) {
            if (chunk instanceof ChunkIHDR chunkIHDR) {
                ihdr = chunkIHDR;
            }
        }
        if (ihdr == null) {
            throw new IOException("missing IHDR from PNG file");
        }
        return ihdr;
    }

    private ChunkPLTE getPLTE(List<PngChunk> chunks) throws IOException {
        ChunkPLTE plte = null;
        for (PngChunk chunk : chunks) {
            if (chunk instanceof ChunkPLTE chunkPLTE) {
                plte = chunkPLTE;
            }
        }
        if (plte == null) {
            throw new IOException("missing PLTE from PNG file");
        }
        return plte;
    }
}
