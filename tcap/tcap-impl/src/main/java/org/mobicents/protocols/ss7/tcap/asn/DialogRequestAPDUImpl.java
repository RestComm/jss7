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

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class DialogRequestAPDUImpl implements DialogRequestAPDU {

	private ApplicationContextName acn;
	private UserInformation ui;

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#
	 * getApplicationContextName()
	 */
	public ApplicationContextName getApplicationContextName() {
		return acn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#getProtocolVersion ()
	 */
	public int getProtocolVersion() {

		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#getUserInformation ()
	 */
	public UserInformation getUserInformation() {
		return this.ui;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#
	 * setApplicationContextName
	 * (org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName)
	 */
	public void setApplicationContextName(ApplicationContextName acn) {
		this.acn = acn;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogRequestAPDU#setUserInformation
	 *      (org.mobicents.protocols.ss7.tcap.asn.UserInformation[])
	 */
	public void setUserInformation(UserInformation ui) {
		this.ui = ui;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#getType()
	 */
	public DialogAPDUType getType() {
		return DialogAPDUType.Request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.DialogAPDU#isUniDirectional()
	 */
	public boolean isUniDirectional() {

		return false;
	}

	
	public String toString() {
		return "DialogRequestAPDU[acn=" + acn + ", ui=" + ui + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 *      .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		try {
			AsnInputStream localAis = ais.readSequenceStream();
			
			int tag = localAis.readTag();
			// optional protocol version
			if (tag == ProtocolVersion._TAG_PROTOCOL_VERSION && localAis.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				// we have protocol version on a
				// decode it
				TcapFactory.createProtocolVersion(localAis);
				tag = localAis.readTag();
			}

			// now there is mandatory part
			if (tag != ApplicationContextName._TAG || localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
				throw new ParseException("Error decoding DialogRequestAPDU.application-context-name: bad tag or tagClass, found tag=" + tag + ", tagClass="
						+ localAis.getTagClass());
			this.acn = TcapFactory.createApplicationContextName(localAis);

			// optional sequence.
			if (localAis.available() > 0) {
				// we have optional seq;

				tag = localAis.readTag();
				if (tag != UserInformation._TAG || localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC)
					throw new ParseException("Error decoding DialogRequestAPDU.user-information: bad tag or tagClass, found tag=" + tag + ", tagClass="
							+ localAis.getTagClass());
				this.ui = TcapFactory.createUserInformation(localAis);
			}
		} catch (IOException e) {
			throw new ParseException("IOException while decoding DialogRequestAPDU: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while decoding DialogRequestAPDU: " + e.getMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 *      .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {

		if (acn == null)
			throw new ParseException("Error encoding DialogRequestAPDU: Application Context Name must not be null");
		
		try {
			aos.writeTag(Tag.CLASS_APPLICATION, false, _TAG_REQUEST);
			int pos = aos.StartContentDefiniteLength();
			
			// lets not omit protocol version, we check byte[] in tests, it
			// screws them :)
			ProtocolVersion pv = TcapFactory.createProtocolVersion();
			pv.encode(aos);
			this.acn.encode(aos);
			
			if (ui != null)
				ui.encode(aos);
			
			aos.FinalizeContent(pos);

		} catch (AsnException e) {
			throw new ParseException("IOException while encoding DialogRequestAPDU: " + e.getMessage(), e);
		}

	}

}
