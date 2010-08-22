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

package org.mobicents.protocols.ss7.sccp.parameter;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * 
 * @author Oleg Kulikov
 */
public interface GlobalTitle {
    
	public void decode(InputStream in) throws IOException;

	public void encode(OutputStream in) throws IOException;

	public int getTranslationType();

	public void setTranslationType(int translationType);

	public int getNumberingPlan();

	public void setNumberingPlan(int numberingPlan);

	public int getEncodingScheme();

	public void setEncodingScheme(int encodingScheme);

	public int getNatureOfAddress();

	public void setNatureOfAddress(int natureOfAddress);

	public String getDigits();

	public void setDigits(String digits);

}
