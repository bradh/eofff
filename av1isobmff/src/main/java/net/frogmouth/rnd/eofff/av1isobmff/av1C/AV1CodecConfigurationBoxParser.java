package net.frogmouth.rnd.eofff.av1isobmff.av1C;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.cicp.ColourPrimary;
import net.frogmouth.rnd.eofff.cicp.MatrixCoefficients;
import net.frogmouth.rnd.eofff.cicp.TransferCharacteristics;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class AV1CodecConfigurationBoxParser extends ItemFullPropertyParser {

    private static final Logger LOG = LoggerFactory.getLogger(AV1CodecConfigurationBoxParser.class);
    private static final int VERSION_MASK = 0x7F;
    private static final int SEQ_LEVEL_IDX_0_MASK = 0x1F;

    public AV1CodecConfigurationBoxParser() {}

    @Override
    public FourCC getFourCC() {
        return AV1CodecConfigurationBox.AV1C_ATOM;
    }

    @Override
    public AV1CodecConfigurationBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        AV1CodecConfigurationBox box = new AV1CodecConfigurationBox();
        int markerAndVersion = parseContext.readUnsignedInt8();
        box.setVersion(markerAndVersion & VERSION_MASK);
        int seqProfileAndIndex = parseContext.readUnsignedInt8();
        box.setSeq_profile(seqProfileAndIndex >> 5);
        box.setSeq_level_idx_0(seqProfileAndIndex & SEQ_LEVEL_IDX_0_MASK);
        int flags = parseContext.readUnsignedInt8();
        box.setSeq_tier_0((flags & 0x80) == 0x80);
        box.setHigh_bitdepth((flags & 0x40) == 0x40);
        box.setTwelve_bit((flags & 0x20) == 0x20);
        box.setMonochrome((flags & 0x10) == 0x10);
        box.setChroma_subsampling_x((flags & 0x08) == 0x08);
        box.setChroma_subsampling_y((flags & 0x04) == 0x04);
        box.setChroma_sample_position(flags & 0x03);
        int initial_pres = parseContext.readUnsignedInt8();
        if ((initial_pres & 0x10) == 0x10) {
            box.setInitial_presentation_delay(initial_pres & 0x0F);
        }
        long obuBytesCount = boxSize - 12;
        byte[] obuBytes = new byte[(int) obuBytesCount];
        parseContext.readBytes(obuBytes);
        box.setObuBytes(obuBytes);
        /*
        for (int i = 0; i < obuBytes.length; ) {
            int obuHeaderByte = obuBytes[i];
            i += 1;
            int obu_type = ((obuHeaderByte & 0x78) >> 3);
            boolean obu_extension_flag = ((obuHeaderByte & 0x04) == 0x04);
            boolean obu_has_size_field = ((obuHeaderByte & 0x02) == 0x02);
            boolean obu_reserved_1bit = ((obuHeaderByte & 0x01) == 0x01);
            if (obu_extension_flag) {
                int obuExtensionByte = obuBytes[i];
                i += 1;
                // TODO: parse extension
            }
            long obu_size = 0;
            if (obu_has_size_field) {
                for (int j = 0; j < 8; j++) {
                    int leb128_byte = obuBytes[i];
                    i += 1;
                    obu_size |= ((leb128_byte & 0x7f) << (j * 7));
                    if ((leb128_byte & 0x80) != 0x80) {
                        break;
                    }
                }
            } else {
                // TODO: work out sz -1 - obu_extension_flag
            }
            // TODO: probably more stuff
            if (obu_type == 1) {
                byte[] seq_header_bits = new byte[(int) obu_size];
                System.arraycopy(obuBytes, i, seq_header_bits, 0, (int) obu_size);
                i += obu_size;
                BitsExtractor bits = new BitsExtractor(seq_header_bits);
                // OBU_SEQUENCE_HEADER
                int seq_profile = bits.extractBits(3);
                int still_picture = bits.extractBits(1);
                int reduced_still_picture_header = bits.extractBits(1);
                int seq_level_idx_0 = 0;
                if (reduced_still_picture_header == 1) {
                    // TODO: other settings
                    seq_level_idx_0 = bits.extractBits(5);
                } else {
                    // TODO
                }
                int frame_width_bits_minus_1 = bits.extractBits(4);
                int frame_height_bits_minus_1 = bits.extractBits(4);
                int max_frame_width_minus_1 = bits.extractBits(frame_width_bits_minus_1 + 1);
                int max_frame_height_minus_1 = bits.extractBits(frame_height_bits_minus_1 + 1);
                System.out.println(
                        String.format(
                                "%d, %d",
                                max_frame_width_minus_1 + 1, max_frame_height_minus_1 + 1));
                // TODO: there is some more logic needed here if reduced_still_picture_header != 1;
                int use_128x128_superblock = bits.extractBits(1);
                int enable_filter_intra = bits.extractBits(1);
                int enable_intra_edge_filter = bits.extractBits(1);
                // TODO: there is some more logic needed here if reduced_still_picture_header != 1;
                int enable_superres = bits.extractBits(1);
                int enable_cdef = bits.extractBits(1);
                int enable_restoration = bits.extractBits(1);
                colour_config(bits, seq_profile);
                int film_grain_params_present = bits.extractBits(1);
            }

        }
        */
        return box;
    }

    private void colour_config(BitsExtractor bits, int seq_profile) {
        int high_bit_depth = bits.extractBits(1);
        int BitDepth = 0;
        if ((seq_profile == 2) && (high_bit_depth == 1)) {
            int twelve_bit = bits.extractBits(1);
            if (twelve_bit == 1) {
                BitDepth = 12;
            } else {
                BitDepth = 10;
            }
        } else {
            if (high_bit_depth == 1) {
                BitDepth = 10;
            } else {
                BitDepth = 8;
            }
        }
        int mono_chrome;
        // 1 indicates that the video does not contain U and V color planes. mono_chrome equal to 0
        // indicates that the video contains Y, U, and V color planes.
        if (seq_profile == 1) {
            mono_chrome = 0;
        } else {
            mono_chrome = bits.extractBits(1);
        }
        int NumPlanes = ((mono_chrome == 1) ? 1 : 3);
        boolean color_description_present_flag = (bits.extractBits(1) == 1);
        ColourPrimary color_primaries;
        TransferCharacteristics transfer_characteristics;
        MatrixCoefficients matrix_coefficients;
        if (color_description_present_flag) {
            color_primaries = ColourPrimary.lookup(bits.extractBits(8));
            transfer_characteristics = TransferCharacteristics.lookup(bits.extractBits(8));
            matrix_coefficients = MatrixCoefficients.lookup(bits.extractBits(8));
        } else {
            // assign defaults
            color_primaries = ColourPrimary.UNSPECIFIED;
            transfer_characteristics = TransferCharacteristics.UNSPECIFIED;
            matrix_coefficients = MatrixCoefficients.UNSPECIFIED;
        }
        int color_range;
        int subsampling_x;
        int subsampling_y;
        int separate_uv_delta_q;
        ChromaSamplePosition chroma_sample_position;
        if (mono_chrome == 1) {
            color_range = bits.extractBits(1);
            subsampling_x = 1;
            subsampling_y = 1;
            chroma_sample_position = ChromaSamplePosition.UNKNOWN;
            separate_uv_delta_q = 0;
            return;
        } else if (color_primaries == ColourPrimary.BT_709
                && transfer_characteristics == TransferCharacteristics.SRGB
                && matrix_coefficients == MatrixCoefficients.IDENTITY) {
            color_range = 1;
            subsampling_x = 0;
            subsampling_y = 0;
        } else {
            color_range = bits.extractBits(1);
            if (seq_profile == 0) {
                subsampling_x = 1;
                subsampling_y = 1;
            } else if (seq_profile == 1) {
                subsampling_x = 0;
                subsampling_y = 0;
            } else {
                if (BitDepth == 12) {
                    subsampling_x = bits.extractBits(1);
                    if (subsampling_x == 1) {
                        subsampling_y = bits.extractBits(1);
                    } else {
                        subsampling_y = 0;
                    }
                } else {
                    subsampling_x = 1;
                    subsampling_y = 0;
                }
            }
            if ((subsampling_x > 0) && (subsampling_y > 0)) {
                chroma_sample_position = ChromaSamplePosition.lookup(bits.extractBits(1));
            }
        }
        separate_uv_delta_q = bits.extractBits(1);
    }
}
