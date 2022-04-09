package net.frogmouth.rnd.eofff.dash.mpd;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.frogmouth.rnd.eofff.dash.mpd.gen.MPDtype;
import org.testng.annotations.Test;

public class ParserTest {

    @Test
    public void exampleG1() {
        File xmlFile = new File("src/main/xsd/DASHSchema/example_G1.mpd");
        XMLInputFactory factory = XMLInputFactory.newInstance();
        JAXBContext jaxbContext;
        try {
            InputStream inputStream = new FileInputStream(xmlFile);
            XMLStreamReader xmlStreamReader = factory.createXMLStreamReader(inputStream);
            jaxbContext = JAXBContext.newInstance(MPDtype.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<MPDtype> rootElement =
                    jaxbUnmarshaller.unmarshal(xmlStreamReader, MPDtype.class);
            MPDtype mpd = rootElement.getValue();
            System.out.println(mpd);
        } catch (JAXBException | FileNotFoundException | XMLStreamException ex) {
            Logger.getLogger(ParserTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
