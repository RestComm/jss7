package org.mobicents.ss7.management.transceiver;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageTest {
    private MessageFactory messageFactory = new MessageFactory();

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

        Message msg = messageFactory.createMessage("Some message");
        msg.encode(buffer);

        buffer.flip();

        Message msg1 = messageFactory.createMessage(buffer);


        assertEquals(msg.toString(), msg1.toString());
    }
}
