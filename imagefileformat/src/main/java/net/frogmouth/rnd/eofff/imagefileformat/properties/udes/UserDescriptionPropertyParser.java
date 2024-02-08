package net.frogmouth.rnd.eofff.imagefileformat.properties.udes;

import static net.frogmouth.rnd.eofff.imagefileformat.properties.udes.UserDescriptionProperty.UDES_ATOM;

import com.google.auto.service.AutoService;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;
import net.frogmouth.rnd.eofff.isobmff.iprp.AbstractItemProperty;
import net.frogmouth.rnd.eofff.isobmff.iprp.ItemFullPropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AutoService(net.frogmouth.rnd.eofff.isobmff.iprp.PropertyParser.class)
public class UserDescriptionPropertyParser extends ItemFullPropertyParser {

    private static final Logger LOG = LoggerFactory.getLogger(UserDescriptionPropertyParser.class);

    public UserDescriptionPropertyParser() {}

    @Override
    public FourCC getFourCC() {
        return UDES_ATOM;
    }

    @Override
    public AbstractItemProperty parse(
            ParseContext parseContext, long initialOffset, long boxSize, FourCC boxName) {
        UserDescriptionProperty box = new UserDescriptionProperty();
        int version = parseContext.readByte();
        box.setVersion(version);
        if (!isSupportedVersion(version)) {
            LOG.warn("Got unsupported version {}, parsing as base box.", version);
            return null;
        }
        box.setFlags(parseFlags(parseContext));
        box.setLang(parseContext.readNullDelimitedString(boxSize));
        box.setDescriptiveName(parseContext.readNullDelimitedString(boxSize));
        box.setDescription(parseContext.readNullDelimitedString(boxSize));
        box.setTags(parseContext.readNullDelimitedString(boxSize));
        return box;
    }

    private boolean isSupportedVersion(int version) {
        return (version == 0x00);
    }
}
