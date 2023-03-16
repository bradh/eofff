package net.frogmouth.rnd.eofff.yuv.converters;

import java.nio.BufferOverflowException;
import net.frogmouth.rnd.eofff.yuv.OutputFormat;

public class Converter {
    private final int frameHeight;
    private final int frameWidth;
    private final SourceFormat sourceFormat;

    public Converter(int frameHeight, int frameWidth, SourceFormat format) {
        this.frameHeight = frameHeight;
        this.frameWidth = frameWidth;
        this.sourceFormat = format;
    }

    public byte[] convert(byte[] frameData, OutputFormat outputFormat) {
        for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth / 2; x++) {
                try {
                    int pixelIndex = 2 * (y * frameWidth + x * 2);
                    int cbValue =
                            (frameData[pixelIndex + this.sourceFormat.getCbOffset()] & 0xFF) - 128;
                    int y0Value = frameData[pixelIndex + this.sourceFormat.getY0Offset()] & 0xFF;
                    int crValue =
                            (frameData[pixelIndex + this.sourceFormat.getCrOffset()] & 0xFF) - 128;
                    int y1Value = frameData[pixelIndex + this.sourceFormat.getY1Offset()] & 0xFF;
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
}
