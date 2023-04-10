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
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

/**
 * @author bradh
 */
public class Uncompressed_rgb555_block_Test extends UncompressedTestSupport {
    private List<Box> buildBoxes_rgb555(ByteOrder blockEndian, boolean padLSB) throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb555(blockEndian, padLSB);
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb555(blockEndian, padLSB);
        boxes.add(mdat);
        return boxes;
    }

    private MetaBox createMetaBox_rgb555(ByteOrder blockEndian, boolean padLSB) {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb555());
        boxes.add(makeItemPropertiesBox_rgb555(blockEndian, padLSB));
        meta.addNestedBoxes(boxes);
        return meta;
    }

    @Test
    public void writeFile_rgb555_block_be() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.BIG_ENDIAN, false);
        writeBoxes(boxes, "test_uncompressed_rgb555_block_be.heif");
    }

    @Test
    public void writeFile_rgb555_block_le() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.LITTLE_ENDIAN, false);
        writeBoxes(boxes, "test_uncompressed_rgb555_block_le.heif");
    }

    @Test
    public void writeFile_rgb555_block_be_pad_lsb() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.BIG_ENDIAN, true);
        writeBoxes(boxes, "test_uncompressed_rgb555_block_be_pad_lsb.heif");
    }

    @Test
    public void writeFile_rgb555_block_le_pad_lsb() throws IOException {
        List<Box> boxes = buildBoxes_rgb555(ByteOrder.LITTLE_ENDIAN, true);
        writeBoxes(boxes, "test_uncompressed_rgb555_block_le_pad_lsb.heif");
    }

    private ItemLocationBox makeItemLocationBox_rgb555() {
        return makeItemLocationBox_two_byte_per_pixel();
    }

    private Box makeItemPropertiesBox_rgb555(ByteOrder blockEndian, boolean padLSB) {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb555(blockEndian, padLSB));
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb555(
            ByteOrder endian, boolean padLSB) {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("gene"));
        uncc.addComponent(new Component(0, 4, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(1, 4, ComponentFormat.UnsignedInteger, 0));
        uncc.addComponent(new Component(2, 4, ComponentFormat.UnsignedInteger, 0));
        uncc.setSamplingType(SamplingType.NoSubsampling);
        uncc.setInterleaveType(Interleaving.Pixel);
        uncc.setBlockSize(2);
        uncc.setComponentLittleEndian(false);
        uncc.setBlockPadLSB(padLSB);
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

    private MediaDataBox createMediaDataBox_rgb555(ByteOrder blockEndian, boolean padLSB)
            throws IOException {
        MediaDataBox mdat = new MediaDataBox();
        BufferedImage image =
                new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_USHORT_555_RGB);
        drawColouredRectangles(image);
        SampleModel sm = image.getSampleModel();
        System.out.println(sm.toString());
        ColorModel cm = image.getColorModel();
        System.out.println(cm.toString());
        DataBufferUShort buffer = (DataBufferUShort) image.getRaster().getDataBuffer();
        if (padLSB) {
            short[] shortsUnshifted = buffer.getData();
            short[] shortsShifted = new short[shortsUnshifted.length];
            for (int i = 0; i < shortsUnshifted.length; i++) {
                shortsShifted[i] = (short) (shortsUnshifted[i] << 1);
            }
            mdat.setData(shortArrayToByteArray(shortsShifted, blockEndian));
        } else {
            mdat.setData(shortArrayToByteArray(buffer.getData(), blockEndian));
        }
        return mdat;
    }
}
