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

package org.mobicents.protocols.ss7.map.service.lsm;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.ResponseTime;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * TODO introduce all params used by LCSQoS and test
 * 
 * @author amit bhayani
 * 
 */
public class LCSQoSImpl implements LCSQoS, MAPAsnPrimitive {

	private static final int _TAG_HORIZONTAL_ACCURACY = 0;
	private static final int _TAG_VERTICAL_COORDINATE_REQUEST = 1;
	private static final int _TAG_VERTICAL_ACCURACY = 2;
	private static final int _TAG_RESPONSE_TIME = 3;
	private static final int _TAG_EXTENSION_CONTAINER = 4;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTag()
	 */
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getTagClass
	 * ()
	 */
	@Override
	public int getTagClass() {
		return Tag.CLASS_CONTEXT_SPECIFIC;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeAll
	 * (org.mobicents.protocols.asn.AsnInputStream)
	 */
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding MWStatus: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream asnIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		AsnInputStream ais = asnIS.readSequenceStreamData(length);

		while (true) {
			if (ais.available() == 0)
				break;

			switch (ais.readTag()) {
			case _TAG_HORIZONTAL_ACCURACY:
				// horizontal-accuracy [0] Horizontal-Accuracy OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding horizontal-accuracy [0] Horizontal-Accuracy: bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				int length1 = ais.readLength();
				this.horizontalAccuracy = new Integer(ais.readOctetStringData(length1)[0]);
				break;
			case _TAG_VERTICAL_COORDINATE_REQUEST:
				// verticalCoordinateRequest [1] NULL OPTIONAL,
				length1 = ais.readLength();
				this.verticalCoordinateRequest = true;
				break;
			case _TAG_VERTICAL_ACCURACY:
				// vertical-accuracy [2] Vertical-Accuracy OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding vertical-accuracy [2] Vertical-Accuracy: bad tag class or not primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				length1 = ais.readLength();
				this.verticalAccuracy = new Integer(ais.readOctetStringData(length1)[0]);
				break;
			case _TAG_RESPONSE_TIME:
				// responseTime [3] ResponseTime OPTIONAL,
				if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Decoding LCSQoS failed. Error while decoding responseTime [3] ResponseTime: bad tag class, tag or not constructed",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.responseTime = new ResponseTimeImpl();
				((ResponseTimeImpl)this.responseTime).decodeAll(ais);
				break;
			case _TAG_EXTENSION_CONTAINER:
				// extensionContainer [4] ExtensionContainer OPTIONAL

				this.extensionContainer = new MAPExtensionContainerImpl();
				((MAPExtensionContainerImpl)this.extensionContainer).decodeAll(ais);
				break;
			default:
				// Do we care?
				ais.advanceElement();
				break;
			}
		}// while
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeAll
	 * (org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding InformServiceCentreRequest: " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		if (this.horizontalAccuracy != null) {
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_HORIZONTAL_ACCURACY, new byte[] { this.horizontalAccuracy.byteValue() });
			} catch (IOException e) {
				throw new MAPException("IOException when encoding parameter horizontalAccuracy: ", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding parameter horizontalAccuracy: ", e);
			}
		}

		if (this.verticalCoordinateRequest != null) {
			try {
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_VERTICAL_COORDINATE_REQUEST);
			} catch (IOException e) {
				throw new MAPException("IOException when encoding parameter verticalCoordinateRequest: ", e);
			} catch (AsnException e) {
				throw new MAPException("IOException when encoding parameter verticalCoordinateRequest: ", e);
			}
		}

		if (this.verticalAccuracy != null) {
			try {
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _TAG_VERTICAL_ACCURACY, new byte[] { this.verticalAccuracy.byteValue() });
			} catch (IOException e) {
				throw new MAPException("IOException when encoding parameter verticalAccuracy: ", e);
			} catch (AsnException e) {
				throw new MAPException("AsnException when encoding parameter verticalAccuracy: ", e);
			}
		}

		if (this.responseTime != null) {
			((ResponseTimeImpl)this.responseTime).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_RESPONSE_TIME);
		}

		if (this.extensionContainer != null) {
			((MAPExtensionContainerImpl)this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_EXTENSION_CONTAINER);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extensionContainer == null) ? 0 : extensionContainer.hashCode());
		result = prime * result + ((horizontalAccuracy == null) ? 0 : horizontalAccuracy.hashCode());
		result = prime * result + ((responseTime == null) ? 0 : responseTime.hashCode());
		result = prime * result + ((verticalAccuracy == null) ? 0 : verticalAccuracy.hashCode());
		result = prime * result + ((verticalCoordinateRequest == null) ? 0 : verticalCoordinateRequest.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LCSQoSImpl other = (LCSQoSImpl) obj;
		if (extensionContainer == null) {
			if (other.extensionContainer != null)
				return false;
		} else if (!extensionContainer.equals(other.extensionContainer))
			return false;
		if (horizontalAccuracy == null) {
			if (other.horizontalAccuracy != null)
				return false;
		} else if (!horizontalAccuracy.equals(other.horizontalAccuracy))
			return false;
		if (responseTime == null) {
			if (other.responseTime != null)
				return false;
		} else if (!responseTime.equals(other.responseTime))
			return false;
		if (verticalAccuracy == null) {
			if (other.verticalAccuracy != null)
				return false;
		} else if (!verticalAccuracy.equals(other.verticalAccuracy))
			return false;
		if (verticalCoordinateRequest == null) {
			if (other.verticalCoordinateRequest != null)
				return false;
		} else if (!verticalCoordinateRequest.equals(other.verticalCoordinateRequest))
			return false;
		return true;
	}

}
