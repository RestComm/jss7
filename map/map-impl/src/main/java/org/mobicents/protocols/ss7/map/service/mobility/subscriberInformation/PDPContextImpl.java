package org.mobicents.protocols.ss7.map.service.mobility.subscriberInformation;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.LIPAPermission;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.PDPContext;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.SIPTOPermission;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;

/**
 * @author amit bhayani
 * 
 */
public class PDPContextImpl implements PDPContext, MAPAsnPrimitive {

	private static final int _ID_pdp_Type = 16;
	private static final int _ID_pdp_Address = 17;
	private static final int _ID_QoS_Subscribed = 18;
	private static final int _ID_vplmnAddressAllowed = 19;
	private static final int _ID_apn = 20;
	private static final int _ID_extensionContainer = 21;
	private static final int _ID_ext_QoS_Subscribed = 0;
	private static final int _ID_pdp_ChargingCharacteristics = 1;
	private static final int _ID_ext2_QoS_Subscribed = 2;
	private static final int _ID_ext3_QoS_Subscribed = 3;
	private static final int _ID_ext4_QoS_Subscribed = 4;
	private static final int _ID_apn_oi_Replacement = 5;
	private static final int _ID_ext_pdp_Type = 6;
	private static final int _ID_ext_pdp_Address = 7;
	private static final int _ID_sipto_Permission = 8;
	private static final int _ID_lipa_Permission = 9;

	private Integer pdpContextId;
	private byte[] pdpType;
	private byte[] pdpAddress;
	private byte[] qosSubscribed;
	private Boolean vplmnAddressAllowed;
	private byte[] apn;
	private MAPExtensionContainer extensionContainer;
	private byte[] extQoSSubscribed;
	private byte[] chargingCharacteristics;
	private byte[] ext2QoSSubscribed;
	private byte[] ext3QoSSubscribed;
	private byte[] ext4QoSSubscribed;
	private byte[] apnoiReplacement;
	private byte[] extpdpType;
	private byte[] extpdpAddress;
	private SIPTOPermission sipToPermission;
	private LIPAPermission lipaPermission;

