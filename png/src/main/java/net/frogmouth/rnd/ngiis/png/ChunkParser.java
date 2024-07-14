package net.frogmouth.rnd.ngiis.png;

public abstract class ChunkParser {

    abstract PngChunk parse(PngParseContext parser, long chunkSize);
}
