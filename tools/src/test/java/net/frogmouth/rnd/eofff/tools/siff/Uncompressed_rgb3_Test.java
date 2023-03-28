package net.frogmouth.rnd.eofff.tools.siff;

import static net.frogmouth.rnd.eofff.tools.siff.UncompressedTestSupport.MAIN_ITEM_ID;
import static org.testng.Assert.*;

import java.io.IOException;
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
import net.frogmouth.rnd.eofff.isobmff.mdat.MediaDataBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;
import org.testng.annotations.Test;

public class Uncompressed_rgb3_Test extends UncompressedTestSupport {

    @Test
    public void writeFile_rgb3() throws IOException {
        List<Box> boxes = new ArrayList<>();
        FileTypeBox ftyp = createFileTypeBox();
        boxes.add(ftyp);
        MetaBox meta = createMetaBox_rgb3();
        boxes.add(meta);
        long lengthOfPreviousBoxes = ftyp.getSize() + meta.getSize();
        long numberOfFillBytes = MDAT_START - lengthOfPreviousBoxes - LENGTH_OF_FREEBOX_HEADER;
        FreeBox free = new FreeBox();
        free.setData(new byte[(int) numberOfFillBytes]);
        boxes.add(free);
        MediaDataBox mdat = createMediaDataBox_rgb3();
        boxes.add(mdat);
        writeBoxes(boxes, "test_uncompressed_rgb3.mp4");
    }

    private MediaDataBox createMediaDataBox_rgb3() {
        MediaDataBox mdat = new MediaDataBox();
        byte[] bytes = new byte[IMAGE_WIDTH * IMAGE_HEIGHT * NUM_BYTES_PER_PIXEL_RGB];
        int index = 0;
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0xFF;
                bytes[index + 1] = (byte) 0x00;
                bytes[index + 2] = (byte) 0x00;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0x00;
                bytes[index + 1] = (byte) 0xFF;
                bytes[index + 2] = (byte) 0x00;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0x00;
                bytes[index + 1] = (byte) 0x00;
                bytes[index + 2] = (byte) 0xFF;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        for (int r = 0; r < IMAGE_HEIGHT / 4; r++) {
            for (int c = 0; c < IMAGE_WIDTH; c++) {
                bytes[index] = (byte) 0xff;
                bytes[index + 1] = (byte) 0xFF;
                bytes[index + 2] = (byte) 0xff;
                index += NUM_BYTES_PER_PIXEL_RGB;
            }
        }
        mdat.setData(bytes);
        return mdat;
    }

    private MetaBox createMetaBox_rgb3() {
        MetaBox meta = new MetaBox();
        List<Box> boxes = new ArrayList<>();
        boxes.add(makeHandlerBox());
        boxes.add(makePrimaryItemBox());
        boxes.add(makeItemInfoBox());
        boxes.add(makeItemLocationBox_rgb_generic());
        boxes.add(makeItemPropertiesBox_rgb3());
        meta.addNestedBoxes(boxes);
        return meta;
    }

    private Box makeItemPropertiesBox_rgb3() {
        ItemPropertiesBox iprp = new ItemPropertiesBox();
        ItemPropertyContainerBox ipco = new ItemPropertyContainerBox();
        ipco.addProperty(makeComponentDefinitionBox_rgb_generic());
        ipco.addProperty(makeUncompressedFrameConfigBox_rgb3());
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

    private UncompressedFrameConfigBox makeUncompressedFrameConfigBox_rgb3() {
        UncompressedFrameConfigBox uncc = new UncompressedFrameConfigBox();
        uncc.setProfile(new FourCC("rgb3"));
        uncc.addComponent(new Component(0, 7, 0, 0));
        uncc.addComponent(new Component(1, 7, 0, 0));
        uncc.addComponent(new Component(2, 7, 0, 0));
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
        uncc.setNumTileColumnsMinusOne(0);
        return uncc;
    }
}
