package net.frogmouth.rnd.eofff.imagefileformat.mini;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Minimised Image Box.
 *
 * <p>The mimimised image box format provides a more compact representation of the MetaBox for a
 * subset of use cases. It is meant to be used for small and simple files where the full MetaBox
 * would result in considerable overhead compared to the image data payload.
 *
 * <p>See ISO/IEC 23008-12:2024 CDAM 2:2024.
 */
public class MinimizedImageBox extends BaseBox {

    public static final FourCC MINI_ATOM = new FourCC("mini");

    public enum ChromaSubsampling {
        Unknown(-1),
        Monochrome(0),
        HorizontalAndVertical(1),
        Horizontal(2),
        None(3);

        private ChromaSubsampling(int val) {
            this.val = val;
        }

        private final int val;

        public int getValue() {
            return val;
        }

        public static ChromaSubsampling lookupEntry(int v) {
            for (var entry : ChromaSubsampling.values()) {
                if (entry.getValue() == v) {
                    return entry;
                }
            }
            return Unknown;
        }
    }

    private int version;
    private boolean explicitCodecTypesFlag;
    private boolean floatFlag;
    private boolean fullRangeFlag;
    private boolean alphaFlag;
    private boolean explicitCICPFlag;
    private boolean hdrFlag;
    private boolean gainmapFlag = false;
    private boolean iccFlag;
    private boolean exifFlag;
    private boolean xmpFlag;
    private ChromaSubsampling chromaSubsampling; // enum?
    private int orientationMinus1; // enum?
    private int width;
    private int height;
    private boolean chromaIsHorizontallyCentered = false;
    private boolean chromaIsVerticallyCentered = false;
    private int bitDepth;
    private boolean alphaIsPremultiplied = false;
    // TODO: CICP properties
    private FourCC infeType;
    private int codecConfigType; // FourCC?
    // TOOD: HDR flag
    private boolean tmapIccFlag = false;

    private byte[] alphaItemCodecConfig;
    private byte[] gainmapItemCodecConfig;
    private byte[] mainItemCodecConfig;
    private byte[] iccData;
    private byte[] tmapIccData;
    private byte[] gainmapMetadata;
    private byte[] alphaItemData;
    private byte[] gainmapItemData;
    private byte[] mainItemData;

    // TODO: exif data
    // TODO: xmp data;

    public MinimizedImageBox() {
        super(MINI_ATOM);
    }

