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
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.ISUPParameter;

/**
 * Base class for all parameters. It defines some methods that must be
 * implemented by specific parameter and not visible to end user
 * 
 * @author baranowb
 * 
 */
public abstract class AbstractISUPParameter implements ISUPParameter {
	
	//protected Logger logger = Logger.getLogger(this.getClass().getName());

	public int encode(ByteArrayOutputStream bos) throws ParameterException {
		// FIXME: this has to be removed, we should not create separate arrays?
		byte[] b = encode();
		try{
			bos.write(b);
		}catch(Exception e)
		{
			//add code.
			throw new ParameterException("Failed to encode parameter", e);
		}
		return b.length;
	}

	/**
	 * Decodes this element from passed byte[] array. This array must contain
	 * only element data. however in case of constructor elements it may contain
	 * more information elements that consist of tag, length and contents
	 * elements, this has to be handled by specific implementation of this
	 * method.
	 * 
	 * @param b
	 *            - array containing body of parameter.
	 * @return
	 */
	public abstract int decode(byte[] b) throws ParameterException;

	/**
	 * Encodes elements as byte[].It contains body, tag and length should be added by enclosing element. ( See B.4/Q.763 - page 119)
	 * 
	 * @return byte[] with encoded element.
	 * @throws IOException
	 */
	public abstract byte[] encode() throws ParameterException;

}
