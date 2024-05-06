package net.frogmouth.rnd.eofff.jpeg2000.codestream;

import java.io.File;
import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.BitReader;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import org.testng.annotations.Test;

public class CodeStreamTest {

    public CodeStreamTest() {}

    @Test
    public void parse() throws IOException, Exception {
        String resourceName = "p0_01.j2k";

        ClassLoader classLoader = getClass().getClassLoader();
        File testfile = new File(classLoader.getResource(resourceName).getFile());
        FileChannel channel = FileChannel.open(testfile.toPath(), StandardOpenOption.READ);
        MemorySegment segment =
                channel.map(
                        FileChannel.MapMode.READ_ONLY,
                        0,
                        Files.size(testfile.toPath()),
                        Arena.ofAuto());
        parseMemorySegment(segment);
    }

    private void parseMemorySegment(MemorySegment segment) throws Exception {
        ParseContext ctx = new ParseContext(segment);
        long offsetOfEndOfBitStream = -1;
        while (ctx.hasRemaining()) {
            long currentMarkerOffset = ctx.getCursorPosition();
            int marker = ctx.readUnsignedInt16();
            System.out.print(HexFormat.of().toHexDigits((short) marker) + " - ");
            switch (marker) {
                case 0xff4f:
                    System.out.println("SOC marker (A.4.1)");
                    break;
                case 0xff51:
                    System.out.println("SIZ marker (A.5.1)");
                    int lsiz = ctx.readUnsignedInt16();
                    int rsiz = ctx.readUnsignedInt16();
                    long xsiz = ctx.readUnsignedInt32();
                    long ysiz = ctx.readUnsignedInt32();
                    long xOsiz = ctx.readUnsignedInt32();
                    long yOsiz = ctx.readUnsignedInt32();
                    long xTsiz = ctx.readUnsignedInt32();
                    long yTsiz = ctx.readUnsignedInt32();
                    long xTOsiz = ctx.readUnsignedInt32();
                    long yTOsiz = ctx.readUnsignedInt32();
                    int csiz = ctx.readUnsignedInt16();
                    System.out.println(
                            "    Image and tile sizes. Rsiz:"
                                    + HexFormat.of().toHexDigits((short) rsiz)
                                    + ", Xsiz:"
                                    + xsiz
                                    + ", Ysiz:"
                                    + ysiz
                                    + ", XOsiz:"
                                    + xOsiz
                                    + ", YOsiz:"
                                    + yOsiz
                                    + ", XTsiz:"
                                    + xTsiz
                                    + ", YTsiz:"
                                    + yTsiz
                                    + ", XTOsiz:"
                                    + xTOsiz
                                    + ", YTOsiz:"
                                    + yTOsiz
                                    + ", Csiz:"
                                    + csiz);
                    for (int i = 0; i < csiz; i++) {
                        int ssiz = ctx.readUnsignedInt8();
                        int xRsiz = ctx.readUnsignedInt8();
                        int yRsiz = ctx.readUnsignedInt8();
                        System.out.println(
                                "    Ssiz:" + ssiz + ", XRsiz:" + xRsiz + ", YRsiz:" + yRsiz);
                    }
                    break;
                case 0xff52:
                    System.out.println("COD marker (A.6.1)");
                    int lcod = ctx.readUnsignedInt16();
                    int scod = ctx.readUnsignedInt8();
                    // TODO: decode bits - expect default precincts, no sop, no eph
                    System.out.println("    Scod: 0x" + HexFormat.of().toHexDigits((byte) scod));
                    int progressionOrder = ctx.readUnsignedInt8();
                    int numberOfLayers = ctx.readUnsignedInt16();
                    int multipleComponentTransformation = ctx.readUnsignedInt8();
                    System.out.print(
                            "    Progression Order: 0x"
                                    + HexFormat.of().toHexDigits((byte) progressionOrder));
                    switch (progressionOrder) {
                        case 0 ->
                                System.out.println(
                                        " - layer - resolution level - component - position progression (L-R-C-P)");
                        case 1 ->
                                System.out.println(
                                        " - resolution level - layer - component - position progression (R-L-C-P)");
                        case 2 ->
                                System.out.println(
                                        " - resolution level - position - component - layer progression (R-P-C-L)");
                        case 3 ->
                                System.out.println(
                                        " - position - component - resolution level - layer progression (P-C-R-L)");
                        case 4 ->
                                System.out.println(
                                        " - component - position - resolution level - layer progression (C-P-R-L)");
                        default -> System.out.println(" - Unknown (TODO)");
                    }
                    System.out.println("    Number of layers: " + numberOfLayers);
                    System.out.println(
                            "    multiple component transformation: 0x"
                                    + HexFormat.of()
                                            .toHexDigits((byte) multipleComponentTransformation));
                    int numberOfDecompositionLevels = ctx.readUnsignedInt8();
                    System.out.println(
                            "    number of decomposition levels (NLevels): "
                                    + numberOfDecompositionLevels);
                    int codeBlockWidth = ctx.readUnsignedInt8();
                    int codeBlockHeight = ctx.readUnsignedInt8();
                    System.out.println(
                            "    code block width: "
                                    + codeBlockWidth
                                    + ", height: "
                                    + codeBlockHeight
                                    + ", actual: "
                                    + (int) Math.pow(2, codeBlockWidth + 2)
                                    + " x "
                                    + (int) Math.pow(2, codeBlockHeight + 2));
                    int codeBlockStyle = ctx.readUnsignedInt8();
                    System.out.println("    code block style: " + codeBlockStyle);
                    /* decode:
                                       <codingBypass>no</codingBypass>
                    <resetOnBoundaries>no</resetOnBoundaries>
                    <termOnEachPass>no</termOnEachPass>
                    <vertCausalContext>no</vertCausalContext>
                    <predTermination>no</predTermination>
                    <segmentationSymbols>no</segmentationSymbols>
                    <transformation>5-3 reversible</transformation>
                    <precinctSizeX>32768</precinctSizeX>
                    <precinctSizeY>32768</precinctSizeY>
                    <precinctSizeX>32768</precinctSizeX>
                    <precinctSizeY>32768</precinctSizeY>
                    <precinctSizeX>32768</precinctSizeX>
                    <precinctSizeY>32768</precinctSizeY>
                    <precinctSizeX>32768</precinctSizeX>
                    <precinctSizeY>32768</precinctSizeY>

                            */
                    int transformation = ctx.readUnsignedInt8();
                    System.out.println(
                            "    transformation: "
                                    + transformation
                                    + " ["
                                    + ((transformation == 1)
                                            ? "5-3 reversible"
                                            : "9-7 irreversible")
                                    + "]");
                    int spcod_remaining_len =
                            lcod
                                    - Short.BYTES
                                    - Byte.BYTES
                                    - Byte.BYTES
                                    - Short.BYTES
                                    - Byte.BYTES
                                    - Byte.BYTES
                                    - Byte.BYTES
                                    - Byte.BYTES
                                    - Byte.BYTES
                                    - Byte.BYTES;
                    if (spcod_remaining_len != 0) {
                        System.out.println("SPcod len: " + spcod_remaining_len);
                        ctx.skipBytes(spcod_remaining_len);
                    }
                    break;

                case 0xff5c:
                    System.out.println("QCD marker (A.6.4)");
                    int lqcd = ctx.readUnsignedInt16();
                    int sqcd = ctx.readUnsignedInt8();
                    System.out.println("    Sqcd: " + HexFormat.of().toHexDigits((byte) sqcd));
                    int quant = sqcd & 0x1F;
                    System.out.print("    Sqcd quantisation style: " + quant);
                    switch (quant) {
                        case 0 -> System.out.println(" - no quantization");
                        default -> System.out.println(" - unknown (TODO)");
                    }
                    int guardBits = sqcd >> 5;
                    System.out.println("    Sqcd guard bits: " + guardBits);
                    // TODO: decode epsilon - 8, 9, 9 expected
                    ctx.skipBytes(lqcd - Short.BYTES - Byte.BYTES);
                    break;
                case 0xff90:
                    System.out.println("SOT marker (A.4.2)");
                    int lsot = ctx.readUnsignedInt16();
                    int isot = ctx.readUnsignedInt16();
                    long psot = ctx.readUnsignedInt32();
                    offsetOfEndOfBitStream = currentMarkerOffset + psot;
                    int tPsot = ctx.readUnsignedInt8();
                    int tNsot = ctx.readUnsignedInt8();
                    System.out.println(
                            "    Start of tile-part: Lsot:"
                                    + lsot
                                    + ", Isot:"
                                    + isot
                                    + ", Psot:"
                                    + psot
                                    + ", TPsot: "
                                    + tPsot
                                    + ", TNsot: "
                                    + tNsot);
                    break;
                case 0xff93:
                    System.out.println("SOD marker (A.4.3)");
                    long bitstreamLength = offsetOfEndOfBitStream - ctx.getCursorPosition();
                    byte[] bitstreamData = ctx.getBytes(bitstreamLength);
                    BitReader bitstream = new BitReader(bitstreamData);
                    while (bitstream.hasRemaining()) {
                        int zeroLength = bitstream.readBits(1);
                        if (zeroLength == 1) {
                            continue;
                        }
                        // TODO: there is logic in B.10.8 that we need here
                        int hasCodeBlock = bitstream.readBits(1);
                        if (hasCodeBlock == 1) {}
                    }
                    break;
                case 0xffd9:
                    System.out.println("EOC marker (A.4.4)");
                    break;
                default:
                    throw new Exception("TODO");
            }
        }
    }
}
