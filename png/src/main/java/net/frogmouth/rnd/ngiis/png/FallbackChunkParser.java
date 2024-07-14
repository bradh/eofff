package net.frogmouth.rnd.ngiis.png;

public class FallbackChunkParser extends ChunkParser {

    public FallbackChunkParser() {}

    @Override
    PngChunk parse(PngParseContext parser, long chunkSize) {
        System.out.println("Fallback parser skipping " + chunkSize + " bytes.");
        parser.skipBytes(chunkSize);
        return new UnhandledPngChunk();
    }
}
