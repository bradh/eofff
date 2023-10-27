package org.frogmouth.rnd.eofff.sidd;

import static org.testng.Assert.*;

import jakarta.xml.bind.JAXBException;
import net.frogmouth.rnd.eofff.sidd.SIDDParser;
import net.frogmouth.rnd.eofff.sidd.v2.gen.EarthModelType;
import net.frogmouth.rnd.eofff.sidd.v2.gen.GeoDataType;
import net.frogmouth.rnd.eofff.sidd.v2.gen.SIDD;
import org.testng.annotations.Test;

/** @author bradh */
public class SIDDParserTest {

    public SIDDParserTest() {}

    @Test
    public void testParse() throws JAXBException {
        String xml =
                "<SIDD xmlns=\"urn:SIDD:2.0.0\" xmlns:ism=\"urn:us:gov:ic:ism:13\" xmlns:sfa=\"urn:SFA:1.2.0\"\n"
                        + "                xmlns:sicommon=\"urn:SICommon:1.0\">\n"
                        + "                <ProductCreation>\n"
                        + "                    <ProcessorInformation>\n"
                        + "                        <Application>sarpy 1.2.5</Application>\n"
                        + "                        <ProcessingDateTime>2021-09-02T00:12:53.779703Z</ProcessingDateTime>\n"
                        + "                        <Site>Unknown</Site>\n"
                        + "                    </ProcessorInformation>\n"
                        + "                    <Classification ism:DESVersion=\"13\" ism:ISMCATCESVersion=\"201903\" ism:classification=\"U\"\n"
                        + "                        ism:compliesWith=\"USGov\" ism:createDate=\"2021-09-02\" ism:ownerProducer=\"USA\"\n"
                        + "                        ism:resourceElement=\"true\" />\n"
                        + "                    <ProductName>Detected Image</ProductName>\n"
                        + "                    <ProductClass>Detected Image</ProductClass>\n"
                        + "                </ProductCreation>\n"
                        + "                <Display>\n"
                        + "                    <PixelType>MONO8I</PixelType>\n"
                        + "                    <NumBands>1</NumBands>\n"
                        + "                    <NonInteractiveProcessing band=\"1\">\n"
                        + "                        <ProductGenerationOptions>\n"
                        + "                            <DataRemapping>\n"
                        + "                                <LUTName>DENSITY</LUTName>\n"
                        + "                                <Predefined>\n"
                        + "                                    <DatabaseName>DENSITY</DatabaseName>\n"
                        + "                                </Predefined>\n"
                        + "                            </DataRemapping>\n"
                        + "                        </ProductGenerationOptions>\n"
                        + "                        <RRDS>\n"
                        + "                            <DownsamplingMethod>DECIMATE</DownsamplingMethod>\n"
                        + "                        </RRDS>\n"
                        + "                    </NonInteractiveProcessing>\n"
                        + "                    <InteractiveProcessing band=\"1\">\n"
                        + "                        <GeometricTransform>\n"
                        + "                            <Scaling>\n"
                        + "                                <AntiAlias>\n"
                        + "                                    <FilterName>AntiAlias</FilterName>\n"
                        + "                                    <FilterBank>\n"
                        + "                                        <Predefined>\n"
                        + "                                            <DatabaseName>BILINEAR</DatabaseName>\n"
                        + "                                        </Predefined>\n"
                        + "                                    </FilterBank>\n"
                        + "                                    <Operation>CONVOLUTION</Operation>\n"
                        + "                                </AntiAlias>\n"
                        + "                                <Interpolation>\n"
                        + "                                    <FilterName>Interpolation</FilterName>\n"
                        + "                                    <FilterBank>\n"
                        + "                                        <Predefined>\n"
                        + "                                            <DatabaseName>BILINEAR</DatabaseName>\n"
                        + "                                        </Predefined>\n"
                        + "                                    </FilterBank>\n"
                        + "                                    <Operation>CONVOLUTION</Operation>\n"
                        + "                                </Interpolation>\n"
                        + "                            </Scaling>\n"
                        + "                            <Orientation>\n"
                        + "                                <ShadowDirection>ARBITRARY</ShadowDirection>\n"
                        + "                            </Orientation>\n"
                        + "                        </GeometricTransform>\n"
                        + "                        <SharpnessEnhancement>\n"
                        + "                            <ModularTransferFunctionEnhancement>\n"
                        + "                                <FilterName>ModularTransferFunctionEnhancement</FilterName>\n"
                        + "                                <FilterBank>\n"
                        + "                                    <Predefined>\n"
                        + "                                        <DatabaseName>BILINEAR</DatabaseName>\n"
                        + "                                    </Predefined>\n"
                        + "                                </FilterBank>\n"
                        + "                                <Operation>CONVOLUTION</Operation>\n"
                        + "                            </ModularTransferFunctionEnhancement>\n"
                        + "                        </SharpnessEnhancement>\n"
                        + "                        <DynamicRangeAdjustment>\n"
                        + "                            <AlgorithmType>NONE</AlgorithmType>\n"
                        + "                            <BandStatsSource>1</BandStatsSource>\n"
                        + "                        </DynamicRangeAdjustment>\n"
                        + "                    </InteractiveProcessing>\n"
                        + "                </Display>\n"
                        + "                <GeoData>\n"
                        + "                    <EarthModel>WGS_84</EarthModel>\n"
                        + "                    <ImageCorners>\n"
                        + "                        <ICP index=\"1:FRFC\">\n"
                        + "                            <sicommon:Lat>48.83655107374327</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.330707882775133</sicommon:Lon>\n"
                        + "                        </ICP>\n"
                        + "                        <ICP index=\"2:FRLC\">\n"
                        + "                            <sicommon:Lat>48.83411005099062</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.261491426352572</sicommon:Lon>\n"
                        + "                        </ICP>\n"
                        + "                        <ICP index=\"3:LRLC\">\n"
                        + "                            <sicommon:Lat>48.87912806432288</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.257814026545359</sicommon:Lon>\n"
                        + "                        </ICP>\n"
                        + "                        <ICP index=\"4:LRFC\">\n"
                        + "                            <sicommon:Lat>48.88157125786822</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.327092403117033</sicommon:Lon>\n"
                        + "                        </ICP>\n"
                        + "                    </ImageCorners>\n"
                        + "                    <ValidData size=\"4\">\n"
                        + "                        <Vertex index=\"1\">\n"
                        + "                            <sicommon:Lat>48.83655107374327</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.330707882775133</sicommon:Lon>\n"
                        + "                        </Vertex>\n"
                        + "                        <Vertex index=\"2\">\n"
                        + "                            <sicommon:Lat>48.83411005099062</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.261491426352572</sicommon:Lon>\n"
                        + "                        </Vertex>\n"
                        + "                        <Vertex index=\"3\">\n"
                        + "                            <sicommon:Lat>48.87912806432288</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.257814026545359</sicommon:Lon>\n"
                        + "                        </Vertex>\n"
                        + "                        <Vertex index=\"4\">\n"
                        + "                            <sicommon:Lat>48.88157125786822</sicommon:Lat>\n"
                        + "                            <sicommon:Lon>2.327092403117033</sicommon:Lon>\n"
                        + "                        </Vertex>\n"
                        + "                    </ValidData>\n"
                        + "                </GeoData>\n"
                        + "                <Measurement>\n"
                        + "                    <PlaneProjection>\n"
                        + "                        <ReferencePoint>\n"
                        + "                            <sicommon:ECEF>\n"
                        + "                                <sicommon:X>4201036.079075577</sicommon:X>\n"
                        + "                                <sicommon:Y>168310.829971347</sicommon:Y>\n"
                        + "                                <sicommon:Z>4780238.735078039</sicommon:Z>\n"
                        + "                            </sicommon:ECEF>\n"
                        + "                            <sicommon:Point>\n"
                        + "                                <sicommon:Row>3948</sicommon:Row>\n"
                        + "                                <sicommon:Col>4001</sicommon:Col>\n"
                        + "                            </sicommon:Point>\n"
                        + "                        </ReferencePoint>\n"
                        + "                        <SampleSpacing>\n"
                        + "                            <sicommon:Row>0.6358467321709981</sicommon:Row>\n"
                        + "                            <sicommon:Col>0.6358467321709981</sicommon:Col>\n"
                        + "                        </SampleSpacing>\n"
                        + "                        <TimeCOAPoly order1=\"0\" order2=\"0\">\n"
                        + "                            <sicommon:Coef exponent1=\"0\" exponent2=\"0\">1.268213184</sicommon:Coef>\n"
                        + "                        </TimeCOAPoly>\n"
                        + "                        <ProductPlane>\n"
                        + "                            <RowUnitVector>\n"
                        + "                                <sicommon:X>-0.7492673203547953</sicommon:X>\n"
                        + "                                <sicommon:Y>-0.08343156435724985</sicommon:Y>\n"
                        + "                                <sicommon:Z>0.6569913673080087</sicommon:Z>\n"
                        + "                            </RowUnitVector>\n"
                        + "                            <ColUnitVector>\n"
                        + "                                <sicommon:X>0.08013462004084643</sicommon:X>\n"
                        + "                                <sicommon:Y>-0.9961653839507845</sicommon:Y>\n"
                        + "                                <sicommon:Z>-0.03511367951518973</sicommon:Z>\n"
                        + "                            </ColUnitVector>\n"
                        + "                        </ProductPlane>\n"
                        + "                    </PlaneProjection>\n"
                        + "                    <PixelFootprint>\n"
                        + "                        <sicommon:Row>7885</sicommon:Row>\n"
                        + "                        <sicommon:Col>8003</sicommon:Col>\n"
                        + "                    </PixelFootprint>\n"
                        + "                    <ARPPoly>\n"
                        + "                        <sicommon:X order1=\"6\">\n"
                        + "                            <sicommon:Coef exponent1=\"0\">4874540.519502115</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"1\">-557.7138761123658</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"2\">-2.4087758009956888</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"3\">0.00010594435572476652</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"4\">1.9190778558069724e-07</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"5\">-7.132571137999018e-12</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"6\">3.619666422585563e-14</sicommon:Coef>\n"
                        + "                        </sicommon:X>\n"
                        + "                        <sicommon:Y order1=\"6\">\n"
                        + "                            <sicommon:Coef exponent1=\"0\">208366.64683537246</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"1\">7222.8833358197235</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"2\">-0.08482622215641994</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"3\">-0.0013326821208216025</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"4\">3.03193662875148e-09</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"5\">6.934396393398494e-11</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"6\">2.524364429144624e-14</sicommon:Coef>\n"
                        + "                        </sicommon:Y>\n"
                        + "                        <sicommon:Z order1=\"6\">\n"
                        + "                            <sicommon:Coef exponent1=\"0\">4882053.379816752</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"1\">241.23665375685013</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"2\">-2.961228559973441</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"3\">-5.0666444085383917e-05</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"4\">2.988109564047803e-07</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"5\">3.732719091882431e-12</sicommon:Coef>\n"
                        + "                            <sicommon:Coef exponent1=\"6\">-6.704823755974243e-15</sicommon:Coef>\n"
                        + "                        </sicommon:Z>\n"
                        + "                    </ARPPoly>\n"
                        + "                    <ValidData size=\"4\">\n"
                        + "                        <Vertex index=\"1\">\n"
                        + "                            <sicommon:Row>0</sicommon:Row>\n"
                        + "                            <sicommon:Col>0</sicommon:Col>\n"
                        + "                        </Vertex>\n"
                        + "                        <Vertex index=\"2\">\n"
                        + "                            <sicommon:Row>0</sicommon:Row>\n"
                        + "                            <sicommon:Col>8003</sicommon:Col>\n"
                        + "                        </Vertex>\n"
                        + "                        <Vertex index=\"3\">\n"
                        + "                            <sicommon:Row>7885</sicommon:Row>\n"
                        + "                            <sicommon:Col>8003</sicommon:Col>\n"
                        + "                        </Vertex>\n"
                        + "                        <Vertex index=\"4\">\n"
                        + "                            <sicommon:Row>7885</sicommon:Row>\n"
                        + "                            <sicommon:Col>0</sicommon:Col>\n"
                        + "                        </Vertex>\n"
                        + "                    </ValidData>\n"
                        + "                </Measurement>\n"
                        + "                <ExploitationFeatures>\n"
                        + "                    <Collection identifier=\"12FEB21capella-2074558\">\n"
                        + "                        <Information>\n"
                        + "                            <SensorName>capella-2</SensorName>\n"
                        + "                            <RadarMode>\n"
                        + "                                <sicommon:ModeType>SPOTLIGHT</sicommon:ModeType>\n"
                        + "                            </RadarMode>\n"
                        + "                            <CollectionDateTime>2021-02-12T07:45:58.011740Z</CollectionDateTime>\n"
                        + "                            <CollectionDuration>2.895360496</CollectionDuration>\n"
                        + "                            <Resolution>\n"
                        + "                                <sicommon:Range>0.3095265058082523</sicommon:Range>\n"
                        + "                                <sicommon:Azimuth>0.418459211659332</sicommon:Azimuth>\n"
                        + "                            </Resolution>\n"
                        + "                            <Polarization>\n"
                        + "                                <TxPolarization>H</TxPolarization>\n"
                        + "                                <RcvPolarization>H</RcvPolarization>\n"
                        + "                            </Polarization>\n"
                        + "                        </Information>\n"
                        + "                        <Geometry>\n"
                        + "                            <Azimuth>177.109687017686</Azimuth>\n"
                        + "                            <Slope>49.71855189791048</Slope>\n"
                        + "                            <Squint>-89.94458773444046</Squint>\n"
                        + "                            <Graze>49.71855189791048</Graze>\n"
                        + "                            <Tilt>-0.05394358893823892</Tilt>\n"
                        + "                            <DopplerConeAngle>90.0000004497284</DopplerConeAngle>\n"
                        + "                        </Geometry>\n"
                        + "                        <Phenomenology>\n"
                        + "                            <Shadow>\n"
                        + "                                <sicommon:Angle>-0.1690168481276828</sicommon:Angle>\n"
                        + "                                <sicommon:Magnitude>0.8475058055800748</sicommon:Magnitude>\n"
                        + "                            </Shadow>\n"
                        + "                            <Layover>\n"
                        + "                                <sicommon:Angle>179.7602724814589</sicommon:Angle>\n"
                        + "                                <sicommon:Magnitude>1.179933818694513</sicommon:Magnitude>\n"
                        + "                            </Layover>\n"
                        + "                            <MultiPath>179.7898307980286</MultiPath>\n"
                        + "                            <GroundTrack>-90.21016949274019</GroundTrack>\n"
                        + "                        </Phenomenology>\n"
                        + "                    </Collection>\n"
                        + "                    <Product>\n"
                        + "                        <Resolution>\n"
                        + "                            <sicommon:Row>0.4787407247536901</sicommon:Row>\n"
                        + "                            <sicommon:Col>0.4184595383963049</sicommon:Col>\n"
                        + "                        </Resolution>\n"
                        + "                        <Ellipticity>1.144054994154048</Ellipticity>\n"
                        + "                        <Polarization>\n"
                        + "                            <TxPolarizationProc>H</TxPolarizationProc>\n"
                        + "                            <RcvPolarizationProc>H</RcvPolarizationProc>\n"
                        + "                        </Polarization>\n"
                        + "                    </Product>\n"
                        + "                </ExploitationFeatures>\n"
                        + "            </SIDD>";
        System.out.println(xml);
        SIDDParser uut = new SIDDParser();
        SIDD result = uut.parse(xml);
        assertEquals(result.getExploitationFeatures().getProduct().size(), 1);
        GeoDataType geodata = result.getGeoData();
        assertEquals(geodata.getEarthModel(), EarthModelType.WGS_84);
        assertEquals(geodata.getImageCorners().getICP().size(), 4);
    }
}