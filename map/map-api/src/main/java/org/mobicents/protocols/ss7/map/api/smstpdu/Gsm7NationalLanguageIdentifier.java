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

package org.mobicents.protocols.ss7.map.api.smstpdu;

import org.mobicents.protocols.ss7.map.api.datacoding.NationalLanguageIdentifier;

/**
 User data header for National Language Locking Shift & National Language Single Shift (Gsm7 encoding)
 * 
 * The national language tables are used for adding the special characters of certain languages that cannot be expressed 
using the GSM default 7 bit alphabet.  
The principle is to use the National Language Identifier to indicate to a receiving entity that the message has been 
encoded using a national language table. Both single shift and locking shift mechanisms are defined.   
The single shift mechanism, as defined in subclause 6.2.1.2.2, applies to a single character and it replaces the GSM 7 bit 
default alphabet extension table defined in subclause 6.2.1.1 with a National Language Single Shift Table (see 
subclause A.2).  
The locking shift mechanism, as defined in subclause 6.2.1.2.3, applies throughout the message, or the current segment 
in case of a concatenated message, and it replaces the GSM 7 bit default alphabet defined in subclause 6.2.1 with a 
National Language Locking Shift Table (see subclause A.3) that defines the whole character set needed for the 
language.   
In case that several languages are used, which require different national language tables, it is recommended to encode 
the message in UCS-2, however it is possible to use both single shift and locking shift with the corresponding tables in a 
single message.  
Implementations based on older reference versions (so-called "legacy implementations") will use the fallback 
mechanisms that are defined in the earlier versions of the specification for handling of unknown characters.   

Look at 3GPP TS 23.038
 *  
 * 
 * @author sergey vetyutnev
 * 
 */
public interface Gsm7NationalLanguageIdentifier extends UserDataHeaderElement {

	public NationalLanguageIdentifier getNationalLanguageIdentifier();

	public byte[] getEncodedInformationElementData();

}
