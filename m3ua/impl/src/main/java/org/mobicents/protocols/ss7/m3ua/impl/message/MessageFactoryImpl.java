/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, encode to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl.message;

import org.mobicents.protocols.ss7.m3ua.message.*;
import java.nio.ByteBuffer;

/**
 *
 * @author kulikov
 */
public class MessageFactoryImpl implements MessageFactory {
    //header binary view
    private byte[] header = new byte[8];
    //variable length parameters
    private byte[] params;
    //current position either in header or in params
    private int pos = 0;
    //flag indicating that header is completely read
    private boolean isHeaderReady = false;
    
    //the length of message's parameters
    private int length;
    //message instance
    private M3UAMessageImpl message;
    
    public M3UAMessageImpl createMessage(int messageClass, int messageType) {
        switch (messageClass) {
            case MessageClass.TRANSFER_MESSAGES :
                switch (messageType) {
                    case MessageType.PAYLOAD : return new TransferMessageImpl();
                }
                break;
        }
        return null;
    }
    
    public M3UAMessageImpl createMessage(ByteBuffer buffer) {
        //fill header buffer completely before start parsing header
        if (!isHeaderReady) {
            //the amount of data possible to read is determined as 
            //minimal remainder either of header buffer or of the input buffer
            int len = Math.min(header.length - pos, buffer.remaining());
            buffer.get(header, pos, len);
            
            //update cursor postion in the header's buffer
            pos += len;
            
            //header completed?
            isHeaderReady = pos == header.length;
            
            if (!isHeaderReady) {
                //no more data available
                return null;
            }
            
            //obtain message class and type from header
            int messageClass = header[2] & 0xff;                
            int messageType = header[3] & 0xff;
                
            //construct new message instance
            message = this.createMessage(messageClass, messageType);
            
            //obtain remaining length of the message and prepare buffer
            length = ((header[4] & 0xff << 24) | 
                      (header[5] & 0xff << 16) | 
                      (header[6] & 0xff << 8) |
                      (header[7] & 0xff))- 8;
            params = new byte[length];
            
            //finally switch cursor position
            pos = 0;                
        }
        
        //at this point we must recheck remainder of the input buffer
        //because possible case when input buffer fits exactly to the header
        if (!buffer.hasRemaining()) {
            return null;
        }
        
        //again, reading all parameters before parsing
        
        //compute available or required data
        int len = Math.min(params.length, buffer.remaining());
        buffer.get(params, pos, len);
        
        //update cursor position
        pos += len;
        
        //end of message not reached
        if (pos < params.length) {
            return null;
        }
        
        //end of message reached and most probably some data remains in buffer
        //do not touch remainder of the input buffer, next call to this method
        //will proceed remainder
        
        
        //parsing params of this message
         message.decode(params);
            
        //switch factory for receiving new message
        this.isHeaderReady = false;
        this.pos = 0;
            
            //return 
        return message;
    }
}
