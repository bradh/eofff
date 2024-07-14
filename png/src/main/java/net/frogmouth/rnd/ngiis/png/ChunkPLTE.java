package net.frogmouth.rnd.ngiis.png;

import java.util.ArrayList;
import java.util.List;

public class ChunkPLTE extends PngChunk {
    private final List<PaletteEntry> entries = new ArrayList<>();

    public List<PaletteEntry> getEntries() {
        return entries;
    }

    public void addPaletteEntry(PaletteEntry entry) {
        entries.add(entry);
    }

    @Override
    public String toString() {
        return "PLTE. entries=" + entries;
    }
}
