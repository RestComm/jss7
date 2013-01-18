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
package org.mobicents.protocols.ss7.cap.service.gprs.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.ROVolumeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.primitives.SequenceBase;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class ROVolumeIfTariffSwitchImpl  extends SequenceBase implements ROVolumeIfTariffSwitch{
	
	public static final int _ID_roVolumeSinceLastTariffSwitch = 0;
	public static final int _ID_roVolumeTariffSwitchInterval = 1;
	
	public static final int _ID_ROVolumeIfTariffSwitch = 1;

	private Integer roVolumeSinceLastTariffSwitch;
	private Integer roVolumeTariffSwitchInterval;
	
	public ROVolumeIfTariffSwitchImpl() {
		super("ROVolumeIfTariffSwitch");
	}
	
	public ROVolumeIfTariffSwitchImpl(
			Integer roVolumeSinceLastTariffSwitch,Integer roVolumeTariffSwitchInterval) {
		super("ROVolumeIfTariffSwitch");
		this.roVolumeSinceLastTariffSwitch = roVolumeSinceLastTariffSwitch;
		this.roVolumeTariffSwitchInterval = roVolumeTariffSwitchInterval;
	}
	
	public Integer getROVolumeSinceLastTariffSwitch(){
		return this.roVolumeSinceLastTariffSwitch;
	}

	public Integer getROVolumeTariffSwitchInterval(){
		return this.roVolumeTariffSwitchInterval ;
	}
	
	public int getTag() throws CAPException {
		return _ID_ROVolumeIfTariffSwitch;
	}

	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}
	
	@Override
	protected void _decode(AsnInputStream asnIS, int length)
			throws CAPParsingComponentException, IOException, AsnException {

		this.roVolumeSinceLastTariffSwitch = -1;
		this.roVolumeTariffSwitchInterval = -1;

		AsnInputStream ais = asnIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				
				switch (tag) {
				case _ID_roVolumeSinceLastTariffSwitch:
					if (!ais.isTagPrimitive())
						throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ".roVolumeSinceLastTariffSwitch: Parameter is not primitive",
								CAPParsingComponentExceptionReason.MistypedParameter);
					this.roVolumeSinceLastTariffSwitch = (int) ais.readInteger();
					break;
				case _ID_roVolumeTariffSwitchInterval:
					if (!ais.isTagPrimitive())
						throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ".roVolumeTariffSwitchInterval: Parameter is not primitive",
								CAPParsingComponentExceptionReason.MistypedParameter);
					this.roVolumeTariffSwitchInterval = (int) ais.readInteger();
					break;
				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}
	}
	
	@Override
	public void encodeData(AsnOutputStream asnOs) throws CAPException {
		
		try {
			if(this.roVolumeSinceLastTariffSwitch != null)
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_roVolumeSinceLastTariffSwitch, this.roVolumeSinceLastTariffSwitch.intValue());
			
			if(this.roVolumeTariffSwitchInterval != null)
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_roVolumeTariffSwitchInterval, this.roVolumeTariffSwitchInterval.intValue());
			
		}catch (IOException e) {
			throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
		
	}
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName + " [");

		if (this.roVolumeSinceLastTariffSwitch != null) {
			sb.append("roVolumeSinceLastTariffSwitch=");
			sb.append(this.roVolumeSinceLastTariffSwitch.toString());
			sb.append(", ");
		}
		
		if (this.roVolumeTariffSwitchInterval != null) {
			sb.append("roVolumeTariffSwitchInterval=");
			sb.append(this.roVolumeTariffSwitchInterval.toString());
		}

		sb.append("]");

		return sb.toString();
	}

}
