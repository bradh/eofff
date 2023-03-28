package net.frogmouth.rnd.eofff.tools.validator;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FileParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeBox;
import net.frogmouth.rnd.eofff.isobmff.hdlr.HandlerBox;
import net.frogmouth.rnd.eofff.isobmff.iinf.ItemInfoBox;
import net.frogmouth.rnd.eofff.isobmff.infe.ItemInfoEntry;
import net.frogmouth.rnd.eofff.isobmff.iref.ItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.iref.SingleItemReferenceBox;
import net.frogmouth.rnd.eofff.isobmff.meta.MetaBox;
import net.frogmouth.rnd.eofff.isobmff.pitm.PrimaryItemBox;

public class Validator {

    /** @param args the command line arguments */
    public static void main(String[] args) throws IOException {
        /*
        if (args.length < 1) {
            System.out.println("Need to specify the file to validate...");
            System.exit(0);
        }
        validate(args[0]);
        */
        validate("/home/bradh/coding/eofff-refactor/eofff/tools/test_siff_rgb_component_rgan.mp4");
    }

    private static void validate(String filename) throws IOException {
        List<Box> boxes = new ArrayList<>();
        boxes.addAll(parseFile(filename));
        for (Box box : boxes) {
            System.out.println(box);
        }
        validateFileBox(boxes);
        System.out.println(
                "This tool is not yet capable of checking requirements -10 through -12.");
        validateMetaBox(boxes);
        validateCoreId(boxes);
        validateTimestamps(boxes);
        // TODO: validate all the other requirements

        /*
                ImageSpatialExtentsProperty ispe = null;
                UncompressedFrameConfigBox uncC = null;
                ComponentDefinitionBox cmpd = null;
                ComponentPaletteBox cpal = null;
                for (AbstractItemProperty prop : properties) {
                    if (prop instanceof ImageSpatialExtentsProperty box) {
                        ispe = box;
                    }
                    if (prop instanceof UncompressedFrameConfigFileTypeBox ftyp) {
                        uncC = box;
                    }
                    if (prop instanceof ComponentDefinitionFileTypeBox ftyp) {
                        cmpd = box;
                    }
                    if (prop instanceof ComponentPaletteFileTypeBox ftyp) {
                        cpal = box;
                    }
                }

                if ((ispe != null) && (uncC != null) && (cmpd != null)) {
                    FourCC profile = uncC.getProfile();
                    byte[] data = getData(boxes, primaryItemId);
                    ByteOrder blockEndian =
                            uncC.isBlockLittleEndian() ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;

                    if (profile.equals(new FourCC("rgb3"))
                            || profile.equals(new FourCC("rgba"))
                            || profile.equals(new FourCC("abgr"))) {
                        SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                        ColorModel colourModel = getColourModelRgb(uncC, cmpd);
                        BufferedImage target =
                                buildBufferedImage(data, sampleModel, colourModel, blockEndian);
                        writeOutput(outputPath, target);
                    } else if (profile.equals(new FourCC("gene"))
                            || profile.equals(new FourCC("2vuy"))
                            || profile.equals(new FourCC("yuv2"))
                            || profile.equals(new FourCC("yvyu"))
                            || profile.equals(new FourCC("vyuy"))
                            || profile.equals(new FourCC("v308"))
                            || profile.equals(new FourCC("i420"))
                            || profile.equals(new FourCC("nv12"))
                            || profile.equals(new FourCC("nv21"))) {
                        if (isRGB(uncC, cmpd)) {
                            // we need to check more cases
                            SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                            ColorModel colourModel = getColourModel(uncC, cmpd);
                            if ((sampleModel != null) && (colourModel != null)) {
                                switch (uncC.getInterleaveType()) {
                                    case Component:
                                        {
                                            BufferedImage target =
                                                    buildBufferedImageBanded(
                                                            data, sampleModel, colourModel);
                                            writeOutput(outputPath, target);
                                            break;
                                        }
                                    case Pixel:
                                        {
                                            BufferedImage target =
                                                    buildBufferedImage(
                                                            data, sampleModel, colourModel, blockEndian);
                                            writeOutput(outputPath, target);
                                            break;
                                        }
                                    default:
                                        fail("unsupported interleave type");
                                        break;
                                }
                            }
                        } else if (isYCbCr(uncC, cmpd)) {
                            OutputFormat outputFormat =
                                    new OutputFormat_BGR_Bytes(
                                            (int) (ispe.getImageHeight() * ispe.getImageWidth()));
                            byte[] rgbData;
                            if (profile.equals(new FourCC("2vuy"))) {
                                YUVConverter converter =
                                        new YUV420Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                SourceFormat.TwoYUV);
                                rgbData = converter.convert(data, outputFormat);
                            } else if (profile.equals(new FourCC("yuv2"))) {
                                YUVConverter converter =
                                        new YUV420Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                SourceFormat.YUV2);
                                rgbData = converter.convert(data, outputFormat);
                            } else if (profile.equals(new FourCC("yvyu"))) {
                                YUVConverter converter =
                                        new YUV420Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                SourceFormat.YVYU);
                                rgbData = converter.convert(data, outputFormat);
                            } else if (profile.equals(new FourCC("vyuy"))) {
                                YUVConverter converter =
                                        new YUV420Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                SourceFormat.VYUY);
                                rgbData = converter.convert(data, outputFormat);
                            } else if (profile.equals(new FourCC("v308"))) {
                                rgbData =
                                        ColourSpaceConverter.V308Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                data,
                                                outputFormat);
                            } else if (profile.equals(new FourCC("nv12"))) {
                                rgbData =
                                        ColourSpaceConverter.NV12Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                data,
                                                outputFormat);
                            } else if (profile.equals(new FourCC("nv21"))) {
                                rgbData =
                                        ColourSpaceConverter.NV21Converter(
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                data,
                                                outputFormat);
                            } else {
                                ColourSpace colourSpace = ColourSpace.YUV444;
                                if (uncC.getSamplingType().equals(SamplingType.YCbCr420)) {
                                    colourSpace = ColourSpace.YUV420;
                                } else if (uncC.getSamplingType().equals(SamplingType.YCbCr422)) {
                                    colourSpace = ColourSpace.YUV422;
                                }
                                rgbData =
                                        ColourSpaceConverter.YuvConverter(
                                                colourSpace,
                                                (int) ispe.getImageHeight(),
                                                (int) ispe.getImageWidth(),
                                                data,
                                                outputFormat);
                            }
                            BufferedImage target =
                                    new BufferedImage(
                                            (int) ispe.getImageWidth(),
                                            (int) ispe.getImageHeight(),
                                            BufferedImage.TYPE_3BYTE_BGR);
                            byte[] imgData =
                                    ((DataBufferByte) target.getRaster().getDataBuffer()).getData();
                            System.arraycopy(rgbData, 0, imgData, 0, rgbData.length);
                            writeOutput(outputPath, target);
                        } else if (isPalette(uncC, cmpd, cpal)) {
                            // we need to check more cases
                            SampleModel sampleModel = getSampleModel(uncC, cmpd, ispe);
                            ColorModel colourModel = getIndexColourModel(cpal);
                            if ((sampleModel != null) && (colourModel != null)) {

                                BufferedImage target =
                                        buildBufferedImage(data, sampleModel, colourModel, blockEndian);
                                writeOutput(outputPath, target);
                            }

                        } else {
                            fail("unsupported component combination");
                        }
                    } else {
                        fail("unsupported profile: " + profile.toString());
                    }
                } else {
                    fail("missing ispe, uncC or cmpd");
                }
            }

                private static Box findChildBox(MetaBox parent, FourCC fourCC) {
                if (parent == null) {
                    return null;
                }
                for (FileTypeBox ftyp : parent.getNestedBoxes()) {
                    if (box.getFourCC().equals(fourCC)) {
                        return box;
                    }
                }
                return null;
        */
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
        System.out.println(
                "NGA.STND.0078_0.1-02 SIFF files compliant with this version of the NGA.STND.0078 profile shall include the ‘ns01’ brand in the compatible brands list inside the FileTypeBox.");
        if (ftyp.getCompatibleBrands().contains(new Brand("ns01"))) {
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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void validate_23(MetaBox meta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void validate_24(MetaBox meta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void validate_25(MetaBox meta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void validate_26(MetaBox meta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void validate_27(MetaBox meta) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
        // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
