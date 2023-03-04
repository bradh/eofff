package net.frogmouth.rnd.eofff.imagefileformat;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;

public abstract class ConformanceTestSupport {

    protected List<Box> boxes;

    public ConformanceTestSupport() {}

    protected abstract Path getExample();

    protected Path getPathFromResourceName(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return Paths.get(classLoader.getResource(fileName).getPath());
    }
}
