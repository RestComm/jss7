/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.mobicents.protocols.ss7.m3ua.impl.message.parm;

import org.mobicents.protocols.ss7.m3ua.impl.message.parms.ProtocolDataImpl;
import org.mobicents.protocols.ss7.m3ua.impl.message.parms.ParameterFactoryImpl;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mobicents.protocols.ss7.m3ua.impl.message.M3UAMessageImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.impl.message.TransferMessageImpl;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class MessageTest {

    private ParameterFactoryImpl parmFactory = new ParameterFactoryImpl();
    private MessageFactoryImpl messageFactory = new MessageFactoryImpl();
    
    public MessageTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getOpc method, of class ProtocolDataImpl.
     */
    @Test
    public void testMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        
        TransferMessageImpl msg = (TransferMessageImpl) messageFactory.createMessage(
                MessageClass.TRANSFER_MESSAGES, 
                MessageType.PAYLOAD);
        ProtocolDataImpl p1 = parmFactory.createProtocolData(1408, 14150, 1, 1, 0, 0, new byte[] {1,2,3,4});
        msg.setData(p1);
        msg.encode(buffer);
        
        buffer.flip();
        
        TransferMessageImpl msg1 = (TransferMessageImpl) messageFactory.createMessage(buffer);
        
        ProtocolDataImpl p2 = (ProtocolDataImpl) msg1.getData();
        
        assertEquals(p1.getTag(), p2.getTag());
        assertEquals(p1.getOpc(), p2.getOpc());
        assertEquals(p1.getDpc(), p2.getDpc());
        assertEquals(p1.getSI(), p2.getSI());
        assertEquals(p1.getNI(), p2.getNI());
        assertEquals(p1.getMP(), p2.getMP());
        assertEquals(p1.getSLS(), p2.getSLS());
    }

}