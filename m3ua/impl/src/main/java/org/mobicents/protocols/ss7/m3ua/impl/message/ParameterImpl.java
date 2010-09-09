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
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.m3ua.impl.message;

import org.mobicents.protocols.ss7.m3ua.message.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 *
 * @author kulikov
 */
public abstract class ParameterImpl implements Parameter {

    protected short tag;
    protected short length;

    public short getTag() {
        return tag;
    }

    protected abstract byte[] getValue();
    
    public void encode(OutputStream out) throws IOException {
        //obtain encoded value 
        byte[] value = getValue();
        
        //encode tag
        out.write((byte)(tag >> 8));        
        out.write((byte)(tag));        
        
        //encode length including value, tag and length field itself
        length = (short)(value.length + 4);
        
        out.write((byte)(length >> 8));        
        out.write((byte)(length));        
        
        //encode value
        out.write(value);        
    }
    
    public void write(ByteBuffer buffer) {
        //obtain encoded value 
        byte[] value = getValue();
        
        //encode tag
        buffer.put((byte)(tag >> 8));        
        buffer.put((byte)(tag));        
        
        //encode length including value, tag and length field itself
        length = (short)(value.length + 4);
        
        buffer.put((byte)(length >> 8));        
        buffer.put((byte)(length));        
        
        //encode value
        buffer.put(value);        
    }
    
}
