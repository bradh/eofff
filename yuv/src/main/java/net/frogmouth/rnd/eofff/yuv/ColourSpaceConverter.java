package net.frogmouth.rnd.eofff.yuv;

import java.nio.BufferOverflowException;

public class ColourSpaceConverter {

    public static byte[] YuvConverter(
            Y4mReader reader, byte[] frameData, OutputFormat outputFormat) {
        return YuvConverter(
                reader.getColourSpace(),
                reader.getFrameHeight(),
                reader.getFrameWidth(),
                frameData,
                outputFormat);
    }

    public static byte[] YuvConverter(
            ColourSpace colourSpace,
            int frameHeight,
            int frameWidth,
            byte[] frameData,
            OutputFormat outputFormat) {
        int numPixels = frameHeight * frameWidth;
        int horizontalSubsampling = colourSpace.getHorizontalSubsampling();
        int verticalSubsampling = colourSpace.getVerticalSubsampling();
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth; x++) {
                try {
                    int yIndex = y * frameWidth + x;
                    int yValue = frameData[yIndex] & 0xFF;
                    int uvIndex =
                            (y / (verticalSubsampling * horizontalSubsampling) * frameWidth)
                                    + x / horizontalSubsampling;
                    int uValue = (frameData[numPixels + uvIndex] & 0xFF) - 128;
                    int vValue =
                            (frameData[
                                                    numPixels
                                                            + numPixels
                                                                    / (horizontalSubsampling
                                                                            * verticalSubsampling)
                                                            + uvIndex]
                                            & 0xFF)
                                    - 128;
                    int r = (int) (yValue + 1.370705f * vValue);
                    int g = (int) (yValue - (0.698001f * vValue) - (0.337633f * uValue));
                    int b = (int) (yValue + 1.732446f * uValue);
                    outputFormat.putRGB(r, g, b);
                } catch (BufferOverflowException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        return outputFormat.getBytes();
    }

    // TODO: this is gross
    public static byte[] TwoVUYConverter(
            int frameHeight, int frameWidth, byte[] frameData, OutputFormat outputFormat) {
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth / 2; x++) {
                try {
                    int pixelIndex = 2 * (y * frameWidth + x * 2);
                    int cbValue = (frameData[pixelIndex] & 0xFF) - 128;
                    int y0Value = frameData[pixelIndex + 1] & 0xFF;
                    int crValue = (frameData[pixelIndex + 2] & 0xFF) - 128;
                    int y1Value = frameData[pixelIndex + 3] & 0xFF;
                    int r0 = (int) (y0Value + 1.370705f * crValue);
                    int g0 = (int) (y0Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b0 = (int) (y0Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r0, g0, b0);
                    int r1 = (int) (y1Value + 1.370705f * crValue);
                    int g1 = (int) (y1Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b1 = (int) (y1Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r1, g1, b1);
                } catch (BufferOverflowException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        return outputFormat.getBytes();
    }

    public static byte[] YUV2Converter(
            int frameHeight, int frameWidth, byte[] frameData, OutputFormat outputFormat) {
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth / 2; x++) {
                try {
                    int pixelIndex = 2 * (y * frameWidth + x * 2);
                    int y0Value = frameData[pixelIndex] & 0xFF;
                    int cbValue = (frameData[pixelIndex + 1] & 0xFF) - 128;
                    int y1Value = frameData[pixelIndex + 2] & 0xFF;
                    int crValue = (frameData[pixelIndex + 3] & 0xFF) - 128;
                    int r0 = (int) (y0Value + 1.370705f * crValue);
                    int g0 = (int) (y0Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b0 = (int) (y0Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r0, g0, b0);
                    int r1 = (int) (y1Value + 1.370705f * crValue);
                    int g1 = (int) (y1Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b1 = (int) (y1Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r1, g1, b1);
                } catch (BufferOverflowException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        return outputFormat.getBytes();
    }

    public static byte[] YVYUConverter(
            int frameHeight, int frameWidth, byte[] frameData, OutputFormat outputFormat) {
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth / 2; x++) {
                try {
                    int pixelIndex = 2 * (y * frameWidth + x * 2);
                    int y0Value = frameData[pixelIndex] & 0xFF;
                    int crValue = (frameData[pixelIndex + 1] & 0xFF) - 128;
                    int y1Value = frameData[pixelIndex + 2] & 0xFF;
                    int cbValue = (frameData[pixelIndex + 3] & 0xFF) - 128;
                    int r0 = (int) (y0Value + 1.370705f * crValue);
                    int g0 = (int) (y0Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b0 = (int) (y0Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r0, g0, b0);
                    int r1 = (int) (y1Value + 1.370705f * crValue);
                    int g1 = (int) (y1Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b1 = (int) (y1Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r1, g1, b1);
                } catch (BufferOverflowException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        return outputFormat.getBytes();
    }

    public static byte[] VYUYConverter(
            int frameHeight, int frameWidth, byte[] frameData, OutputFormat outputFormat) {
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth / 2; x++) {
                try {
                    int pixelIndex = 2 * (y * frameWidth + x * 2);
                    int crValue = (frameData[pixelIndex] & 0xFF) - 128;
                    int y0Value = frameData[pixelIndex + 1] & 0xFF;
                    int cbValue = (frameData[pixelIndex + 2] & 0xFF) - 128;
                    int y1Value = frameData[pixelIndex + 3] & 0xFF;
                    int r0 = (int) (y0Value + 1.370705f * crValue);
                    int g0 = (int) (y0Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b0 = (int) (y0Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r0, g0, b0);
                    int r1 = (int) (y1Value + 1.370705f * crValue);
                    int g1 = (int) (y1Value - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b1 = (int) (y1Value + 1.732446f * cbValue);
                    outputFormat.putRGB(r1, g1, b1);
                } catch (BufferOverflowException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        return outputFormat.getBytes();
    }

    public static byte[] V308Converter(
            int frameHeight, int frameWidth, byte[] frameData, OutputFormat outputFormat) {
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth; x++) {
                try {
                    int pixelIndex = 3 * (y * frameWidth + x);
                    int crValue = (frameData[pixelIndex] & 0xFF) - 128;
                    int yValue = frameData[pixelIndex + 1] & 0xFF;
                    int cbValue = (frameData[pixelIndex + 2] & 0xFF) - 128;
                    int r = (int) (yValue + 1.370705f * crValue);
                    int g = (int) (yValue - (0.698001f * crValue) - (0.337633f * cbValue));
                    int b = (int) (yValue + 1.732446f * cbValue);
                    outputFormat.putRGB(r, g, b);
                } catch (BufferOverflowException ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        return outputFormat.getBytes();
    }
}
