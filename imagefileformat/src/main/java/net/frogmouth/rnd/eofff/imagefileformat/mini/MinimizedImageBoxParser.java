package net.frogmouth.rnd.eofff.imagefileformat.mini;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.BitReader;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBoxParser;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.BoxParser.class)
public class MinimizedImageBoxParser extends FullBoxParser {
    private static final Logger LOG = LoggerFactory.getLogger(MinimizedImageBoxParser.class);

    public MinimizedImageBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return MinimizedImageBox.MINI_ATOM;
    }

    @Override
    public Box parse(ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        MinimizedImageBox box = new MinimizedImageBox();
        System.out.println("initial position: " + parseContext.getCursorPosition());
        long remainingBytesInBox = boxSize - (parseContext.getCursorPosition() - initialOffset);
        byte[] boxBytes = parseContext.getBytes(remainingBytesInBox);
        BitReader bitReader = new BitReader(boxBytes);
        box.setVersion(bitReader.readBits(2));
        box.setExplicitCodecTypesFlag(bitReader.readBits(1) == 0x01);
        box.setFloatFlag(bitReader.readBits(1) == 0x01);
        box.setFullRangeFlag(bitReader.readBits(1) == 0x01);
        box.setAlphaFlag(bitReader.readBits(1) == 0x01);
        box.setExplicitCICPFlag(bitReader.readBits(1) == 0x01);
        box.setHdrFlag(bitReader.readBits(1) == 0x01);
        box.setIccFlag(bitReader.readBits(1) == 0x01);
        box.setExifFlag(bitReader.readBits(1) == 0x01);
        box.setXmpFlag(bitReader.readBits(1) == 0x01);
        box.setChromaSubsampling(
                MinimizedImageBox.ChromaSubsampling.lookupEntry(bitReader.readBits(2)));
        box.setOrientationMinus1(bitReader.readBits(3));
        int smallDimensionsFlag = bitReader.readBits(1);
        int widthMinus1;
        int heightMinus1;
        if (smallDimensionsFlag == 0x01) {
            widthMinus1 = bitReader.readBits(7);
            heightMinus1 = bitReader.readBits(7);
        } else {
            widthMinus1 = bitReader.readBits(15);
            heightMinus1 = bitReader.readBits(15);
        }
        box.setWidth(widthMinus1 + 1);
        box.setHeight(heightMinus1 + 1);
        // TODO: chroma subsampling
        if (box.isFloatFlag()) {
            // TODO: bit_depth_log2_minus4
        } else {
            boolean high_bit_depth_flag = (bitReader.readBits(1) == 0x01);
            if (high_bit_depth_flag) {
                int bit_depth_minus9 = bitReader.readBits(3);
                box.setBitDepth(bit_depth_minus9 + 9);
            } else {
                box.setBitDepth(8);
            }
        }
        if (box.isAlphaFlag()) {
            // TODO
        }
        if (box.isExplicitCICPFlag()) {
            // TODO
        } else {
            // TODO: defaults
        }
        if (box.isExplicitCodecTypesFlag()) {
            // TODO
        }
        if (box.isHdrFlag()) {
            // TODO - much code
        }
        // if (icc_flag || exif_flag....
        boolean few_codec_config_bytes_flag = (bitReader.readBits(1) == 0x01);
        boolean few_item_data_bytes_flag = (bitReader.readBits(1) == 0x01);
        // if icc_flag
        // if hdr_flag && gainmap_flag && tmap_icc_flag
        // if hdr_flag && gainmap_flag
        // if hdr_flag && gainmap_flag
        // if hdr_flag && gainmap_flag && gainmap_item_data_size

        int main_item_codec_config_size = bitReader.readBits(few_codec_config_bytes_flag ? 3 : 12);

        int main_item_data_size_minus_1;
        if (few_item_data_bytes_flag) {
            main_item_data_size_minus_1 = bitReader.readBits(15);
        } else {
            main_item_data_size_minus_1 = bitReader.readBits(28);
        }
        // if alpha_flag
        // if alpha_flag && alpha_item_data_size > 0
        // if exif_flag
        // if xmp_flag

        bitReader.byteAlign();
        // Chunks
        // if alpha_flag
        // if hdr_flag
        if (main_item_codec_config_size > 0) {
            box.setMainItemCodecConfig(bitReader.readBytes(main_item_codec_config_size));
        }
        // if icc_flag
        // if hdr_flag && ...
        // if hdr_flag && ...
        // if alpha_flag
        // if hdr_flag
        System.out.println("data offset: " + bitReader.getCursorPosition());
        System.out.println("length: " + (main_item_data_size_minus_1 + 1));
        box.setMainItemData(bitReader.readBytes(main_item_data_size_minus_1 + 1));
        // if exif_flag
        // if xmp_flag
        return box;
    }
}
