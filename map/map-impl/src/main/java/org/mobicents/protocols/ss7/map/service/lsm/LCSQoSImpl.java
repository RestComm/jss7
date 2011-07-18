/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.service.lsm;

import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPPrimitiveBase;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;

/**
 * TODO introduce all params used by LCSQoS and test
 * 
 * @author amit bhayani
 * 
 */
public class LCSQoSImpl extends MAPPrimitiveBase implements LCSQoS {

	private Integer horizontalAccuracy = null;
	private Integer verticalAccuracy = null;
	private Boolean verticalCoordinateRequest = false;
	private ResponseTime responseTime = null;
	private MAPExtensionContainer extensionContainer = null;

	/**
	 * 
	 */
	public LCSQoSImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param horizontalAccuracy
	 * @param verticalAccuracy
	 * @param verticalCoordinateRequest
	 * @param responseTime
	 * @param extensionContainer
	 */
	public LCSQoSImpl(Integer horizontalAccuracy, Integer verticalAccuracy, Boolean verticalCoordinateRequest, ResponseTime responseTime,
			MAPExtensionContainer extensionContainer) {
		super();
		this.horizontalAccuracy = horizontalAccuracy;
		this.verticalAccuracy = verticalAccuracy;
		this.verticalCoordinateRequest = verticalCoordinateRequest;
		this.responseTime = responseTime;
		this.extensionContainer = extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS#getHorizontalAccuracy
	 * ()
	 */
	@Override
	public Integer getHorizontalAccuracy() {
		return this.horizontalAccuracy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS#
	 * getVerticalCoordinateRequest()
	 */
	@Override
	public Boolean getVerticalCoordinateRequest() {
		return this.verticalCoordinateRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS#getVerticalAccuracy
	 * ()
	 */
	@Override
	public Integer getVerticalAccuracy() {
		return this.verticalAccuracy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS#getResponseTime()
	 */
	@Override
	public ResponseTime getResponseTime() {
		return this.responseTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS#getExtensionContainer
	 * ()
	 */
	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public void decode(Parameter param) throws MAPParsingComponentException {
		Parameter[] parameters = param.getParameters();

		if (parameters == null || parameters.length == 0) {
			// TODO is this error since all the parameters are optional
			return;
		}

		for (int count = 0; count < parameters.length; count++) {
			Parameter p = parameters[count];

			switch (p.getTag()) {

			case 0:
				// horizontal-accuracy [0] Horizontal-Accuracy OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding horizontal-accuracy [0] Horizontal-Accuracy: bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.horizontalAccuracy = new Integer(p.getData()[0]);
				break;
			case 1:
				// verticalCoordinateRequest [1] NULL OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding verticalCoordinateRequest [1] NULL: bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.verticalCoordinateRequest = true;
				break;
			case 2:
				// vertical-accuracy [2] Vertical-Accuracy OPTIONAL,
				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding vertical-accuracy [2] Vertical-Accuracy: bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.verticalAccuracy = new Integer(p.getData()[0]);
				break;
			case 3:
				// responseTime [3] ResponseTime OPTIONAL,

				if (p.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || p.isPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding responseTime [3] ResponseTime: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}

				this.responseTime = new ResponseTimeImpl();
				this.responseTime.decode(p);

				break;
			case 4:
				this.extensionContainer = new MAPExtensionContainerImpl();
				this.extensionContainer.decode(p);
				break;
			default:
				throw new MAPParsingComponentException(
						"Decoding LCSQoS failed. Expected horizontal-accuracy [0], verticalCoordinateRequest [1], vertical-accuracy [2], responseTime [3], extensionContainer [4] but found "
								+ p.getTag(), MAPParsingComponentExceptionReason.MistypedParameter);
			}
		}

	}

	@Override
	public void encode(AsnOutputStream asnOs) throws MAPException {

		if (this.horizontalAccuracy != null) {
			// horizontal-accuracy [0] Horizontal-Accuracy OPTIONAL,
			asnOs.write(0x80);
			asnOs.write(0x01);
			asnOs.write(this.horizontalAccuracy);
		}

		if (this.verticalCoordinateRequest != null) {
			// verticalCoordinateRequest [1] NULL OPTIONAL,
			asnOs.write(0x81);
			asnOs.write(0x00);
		}

		if (this.verticalAccuracy != null) {
			// vertical-accuracy [2] Vertical-Accuracy OPTIONAL,
			asnOs.write(0x82);
			asnOs.write(0x01);
			asnOs.write(this.verticalAccuracy);
		}

		if (this.responseTime != null) {
			// responseTime [3] ResponseTime OPTIONAL,
			Parameter p = this.responseTime.encode();
			p.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(false);
			p.setTag(3);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSQoS failed. Failed to Encode responseTime [3] ResponseTime", e);
			}
		}

		if (this.extensionContainer != null) {
			// extensionContainer [4] ExtensionContainer OPTIONAL,
			Parameter p = this.extensionContainer.encode();
			p.setTag(Tag.CLASS_CONTEXT_SPECIFIC);
			p.setPrimitive(true);
			p.setTag(4);

			try {
				p.encode(asnOs);
			} catch (ParseException e) {
				throw new MAPException("Encoding of LCSQoS failed. Failed to Encode extensionContainer [4] ExtensionContainer", e);
			}
		}
	}

}
