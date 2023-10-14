package net.frogmouth.rnd.eofff.sidd;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.sidd.v2.gen.SIDD;

public class SIDDParser {

    public SIDD parse(String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SIDD.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        SIDD sidd = (SIDD) unmarshaller.unmarshal(is);
        return sidd;
    }
}
