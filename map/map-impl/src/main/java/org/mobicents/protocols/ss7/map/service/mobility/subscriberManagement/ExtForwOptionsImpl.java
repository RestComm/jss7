/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptions;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwOptionsForwardingReason;
import org.mobicents.protocols.ss7.map.primitives.OctetStringBase;

/**
*
* @author daniel bichara
*
*/
public class ExtForwOptionsImpl extends OctetStringBase implements ExtForwOptions {

	ExtForwOptionsForwardingReason extForwOptionsForwardingReason = null;

	public ExtForwOptionsImpl() {
		super(1, 5, "ExtForwOptions");
	}

	public ExtForwOptionsImpl(byte[] data) {
		super(1, 5, "ExtForwOptions", data);
		// Bits 3&4
		extForwOptionsForwardingReason = ExtForwOptionsForwardingReason.getInstance((int)(data[0]&0xC)-8);
	}

	public byte[] getData() {
		return data;
	}

	public boolean getNotificationToForwardingParty() {
		/*	--  bit 8: notification to forwarding party
		 	--	0  no notification
			--	1  notification
		*/
		return ((data[0]&0x80) > 0?true:false);
	}

	public boolean getRedirectingPresentation() {
		/*	--  bit 7: redirecting presentation
			--	0 no presentation  
			--	1  presentation
		*/
		return ((data[0]&0x40) > 0?true:false);
	}

	public boolean getNotificationToCallingParty() {
		/*	--  bit 6: notification to calling party
			--	0  no notification
			--	1  notification
		*/
		return ((data[0]&0x20) > 0?true:false);
	}

	public ExtForwOptionsForwardingReason getExtForwOptionsForwardingReason() {
		if (extForwOptionsForwardingReason == null && data != null && data.length > 0) {
			// Bits 3&4
			extForwOptionsForwardingReason = ExtForwOptionsForwardingReason.getInstance((int)(data[0]&0xC)-8);
		}
		return extForwOptionsForwardingReason;
	}
}