	/**
	 * 
	 */
	public PDPContextImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getPDPContextId()
	 */
	public Integer getPDPContextId() {
		return this.pdpContextId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getPDPType()
	 */
	public byte[] getPDPType() {
		return this.pdpType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getPDPAddress()
	 */
	public byte[] getPDPAddress() {
		return this.pdpAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getQoSSubscribed()
	 */
	public byte[] getQoSSubscribed() {
		return this.qosSubscribed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #isVPLMNAddressAllowed()
	 */
	public Boolean isVPLMNAddressAllowed() {
		return this.vplmnAddressAllowed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getAPN()
	 */
	public byte[] getAPN() {
		return this.apn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExtensionContainer()
	 */
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExtQoSSubscribed()
	 */
	public byte[] getExtQoSSubscribed() {
		return this.extQoSSubscribed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getChargingCharacteristics()
	 */
	public byte[] getChargingCharacteristics() {
		return this.chargingCharacteristics;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExt2QoSSubscribed()
	 */
	public byte[] getExt2QoSSubscribed() {
		return this.ext2QoSSubscribed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExt3QoSSubscribed()
	 */
	public byte[] getExt3QoSSubscribed() {
		return this.ext3QoSSubscribed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExt4QoSSubscribed()
	 */
	public byte[] getExt4QoSSubscribed() {
		return this.ext4QoSSubscribed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getAPNOIReplacement()
	 */
	public byte[] getAPNOIReplacement() {
		return this.apnoiReplacement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExtPDPType()
	 */
	public byte[] getExtPDPType() {
		return this.extpdpType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getExtPDPAddress()
	 */
	public byte[] getExtPDPAddress() {
		return this.extpdpAddress;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getSIPTOPermission()
	 */
	public SIPTOPermission getSIPTOPermission() {
		return this.sipToPermission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.api.service.subscriberInformation.PDPContext
	 * #getLIPAPermission()
	 */
	public LIPAPermission getLIPAPermission() {
		return this.lipaPermission;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(
	 * org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding PDPContext: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding PDPContext: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding PDPContext: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding PDPContext: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.pdpContextId = null;
		this.pdpType = null;
		this.pdpAddress = null;
		this.qosSubscribed = null;
		this.vplmnAddressAllowed = null;
		this.apn = null;
		this.extensionContainer = null;
		this.extQoSSubscribed = null;
		this.chargingCharacteristics = null;
		this.ext2QoSSubscribed = null;
		this.ext3QoSSubscribed = null;
		this.ext4QoSSubscribed = null;
		this.apnoiReplacement = null;
		this.extpdpType = null;
		this.extpdpAddress = null;
		this.sipToPermission = null;
		this.lipaPermission = null;

		AsnInputStream ais = ansIS.readSequenceStreamData(length);

		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();

			if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {

				switch (tag) {
				case Tag.INTEGER: // AgeOfLocationInformation
					this.pdpContextId = (int) ais.readInteger();
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_pdp_Type:
					this.pdpType = ais.readOctetStringData(length);
					if (this.pdpType.length != 2)
						throw new MAPParsingComponentException("Error while decoding pdpType value must be 2 bytes length, found: " + this.pdpType.length,
								MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_pdp_Address:
					this.pdpAddress = ais.readOctetStringData(length);
					if (this.pdpAddress.length < 1 || this.pdpAddress.length > 16)
						throw new MAPParsingComponentException("Error while decoding pdpAddress value must be 1 to 16 bytes length, found: "
								+ this.pdpAddress.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_QoS_Subscribed:
					this.qosSubscribed = ais.readOctetStringData(length);
					if (this.qosSubscribed.length != 3)
						throw new MAPParsingComponentException("Error while decoding qosSubscribed value must be 3 bytes length, found: "
								+ this.qosSubscribed.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_vplmnAddressAllowed:
					if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC || !ais.isTagPrimitive()) {
						throw new MAPParsingComponentException(
								"Error while decoding PDPContext: Parameter [vplmnAddressAllowed	[19] NULL ] bad tag class, tag or not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					this.vplmnAddressAllowed = true;
					break;

				case _ID_apn:
					this.apn = ais.readOctetStringData(length);
					if (this.apn.length < 2 || this.apn.length > 63)
						throw new MAPParsingComponentException("Error while decoding APN value must be 2 to 63 bytes length, found: " + this.apn.length,
								MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_extensionContainer:
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer).decodeAll(ais);
					break;

				case _ID_ext_QoS_Subscribed:
					this.extQoSSubscribed = ais.readOctetStringData(length);
					if (this.extQoSSubscribed.length < 1 || this.extQoSSubscribed.length > 9)
						throw new MAPParsingComponentException("Error while decoding extQoSSubscribed value must be 1 to 9 bytes length, found: "
								+ this.extQoSSubscribed.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_pdp_ChargingCharacteristics:
					this.chargingCharacteristics = ais.readOctetStringData(length);
					if (this.ext4QoSSubscribed.length != 2)
						throw new MAPParsingComponentException("Error while decoding chargingCharacteristics value must be 2 bytes length, found: "
								+ this.chargingCharacteristics.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_ext2_QoS_Subscribed:
					this.ext2QoSSubscribed = ais.readOctetStringData(length);
					if (this.ext2QoSSubscribed.length < 1 || this.ext2QoSSubscribed.length > 3)
						throw new MAPParsingComponentException("Error while decoding GeographicalInformation value must be 1 to 3 bytes length, found: "
								+ this.ext2QoSSubscribed.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_ext3_QoS_Subscribed:
					this.ext3QoSSubscribed = ais.readOctetStringData(length);
					if (this.ext3QoSSubscribed.length < 1 || this.ext3QoSSubscribed.length > 2)
						throw new MAPParsingComponentException("Error while decoding ext3QoSSubscribed value must be 1 to 2 bytes length, found: "
								+ this.ext3QoSSubscribed.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_ext4_QoS_Subscribed:
					this.ext4QoSSubscribed = ais.readOctetStringData(length);
					if (this.ext4QoSSubscribed.length != 1)
						throw new MAPParsingComponentException("Error while decoding ext4QoSSubscribed value must be 1 bytes length, found: "
								+ this.ext4QoSSubscribed.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_apn_oi_Replacement:
					this.apnoiReplacement = ais.readOctetStringData(length);
					if (this.apnoiReplacement.length < 9 || this.apnoiReplacement.length > 100)
						throw new MAPParsingComponentException("Error while decoding apnoiReplacement value must be 9 to 100 bytes length, found: "
								+ this.apnoiReplacement.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_ext_pdp_Type:
					this.extpdpType = ais.readOctetStringData(length);
					if (this.extpdpType.length != 2)
						throw new MAPParsingComponentException(
								"Error while decoding extpdpType value must be 2 bytes length, found: " + this.extpdpType.length,
								MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_ext_pdp_Address:
					this.extpdpAddress = ais.readOctetStringData(length);
					if (this.extpdpAddress.length < 1 || this.extpdpAddress.length > 16)
						throw new MAPParsingComponentException("Error while decoding extpdpAddress value must be 1 to 16 bytes length, found: "
								+ this.extpdpAddress.length, MAPParsingComponentExceptionReason.MistypedParameter);
					break;

				case _ID_sipto_Permission:
					if (tag != Tag.ENUMERATED || !ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding sipToPermission: Parameter bad tag or tag class or not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					int i1 = (int) ais.readInteger();
					this.sipToPermission = SIPTOPermission.getInstance(i1);
					break;

				case _ID_lipa_Permission:
					if (tag != Tag.ENUMERATED || !ais.isTagPrimitive())
						throw new MAPParsingComponentException("Error while decoding lipaPermission: Parameter bad tag or tag class or not primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					int i2 = (int) ais.readInteger();
					this.lipaPermission = LIPAPermission.getInstance(i2);
					break;

				default:
					ais.advanceElement();
					break;

				}
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, this.getTag());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		try {
			asnOs.writeTag(tagClass, true, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding PDPContext : " + e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		try {
			if (this.pdpContextId != null)
				asnOs.writeInteger((int) this.pdpContextId);

			if (this.pdpType != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdp_Type, this.pdpType);

			if (this.pdpAddress != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdp_Address, this.pdpAddress);

			if (this.qosSubscribed != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_QoS_Subscribed, this.qosSubscribed);

			if (this.vplmnAddressAllowed != null && this.vplmnAddressAllowed)
				asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, _ID_vplmnAddressAllowed);

			if (this.apn != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_apn, this.apn);

			if (this.extensionContainer != null)
				((MAPExtensionContainerImpl) this.extensionContainer).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _ID_extensionContainer);

			if (this.extQoSSubscribed != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext_QoS_Subscribed, this.extQoSSubscribed);

			if (this.chargingCharacteristics != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_pdp_ChargingCharacteristics, this.chargingCharacteristics);

			if (this.ext2QoSSubscribed != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext2_QoS_Subscribed, this.ext2QoSSubscribed);

			if (this.ext3QoSSubscribed != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext3_QoS_Subscribed, this.ext3QoSSubscribed);

			if (this.ext4QoSSubscribed != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext4_QoS_Subscribed, this.ext4QoSSubscribed);

			if (this.apnoiReplacement != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_apn_oi_Replacement, this.apnoiReplacement);

			if (this.extpdpType != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext_pdp_Type, this.extpdpType);

			if (this.extpdpAddress != null)
				asnOs.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, _ID_ext_pdp_Address, this.extpdpAddress);

			if (this.sipToPermission != null)
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_sipto_Permission, this.sipToPermission.getCode());

			if (this.lipaPermission != null)
				asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, _ID_lipa_Permission, this.lipaPermission.getCode());

		} catch (IOException e) {
			throw new MAPException("IOException when encoding PDPContext : " + e.getMessage(), e);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding PDPContext : " + e.getMessage(), e);
		}
	}

}
