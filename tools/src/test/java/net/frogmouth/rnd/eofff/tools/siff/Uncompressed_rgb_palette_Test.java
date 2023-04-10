package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_DATA_START;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_HEIGHT;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_WIDTH;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.LENGTH_OF_FREEBOX_HEADER;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MAIN_ITEM_ID;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MDAT_START;
import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocExtent;
import net.frogmouth.rnd.eofff.isobmff.iloc.ILocItem;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.PaletteComponent;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

public class Uncompressed_rgb_palette_Test extends UncompressedTestSupport {

    private static final int NUM_BYTES_PER_PIXEL_RGB_PALETTE = 1;

    @Test
    public void writeFile_rgb_palette() throws IOException {
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);
        drawColouredRectangles(image);
        ImageIO.write(image, "PNG", new File("ref_byte_indexed.png"));
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb_palette(image.getColorModel());
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb_palette(image);
        boxes.add(mdat);
        writeBoxes(boxes, "test_uncompressed_rgb_palette.heif");
    }

    private MediaDataBox createMediaDataBox_rgb_palette(BufferedImage image) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        DataBufferByte buffer = (DataBufferByte) image.getRaster().getDataBuffer();
        mdat.setData(buffer.getData());
        return mdat;
    }

    private MetaBox createMetaBox_rgb_palette(ColorModel colourModel) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb_palette());
        boxes.add(makeItemPropertiesBox_rgb_palette(colourModel));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemLocationBox makeItemLocationBox_rgb_palette() {
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
        mainItemExtent.setExtentLength(
                IMAGE_HEIGHT * IMAGE_WIDTH * NUM_BYTES_PER_PIXEL_RGB_PALETTE);
        mainItemLocation.addExtent(mainItemExtent);
        iloc.addItem(mainItemLocation);
        return iloc;
    }

    private Box makeItemPropertiesBox_rgb_palette(ColorModel colourModel) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_palette());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb_palette());
        ipco.addProperty(makeImageSpatialExtentsProperty());
        ipco.addProperty(makeComponentPaletteBox(colourModel));
        iprp.setItemProperties(ipco);
        ItemPropertyAssociation componentDefinitionAssociation = new ItemPropertyAssociation();
        AssociationEntry componentDefinitionAssociationEntry = new AssociationEntry();
        componentDefinitionAssociationEntry.setItemId(MAIN_ITEM_ID);

        PropertyAssociation associationToComponentDefinitionBox = new PropertyAssociation();
        associationToComponentDefinitionBox.setPropertyIndex(1);
        associationToComponentDefinitionBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentDefinitionBox);

        PropertyAssociation associationToUncompressedFrameConfigBox = new PropertyAssociation();
        associationToUncompressedFrameConfigBox.setPropertyIndex(2);
        associationToUncompressedFrameConfigBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToUncompressedFrameConfigBox);

        PropertyAssociation associationToImageSpatialExtentsProperty = new PropertyAssociation();
        associationToImageSpatialExtentsProperty.setPropertyIndex(3);
        associationToImageSpatialExtentsProperty.setEssential(false);
        componentDefinitionAssociationEntry.addAssociation(
                associationToImageSpatialExtentsProperty);

        PropertyAssociation associationToComponentPaletteBox = new PropertyAssociation();
        associationToComponentPaletteBox.setPropertyIndex(4);
        associationToComponentPaletteBox.setEssential(true);
        componentDefinitionAssociationEntry.addAssociation(associationToComponentPaletteBox);

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private ComponentDefinitionBox makeComponentDefinitionBox_rgb_palette() {
        ComponentDefinitionBox cmpd = new ComponentDefinitionBox();
        ComponentDefinition paletteComponent = new ComponentDefinition(10, null);
        cmpd.addComponentDefinition(paletteComponent);
        ComponentDefinition redComponent = new ComponentDefinition(4, null);
        cmpd.addComponentDefinition(redComponent);
        ComponentDefinition greenComponent = new ComponentDefinition(5, null);
        cmpd.addComponentDefinition(greenComponent);
        ComponentDefinition blueComponent = new ComponentDefinition(6, null);
        cmpd.addComponentDefinition(blueComponent);
        return cmpd;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb_palette() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 7, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Component);
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
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }

    private AbstractItemProperty makeComponentPaletteBox(ColorModel colourModel) {
        ComponentPaletteBox cpal = new ComponentPaletteBox();
        cpal.addComponent(new PaletteComponent(1, 7, 0));
        cpal.addComponent(new PaletteComponent(2, 7, 0));
        cpal.addComponent(new PaletteComponent(3, 7, 0));
        IndexColorModel icm = (IndexColorModel) colourModel;
        int mapSize = icm.getMapSize();
        byte[][] values = new byte[mapSize][3];
        byte[] reds = new byte[mapSize];
        icm.getReds(reds);
        byte[] greens = new byte[mapSize];
        icm.getGreens(greens);
        byte[] blues = new byte[mapSize];
        icm.getBlues(blues);
        for (int i = 0; i < mapSize; i++) {
            values[i][0] = reds[i];
            values[i][1] = greens[i];
            values[i][2] = blues[i];
        }
        cpal.setComponentValues(values);
        return cpal;
    }
}
