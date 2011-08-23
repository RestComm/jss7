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
import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;

/**
 * @author baranowb
 * @author sergey vetyutnev
 * 
 */
public class TCUniMessageImpl implements TCUniMessage {

	private DialogPortion dp;
	private Component[] component;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#getComponent()
	 */
	public Component[] getComponent() {

		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#getDialogPortion()
	 */
	public DialogPortion getDialogPortion() {

		return dp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#setComponent(org
	 * .mobicents.protocols.ss7.tcap.asn.comp.Component[])
	 */
	public void setComponent(Component[] c) {
		this.component = c;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage#setDialogPortion
	 * (org.mobicents.protocols.ss7.tcap.asn.DialogPortion)
	 */
	public void setDialogPortion(DialogPortion dp) {
		this.dp = dp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#decode(org.mobicents.protocols
	 * .asn.AsnInputStream)
	 */
	public void decode(AsnInputStream ais) throws ParseException {
		try {
			AsnInputStream localAis = ais.readSequenceStream();

			while (true) {
				if (localAis.available() == 0)
					return;
				
				int tag = localAis.readTag();
				if (localAis.isTagPrimitive() || localAis.getTagClass() != Tag.CLASS_APPLICATION)
					throw new ParseException(
							"Error decoding TC-Uni: DialogPortion and Component portion must be constructive and has tag class CLASS_APPLICATION");
				
				switch(tag) {
				case DialogPortion._TAG:
					this.dp = TcapFactory.createDialogPortion(localAis);
					break;
					
				case Component._COMPONENT_TAG:
					AsnInputStream compAis = localAis.readSequenceStream();
					List<Component> cps = new ArrayList<Component>();
					// its iterator :)
					while (compAis.available() > 0) {
						Component c = TcapFactory.createComponent(compAis);
						if(c == null)
						{
							break;
						}
						cps.add(c);
					}

					this.component = new Component[cps.size()];
					this.component = cps.toArray(this.component);
					break;
					
				default:
					throw new ParseException("Error decoding TC-Uni: DialogPortion and Componebt parsing: bad tag - " + tag);
				}
			}
		} catch (IOException e) {
			throw new ParseException("IOException while decoding TC-Uni: " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new ParseException("AsnException while decoding TC-Uni: " + e.getMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.tcap.asn.Encodable#encode(org.mobicents.protocols
	 * .asn.AsnOutputStream)
	 */
	public void encode(AsnOutputStream aos) throws ParseException {
		
		if (this.component == null || this.component.length == 0)
			throw new ParseException("Error encoding TC-Uni: Component portion is mandatory but not defined");
		
		try {
			
			aos.writeTag(Tag.CLASS_APPLICATION, false, _TAG);
			int pos = aos.StartContentDefiniteLength();

			if (this.dp != null)
				this.dp.encode(aos);

			aos.writeTag(Tag.CLASS_APPLICATION, false, Component._COMPONENT_TAG);
			int pos2 = aos.StartContentDefiniteLength();
			for (Component c : this.component) {
				c.encode(aos);
			}
			aos.FinalizeContent(pos2);

			aos.FinalizeContent(pos);
			
		} catch (AsnException e) {
			throw new ParseException("AsnException while encoding TC-Uni: " + e.getMessage(), e);
		}

	}

}
