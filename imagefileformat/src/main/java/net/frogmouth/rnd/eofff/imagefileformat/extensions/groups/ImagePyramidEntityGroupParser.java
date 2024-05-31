package net.frogmouth.rnd.eofff.imagefileformat.extensions.groups;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.grpl.AbstractEntityToGroupBoxParser;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroup;
import net.frogmouth.rnd.eofff.isobmff.grpl.EntityToGroupParser;

@AutoService(EntityToGroupParser.class)
public class ImagePyramidEntityGroupParser extends AbstractEntityToGroupBoxParser {

    @Override
    public FourCC getFourCC() {
        return ImagePyramidEntityGroup.PYMD_ATOM;
    }

    @Override
    public EntityToGroup parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        ImagePyramidEntityGroup pymd = new ImagePyramidEntityGroup();
        parseEntityToGroupBox(parseContext, pymd, initialOffset, boxSize, boxName);
        pymd.setTileSizeX(parseContext.readUnsignedInt16());
        pymd.setTileSizeY(parseContext.readUnsignedInt16());
        for (int i = 0; i < pymd.getEntities().size(); i++) {
            PyramidEntityEntry entry =
                    new PyramidEntityEntry(
                            parseContext.readUnsignedInt16(),
                            parseContext.readUnsignedInt16(),
                            parseContext.readUnsignedInt16());
            pymd.addPyramidEntry(entry);
        }
        return pymd;
    }
}
