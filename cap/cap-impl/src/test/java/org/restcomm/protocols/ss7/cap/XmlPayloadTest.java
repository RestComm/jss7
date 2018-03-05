package org.restcomm.protocols.ss7.cap;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javolution.xml.XMLObjectReader;

import org.restcomm.protocols.ss7.cap.service.circuitSwitchedCall.ConnectRequestImpl;


public class XmlPayloadTest {

    //@Test(groups = { "functional.xml.serialize", "circuitSwitchedCall" })
    public void testXMLSerialize() throws Exception {
        File file = new File( "E:\\01\\aaa.txt" );

        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        String line = null;
        while ((line = br.readLine()) != null) {
            // variable line does NOT have new-line-character at the end
            sb.append(line);
            sb.append("\n");
        }
        br.close();

        byte[] rawData = sb.toString().getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
        XMLObjectReader reader = XMLObjectReader.newInstance(bais);
        ConnectRequestImpl copy;
        try {
            copy = reader.read("connect_Request", ConnectRequestImpl.class);
            int g = 0;
        } catch (Exception e) {
            int g1 = 0;
        }

    }
}
