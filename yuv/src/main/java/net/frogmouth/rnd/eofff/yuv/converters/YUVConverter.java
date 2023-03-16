package net.frogmouth.rnd.eofff.yuv.converters;

import net.frogmouth.rnd.eofff.yuv.OutputFormat;

public interface YUVConverter {

    byte[] convert(byte[] frameData, OutputFormat outputFormat);
}
