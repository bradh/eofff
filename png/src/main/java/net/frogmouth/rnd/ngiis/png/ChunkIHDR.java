package net.frogmouth.rnd.ngiis.png;

public class ChunkIHDR extends PngChunk {
    private int width;
    private int height;
    private int bitDepth;
    private int colorType;
    private int compressionMethod;
    private int filterMethod;
    private int interlaceMethod;

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

    public int getBitDepth() {
        return bitDepth;
    }

    public void setBitDepth(int bitDepth) {
        this.bitDepth = bitDepth;
    }

    public int getColorType() {
        return colorType;
    }

    public void setColorType(int colorType) {
        this.colorType = colorType;
    }

    public int getCompressionMethod() {
        return compressionMethod;
    }

    public void setCompressionMethod(int compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public int getFilterMethod() {
        return filterMethod;
    }

    public void setFilterMethod(int filterMethod) {
        this.filterMethod = filterMethod;
    }

    public int getInterlaceMethod() {
        return interlaceMethod;
    }

    public void setInterlaceMethod(int interlaceMethod) {
        this.interlaceMethod = interlaceMethod;
    }

    @Override
    public String toString() {
        return "IHDR. "
                + "width="
                + width
                + ", height="
                + height
                + ", bitDepth="
                + bitDepth
                + ", colorType="
                + colorType
                + " => "
                + lookupColorType()
                + ""
                + ", compressionMethod="
                + compressionMethod
                + " => "
                + lookupCompressionMethod()
                + ", filterMethod="
                + filterMethod
                + " => "
                + lookupFilterMethod()
                + ", interlaceMethod="
                + interlaceMethod
                + " => "
                + lookupInterlaceMethod();
    }

    private String lookupColorType() {
        return switch (colorType) {
            case 0 -> "grayscale";
            case 2 -> "RGB";
            case 3 -> "palette index";
            case 4 -> "grayscale + alpha";
            case 6 -> "RGB + alpha";
            default -> "unknown";
        };
    }

    private String lookupCompressionMethod() {
        return switch (compressionMethod) {
            case 0 -> "deflate";
            default -> "unknown";
        };
    }

    private String lookupFilterMethod() {
        return switch (filterMethod) {
            case 0 -> "adaptive";
            default -> "unknown";
        };
    }

    private String lookupInterlaceMethod() {
        return switch (interlaceMethod) {
            case 0 -> "no interlace";
            case 1 -> "Adam7";
            default -> "unknown";
        };
    }
}
