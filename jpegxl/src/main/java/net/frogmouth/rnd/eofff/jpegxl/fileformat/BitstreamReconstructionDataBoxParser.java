package net.frogmouth.rnd.eofff.jpegxl.fileformat;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBoxParser;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class BitstreamReconstructionDataBoxParser extends BaseBoxParser {

    @Override
    public BitstreamReconstructionDataBox parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        BitstreamReconstructionDataBox box = new BitstreamReconstructionDataBox();
        int dataLen = (int) (initialOffset + boxSize - parseContext.getCursorPosition());
        byte[] data = new byte[dataLen];
        parseContext.readBytes(data);
        box.setData(data);
        for (int i = 0; i < data.length; i++) {
            System.out.print(String.format("0x%02x, ", data[i]));
            if ((i + 1) % 8 == 0) {
                System.out.println();
            }
        }
        System.out.println();
        BitsExtractor bits = new BitsExtractor(data);
        boolean is_grey = (bits.readBits(1) == 1);
        List<Integer> markers = new ArrayList<>();
        while (true) {
            int marker = 0xC0 + (int) bits.readBits(6);
            markers.add(marker);
            if (marker == 0xd9) {
                break;
            }
        }
        int num_app_markers = 0;
        int num_com_markers = 0;
        int num_scans = 0;
        int num_intermarker = 0;
        boolean has_dri = false;
        for (Integer marker : markers) {
            if ((marker >= 0xe0) && (marker <= 0xef)) {
                num_app_markers += 1;
            }
            if (marker == 0xfe) {
                num_com_markers += 1;
            }
            if (marker == 0xda) {
                num_scans += 1;
            }
            if (marker == 0xff) {
                num_intermarker += 1;
            }
            if (marker == 0xdd) {
                has_dri = true;
            }
            System.out.println(
                    String.format("marker: 0x%02x - %s", marker, getMarkerDescription(marker)));
        }
        System.out.println("num_app_markers: " + num_app_markers);
        System.out.println("num_com_markers: " + num_com_markers);
        System.out.println("num_scans: " + num_scans);
        System.out.println("num_intermarker: " + num_intermarker);
        System.out.println("has_dri: " + has_dri);
        return box;
    }

    private String getMarkerDescription(int marker) {
        return switch (marker) {
            case 0xc0, 0xc1, 0xc2, 0xc9, 0xca -> "Start of Frame (SOF)";
            case 0xc4 -> "Define Huffman Table(s) (DHT)";
            case 0xd0, 0xd1, 0xd2, 0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8 -> "Restart (RST)";
            case 0xd9 -> "End of Image (EOI)";
            case 0xda -> "Start of scan (SOS)";
            case 0xdb -> "Define Quanization Table(s) (DQT)";
            case 0xdd -> "Define Restart Interval (DRI)";
            case 0xe0,
                            0xe1,
                            0xe2,
                            0xe3,
                            0xe4,
                            0xe5,
                            0xe6,
                            0xe7,
                            0xe8,
                            0xe9,
                            0xea,
                            0xeb,
                            0xec,
                            0xed,
                            0xee,
                            0xef,
                            0xf0 ->
                    "Application specific (APPn)";
            default -> "Unknown / unhandled";
        };
    }

    @Override
    public FourCC getFourCC() {
        return BitstreamReconstructionDataBox.JBRD_ATOM;
    }
}
