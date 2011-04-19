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

package org.mobicents.protocols.ss7.map.dialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.MAPOpenInfo;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPOpenInfoImpl implements MAPOpenInfo {
	
	protected static final int MAP_OPEN_INFO_TAG = 0x00;
	
	protected static final int DESTINATION_REF_TAG = 0x00;
	protected static final int ORIGINATION_REF_TAG = 0x01;
	
	protected static final int  OPEN_INFO_TAG_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	protected static final boolean OPEN_INFO_TAG_PC_PRIMITIVE = true;
	protected static final boolean OPEN_INFO_TAG_PC_CONSTRUCTED = false;
	
	private MAPDialog mapDialog = null;
	
	private AddressString destReference;

	private AddressString origReference;	

	public AddressString getDestReference() {
		return this.destReference;
	}

	public MAPDialog getMAPDialog() {
		return this.mapDialog;
	}

	public AddressString getOrigReference() {
		return this.origReference;
	}

	public void setDestReference(AddressString destReference) {
		this.destReference = destReference;
	}

	public void setMAPDialog(MAPDialog mapDialog) {
		this.mapDialog = mapDialog;
	}

	public void setOrigReference(AddressString origReference) {
		this.origReference = origReference;

	}
	
	
	public void decode(AsnInputStream ais) throws AsnException, IOException, MAPException{
		
		//Definitioon from GSM 09.02 version 5.15.1 Page 690
//		map-open   [0] IMPLICIT SEQUENCE {
//			   destinationReference [0] IMPLICIT OCTET STRING ( SIZE (1
//			   originationReference [1] IMPLICIT OCTET STRING ( SIZE (1
//			   ... ,
//			   extensionContainer SEQUENCE {
//			      privateExtensionList [0] IMPLICIT SEQUENCE SIZE (1 ..
//			         SEQUENCE {
//			            extId      MAP-EXTENSION .&extensionId ( {
//			               ,
//			               ...} ) ,
//			            extType    MAP-EXTENSION .&ExtensionType ( {
//			               ,
//			               ...} { @extId   } ) OPTIONAL} OPTIONAL,
//			      pcs-Extensions [1] IMPLICIT SEQUENCE {
//			         ... } OPTIONAL,
//			      ... } OPTIONAL},
		
		
		byte[] seqData = ais.readSequence();
		
		AsnInputStream localAis = new AsnInputStream(new ByteArrayInputStream(seqData));
		
		int tag;
		
		while(localAis.available() > 0){
			tag = localAis.readTag();
			if(tag == DESTINATION_REF_TAG){
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				localAis.readOctetString(outputStream);
				
				this.destReference = new AddressStringImpl();
				((AddressStringImpl)this.destReference).decode(new AsnInputStream(new ByteArrayInputStream(outputStream.toByteArray())));
				
			} else if(tag == ORIGINATION_REF_TAG){
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				localAis.readOctetString(outputStream);
				
				this.origReference = new AddressStringImpl();
				((AddressStringImpl)this.origReference).decode(new AsnInputStream(new ByteArrayInputStream(outputStream.toByteArray())));
				
			} else if(tag == Tag.SEQUENCE){
				//TODO
			}
		}

	}	
	
	
	public void encode(AsnOutputStream asnOS) throws IOException, MAPException{
		
		AsnOutputStream localAos = new AsnOutputStream();
		
		((AddressStringImpl)this.destReference).encode(localAos);
		byte[] destAddData = localAos.toByteArray();
		
		localAos.reset();
		
		((AddressStringImpl)this.origReference).encode(localAos);
		byte[] origAddData = localAos.toByteArray();
		
		localAos.reset();
		
		localAos.writeTag(OPEN_INFO_TAG_CLASS, OPEN_INFO_TAG_PC_PRIMITIVE, DESTINATION_REF_TAG);
		localAos.writeLength(destAddData.length);
		localAos.write(destAddData);
		
		localAos.writeTag(OPEN_INFO_TAG_CLASS, OPEN_INFO_TAG_PC_PRIMITIVE, ORIGINATION_REF_TAG);
		localAos.writeLength(origAddData.length);
		localAos.write(origAddData);	
		
		byte[] data = localAos.toByteArray();
		
		//Now let us write the MAP OPEN-INFO Tags
		asnOS.writeTag(OPEN_INFO_TAG_CLASS, OPEN_INFO_TAG_PC_CONSTRUCTED, MAP_OPEN_INFO_TAG);
		asnOS.writeLength(data.length);
		asnOS.write(data);
		
	}

}
