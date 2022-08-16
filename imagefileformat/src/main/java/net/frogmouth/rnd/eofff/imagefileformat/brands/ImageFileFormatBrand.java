package net.frogmouth.rnd.eofff.imagefileformat.brands;

import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;

/**
 * File type brands for Image File Format.
 *
 * <p>These are used in the File Type box ({@code ftyp}).
 *
 * <p>There are additional brands ({@code mif2} and {@code pred}) that are on the MP4RA site. Those
 * appear to be from an unreleased version of HEIF.
 */
public class ImageFileFormatBrand {

    /**
     * AVCI Image File Format Brand.
     *
     * <p>This is {@code avci}, indicating an AVC (Constrained High Profile) image.
     *
     * <p>See ISO/IEC 23008-12:2017 Section E.4.1 for interpretation.
     */
    public static final Brand AVCI = new Brand("avci");

    /**
     * AVCS Sequential Image File Format Brand.
     *
     * <p>This is {@code avcs}, indicating an AVC (Progressive High Profile) image.
     *
     * <p>See ISO/IEC 23008-12:2017 Section E.4.2 for interpretation.
     */
    public static final Brand AVCS = new Brand("avcs");

    /**
     * HEIC Codec Brand.
     *
     * <p>This is {@code heic}, indicating a HEVC file. This is a codec brand.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4 for interpretation.
     */
    public static final Brand HEIC = new Brand("heic");

    /**
     * HEIX Codec Brand.
     *
     * <p>This is {@code heix}, indicating a HEVC extended (Main 10 or format range extension) file.
     * This is a codec brand.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4 for interpretation.
     */
    public static final Brand HEIX = new Brand("heix");

    /**
     * HEVC Image Sequence Brand.
     *
     * <p>This is {@code hevc}, indicating a HEVC file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4.2 for interpretation.
     */
    public static final Brand HEVC = new Brand("hevc");

    /**
     * HEVC Extended Image Sequence Brand.
     *
     * <p>This is {@code hevx}, indicating a HEVC (Main 10 or format range extension) file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4.2 for interpretation.
     */
    public static final Brand HEVX = new Brand("hevx");

    /**
     * L-HEVC Image Collection Brand.
     *
     * <p>This is {@code heim}, indicating a HEVC (Main or Multiview Main) file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4.3 for interpretation.
     */
    public static final Brand HEIM = new Brand("heim");

    /**
     * L-HEVC Extended Image Collection Brand.
     *
     * <p>This is {@code heis}, indicating a HEVC (Main, Main 10, Scalable Main or Scalable Main 10)
     * file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4.3 for interpretation.
     */
    public static final Brand HEIS = new Brand("heis");

    /**
     * L-HEVC Image Sequence Brand.
     *
     * <p>This is {@code hevm}, indicating a HEVC (Main or Multiview Main) image sequence file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4.4 for interpretation.
     */
    public static final Brand HEVM = new Brand("hevm");

    /**
     * L-HEVC Extended Image Collection Brand.
     *
     * <p>This is {@code hevs}, indicating a HEVC (Main, Main 10, Scalable Main or Scalable Main 10)
     * image sequence file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section B.4.4 for interpretation.
     */
    public static final Brand HEVS = new Brand("hevs");

    /**
     * JPEG Image File Format Brand.
     *
     * <p>This is {@code jpeg}, indicating an JPEG image.
     *
     * <p>See ISO/IEC 23008-12:2017 Section H.4 for interpretation.
     */
    public static final Brand JPEG = new Brand("jpeg");

    /**
     * JPEG Sequential Image File Format Brand.
     *
     * <p>This is {@code jpgs}, indicating a JPEG sequence (motion JPEG) file.
     *
     * <p>See ISO/IEC 23008-12:2017 Section H.5 for interpretation.
     */
    public static final Brand JPGS = new Brand("jpgs");

    /**
     * MIF1 Image File Format Brand.
     *
     * <p>This is {@code mif1}, indicating an Image File Format image. This is a structural brand.
     *
     * <p>See ISO/IEC 23008-12:2017 Section 10.2.1 for interpretation.
     */
    public static final Brand MIF1 = new Brand("mif1");

    /**
     * MSF1 Sequential Image File Format Brand.
     *
     * <p>This is {@code msf1}, indicating an Image File Format sequential image. This is a
     * structural brand.
     *
     * <p>See ISO/IEC 23008-12:2017 Section 10.3.1 for interpretation.
     */
    public static final Brand MSF1 = new Brand("msf1");
}
