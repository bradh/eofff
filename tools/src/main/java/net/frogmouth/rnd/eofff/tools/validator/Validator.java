package net.frogmouth.rnd.eofff.tools.validator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.AbstractItemProperty;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertiesBox;
import net.frogmouth.rnd.eofff.imagefileformat.extensions.properties.ItemPropertyContainerBox;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.CleanAperture;
import net.frogmouth.rnd.eofff.imagefileformat.properties.image.ImageSpatialExtentsProperty;
import net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.idat.ItemDataBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.iloc.ItemLocationBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinition;
import net.frogmouth.rnd.eofff.uncompressed.cmpd.ComponentDefinitionBox;
import net.frogmouth.rnd.eofff.uncompressed.cpal.ComponentPaletteBox;
import net.frogmouth.rnd.eofff.uncompressed.sbpm.SensorBadPixelsMapBox;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Component;
import net.frogmouth.rnd.eofff.uncompressed.uncc.ComponentFormat;
import net.frogmouth.rnd.eofff.uncompressed.uncc.Interleaving;
import net.frogmouth.rnd.eofff.uncompressed.uncc.SamplingType;
import net.frogmouth.rnd.eofff.uncompressed.uncc.UncompressedFrameConfigBox;

public class Validator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Need to specify the file to validate...");
            // System.exit(0);
            validate(
                    //        "/home/bradh/Uncompressed Test Files/Uncompressed Test
                    // Files/uncC_1_rgb_planar.heif");
                    "/home/bradh/Uncompressed Test Files/Uncompressed Test Files/uncC_2_rgb_planar_tiled.heif");
            // "/home/bradh/Uncompressed Test Files/Uncompressed Test
            // Files/uncC_3_rgb_interleaved_tiled.heif");
            // "/home/bradh/Uncompressed Test Files/Uncompressed Test
            // Files/uncC_4_rgb_interleaved.heif");
        } else {
            validate(args[0]);
        }
    }

    private static void validate(String filename) throws IOException {
        List<Box> boxes = new ArrayList<>();
        boxes.addAll(parseFile(filename));
        for (Box box : boxes) {
            System.out.println(box);
        }
        validateFileBox(boxes);
        validatePrimaryItem(boxes);
        validateMetaBox(boxes);
        validateCoreId(boxes);
        validateTimestamps(boxes);
        validateSubsampling(boxes);
        validateFormats(boxes);
        validateProperties(boxes);
        validateImageOverlay(boxes);
        validateImageSequence(boxes);
        validateMetadata(boxes);
        validateMetadataEncoding(boxes);
        validateSecurityMetadata(boxes);
        validateMetadataTimestamps(boxes);
        validateMetadataIdentifiers(boxes);
        validateGeneralisedMetadata(boxes);
        validateRegionAnnotation(boxes);
        validateImageSequenceMetadata(boxes);
        validateTimedTrackMetadata(boxes);
        validateFileGenerationAndEditing(boxes);
    }

    private static List<Box> parseFile(String inputPath) throws IOException {
        Path testFile = Paths.get(inputPath);
        FileParser fileParser = new FileParser();
        return fileParser.parse(testFile);
    }

    private static Box getTopLevelBoxByFourCC(List<Box> boxes, String fourCC) {
        for (Box box : boxes) {
            if (box.getFourCC().equals(new FourCC(fourCC))) {
                return box;
            }
        }
        return null;
    }

    private static Box findChildBox(MetaBox parent, FourCC fourCC) {
        if (parent == null) {
            return null;
        }
        for (Box box : parent.getNestedBoxes()) {
            if (box.getFourCC().equals(fourCC)) {
                return box;
            }
        }
        return null;
    }

    /**
     * Validates ftyp box related requirements.
     *
     * <p>*
     *
     * <p>NGA.STND.0078_0.1-01 SIFF files shall contain a FileTypeBox, starting at the first byte of
     * the file
     *
     * <p>NGA.STND.0078_0.1-02 SIFF files compliant with this version of the NGA.STND.0078 profile
     * shall include the ‘ns01’ brand in the compatible brands list inside the FileTypeBox.
     *
     * <p>NGA.STND.0078_0.1-03 SIFF files shall meet the requirements of the ‘mif2’ brand.
     *
     * <p>NGA.STND.0078_0.1-04 SIFF files shall include the ‘mif2’ brand in the compatible brands
     * list
     *
     * <p>NGA.STND.0078_0.1-05 When a SIFF file contains an image sequence, the ‘msf1’ brand [6]
     * shall be included in the compatible brands list
     *
     * <p>NGA.STND.0078_0.1-06 SIFF files shall include the ‘unif’ brand, indicating the use of
     * unique IDs as per ISO/IEC 14496-12
     *
     * <p>NGA.STND.0078_0.1-07 A SIFF file shall contain a MovieBox when it contains timed media
     *
     * <p>NGA.STND.0078_0.1-08 SIFF files shall provide a major brand indicating the primary
     * intended use of the file.
     *
     * <p>NGA.STND.0078_0.1-09 SIFF files shall provide a complete list of compatible brands
     * representing the full set of requirements for reading and interpreting the content of the
     * file.
     *
     * @param boxes
     */
    private static void validateFileBox(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-01 SIFF files shall contain a FileTypeBox, starting at the first byte of the file");
        Box box = getTopLevelBoxByFourCC(boxes, "ftyp");
        if (box == null) {
            System.out.println("\tFAIL.");
            System.out.println("\tRemaining ftyp box related requirements will be skipped.");
        } else {
            System.out.println("\tPASS.");
            FileTypeBox ftyp = (FileTypeBox) box;
            validate_02(ftyp);
            validate_03(ftyp);
            validate_04(ftyp);
            validate_05(ftyp);
            validate_06(ftyp);
            validate_07(ftyp);
            validate_08(ftyp);
            validate_09(ftyp);
        }
    }

    private static void validate_02(FileTypeBox ftyp) {
        // TODO: we need to update the requirements text
        System.out.println(
                "NGA.STND.0078_0.1-02 SIFF files compliant with this version of the NGA.STND.0078 profile shall include the ‘ns01’ brand in the compatible brands list inside the FileTypeBox.");
        if (ftyp.getCompatibleBrands().contains(new Brand("geo1"))) {
            System.out.println("\tPASS.");
        } else {
            System.out.println("\tFAIL.");
            System.out.print("\tCompatible brands:");
            System.out.println(ftyp.getCompatibleBrands());
        }
    }

    private static void validate_03(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-03 SIFF files shall meet the requirements of the ‘mif2’ brand.");
        System.out.println(
                "\tTBD. This tool is not yet capable of checking mif2 brand requirements.");
    }

    private static void validate_04(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-04 SIFF files shall include the ‘mif2’ brand in the compatible brands list.");
        if (ftyp.getCompatibleBrands().contains(new Brand("mif2"))) {
            System.out.println("\tPASS.");
        } else {
            System.out.println("\tFAIL.");
            System.out.print("\tCompatible brands:");
            System.out.println(ftyp.getCompatibleBrands());
        }
    }

    private static void validate_05(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-05 When a SIFF file contains an image sequence, the ‘msf1’ brand shall be included in the compatible brands list.");
        if (ftyp.getCompatibleBrands().contains(new Brand("msf1"))) {
            System.out.println(
                    "\tTBD. This tool is not yet capable of checking if the file contains an image sequence, however msf1 was found.");
        } else {
            System.out.println(
                    "\tTBD. This tool is not yet capable of checking if the file contains an image sequence, however msf1 was not found.");
        }
    }

    private static void validate_06(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-06 SIFF files shall include the ‘unif’ brand, indicating the use of unique IDs as per ISO/IEC 14496-12.");
        if (ftyp.getCompatibleBrands().contains(new Brand("unif"))) {
            System.out.println("\tPASS.");
        } else {
            System.out.println("\tFAIL.");
            System.out.print("\tCompatible brands:");
            System.out.println(ftyp.getCompatibleBrands());
        }
    }

    private static void validate_07(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-07 A SIFF file shall contain a MovieBox when it contains timed media.");
        System.out.println("\tTBD. This tool is not yet capable of checking this requirement.");
    }

    private static void validate_08(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-08 SIFF files shall provide a major brand indicating the primary intended use of the file.");
        System.out.println("\tTBD. The major brand is: '" + ftyp.getMajorBrand().toString() + "'.");
    }

    private static void validate_09(FileTypeBox ftyp) {
        System.out.println(
                "NGA.STND.0078_0.1-09 SIFF files shall provide a complete list of compatible brands representing the full set of requirements for reading and interpreting the content of the file.");
        System.out.println(
                "\tTBD. This tool is not yet capable of determining the required compatible brands.");
        System.out.print("\tCompatible brands found:");
        System.out.println(ftyp.getCompatibleBrands());
    }

    private static void validatePrimaryItem(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-10 SIFF files containing still images or image sequences shall contain a primary item.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-11 Selection of a primary item is dependent on application needs and shall identify a primary image most representative of the intended use of the file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-12 In SIFF files containing only sequences, the primary item shall be a single renderable intra-coded image from a sequence.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }
    /**
     * Metabox related requirements.
     *
     * <p>NGA.STND.0078_0.1-13 A file-level Metabox shall be present in a SIFF file.
     *
     * <p>NGA.STND.0078_0.1-14 The handler in the file-level MetaBox of a SIFF file containing still
     * imagery shall be ‘pict’.
     *
     * <p>NGA.STND.0078_0.1-15 SIFF files containing imagery shall contain a PrimaryItemBox with a
     * primary item declared.
     *
     * <p>NGA.STND.0078_0.1-16 A MetaBox declaring NTB/MISB defined KLV metadata sets and packs
     * shall set the ItemInfoEntry field version equal to ‘2’ if the number of item_IDs required is
     * less than or equal to 65535, or ‘3’, otherwise.
     *
     * <p>NGA.STND.0078_0.1-17 A MetaBox declaring NTB/MISB defined KLV metadata sets and packs
     * shall set the ItemInfoEntry field item_type = ‘uri ‘
     *
     * <p>NGA.STND.0078_0.1-18 A MetaBox declaring NTB/MISB defined KLV metadata sets and packs
     * shall encode ItemInfoEntry field item_uri_type as a URN in the format of:
     * ‘urn:nsg:klv:ul:16-byte key’, with the 16-byte key following the QUADBYTE notation declared
     * in RFC 5119.
     *
     * @param boxes
     */
    private static void validateMetaBox(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-13 A file-level Metabox shall be present in a SIFF file.");
        Box box = getTopLevelBoxByFourCC(boxes, "meta");
        if (box == null) {
            System.out.println("\tFAIL.");
            System.out.println("\tRemaining meta box related requirements will be skipped.");
        } else {
            System.out.println("\tPASS.");
            MetaBox meta = (MetaBox) box;
            validate_14(meta);
            validate_15(meta);
            validate_16(meta);
            validate_17(meta);
            validate_18(meta);
        }
    }

    private static void validate_14(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-14 The handler in the file-level MetaBox of a SIFF file containing still imagery shall be ‘pict’.");
        HandlerBox hdlr = (HandlerBox) findChildBox(meta, new FourCC("hdlr"));
        if (hdlr == null) {
            System.out.println("\tFAIL. No handler box found.");
        } else if (hdlr.getHandlerType().equals("pict")) {
            System.out.println("\tPASS.");
        } else {
            System.out.println("\tFAIL. Handler is: '" + hdlr.getHandlerType() + "'.");
        }
    }

    private static void validate_15(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-15 SIFF files containing imagery shall contain a PrimaryItemBox with a primary item declared.");
        PrimaryItemBox pitm = (PrimaryItemBox) findChildBox(meta, new FourCC("pitm"));
        if (pitm == null) {
            System.out.println("\tFAIL. No primary item box found.");
        } else {
            System.out.println("\tPass. Primary item identifier: " + pitm.getItemID());
        }
    }

    private static void validate_16(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-16 A MetaBox declaring NTB/MISB defined KLV metadata sets and packs shall set the ItemInfoEntry field version equal to ‘2’ if the number of item_IDs required is less than or equal to 65535, or ‘3’, otherwise.");
        ItemInfoBox iinf = (ItemInfoBox) findChildBox(meta, new FourCC("iinf"));
        if (iinf == null) {
            System.out.println("\tFAIL. No item info box found.");
        } else {
            long highestItemId = 0;
            for (ItemInfoEntry infe : iinf.getItems()) {
                if (infe.getItemID() > highestItemId) {
                    highestItemId = infe.getItemID();
                }
            }
            int requiredVersion = 2;
            if (highestItemId > 65535) {
                requiredVersion = 3;
            }
            for (ItemInfoEntry infe : iinf.getItems()) {
                System.out.print("\tItem ID = " + infe.getItemID());
                // TODO: this should be a FourCC derived ItemType or similar, not long.
                if ((infe.getItemUriType() != null)
                        && (infe.getItemUriType().startsWith("urn:nsg:KLV:ul"))) {
                    if (infe.getVersion() == requiredVersion) {
                        System.out.println(". PASS.");
                    } else {
                        System.out.println(". FAIL. Version is " + infe.getVersion());
                    }
                } else {
                    System.out.println(
                            ". Not applicable for this item type:" + infe.getItemTypeAsText());
                }
            }
        }
    }

    private static void validate_17(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-17 A MetaBox declaring NTB/MISB defined KLV metadata sets and packs shall set the ItemInfoEntry field item_type = ‘uri ‘");
        ItemInfoBox iinf = (ItemInfoBox) findChildBox(meta, new FourCC("iinf"));
        if (iinf == null) {
            System.out.println("\tFAIL. No item info box found.");
        } else {
            for (ItemInfoEntry infe : iinf.getItems()) {
                System.out.print("\tItem ID = " + infe.getItemID());
                // TODO: this should be a FourCC derived ItemType or similar, not long.
                if ((infe.getItemUriType() != null)
                        && (infe.getItemUriType().startsWith("urn:nsg:KLV:ul"))) {
                    if (infe.getItemType() == new FourCC("uri ").asUnsigned()) {
                        System.out.println(". PASS.");
                    } else {
                        System.out.println(". FAIL. item_type " + infe.getItemTypeAsText());
                    }
                } else {
                    System.out.println(
                            ". Not applicable for this item type:" + infe.getItemTypeAsText());
                }
            }
        }
    }

    private static void validate_18(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-18 A MetaBox declaring NTB/MISB defined KLV metadata sets and packs shall encode ItemInfoEntry field item_uri_type as a URN in the format of: ‘urn:nsg:klv:ul:16-byte key’, with the 16-byte key following the QUADBYTE notation declared in RFC 5119.");
        System.out.println(
                "\tTBD. This tool is not currently capable of determing NTB/MISB defined KLV metadata sets.");
    }

    /**
     * Validate core identifier requirements.
     *
     * <p>NGA.STND.0078_0.1-19 When ISOBMFF/HEIF media IDs are created, a MISB Core ID shall be
     * generated and referenced to the media item or track in the SIFF file to provide a universally
     * unique identifier for the media content.
     *
     * <p>NGA.STND.0078_0.1-20 When ISOBMFF/HEIF media content are deleted from a file, the media ID
     * and referenced Core ID shall be removed from the SIFF file.
     *
     * <p>NGA.STND.0078_0.1-21 When ISOBMFF/HEIF media content are modified, a new Core ID shall be
     * referenced to the new content.
     *
     * @param boxes
     */
    private static void validateCoreId(List<Box> boxes) {
        validate_19(boxes);
        validate_20(boxes);
        validate_21(boxes);
    }

    private static void validate_19(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-19 When ISOBMFF/HEIF media IDs are created, a MISB Core ID shall be generated and referenced to the media item or track in the SIFF file to provide a universally unique identifier for the media content.");
        Box box = getTopLevelBoxByFourCC(boxes, "meta");
        if (box == null) {
            System.out.println("\tFAIL. No MetaBox found.");
            return;
        }
        MetaBox meta = (MetaBox) box;
        ItemInfoBox iinf = (ItemInfoBox) findChildBox(meta, new FourCC("iinf"));
        PrimaryItemBox pitm = (PrimaryItemBox) findChildBox(meta, new FourCC("pitm"));
        ItemReferenceBox iref = (ItemReferenceBox) findChildBox(meta, new FourCC("iref"));
        if (iinf == null) {
            System.out.println("\tFAIL. No item info box found.");
        } else if (pitm == null) {
            System.out.println("\tFAIL. No primary item box found.");
        } else if (iref == null) {
            System.out.println("\tFAIL. No item reference item box found.");
        } else {
            Long primaryItemId = pitm.getItemID();
            List<Long> coreIdItems = new ArrayList<>();
            for (ItemInfoEntry infe : iinf.getItems()) {
                if ((infe.getItemUriType() != null)
                        && (infe.getItemUriType()
                                .equalsIgnoreCase(
                                        "urn:nsg:KLV:ul:060E2B34.01010101.0E010405.03000000"))) {
                    coreIdItems.add(infe.getItemID());
                }
            }
            boolean found = false;
            for (Long coreIdItem : coreIdItems) {
                for (SingleItemReferenceBox ref : iref.getItems()) {
                    if (ref.getFromItemId() == coreIdItem) {
                        if (ref.getReferences().contains(primaryItemId)) {
                            found = true;
                        }
                    }
                }
            }
            if (found) {
                System.out.println("\tPASS. MIIS Core ID found for at least primary item.");
            } else {
                System.out.println("\tFAIL. MIIS Core ID not found for at least primary item.");
            }
        }
    }

    private static void validate_20(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-20 When ISOBMFF/HEIF media content are deleted from a file, the media ID and referenced Core ID shall be removed from the SIFF file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validate_21(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-21 When ISOBMFF/HEIF media content are modified, a new Core ID shall be referenced to the new content.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    /**
     * Timestamp validation.
     *
     * <p>NGA.STND.0078_0.1-22 For absolute time labelling, the MISP Time format, defined in MISB ST
     * 0603, shall be the basis for time information.
     *
     * <p>NGA.STND.0078_0.1-23 The nanosecond form of ST 1603 timestamps shall be used for absolute
     * time labelling of media content.
     *
     * <p>NGA.STND.0078_0.1-24 Absolute time labelling shall be encoded using KLV as per MISB ST
     * 1603.
     *
     * <p>NGA.STND.0078_0.1-25 Absolute time labelling shall include time quality metadata as per
     * MISB ST 1603.
     *
     * <p>NGA.STND.0078_0.1-26 MISB ST 1507 [13] shall be used to label intraframe timing variation
     * related to rolling shutters and raster scanning devices.
     *
     * <p>NGA.STND.0078_0.1-27 GEOINT functionality shall rely on timestamps based on the MISP Time
     * format.
     *
     * @param boxes
     */
    private static void validateTimestamps(List<Box> boxes) {
        Box box = getTopLevelBoxByFourCC(boxes, "meta");
        if (box == null) {
            System.out.println("\tFAIL. No MetaBox found.");
            System.out.println("Remaining timestamp related requirements will be skipped");
        } else {
            MetaBox meta = (MetaBox) box;
            validate_22(meta);
            validate_23(meta);
            validate_24(meta);
            validate_25(meta);
            validate_26(meta);
            validate_27(meta);
        }
    }

    private static void validate_22(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-22 For absolute time labelling, the MISP Time format, defined in MISB ST 0603, shall be the basis for time information.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validate_23(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-23 The nanosecond form of ST 1603 timestamps shall be used for absolute time labelling of media content.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validate_24(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-24 Absolute time labelling shall be encoded using KLV as per MISB ST 1603");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validate_25(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-25 Absolute time labelling shall include time quality metadata as per MISB ST 1603.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validate_26(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-26 MISB ST 1507 shall be used to label intraframe timing variation related to rolling shutters and raster scanning devices.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validate_27(MetaBox meta) {
        System.out.println(
                "NGA.STND.0078_0.1-27 GEOINT functionality shall rely on timestamps based on the MISP Time format.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateSubsampling(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-28 Subsampling in uncompressed imagery shall be restricted to 4:4:4, 4:2:2, and 4:2:0 within a SIFF file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    /** Validate formats. */
    private static void validateFormats(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-29 Uncompressed image item and image sequence content shall be encoded in a manner compliant with ISO/IEC 23001-17");
        System.out.println(
                "\tThis tool is not currently capable of fully checking this requirement. Partial checks:");
        validateUncompressedImageItem(boxes);
        System.out.println(
                "NGA.STND.0078_0.1-30 JPEG2000 'j2ki' branded files shall conform to the 'mif1' brand");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-31 JPEG2000 'j2ki' branded files shall include 'j2ki' as a compatible brand.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-32 Compressed image item and image sequence content shall be encoded using one of the following codecs: ISO/IEC 15444-1 (JPEG 2000),  ISO/IEC 15444-15 (HTJ2K),  ISO/IEC 21122-3 (JPEG XS), ISO/IEC 14496-10 (AVC/H.264), ISO/IEC 23008-2 (HEVC/H.265), AV1");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-33 Reader applications shall be capable to decoding content compressed using ISO/IEC 15444-1 (JPEG 2000)");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-34 Reader applications shall be capable to decoding content compressed using ISO/IEC 15444-15 (HTJ2K)");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-35 Reader applications shall be capable to decoding content compressed using ISO/IEC 21122-3 (JPEG XS)");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-36 Reader applications shall be capable to decoding content compressed using ISO/IEC 14496-10 (AVC/H.264)");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-37 Reader applications shall be capable to decoding content compressed using ISO/IEC 23008-2 (HEVC/H.265)");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-38 Reader applications shall be capable to decoding content compressed using AV1");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-39 If a SIFF file contains timed media, then the SIFF file shall contain a MovieBox");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-40 When a SIFF file contains video content, the file shall be dual branded to accommodate the video formatting requirements");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-41 When a SIFF file contains video content and associated timed metadata, the file shall be compliant with NGA.STND.0076.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateProperties(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-42 When descriptive or transformative image property requirements are addressed by metadata or image properties documented in NGA GEOINT standards and by image item properties included in commercial media (ISO/IEC, AOMedia, etc.) documentation, the approach identified by the NGA GEOINT standard shall take precedent and be used instead of the commercial version.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-43 As interlaced video is non-compliant with MISB standards, the ‘ilcp’ image item property, described in ISO/IEC 23001-17, shall only be used with image items extracted from Class 3 video sources utilizing interlaced sensors.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateImageOverlay(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-44 When included in an image overlay, classification banners shall be at the highest level of the layering order.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateImageSequence(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-45 Uncompressed image sequence content shall be encoded as per ISO/IEC 23001-17.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-46 Images in a sequence shall include timestamp information carried in a Collection Timestamp box");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateMetadata(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-47 Files compliant with NGA.STND.0078 shall carry a URI for each declared piece of media (item, track, and sample) in a file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-48 Files compliant with NGA.STND.0078 shall carry MISP compliant timestamps for each declared piece of media (item, track, and sample) in a file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateMetadataEncoding(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-49 NGA.STND.0078 compliant files shall utilize Key-Length-Value encoding for metadata stored internal to the file, as per SMPTE 336M and MISB ST 0107.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateSecurityMetadata(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-50 When files compliant with NGA.STND.0078 carry security/classification information, it shall be carried in a MIMD Defined Length Pack.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-51 When required by classification guidelines covering a specific file, individual items and tracks shall be labeled with classification labeling metadata, as per MISB ST 1902, in the MetaBox that addresses the specific item, presentation, or track.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-52 The file level classification labeling shall be at, or above, the highest level of classification for any other labeling within the file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateMetadataTimestamps(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-53 Each still image in a SIFF file shall have a reference from a MISB ST 1603 Nano Time Transfer Pack, which includes a 64-bit nanosecond precision absolute timestamp.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateMetadataIdentifiers(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-54 Each still image in a SIFF file shall have a reference from a MISB ST 1204 Core Identifier, which provides a universally unique ID for the image.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-55 Core ID metadata shall be referred to a still image item using the content describes (‘cdsc’) reference type.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-56 Once a Core ID is attached to an image, it shall never be modified or removed, unless the image is deleted.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-57 When a SIFF Image item is copied to another file, the CoreID shall be copied with it and maintain the same relationship to the image.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-58 When a SIFF image item is modified in any way, a new CoreID shall be generated for the new instance and form of the image item.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateGeneralisedMetadata(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-59 Generalized metadata referenced to a still image shall be encoded using an NTB/MISB approved KLV set or pack.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-60 Generalized metadata shall be referred to a still image item using the content describes (‘cdsc’) reference type.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateRegionAnnotation(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-61 Region item metadata shall include the union of metadata referenced to the parent image item and metadata referenced to the region item.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-62 Generalized metadata referred to a region item shall be encoded using an NTB/MISB defined KLV set or pack.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-63 Generalized metadata shall be referred to a region item using the content describes (‘cdsc’) reference type.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateImageSequenceMetadata(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-64 All image sequences shall include a KLV encoded CoreID, as per MISB ST 1204, via the Track MetaBox.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateTimedTrackMetadata(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-65 Metadata in a timed metadata track shall be sourced from a single KLV set or pack with a registered and unique KLV key.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-66 Timed metadata tracks shall utilize the URIMetaSampleEntryBox with a URI formatted as a URN in the format of: ‘urn:nsg:klv:ul:16-byte key’, with the 16-byte key following the QUADBYTE notation declared in RFC 5119.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-67 KLV metadata tracks shall contain data defined by and formatted according to NTB/MISB specification.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateFileGenerationAndEditing(List<Box> boxes) {
        System.out.println(
                "NGA.STND.0078_0.1-68 When deleting an item from a file, the media for the item shall also be removed (deleted, overwritten, etc.) from the file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-69 When deleting an item from a file, all associated references shall be updated/removed as appropriate.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-70 When deleting an item from a file, all associated groups shall be updated to remove the deleted item listing from the group.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-71 When deleting a track residing in a file, the media for the track shall also be removed (deleted, overwritten, etc.) from the file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-72 When deleting a track from a file, all associated references shall be updated/removed as appropriate.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-73 When deleting a track from a file, all associated groups shall be updated to remove the deleted track listing from the group.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
        System.out.println(
                "NGA.STND.0078_0.1-74 When deleting an image item from a file, all region items and associated references to the image shall be deleted from the file.");
        System.out.println(
                "\tTBD. This tool is not currently capable of checking this requirement.");
    }

    private static void validateUncompressedImageItem(List<Box> boxes) {
        Box maybeMetaBox = getTopLevelBoxByFourCC(boxes, "meta");
        if (maybeMetaBox == null) {
            System.out.println("\tFAIL.");
            System.out.println("\tRemaining meta box related requirements will be skipped.");
            return;
        }
        MetaBox metaBox = (MetaBox) maybeMetaBox;
        for (Box box : metaBox.getNestedBoxes()) {
            if (box instanceof ItemPropertiesBox iprp) {
                validateItemPropertiesBox(iprp);
            } else if (box instanceof HandlerBox hdlr) {
                // Not yet
            } else if (box instanceof PrimaryItemBox pitm) {
                // Not yet
            } else if (box instanceof ItemInfoBox iinf) {
                // Not yet
            } else if (box instanceof ItemLocationBox iloc) {
                // Not yet
            } else if (box instanceof ItemDataBox idat) {
                // Not yet
            } else if (box instanceof ItemReferenceBox iref) {
                // Not yet
            } else {
                System.out.println(
                        "TBD. metaBox validation is not yet implemented for this case: "
                                + box.getFullName());
            }
        }
    }

    private static void validateItemProperties(List<AbstractItemProperty> properties) {
        ComponentDefinitionBox cmpd = null;
        UncompressedFrameConfigBox uncC = null;
        ImageSpatialExtentsProperty ispe = null;
        ComponentPaletteBox cpal = null;
        SensorBadPixelsMapBox sbpm = null;
        UserDescriptionProperty udes = null;
        for (AbstractItemProperty property : properties) {
            if (property instanceof ComponentDefinitionBox componentDefinitionBox) {
                cmpd = componentDefinitionBox;
            } else if (property instanceof UncompressedFrameConfigBox uncompressedFrameConfigBox) {
                uncC = uncompressedFrameConfigBox;
            } else if (property
                    instanceof ImageSpatialExtentsProperty imageSpatialExtentsProperty) {
                ispe = imageSpatialExtentsProperty;
            } else if (property instanceof ComponentPaletteBox componentPaletteBox) {
                cpal = componentPaletteBox;
            } else if (property instanceof SensorBadPixelsMapBox sensorBadPixelsMapBox) {
                sbpm = sensorBadPixelsMapBox;
            } else if (property instanceof UserDescriptionProperty userDescriptionProperty) {
                udes = userDescriptionProperty;
            } else if (property instanceof CleanAperture clap) {
                // TODO
            } else {
                System.out.println("TBD. property: " + property.toString());
            }
        }
        if (cmpd == null) {
            System.out.println("\t\tFAIL. ComponentDefinitionBox is required (see 4.3)");
        }
        if (uncC == null) {
            System.out.println("\t\tFAIL. UncompressedFrameConfigBox is required (see 4.3)");
        }
        if (ispe == null) {
            System.out.println("\t\tFAIL. ImageSpatialExtentsProperty is required (see 4.3)");
        }
        checkUnccComponentsAreInRange(uncC.getComponents(), cmpd.getComponentDefinitions());
        checkAtMostOneFAComponent(uncC.getComponents(), cmpd.getComponentDefinitions());
        // TODO: check we have ComponentPatternDefinitionBox if FA is present.
        checkAtMostOnePaletteComponent(uncC.getComponents(), cmpd.getComponentDefinitions());
        checkComponentPaletteBoxIsPresentIfNeeded(
                uncC.getComponents(), cmpd.getComponentDefinitions(), cpal);
        checkComponentFormatIsValid(uncC.getComponents());
        checkComponentAlignSizeIsValid(uncC.getComponents());
        checkComponentAlignSizeIsConsistent(uncC);
        checkTileWidthIsConsistentWithFrameWidth(uncC, ispe);
        checkTileHeightIsConsistentWithFrameHeight(uncC, ispe);
        checkSamplingTypeIsValid(uncC);
        // TODO: sampling_type == 1 checks
        // TODO: sampling_type == 2 checks
        // TODO: sampling_type == 3 checks
        checkInterleaveTypeIsValid(uncC);
        // TODO: interleave consistency checks
        checkBlockingConsistency(uncC);
        checkBlockReversed(uncC);
        checkPixelSizeConsistency(uncC);
        checkTileAlignConsistency(uncC);
        checkProfile(uncC, cmpd);
        checkSensorBadPixelsMap(sbpm, cmpd);
    }

    private static void checkAllOtherFieldsAreZero(UncompressedFrameConfigBox uncC) {
        String message =
                String.format(
                        "\t\tFAIL. 5.3.2 requires all other fields to be zero for profile: %s",
                        uncC.getProfile().toString());
        for (Component component : uncC.getComponents()) {
            if (component.getComponentFormat() == ComponentFormat.UnsignedInteger) {
                System.out.println(message);
            }
            if (component.getComponentAlignSize() == 0) {
                System.out.println(message);
            }
        }
        if (uncC.getBlockSize() == 0) {
            System.out.println(message);
        }
        if (uncC.isComponentLittleEndian()) {
            System.out.println(message);
        }
        if (uncC.isBlockPadLSB()) {
            System.out.println(message);
        }
        if (uncC.isBlockLittleEndian()) {
            System.out.println(message);
        }
        if (uncC.isBlockReversed()) {
            System.out.println(message);
        }
        if (uncC.isPadUnknown()) {
            System.out.println(message);
        }
        if (uncC.getPixelSize() == 0) {
            System.out.println(message);
        }
        if (uncC.getRowAlignSize() == 0) {
            System.out.println(message);
        }
        if (uncC.getTileAlignSize() == 0) {
            System.out.println(message);
        }
        if (uncC.getNumTileRowsMinusOne() == 0) {
            System.out.println(message);
        }
        if (uncC.getNumTileColumnsMinusOne() == 0) {
            System.out.println(message);
        }
    }

    private static void checkAtMostOneFAComponent(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        int numberOfFA = getNumberOfFAComponents(components, componentDefinitions);
        if (numberOfFA > 1) {
            System.out.println(
                    "\t\tFAIL. 5.2.1.2 'There shall be at most one component with type 11 (FA) present in the component list'");
        }
    }

    private static void checkAtMostOnePaletteComponent(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        int numberOfP = getNumberOfPaletteComponents(components, componentDefinitions);
        if (numberOfP > 1) {
            System.out.println(
                    "\t\tFAIL. 5.2.1.2 'There shall be at most one component with type 10 (P) present in the component list'");
        }
    }

    private static void checkBlockReversed(UncompressedFrameConfigBox uncC) {
        if (!uncC.isBlockLittleEndian()) {
            if (uncC.isBlockReversed()) {
                System.out.println(
                        "\t\tFAIL. 5.2.1.7 'block_reversed shall be 0 if block_little_endian is 0");
            }
        }
    }

    private static void checkBlockingConsistency(UncompressedFrameConfigBox uncC) {
        if (uncC.getBlockSize() == 0) {
            if (uncC.isBlockPadLSB()) {
                System.out.println(
                        "\t\tFAIL. 5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            }
            if (uncC.isBlockLittleEndian()) {
                System.out.println(
                        "\t\tFAIL. 5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            }
            if (uncC.isBlockReversed()) {
                System.out.println(
                        "\t\tFAIL. 5.2.1.7 'If the block size is 0 (blocking is not used within the sample data), block_pad_lsb, block_little_endian and block_reversed shall all be 0'");
            }
        } else {
            // TODO: see what is possible.
        }
    }

    private static int getNumberOfFAComponents(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        return getNumberOfComponentsWithType(components, componentDefinitions, 11);
    }

    private static int getNumberOfPaletteComponents(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        return getNumberOfComponentsWithType(components, componentDefinitions, 10);
    }

    private static int getNumberOfComponentsWithType(
            List<Component> components,
            List<ComponentDefinition> componentDefinitions,
            int componentType) {
        int count = 0;
        for (Component component : components) {
            if (componentDefinitions.get(component.getComponentIndex()).getComponentType()
                    == componentType) {
                count += 1;
            }
        }
        return count;
    }

    private static void checkComponentAlignSizeIsConsistent(UncompressedFrameConfigBox uncC) {
        if (uncC.isComponentLittleEndian()) {
            if (uncC.isBlockLittleEndian()) {
                System.out.println(
                        "\t\tFAIL.  5.2.1.3 ' If components_little_endian is 1, block_little_endian shall be 0 and component_align_size of each component shall be different from 0.'");
            }
            for (Component component : uncC.getComponents()) {
                if (component.getComponentAlignSize() != 0) {
                    System.out.println(
                            "\t\tFAIL.  5.2.1.3 ' If components_little_endian is 1, block_little_endian shall be 0 and component_align_size of each component shall be different from 0.'");
                }
            }
        }
    }

    private static void checkComponentAlignSizeIsValid(List<Component> components) {
        for (Component component : components) {
            int component_bit_depth = component.getComponentBitDepthMinusOne() + 1;
            if (component.getComponentAlignSize() != 0) {
                if (component.getComponentAlignSize() * 8 > component_bit_depth) {
                    System.out.println(
                            "\t\tFAIL. 5.2.1.3 ' component_align_size shall be either 0 or such that component_align_size*8 is greater than component_bit_depth'");
                }
            }
        }
    }

    private static void checkComponentFormatIsValid(List<Component> components) {
        for (Component component : components) {
            if (component.getComponentFormat() == ComponentFormat.Reserved) {
                System.out.println(
                        "\t\tFAIL. Table 2. component_format field is outside of defined range");
            }
        }
    }

    private static void checkComponentPaletteBoxIsPresentIfNeeded(
            List<Component> components,
            List<ComponentDefinition> componentDefinitions,
            ComponentPaletteBox cpal) {
        int numberOfP = getNumberOfPaletteComponents(components, componentDefinitions);
        if ((numberOfP > 0) && (cpal == null)) {
            System.out.println(
                    "\t\tFAIL. 6.1.2.1 'This box shall be present if and only if there is an associated ComponentDefinitionBox present, in which there is a component defined with a component_type of 10 ('P')'");
        }
        if ((numberOfP == 0) && (cpal != null)) {
            System.out.println(
                    "\t\tFAIL. 6.1.2.1 'This box shall be present if and only if there is an associated ComponentDefinitionBox present, in which there is a component defined with a component_type of 10 ('P')'");
        }
    }

    private static void checkInterleaveTypeIsValid(UncompressedFrameConfigBox uncC) {
        if (uncC.getInterleaveType().equals(Interleaving.Reserved)) {
            System.out.println(
                    "\t\tFAIL. Table 4. interleave_type field is outside of defined range");
        }
    }

    private static void checkPixelSizeConsistency(UncompressedFrameConfigBox uncC) {
        if (uncC.getPixelSize() > 0) {
            // TODO: check pixel_size is large enough
        }
        if ((!uncC.getInterleaveType().equals(Interleaving.Pixel))
                && (!uncC.getInterleaveType().equals(Interleaving.MultiY))) {
            if (uncC.getPixelSize() == 0) {
                System.out.print("\t\tPASS. ");
            } else {
                System.out.print("\t\tFAIL. ");
            }
            System.out.println(
                    "5.2.1.7 'pixel_size shall be 0 if interleave_type is different to 1 or 5'");
        }
    }

    private static void checkProfile(UncompressedFrameConfigBox uncC, ComponentDefinitionBox cmpd) {
        if ((uncC.getProfile().equals(new FourCC("gene"))) || (uncC.getProfile().hashCode() == 0)) {
            return;
        }
        List<ComponentDefinition> componentDefinitions = cmpd.getComponentDefinitions();
        List<Component> components = uncC.getComponents();
        switch (uncC.getProfile().toString()) {
            case "2vuy":
                if (components.size() == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (components.get(3).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (uncC.getSamplingType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                if (uncC.getInterleaveType().getEncodedValue() == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 2vuy requires [{2,7},{1,7},{3,7},{1,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "yuv2":
                if (components.size() == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (components.get(3).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (uncC.getSamplingType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                if (uncC.getInterleaveType().getEncodedValue() == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yuv2 requires [{1,7},{2,7},{1,7},{3,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "yvyu":
                if (components.size() == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (components.get(3).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (uncC.getSamplingType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (uncC.getInterleaveType().getEncodedValue() == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "vyuy":
                if (components.size() == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 yvyu requires [{1,7},{3,7},{1,7},{2,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (components.get(3).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (uncC.getSamplingType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                if (uncC.getInterleaveType().getEncodedValue() == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 vyuy requires [{3,7},{1,7},{2,7},{1,7}], 1, 5");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "v308":
                if (components.size() == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (uncC.getSamplingType().getEncodedValue() == 0) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                if (uncC.getInterleaveType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 v308 requires [{3,7},{1,7},{2,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "rgb3":
                if (components.size() == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 6) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (uncC.getSamplingType().getEncodedValue() == 0) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                if (uncC.getInterleaveType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgb3 requires [{4,7},{5,7},{6,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "rgba":
                if (components.size() == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 6) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType()
                        == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (components.get(3).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (uncC.getSamplingType().getEncodedValue() == 0) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                if (uncC.getInterleaveType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 rgba requires [{4,7},{5,7},{6,7},{7,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "i420":
                if (components.size() == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (uncC.getSamplingType().getEncodedValue() == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                if (uncC.getInterleaveType().getEncodedValue() == 0) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 i420 requires [{1,7},{2,7},{3,7}], 2, 0");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "nv12":
                if (components.size() == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (uncC.getSamplingType().getEncodedValue() == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                if (uncC.getInterleaveType().getEncodedValue() == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv12 requires [{1,7},{2,7},{3,7}], 2, 2");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "nv21":
                if (components.size() == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 3) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (uncC.getSamplingType().getEncodedValue() == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                if (uncC.getInterleaveType().getEncodedValue() == 2) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 nv21 requires [{1,7},{3,7},{2,7}], 2, 2");
                checkAllOtherFieldsAreZero(uncC);
                break;
            case "abgr":
                if (components.size() == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(0).getComponentIndex())
                                .getComponentType()
                        == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (components.get(0).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(1).getComponentIndex())
                                .getComponentType()
                        == 6) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (components.get(1).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(2).getComponentIndex())
                                .getComponentType()
                        == 5) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (components.get(2).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (componentDefinitions
                                .get(components.get(3).getComponentIndex())
                                .getComponentType()
                        == 4) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (components.get(3).getComponentBitDepthMinusOne() == 7) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (uncC.getSamplingType().getEncodedValue() == 0) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                if (uncC.getInterleaveType().getEncodedValue() == 1) {
                    System.out.print("\t\tPASS. ");
                } else {
                    System.out.print("\t\tFAIL. ");
                }
                System.out.println("5.3.2 Table 5 abgr requires [{7,7},{6,7},{5,7},{4,7}], 0, 1");
                checkAllOtherFieldsAreZero(uncC);
                break;
            default:
                System.out.println(
                        "\t\tFAIL. unhandled profile case: " + uncC.getProfile().toString());
        }
    }

    private static void checkSamplingTypeIsValid(UncompressedFrameConfigBox uncC) {
        if (uncC.getSamplingType().equals(SamplingType.Reserved)) {
            System.out.println(
                    "\t\tFAIL. Table 3. sampling_type field is outside of defined range");
        }
    }

    private static void checkSensorBadPixelsMap(
            SensorBadPixelsMapBox sbpm, ComponentDefinitionBox cmpd) {
        // TODO: implement checks
    }

    private static void checkTileAlignConsistency(UncompressedFrameConfigBox uncC) {
        if ((uncC.getNumTileColumnsMinusOne() == 0) && (uncC.getNumTileRowsMinusOne() == 0)) {
            if (uncC.getTileAlignSize() == 0) {
                System.out.print("\t\tPASS. ");
            } else {
                System.out.print("\t\tFAIL. ");
            }
            System.out.println("5.2.1.7 'tile_align_size shall be 0 if a single tile is used'");
        }
    }

    private static void checkTileHeightIsConsistentWithFrameHeight(
            UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        if (ispe.getImageHeight() % (uncC.getNumTileRowsMinusOne() + 1) == 0) {
            System.out.print("\t\tPASS. ");
        } else {
            System.out.print("\t\tFAIL. ");
        }
        System.out.println(
                "5.2.1.4 'The frame height shall be a multiple of num_tile_rows_minus_one+1");
    }

    private static void checkTileWidthIsConsistentWithFrameWidth(
            UncompressedFrameConfigBox uncC, ImageSpatialExtentsProperty ispe) {
        if (ispe.getImageWidth() % (uncC.getNumTileColumnsMinusOne() + 1) == 0) {
            System.out.print("\t\tPASS. ");
        } else {
            System.out.print("\t\tFAIL. ");
        }
        System.out.println(
                "5.2.1.4 'The frame width shall be a multiple of num_tile_cols_minus_one+1");
    }

    private static void checkUnccComponentsAreInRange(
            List<Component> components, List<ComponentDefinition> componentDefinitions) {
        for (Component component : components) {
            if (component.getComponentIndex() < componentDefinitions.size()) {
                System.out.print("\t\tPASS. ");
            } else {
                System.out.print("\t\tFAIL. ");
            }
            System.out.println(
                    "5.1.1. 'The component_index field shall be strictly less than the component_count field value of the associated ComponentDefinitionBox'");
        }
    }

    private static void validateItemPropertiesBox(ItemPropertiesBox iprp) {
        ItemPropertyContainerBox ipco = iprp.getItemProperties();
        validateItemProperties(ipco.getProperties());
    }
}
