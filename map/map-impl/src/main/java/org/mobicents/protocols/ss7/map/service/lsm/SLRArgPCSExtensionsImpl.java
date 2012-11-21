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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgPCSExtensions;
import org.mobicents.protocols.ss7.map.primitives.SequenceBase;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SLRArgPCSExtensionsImpl extends SequenceBase implements SLRArgPCSExtensions {

	private static final int _TAGna_ESRK_Request = 0;

	private boolean naEsrkRequest;

	public SLRArgPCSExtensionsImpl() {
		super("SLRArgPCSExtensions");
	}

	public SLRArgPCSExtensionsImpl(boolean naEsrkRequest) {
		super("SLRArgPCSExtensions");

		this.naEsrkRequest = naEsrkRequest;
	}

	@Override
	public boolean getNaEsrkRequest() {
		return naEsrkRequest;
	}

	@Override
	protected void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {

		this.naEsrkRequest = false;

		AsnInputStream ais = asnIS.readSequenceStreamData(length);

		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _TAGna_ESRK_Request:
					// naEsrkRequest
					if (!ais.isTagPrimitive()) {
						throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName
								+ ": Parameter naEsrkRequest is not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
					}
					ais.readNull();
					this.naEsrkRequest = true;
					break;
				default:
					ais.advanceElement();
				}
			} else {
				ais.advanceElement();
			}
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.naEsrkRequest) {
			// naEsrkRequest
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAGna_ESRK_Request);
			} catch (IOException e) {
				throw new MAPException("Error while encoding " + _PrimitiveName + " the optional parameter naEsrkRequest encoding failed ", e);
			} catch (AsnException e) {
				throw new MAPException("Error while encoding " + _PrimitiveName + " the optional parameter naEsrkRequest encoding failed ", e);
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		if (this.naEsrkRequest) {
			sb.append("naEsrkRequest");
		}

		sb.append("]");

		return sb.toString();
	}
}
