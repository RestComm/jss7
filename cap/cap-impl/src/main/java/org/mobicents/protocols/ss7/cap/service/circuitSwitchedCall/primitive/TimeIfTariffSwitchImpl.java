/*
 * TeleStax, Open Source Cloud Communications  
 * Copyright 2012, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.TimeIfTariffSwitch;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 * 
 * @author sergey vetyutnev
 * @author Amit Bhayani
 * 
 */
public class TimeIfTariffSwitchImpl implements TimeIfTariffSwitch, CAPAsnPrimitive {

	private static final String TIME_SINCE_TARIFF_SWITCH = "timeSinceTariffSwitch";
	private static final String TARIFF_SWITCH_INTERVAL = "tariffSwitchInterval";

	public static final int _ID_timeSinceTariffSwitch = 0;
	public static final int _ID_tariffSwitchInterval = 1;

	public static final String _PrimitiveName = "TimeIfTariffSwitch";

	private int timeSinceTariffSwitch;
	private Integer tariffSwitchInterval;

	public TimeIfTariffSwitchImpl() {
	}

	public TimeIfTariffSwitchImpl(int timeSinceTariffSwitch, Integer tariffSwitchInterval) {
		this.timeSinceTariffSwitch = timeSinceTariffSwitch;
		this.tariffSwitchInterval = tariffSwitchInterval;
	}

	@Override
	public int getTimeSinceTariffSwitch() {
		return timeSinceTariffSwitch;
	}

	@Override
	public Integer getTariffSwitchInterval() {
		return tariffSwitchInterval;
	}

	@Override
	public int getTag() throws CAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
					+ e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
					+ e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
					+ ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": "
					+ e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": "
					+ e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
		} catch (MAPParsingComponentException e) {
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName
					+ ": " + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException,
			MAPParsingComponentException, IOException, AsnException {

		this.timeSinceTariffSwitch = -1;
		this.tariffSwitchInterval = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_timeSinceTariffSwitch:
					this.timeSinceTariffSwitch = (int) ais.readInteger();
					break;
				case _ID_tariffSwitchInterval:
					this.tariffSwitchInterval = (int) ais.readInteger();
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}

		if (this.timeSinceTariffSwitch == -1)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
					+ ": timeSinceTariffSwitch is mandatory but not found",
					CAPParsingComponentExceptionReason.MistypedParameter);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws CAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream aos) throws CAPException {

		try {
			if (this.timeSinceTariffSwitch < 0 || this.timeSinceTariffSwitch > 864000)
				throw new CAPException("Error while encoding " + _PrimitiveName
						+ ": timeSinceTariffSwitch must be from 0 to 864000");

			aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_timeSinceTariffSwitch, this.timeSinceTariffSwitch);

			if (this.tariffSwitchInterval != null) {
				if (this.tariffSwitchInterval < 1 || this.tariffSwitchInterval > 864000)
					throw new CAPException("Error while encoding " + _PrimitiveName
							+ ": tariffSwitchInterval must be from 1 to 864000");
				aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_tariffSwitchInterval, this.tariffSwitchInterval);
			}
		} catch (IOException e) {
			throw new CAPException("IOException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName);
		sb.append(" [");

		sb.append("timeSinceTariffSwitch=");
		sb.append(timeSinceTariffSwitch);
		if (this.tariffSwitchInterval != null) {
			sb.append(", tariffSwitchInterval=");
			sb.append(tariffSwitchInterval);
		}

		sb.append("]");

		return sb.toString();
	}

	/**
	 * XML Serialization/Deserialization
	 */
	protected static final XMLFormat<TimeIfTariffSwitchImpl> TIME_IF_TARIFF_SWITCH_XML = new XMLFormat<TimeIfTariffSwitchImpl>(
			TimeIfTariffSwitchImpl.class) {

		@Override
		public void read(javolution.xml.XMLFormat.InputElement xml, TimeIfTariffSwitchImpl timeIfTariffSwitch)
				throws XMLStreamException {
			timeIfTariffSwitch.timeSinceTariffSwitch = xml.get(TIME_SINCE_TARIFF_SWITCH, Integer.class);
			timeIfTariffSwitch.tariffSwitchInterval = xml.get(TARIFF_SWITCH_INTERVAL, Integer.class);
		}

		@Override
		public void write(TimeIfTariffSwitchImpl timeIfTariffSwitch, javolution.xml.XMLFormat.OutputElement xml)
				throws XMLStreamException {
			xml.add(timeIfTariffSwitch.timeSinceTariffSwitch, TIME_SINCE_TARIFF_SWITCH, Integer.class);

			if (timeIfTariffSwitch.tariffSwitchInterval != null) {
				xml.add(timeIfTariffSwitch.tariffSwitchInterval, TARIFF_SWITCH_INTERVAL, Integer.class);
			}
		}
	};
}
