package net.frogmouth.rnd.ngiis.cine;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CineParser {

    public ParseResult parse(String filename) throws IOException {
        Path filePath = Path.of(filename);
        MemorySegment segment = getMemorySegment(filePath);
        return parseMemorySegment(segment);
    }

    private MemorySegment getMemorySegment(Path testFile) throws IOException {
        FileChannel channel = FileChannel.open(testFile, StandardOpenOption.READ);
        MemorySegment segment =
                channel.map(FileChannel.MapMode.READ_ONLY, 0, Files.size(testFile), Arena.ofAuto());
        return segment;
    }

    protected ParseResult parseMemorySegment(MemorySegment segment) throws IOException {
        CineParseContext parser = new CineParseContext(segment);
        ParseResult result = parseFileHeader(parser);
        while (parser.getCursorPosition() < result.getOffImageOffsets()) {
            parseTaggedInformationBlock(parser, result);
        }
        long pPreviousImage = 0;
        for (long i = 0; i < result.getImageCount(); i++) {
            long pImage = parser.readInt64();
            // System.out.print("pImage: " + pImage);
            if (pPreviousImage != 0) {
                // System.out.println(", size: " + (pImage - pPreviousImage));
            } else {
                // System.out.println("");
            }
            result.addImageOffset(pImage);
            pPreviousImage = pImage;
        }
        return result;
    }

    private ParseResult parseFileHeader(CineParseContext parser) {
        ParseResult parseResult = new ParseResult(parser);
        int type = parser.readUnsignedInt16();
        int headerSize = parser.readUnsignedInt16();
        int compression = parser.readUnsignedInt16();
        int version = parser.readUnsignedInt16();
        int firstMovieImage = parser.readInt32();
        long totalImageCount = parser.readUnsignedInt32();
        System.out.println("total image count: " + totalImageCount);
        int firstImageNumber = parser.readInt32();
        parseResult.setImageCount(parser.readUnsignedInt32());
        long offImageHeader = parser.readUnsignedInt32();
        long offSetup = parser.readUnsignedInt32();
        parseResult.setOffImageOffsets(parser.readUnsignedInt32());
        Time64 triggerTime = parser.readTIME64();
        long cursor = parser.getCursorPosition();
        assert (cursor == headerSize);
        parseBitMapInfoHeader(parser, offImageHeader);
        parseCameraSetup(parser, offSetup);
        return parseResult;
    }

    private void parseBitMapInfoHeader(CineParseContext parser, long offImageHeader) {
        parser.skipToOffset(offImageHeader);
        long biSize = parser.readUnsignedInt32();
        int biWidth = parser.readInt32();
        int biHeight = parser.readInt32();
        int biPlanes = parser.readUnsignedInt16();
        System.out.println("dims: " + biWidth + " x " + biHeight + " x " + biPlanes);
        int biBitCount = parser.readUnsignedInt16();
        System.out.println("bit count: " + biBitCount);
        long biCompression = parser.readUnsignedInt32();
        System.out.println("compression: " + biCompression);
        long biSizeImage = parser.readUnsignedInt32();
        int biXPelsPerMeter = parser.readInt32();
        int biYPelsPerMeter = parser.readInt32();
        long biClrUsed = parser.readUnsignedInt32();
        long biClrImportant = parser.readUnsignedInt32();
        long cursor = parser.getCursorPosition();
        assert (cursor == offImageHeader + biSize);
    }

    private void parseCameraSetup(CineParseContext parser, long offSetup) {
        parser.skipToOffset(offSetup);
        int frameRate16 = parser.readUnsignedInt16();
        int shutter16 = parser.readUnsignedInt16();
        int postTrigger16 = parser.readUnsignedInt16();
        int frameDelay16 = parser.readUnsignedInt16();
        int aspectRatio = parser.readUnsignedInt16();
        int res7 = parser.readUnsignedInt16();
        int res8 = parser.readUnsignedInt16();
        int res9 = parser.readUnsignedInt8();
        int res10 = parser.readUnsignedInt8();
        int res11 = parser.readUnsignedInt8();
        int trigFrame = parser.readUnsignedInt8();
        int res12 = parser.readUnsignedInt8();
        String descriptionOld = parser.readString(121);
        System.out.println("descriptionOld: " + descriptionOld);
        int mark = parser.readUnsignedInt16();
        int length = parser.readUnsignedInt16();
        int res13 = parser.readUnsignedInt16();
        int sigOption = parser.readUnsignedInt16();
        int binChannels = parser.readUnsignedInt16();
        int samplesPerImage = parser.readUnsignedInt8();
        assert (binChannels == 0);
        parser.skipBytes(11 * 8); // BinName
        int anaOption = parser.readUnsignedInt16();
        int anaChannels = parser.readInt16();
        assert (anaChannels == 0);
        int res6 = parser.readUnsignedInt8();
        int anaBoard = parser.readUnsignedInt8();
        parser.skipBytes(8 * 2); // ChOption
        parser.skipBytes(4 * 8); // anaGain
        parser.skipBytes(8 * 6); // anaUnit
        parser.skipBytes(8 * 11); // ananame
        int firstImage = parser.readInt32();
        long dwImageCount = parser.readUnsignedInt32();
        int nQFactor = parser.readUnsignedInt16();
        int wCineFileType = parser.readUnsignedInt16();
        String path0 = parser.readString(65);
        String path1 = parser.readString(65);
        String path2 = parser.readString(65);
        String path3 = parser.readString(65);
        int res14 = parser.readUnsignedInt16();
        int res15 = parser.readUnsignedInt8();
        int res16 = parser.readUnsignedInt8();
        int res17 = parser.readUnsignedInt16();
        parser.skipBytes(8); // res18
        parser.skipBytes(8); // res19
        int res20 = parser.readUnsignedInt16();
        int res1 = parser.readInt32();
        int res2 = parser.readInt32();
        int res3 = parser.readInt32();
        int imWidth = parser.readInt16();
        int imHeight = parser.readInt16();
        int edrShutter16 = parser.readUnsignedInt16();
        long serial = parser.readUnsignedInt32();
        System.out.println("Camera serial number: " + serial);
        int saturation = parser.readInt32();
        int res5 = parser.readUnsignedInt8();
        long autoExposure = parser.readUnsignedInt32();
        int bFlipH = parser.readBool32();
        int bFlipV = parser.readBool32();
        long grid = parser.readUnsignedInt32();
        long frameRate = parser.readUnsignedInt32();
        System.out.println("Frame rate: " + frameRate);
        long shutter = parser.readUnsignedInt32();
        long edrShutter = parser.readUnsignedInt32();
        long postTrigger = parser.readUnsignedInt32();
        long frameDelay = parser.readUnsignedInt32();
        int bEnableColor = parser.readBool32();
        long cameraVersion = parser.readUnsignedInt32();
        System.out.println("Camera version: " + cameraVersion);
        long firmwareVersion = parser.readUnsignedInt32();
        System.out.println("Firmware version: " + firmwareVersion);
        long softwareVersion = parser.readUnsignedInt32();
        System.out.println("Software version: " + softwareVersion);
        int recordingTimeZone = parser.readInt32();
        long cfa = parser.readUnsignedInt32();
        System.out.println("cfa: " + cfa);
        int bright = parser.readInt32();
        int contrast = parser.readInt32();
        int gamma = parser.readInt32();
        long res21 = parser.readUnsignedInt32();
        long autoExpLevel = parser.readUnsignedInt32();
        long autoExpSpeed = parser.readUnsignedInt32();
        RECT autoExpRect = parser.readRECT();
        WBGAIN wbgain0 = parser.readWBGAIN();
        WBGAIN wbgain1 = parser.readWBGAIN();
        WBGAIN wbgain2 = parser.readWBGAIN();
        WBGAIN wbgain3 = parser.readWBGAIN();
        int rotate = parser.readInt32();
        WBGAIN wbView = parser.readWBGAIN();
        long realBPP = parser.readUnsignedInt32();
        long conv8Min = parser.readUnsignedInt32();
        long conv8Max = parser.readUnsignedInt32();
        int filterCode = parser.readInt32();
        int filterParam = parser.readInt32();
        IMFILTER uf = parser.readIMFILTER();
        long blackCalSVer = parser.readUnsignedInt32();
        long whiteCalSVer = parser.readUnsignedInt32();
        long grayCalSVer = parser.readUnsignedInt32();
        int bTimeStamp = parser.readBool32();
        long soundDest = parser.readUnsignedInt32();
        long frameRateProfileSteps = parser.readUnsignedInt32();
        long frameRateProfileImageNumber[] = new long[16];
        for (int i = 0; i < frameRateProfileImageNumber.length; i++) {
            frameRateProfileImageNumber[i] = parser.readInt32();
        }
        long frameRateProfileRate[] = new long[16];
        for (int i = 0; i < frameRateProfileRate.length; i++) {
            frameRateProfileRate[i] = parser.readUnsignedInt32();
        }
        long frameRateProfileExp[] = new long[16];
        for (int i = 0; i < frameRateProfileExp.length; i++) {
            frameRateProfileExp[i] = parser.readUnsignedInt32();
        }
        int mccnt = parser.readInt32();
        float mcpercent[] = new float[64];
        for (int i = 0; i < mcpercent.length; i++) {
            mcpercent[i] = parser.readFloat32();
        }
        long ciCalib = parser.readUnsignedInt32();
        long calibWidth = parser.readUnsignedInt32();
        long calibHeight = parser.readUnsignedInt32();
        long calibRate = parser.readUnsignedInt32();
        long calibExp = parser.readUnsignedInt32();
        long calibEDR = parser.readUnsignedInt32();
        long calibTemp = parser.readUnsignedInt32();
        long headSerial[] = new long[4];
        for (int i = 0; i < headSerial.length; i++) {
            headSerial[i] = parser.readUnsignedInt32();
        }
        long rangeCode = parser.readUnsignedInt32();
        long rangeSize = parser.readUnsignedInt32();
        long decimation = parser.readUnsignedInt32();
        long masterSerial = parser.readUnsignedInt32();
        long sensor = parser.readUnsignedInt32();
        long shutterNS = parser.readUnsignedInt32();
        long edrShutterNS = parser.readUnsignedInt32();
        long frameDelayNS = parser.readUnsignedInt32();
        long imPosXAcq = parser.readUnsignedInt32();
        long imPosYAcq = parser.readUnsignedInt32();
        long imWidthAcq = parser.readUnsignedInt32();
        long imHeightAcq = parser.readUnsignedInt32();
        String description = parser.readString(4096);
        System.out.println("description: " + description);
        int risingEdge = parser.readBool32();
        long filterTime = parser.readUnsignedInt32();
        int longReady = parser.readBool32();
        int shutterOff = parser.readBool32();
        parser.skipBytes(16); // Res4
        int bMetaWB = parser.readBool32();
        int hue = parser.readInt32();
        int blackLevel = parser.readInt32();
        int whiteLevel = parser.readInt32();
        String lensDescription = parser.readString(256);
        float lensAperture = parser.readFloat32();
        float lensFocusDistance = parser.readFloat32();
        float lensFocalLength = parser.readFloat32();
        float fOffset = parser.readFloat32();
        float fGain = parser.readInt32();
        float fSaturation = parser.readInt32();
        float fHue = parser.readFloat32();
        float fGamma = parser.readFloat32();
        float fGammaR = parser.readFloat32();
        float fGammaB = parser.readFloat32();
        float fFlare = parser.readFloat32();
        float fPedistalR = parser.readFloat32();
        float fPedistalG = parser.readFloat32();
        float fPedistalB = parser.readFloat32();
        float fChroma = parser.readFloat32();
        String toneLabel = parser.readString(256);
        int tonePoints = parser.readInt32();
        parser.skipBytes(2 * 32 * 4); // fTone
        String userMatrixLabel = parser.readString(256);
        int enableMatrices = parser.readBool32();
        parser.skipBytes(9 * 4); // fUserMatrix
        int enableCrop = parser.readBool32();
        RECT cropRect = parser.readRECT();
        System.out.println(
                "Crop left: "
                        + cropRect.left()
                        + ", top: "
                        + cropRect.top()
                        + ", right: "
                        + cropRect.right()
                        + ", bottom: "
                        + cropRect.bottom());
        int enableResample = parser.readBool32();
        long resampleWidth = parser.readUnsignedInt32();
        long resampleHeight = parser.readUnsignedInt32();
        float fGain16_8 = parser.readFloat32();
        parser.skipBytes(16 * 4); // FRPShape
        TC trigTC = parser.readTC();
        float fPbRate = parser.readFloat32();
        float fTcRate = parser.readFloat32();
        String cineName = parser.readString(256);
        System.out.println("CineName: " + cineName);
        long cursor = parser.getCursorPosition();
        long remaining = offSetup + length - cursor;
        parser.skipBytes(remaining);
        cursor = parser.getCursorPosition();
        System.out.println("cursor: " + cursor);
        assert (cursor == offSetup + length);
    }

    private void parseTaggedInformationBlock(CineParseContext parser, ParseResult result) {
        long blockSize = parser.readUnsignedInt32();
        int type = parser.readUnsignedInt16();
        int reserved = parser.readUnsignedInt16();
        System.out.println("type: " + type + ", data size: " + (blockSize - 8));
        if (type == 0x03ea) {
            processTimeOnlyBlock(parser, blockSize - 8, result);
        } else {
            parser.skipBytes(blockSize - 8);
        }
    }

    private void processTimeOnlyBlock(CineParseContext parser, long numBytes, ParseResult result) {
        long numElements = numBytes / 8;
        for (int i = 0; i < numElements; i++) {
            Time64 time = parser.readTIME64();
            // System.out.println("time: " + time.toString());
            result.addTimestamp(time);
        }
    }
}
