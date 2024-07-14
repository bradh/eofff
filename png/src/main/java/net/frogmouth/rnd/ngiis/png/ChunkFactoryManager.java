/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.frogmouth.rnd.ngiis.png;

/**
 * @author bradh
 */
class ChunkFactoryManager {

    static ChunkParser getParser(PngChunkType chunkType) {
        // System.out.println("chunkType: " + chunkType.name());
        return switch (chunkType) {
            case IHDR -> new ChunkParserIHDR();
            case PLTE -> new ChunkParserPLTE();
            case IDAT -> new ChunkParserIDAT();
            case IEND -> new ChunkParserIEND();
            default -> new FallbackChunkParser();
        };
    }
}
