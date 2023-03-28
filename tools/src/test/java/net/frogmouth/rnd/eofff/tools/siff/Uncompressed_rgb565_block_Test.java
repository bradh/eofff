package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_HEIGHT;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.IMAGE_WIDTH;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.LENGTH_OF_FREEBOX_HEADER;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MAIN_ITEM_ID;
import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MDAT_START;
import static org.testng.Assert.*;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferUShort;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AssociationEntry;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyAssociation;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.PropertyAssociation;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.free.FreeBox;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

public class Uncompressed_rgb565_block_Test extends UncompressedTestSupport {
    @Test
    public void writeFile_rgb565_block_be() throws IOException {
        List<Box> boxes = buildBoxes_rgb565(ByteOrder.BIG_ENDIAN);
        writeBoxes(boxes, "test_uncompressed_rgb565_block_be.mp4");
    }

    @Test
    public void writeFile_rgb565_block_le() throws IOException {
        List<Box> boxes = buildBoxes_rgb565(ByteOrder.LITTLE_ENDIAN);
        writeBoxes(boxes, "test_uncompressed_rgb565_block_le.mp4");
    }

    private List<Box> buildBoxes_rgb565(ByteOrder blockEndian) throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb565(blockEndian);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb565(blockEndian);
        boxes.add(mdat);
        return boxes;
    }

    private MediaDataBox createMediaDataBox_rgb565(ByteOrder blockEndian) throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_USHORT_565_RGB);
        drawColouredRectangles(image);
        SampleModel sm = image.getSampleModel();
        System.out.println(sm.toString());
        ColorModel cm = image.getColorModel();
        System.out.println(cm.toString());
        DataBufferUShort buffer = (DataBufferUShort) image.getRaster().getDataBuffer();
        mdat.setData(shortArrayToByteArray(buffer.getData(), blockEndian));
        return mdat;
    }

    private MetaBox createMetaBox_rgb565(ByteOrder blockEndian) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb565());
        boxes.add(makeItemPropertiesBox_rgb565(blockEndian));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private ItemLocationBox makeItemLocationBox_rgb565() {
        return makeItemLocationBox_two_byte_per_pixel();
    }

    private Box makeItemPropertiesBox_rgb565(ByteOrder blockEndian) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb565(blockEndian));
        ipco.addProperty(makeImageSpatialExtentsProperty());
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

        componentDefinitionAssociation.addEntry(componentDefinitionAssociationEntry);
        iprp.addItemPropertyAssociation(componentDefinitionAssociation);
        return iprp;
    }

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb565(ByteOrder endian) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 4, 0, 0));
        uncc.addComponent(new Component(1, 5, 0, 0));
        uncc.addComponent(new Component(2, 4, 0, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
        uncc.setBlockSize(2);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(false);
        uncc.setBlockLittleEndian(endian == ByteOrder.LITTLE_ENDIAN);
        uncc.setBlockReversed(false);
        uncc.setPadUnknown(false);
        uncc.setPixelSize(0);
        uncc.setRowAlignSize(0);
        uncc.setTileAlignSize(0);
        uncc.setNumTileColumnsMinusOne(0);
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }
}
