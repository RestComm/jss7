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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * @author amit bhayani
 * 
 */
public class TCUnidentifiedMessage implements Encodable {

	private static final Logger logger = Logger.getLogger(TCUnidentifiedMessage.class);

	public static final int _TAG_OTX = 0x08;

	public static final int _TAG_DTX = 0x09;

	// mandatory
	private Long originatingTransactionId;
	private Long destinationTransactionId;

	/**
	 * 
	 */
	public TCUnidentifiedMessage() {
	}

	public Long getOriginatingTransactionId() {
		return originatingTransactionId;
	}

	public Long getDestinationTransactionId() {
		return destinationTransactionId;
	}

	public void encode(AsnOutputStream aos) throws ParseException {
		throw new ParseException("Not Supported");
	}

	public void decode(AsnInputStream ais) throws ParseException {
		try {
			AsnInputStream localAis = ais.readSequenceStream();

			int tag = localAis.readTag();
			if (tag != _TAG_OTX) {
				return;
			}

			this.originatingTransactionId = Utils.readTransactionId(localAis);

			tag = localAis.readTag();

			if (tag != _TAG_DTX) {
				return;
			}

			this.destinationTransactionId = Utils.readTransactionId(localAis);

		} catch (IOException e) {
			logger.error("Error while decoding for TCUnidentifiedMessage", e);
		} catch (AsnException e) {
			logger.error("Error while decoding for TCUnidentifiedMessage", e);
		}

	}

}
