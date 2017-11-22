/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.ss7.management.transceiver;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

    @Test
    public void testMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8192);

        Message msg = messageFactory.createMessage("Some message");
        msg.encode(buffer);

        buffer.flip();

        Message msg1 = messageFactory.createMessage(buffer);

        assertEquals(msg.toString(), msg1.toString());
    }

    // message over allocated byteBuf size
    @Test
    public void testLongMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(120);

        String segment = "Some message. ";
        String content = "";
        int messageSize = (int) ((double) buffer.capacity() * 1.5);
        while (content.length() < messageSize) {
            content += segment;
        }
        messageSize = content.length();
        Message msg = messageFactory.createMessage(content);

        int count = 0;
        while (msg.hasMoreData()) {
            // header
            messageSize += 4;

            buffer.clear();
            buffer.rewind();

            msg.encode(buffer);
            buffer.flip();

            if (messageSize >= buffer.capacity()) {
                assertEquals(buffer.remaining(), buffer.capacity());
            } else {
                assertEquals(buffer.remaining(), messageSize);
            }

            messageSize -= buffer.capacity();
            count++;
        }

        assertEquals(count, 2);
    }

    // message over allocated byteBuf size with \n in it
    @Test
    public void testLongMultilineMessage() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(120);

        String segment = "Some message2.\n";
        String content = "";
        int messageSize = (int) ((double) buffer.capacity() * 1.5);
        while (content.length() < messageSize) {
            content += segment;
        }
        messageSize = content.length();
        Message msg = messageFactory.createMessage(content);

        int count = 0;
        while (msg.hasMoreData()) {
            // header
            messageSize += 4;

            buffer.clear();
            buffer.rewind();

            msg.encode(buffer);
            buffer.flip();
            
            int bytesToWrite = (buffer.capacity() - 4)/segment.length() * segment.length() + 4;

            if (messageSize >= buffer.capacity()) {
                assertEquals(buffer.remaining(), bytesToWrite);
            } else {
                assertEquals(buffer.remaining(), messageSize);
            }

            messageSize -= bytesToWrite;
            count++;
        }

        assertEquals(count, 2);
    }
}