    @Override
    public String getFullName() {
        return "MinimizedImageBox";
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isExplicitCodecTypesFlag() {
        return explicitCodecTypesFlag;
    }

    public void setExplicitCodecTypesFlag(boolean explicitCodecTypesFlag) {
        this.explicitCodecTypesFlag = explicitCodecTypesFlag;
    }

    public boolean isFloatFlag() {
        return floatFlag;
    }

    public void setFloatFlag(boolean floatFlag) {
        this.floatFlag = floatFlag;
    }

    public boolean isFullRangeFlag() {
        return fullRangeFlag;
    }

    public void setFullRangeFlag(boolean fullRangeFlag) {
        this.fullRangeFlag = fullRangeFlag;
    }

    public boolean isAlphaFlag() {
        return alphaFlag;
    }

    public void setAlphaFlag(boolean alphaFlag) {
        this.alphaFlag = alphaFlag;
    }

    public boolean isExplicitCICPFlag() {
        return explicitCICPFlag;
    }

    public void setExplicitCICPFlag(boolean explicitCICPFlag) {
        this.explicitCICPFlag = explicitCICPFlag;
    }

    public boolean isHdrFlag() {
        return hdrFlag;
    }

    public void setHdrFlag(boolean hdrFlag) {
        this.hdrFlag = hdrFlag;
    }

    public boolean isIccFlag() {
        return iccFlag;
    }

    public void setIccFlag(boolean iccFlag) {
        this.iccFlag = iccFlag;
    }

    public boolean isExifFlag() {
        return exifFlag;
    }

    public void setExifFlag(boolean exifFlag) {
        this.exifFlag = exifFlag;
    }

    public boolean isXmpFlag() {
        return xmpFlag;
    }

    public void setXmpFlag(boolean xmpFlag) {
        this.xmpFlag = xmpFlag;
    }

    public ChromaSubsampling getChromaSubsampling() {
        return chromaSubsampling;
    }

    public void setChromaSubsampling(ChromaSubsampling chromaSubsampling) {
        this.chromaSubsampling = chromaSubsampling;
    }

    public int getOrientationMinus1() {
        return orientationMinus1;
    }

    public void setOrientationMinus1(int orientationMinus1) {
        this.orientationMinus1 = orientationMinus1;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isChromaIsHorizontallyCentered() {
        return chromaIsHorizontallyCentered;
    }

    public void setChromaIsHorizontallyCentered(boolean chromaIsHorizontallyCentered) {
        this.chromaIsHorizontallyCentered = chromaIsHorizontallyCentered;
    }

    public boolean isChromaIsVerticallyCentered() {
        return chromaIsVerticallyCentered;
    }

    public void setChromaIsVerticallyCentered(boolean chromaIsVerticallyCentered) {
        this.chromaIsVerticallyCentered = chromaIsVerticallyCentered;
    }

    public int getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public boolean isAlphaIsPremultiplied() {
        return alphaIsPremultiplied;
    }

    public void setAlphaIsPremultiplied(boolean alphaIsPremultiplied) {
        this.alphaIsPremultiplied = alphaIsPremultiplied;
    }

    public FourCC getInfeType() {
        return infeType;
    }

    public void setInfeType(FourCC infeType) {
        this.infeType = infeType;
    }

    public int getCodecConfigType() {
        return codecConfigType;
    }

    public void setCodecConfigType(int codecConfigType) {
        this.codecConfigType = codecConfigType;
    }

    public byte[] getMainItemCodecConfig() {
        return mainItemCodecConfig;
    }

    public void setMainItemCodecConfig(byte[] mainItemCodecConfig) {
        this.mainItemCodecConfig = mainItemCodecConfig;
    }

    public byte[] getMainItemData() {
        return mainItemData;
    }

    public void setMainItemData(byte[] mainItemData) {
        this.mainItemData = mainItemData;
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBoxHeader(writer);
        writeBody(writer);
    }

    protected void writeBody(OutputStreamWriter writer) throws IOException {
        writer.writeBits(version, 2);
        writer.writeBit(explicitCodecTypesFlag);
        writer.writeBit(floatFlag);
        writer.writeBit(fullRangeFlag);
        writer.writeBit(alphaFlag);
        writer.writeBit(explicitCICPFlag);
        writer.writeBit(hdrFlag);
        writer.writeBit(iccFlag);
        writer.writeBit(exifFlag);
        writer.writeBit(xmpFlag);
        writer.writeBits(chromaSubsampling.val, 2);
        writer.writeBits(orientationMinus1, 3);

        int width_minus1 = width - 1;
        int height_minus1 = height - 1;
        boolean small_dimensions_flag = (width_minus1 < 128) && (height_minus1 < 128);
        writer.writeBit(small_dimensions_flag);
        writer.writeBits(width_minus1, small_dimensions_flag ? 7 : 15);
        writer.writeBits(height_minus1, small_dimensions_flag ? 7 : 15);

        if ((chromaSubsampling == ChromaSubsampling.HorizontalAndVertical)
                || (chromaSubsampling == ChromaSubsampling.Horizontal)) {
            writer.writeBit(chromaIsHorizontallyCentered);
        }
        if (chromaSubsampling == ChromaSubsampling.HorizontalAndVertical) {
            writer.writeBit(chromaIsVerticallyCentered);
        }

        if (floatFlag) {
            throw new UnsupportedOperationException("TODO - bit depth for float");
        } else {
            boolean highBitDepthFlag = bitDepth > 8;
            writer.writeBit(highBitDepthFlag);
            if (highBitDepthFlag) {
                writer.writeBits(bitDepth - 9, 3);
            }
        }
        if (alphaFlag) {
            writer.writeBit(alphaIsPremultiplied);
        }
        if (explicitCICPFlag) {
            throw new UnsupportedOperationException("TODO - explicit CICP write");
        }
        if (explicitCodecTypesFlag) {
            writer.writeBits((int) infeType.asUnsigned(), 32);
            writer.writeBits(codecConfigType, 32);
        }
        if (hdrFlag) {
            throw new UnsupportedOperationException("TODO - big HDR block");
        }

        // chunk sizes
        if (iccFlag || exifFlag || xmpFlag || (hdrFlag && gainmapFlag)) {
            throw new UnsupportedOperationException("TODO - few metadata bytes flag");
        }
        boolean few_item_codec_config_bytes = true;
        if (mainItemCodecConfig.length > 7) {
            few_item_codec_config_bytes = false;
        }
        // todo; check everything else that depends on this flag
        writer.writeBit(few_item_codec_config_bytes);
        boolean few_item_data_bytes_flag = true;
        if (mainItemData.length - 1 > 32767) {
            few_item_data_bytes_flag = false;
        }
        // todo; check everything else that depends on this flag
        writer.writeBit(few_item_data_bytes_flag);

        if (iccFlag) {
            throw new UnsupportedOperationException("TODO - icc data size");
        }
        if (hdrFlag && gainmapFlag && tmapIccFlag) {
            throw new UnsupportedOperationException("TODO - tmap icc data size");
        }
        if (hdrFlag && gainmapFlag) {
            throw new UnsupportedOperationException("TODO - gainmap metadata size");
        }
        if (hdrFlag && gainmapFlag) {
            throw new UnsupportedOperationException("TODO - gainmap item data size");
        }
        if (hdrFlag && gainmapFlag && (gainmapItemData.length > 0)) {
            throw new UnsupportedOperationException("TODO - gainmap item codec config size");
        }
        writer.writeBits(mainItemCodecConfig.length, few_item_codec_config_bytes ? 3 : 12);
        writer.writeBits(mainItemData.length - 1, few_item_data_bytes_flag ? 15 : 28);

        if (alphaFlag) {
            throw new UnsupportedOperationException("TODO - alpha item data size");
        }
        if (alphaFlag && alphaItemData.length > 0) {
            throw new UnsupportedOperationException("TODO - alpha item codec config size");
        }
        if (exifFlag) {
            throw new UnsupportedOperationException("TODO - exif data size");
        }
        if (xmpFlag) {
            throw new UnsupportedOperationException("TODO - XMP data size");
        }
        writer.alignToByteBoundary();
        // chunks
        if (alphaFlag && (alphaItemData.length > 0) && (alphaItemCodecConfig.length > 0)) {
            writer.write(alphaItemCodecConfig);
        }
        if (hdrFlag && gainmapFlag && (gainmapItemCodecConfig.length > 0)) {
            writer.write(gainmapItemCodecConfig);
        }
        if (mainItemCodecConfig.length > 0) {
            writer.write(mainItemCodecConfig);
        }
        if (iccFlag) {
            writer.write(iccData);
        }
        if (hdrFlag && gainmapFlag && tmapIccFlag) {
            writer.write(tmapIccData);
        }
        if (hdrFlag && gainmapFlag && (gainmapMetadata.length > 0)) {
            writer.write(gainmapMetadata);
        }
        if (alphaFlag && (alphaItemData.length > 0)) {
            writer.write(alphaItemData);
        }
        if (hdrFlag && gainmapFlag && (gainmapItemData.length > 0)) {
            writer.write(gainmapItemData);
        }
        writer.write(mainItemData);

        if (exifFlag) {
            throw new UnsupportedOperationException("TODO - exif data");
        }
        if (xmpFlag) {
            throw new UnsupportedOperationException("TODO - xmp data");
        }
    }

    @Override
    public long getBodySize() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStreamWriter streamWriter = new OutputStreamWriter(baos);
        try {
            writeBody(streamWriter);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return baos.size();
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        // TODO
        return sb.toString();
    }
}
