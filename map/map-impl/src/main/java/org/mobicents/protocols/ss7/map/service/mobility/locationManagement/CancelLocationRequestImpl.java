package org.mobicents.protocols.ss7.map.service.mobility.locationManagement;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.CancelablePrintJob;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.MAPParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPMessageType;
import org.mobicents.protocols.ss7.map.api.MAPOperationCode;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.EMLPPPriority;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.MAPPrivateExtension;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.MAPDialogMobility;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancelLocationRequest;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.CancellationType;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.IMSIWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.TypeOfUpdate;
import org.mobicents.protocols.ss7.map.primitives.AlertingPatternImpl;
import org.mobicents.protocols.ss7.map.primitives.ExtExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.ExternalSignalInfoImpl;
import org.mobicents.protocols.ss7.map.primitives.GSNAddressImpl;
import org.mobicents.protocols.ss7.map.primitives.IMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;
import org.mobicents.protocols.ss7.map.primitives.LMSIImpl;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerImpl;
import org.mobicents.protocols.ss7.map.service.callhandling.CallReferenceNumberImpl;
import org.mobicents.protocols.ss7.map.service.mobility.MobilityMessageImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.OfferedCamel4CSIsImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.SupportedCamelPhasesImpl;

/**
 * 
 * @author Lasith Waruna Perera
 * 
 */
public class CancelLocationRequestImpl extends MobilityMessageImpl implements
		CancelLocationRequest {

	private static final int TAG_typeOfUpdate = 0;
	private static final int TAG_mtrfSupportedAndAuthorized = 1;
	private static final int TAG_mtrfSupportedAndNotAuthorized = 2;
	private static final int TAG_newMSCNumber = 3;
	private static final int TAG_newVLRNumber = 4;
	private static final int TAG_newLmsi = 5;

	public static final int TAG_cancelLocationRequest = 3;
	public static final String _PrimitiveName = "CancelLocationRequest";

	private IMSI imsi;
	private IMSIWithLMSI imsiWithLmsi;
	private CancellationType cancellationType;
	private MAPExtensionContainer extensionContainer;
	private TypeOfUpdate typeOfUpdate;
	private boolean mtrfSupportedAndAuthorized;
	private boolean mtrfSupportedAndNotAuthorized;
	private ISDNAddressString newMSCNumber;
	private ISDNAddressString newVLRNumber;
	private LMSI newLmsi;
	private long mapProtocolVersion;

	public CancelLocationRequestImpl(long mapProtocolVersion) {
		this.mapProtocolVersion = mapProtocolVersion;
	}

	public CancelLocationRequestImpl(IMSI imsi, IMSIWithLMSI imsiWithLmsi,
			CancellationType cancellationType,
			MAPExtensionContainer extensionContainer,
			TypeOfUpdate typeOfUpdate, boolean mtrfSupportedAndAuthorized,
			boolean mtrfSupportedAndNotAuthorized,
			ISDNAddressString newMSCNumber, ISDNAddressString newVLRNumber,
			LMSI newLmsi, long mapProtocolVersion) {
		super();
		this.imsi = imsi;
		this.imsiWithLmsi = imsiWithLmsi;
		this.cancellationType = cancellationType;
		this.extensionContainer = extensionContainer;
		this.typeOfUpdate = typeOfUpdate;
		this.mtrfSupportedAndAuthorized = mtrfSupportedAndAuthorized;
		this.mtrfSupportedAndNotAuthorized = mtrfSupportedAndNotAuthorized;
		this.newMSCNumber = newMSCNumber;
		this.newVLRNumber = newVLRNumber;
		this.newLmsi = newLmsi;
		this.mapProtocolVersion = mapProtocolVersion;
	}

	@Override
	public MAPMessageType getMessageType() {
		return MAPMessageType.cancelLocation_Request;
	}

	@Override
	public int getOperationCode() {
		return MAPOperationCode.cancelLocation;
	}

	@Override
	public IMSI getImsi() {
		return this.imsi;
	}

	@Override
	public IMSIWithLMSI getImsiWithLmsi() {
		return this.imsiWithLmsi;
	}

	@Override
	public CancellationType getCancellationType() {
		return this.cancellationType;
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.extensionContainer;
	}

	@Override
	public TypeOfUpdate getTypeOfUpdate() {
		return this.typeOfUpdate;
	}

	@Override
	public boolean getMtrfSupportedAndAuthorized() {
		return this.mtrfSupportedAndAuthorized;
	}

	@Override
	public boolean getMtrfSupportedAndNotAuthorized() {
		return this.mtrfSupportedAndNotAuthorized;
	}

	@Override
	public ISDNAddressString getNewMSCNumber() {
		return this.newMSCNumber;
	}

	@Override
	public ISDNAddressString getNewVLRNumber() {
		return this.newVLRNumber;
	}

	@Override
	public LMSI getNewLmsi() {
		return this.newLmsi;
	}

	@Override
	public int getTag() throws MAPException {
		if (this.mapProtocolVersion == 3) {
			return TAG_cancelLocationRequest;
		} else {
			if (imsi != null) {
				return Tag.STRING_OCTET;
			} else {
				return Tag.SEQUENCE;
			}
		}
	}

	@Override
	public int getTagClass() {
		if (this.mapProtocolVersion == 3) {
			return Tag.CLASS_CONTEXT_SPECIFIC;
		} else {
			return Tag.CLASS_UNIVERSAL;
		}

	}

	@Override
	public boolean getIsPrimitive() {
		if (mapProtocolVersion < 3 && this.imsi != null) {
			return true;
		}
		return false;
	}

	@Override
	public void decodeAll(AsnInputStream ansIS)
			throws MAPParsingComponentException {
		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException(
					"IOException when decoding CancelLocationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException {
		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException(
					"IOException when decoding CancelLocationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException(
					"AsnException when decoding CancelLocationRequest: ", e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}

	}

	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		try {
			this.encodeAll(asnOs, this.getTagClass(), this.getTag());
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPException(e);
		}

	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag)
			throws MAPException {
		try {
			asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);

		} catch (AsnException e) {
			e.printStackTrace();
			throw new MAPException(
					"AsnException when encoding CancelLocationRequest: "
							+ e.getMessage(), e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MAPException(
					"AsnException when encoding CancelLocationRequest: "
							+ e.getMessage(), e);
		}

	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.imsi == null && this.imsiWithLmsi == null) {
			throw new MAPException(
					"Error while encoding CancelLocationRequest the mandatory parameter imsi and imsiWithLmsi is not defined");
		}

	
		if (mapProtocolVersion >= 3) {

			if (this.imsi != null) {
				((IMSIImpl) this.imsi).encodeAll(asnOs);
			} else {
				((IMSIWithLMSIImpl) this.imsiWithLmsi).encodeAll(asnOs);
			}
			
			if (this.mapProtocolVersion >= 3) {

				if (this.cancellationType != null) {
					try {
						asnOs.writeInteger(this.cancellationType.getCode());
					} catch (IOException e) {
						throw new MAPException(
								"IOException while encoding CancelLocationRequest parameter cancellationType",
								e);
					} catch (AsnException e) {
						throw new MAPException(
								"IOException while encoding CancelLocationRequest parameter cancellationType",
								e);
					}
				}

				if (this.extensionContainer != null) {
					((MAPExtensionContainerImpl) this.extensionContainer)
							.encodeAll(asnOs);
				}

				if (this.typeOfUpdate != null) {
					try {
						asnOs.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC,
								CancelLocationRequestImpl.TAG_typeOfUpdate,
								this.typeOfUpdate.getCode());
					} catch (IOException e) {
						throw new MAPException(
								"IOException while encoding CancelLocationRequest parameter typeOfUpdate",
								e);
					} catch (AsnException e) {
						throw new MAPException(
								"IOException while encoding CancelLocationRequest parameter typeOfUpdate",
								e);
					}
				}

				if (this.mtrfSupportedAndAuthorized) {
					try {
						asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC,
								TAG_mtrfSupportedAndAuthorized);
					} catch (IOException e) {
						throw new MAPException(
								"IOException while encoding CancelLocationRequest parameter mtrfSupportedAndAuthorized",
								e);
					} catch (AsnException e) {
						throw new MAPException(
								"AsnException while encoding CancelLocationRequest parameter mtrfSupportedAndAuthorized",
								e);
					}
				}

				if (this.mtrfSupportedAndNotAuthorized) {
					try {
						asnOs.writeNull(Tag.CLASS_CONTEXT_SPECIFIC,
								TAG_mtrfSupportedAndNotAuthorized);
					} catch (IOException e) {
						throw new MAPException(
								"IOException while encoding CancelLocationRequest parameter mtrfSupportedAndNotAuthorized",
								e);
					} catch (AsnException e) {
						throw new MAPException(
								"AsnException while encoding CancelLocationRequest parameter mtrfSupportedAndNotAuthorized",
								e);
					}
				}

				if (this.newMSCNumber != null) {
					((ISDNAddressStringImpl) this.newMSCNumber)
							.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
									TAG_newMSCNumber);
				}

				if (this.newVLRNumber != null) {
					((ISDNAddressStringImpl) this.newVLRNumber)
							.encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC,
									TAG_newVLRNumber);
				}

				if (this.newLmsi != null) {
					((LMSIImpl) this.newLmsi).encodeAll(asnOs,
							Tag.CLASS_CONTEXT_SPECIFIC, TAG_newLmsi);
				}
			}

		}else{
			if (this.imsi != null) {
				((IMSIImpl) this.imsi).encodeData(asnOs);
			} else {
				((IMSIWithLMSIImpl) this.imsiWithLmsi).encodeData(asnOs);
			}
			
		}

	}

	@Override
	public long getMapProtocolVersion() {
		return this.mapProtocolVersion;
	}

	private void _decode(AsnInputStream ansIS, int length)
			throws MAPParsingComponentException, IOException, AsnException {

		this.imsi = null;
		this.imsiWithLmsi = null;
		this.cancellationType = null;
		this.extensionContainer = null;
		this.typeOfUpdate = null;
		this.mtrfSupportedAndAuthorized = false;
		this.mtrfSupportedAndNotAuthorized = false;
		this.newMSCNumber = null;
		this.newVLRNumber = null;
		this.newLmsi = null;

		if (this.mapProtocolVersion == 3) {
			AsnInputStream ais = ansIS.readSequenceStreamData(length);
			int num = 0;
			while (true) {
				if (ais.available() == 0)
					break;

				int tag = ais.readTag();

				if (num == 0 && tag == Tag.STRING_OCTET
						&& ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
					this.imsi = new IMSIImpl();
					((IMSIImpl) this.imsi).decodeAll(ais);
				} else if (num == 0 && tag == Tag.SEQUENCE
						&& ais.getTagClass() == Tag.CLASS_UNIVERSAL
						) {
					if (ais.isTagPrimitive()) {
						throw new MAPParsingComponentException(
								"Error while decoding " + _PrimitiveName
										+ ".extensionContainer: is primitive",
								MAPParsingComponentExceptionReason.MistypedParameter);
					}
					this.imsiWithLmsi = new IMSIWithLMSIImpl();
					((IMSIWithLMSIImpl) this.imsiWithLmsi).decodeAll(ais);
				} else if (tag == Tag.INTEGER
						&& ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
					this.cancellationType = CancellationType
							.getInstance((int) ais.readInteger());
				} else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL
						&& tag == Tag.SEQUENCE && (!ais.isTagPrimitive())) {
					this.extensionContainer = new MAPExtensionContainerImpl();
					((MAPExtensionContainerImpl) this.extensionContainer)
							.decodeAll(ais);
				} else if(ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC){
					switch (tag) {
					case CancelLocationRequestImpl.TAG_typeOfUpdate: // lmsi
						this.typeOfUpdate = TypeOfUpdate.getInstance((int) ais
								.readInteger());
						break;
					case CancelLocationRequestImpl.TAG_mtrfSupportedAndAuthorized:
						ais.readNull();
						this.mtrfSupportedAndAuthorized = true;
						break;
					case CancelLocationRequestImpl.TAG_mtrfSupportedAndNotAuthorized:
						ais.readNull();
						this.mtrfSupportedAndNotAuthorized = true;
						break;
					case CancelLocationRequestImpl.TAG_newMSCNumber:
						if (!ais.isTagPrimitive()) {
							throw new MAPParsingComponentException(
									"Error while decoding "
											+ _PrimitiveName
											+ ".extensionContainer: is primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						}
						this.newMSCNumber = new ISDNAddressStringImpl();
						((ISDNAddressStringImpl) this.newMSCNumber)
								.decodeAll(ais);
						break;
					case CancelLocationRequestImpl.TAG_newVLRNumber:
						if (!ais.isTagPrimitive()) {
							throw new MAPParsingComponentException(
									"Error while decoding "
											+ _PrimitiveName
											+ ".extensionContainer: is primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						}
						this.newVLRNumber = new ISDNAddressStringImpl();
						((ISDNAddressStringImpl) this.newVLRNumber)
								.decodeAll(ais);
						break;
					case CancelLocationRequestImpl.TAG_newLmsi:
						if (!ais.isTagPrimitive()) {
							throw new MAPParsingComponentException(
									"Error while decoding "
											+ _PrimitiveName
											+ ".extensionContainer: is primitive",
									MAPParsingComponentExceptionReason.MistypedParameter);
						}
						this.newLmsi = new LMSIImpl();
						((LMSIImpl) this.newLmsi).decodeAll(ais);
						break;
					default:
						ais.advanceElement();
						break;
					}
				}

				num++;
			}
			if (num < 1)
				throw new MAPParsingComponentException(
						"Error while decoding CancelLocationRequest: Needs at least 1 mandatory parameters, found "
								+ num,
						MAPParsingComponentExceptionReason.MistypedParameter);

		} else {
			AsnInputStream ais = ansIS.readSequenceStreamData(length);
			int tag = ais.getTag();
			if (tag == Tag.STRING_OCTET
					&& ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
				if (!ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding " + _PrimitiveName
									+ ".extensionContainer: is primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imsi = new IMSIImpl();
				((IMSIImpl) this.imsi).decodeData(ais,length);
			}else if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL
					) {
				if (ais.isTagPrimitive()) {
					throw new MAPParsingComponentException(
							"Error while decoding " + _PrimitiveName
									+ ".extensionContainer: is primitive",
							MAPParsingComponentExceptionReason.MistypedParameter);
				}
				this.imsiWithLmsi = new IMSIWithLMSIImpl();
				((IMSIWithLMSIImpl) this.imsiWithLmsi).decodeData(ais,length);
			}
		}

	}

	@Override
	public String toString() {
		return "CancelLocationRequestImpl [imsi=" + imsi + ", imsiWithLmsi="
				+ imsiWithLmsi + ", cancellationType=" + cancellationType
				+ ", extensionContainer=" + extensionContainer
				+ ", typeOfUpdate=" + typeOfUpdate
				+ ", mtrfSupportedAndAuthorized=" + mtrfSupportedAndAuthorized
				+ ", mtrfSupportedAndNotAuthorized="
				+ mtrfSupportedAndNotAuthorized + ", newMSCNumber="
				+ newMSCNumber + ", newVLRNumber=" + newVLRNumber
				+ ", newLmsi=" + newLmsi + ", mapProtocolVersion="
				+ mapProtocolVersion + "]";
	}
	
	
//	public static void main(String args[]){
//
//
//		try {
//			
////			IMSI imsi = new IMSIImpl("1111122222");
////			LMSIImpl lmsi = new LMSIImpl(getDataLmsi());
////			IMSIWithLMSI imsiWithLmsi  = new IMSIWithLMSIImpl(imsi, lmsi);
////			CancellationType cancellationType = CancellationType.getInstance(1);
////			MAPExtensionContainer extensionContainer =  GetTestExtensionContainer();
////			TypeOfUpdate typeOfUpdate = TypeOfUpdate.getInstance(0);
////			boolean mtrfSupportedAndAuthorized = false;
////			boolean mtrfSupportedAndNotAuthorized = false;
////			ISDNAddressString newMSCNumber = new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22228");
////			ISDNAddressString newVLRNumber=  new ISDNAddressStringImpl(AddressNature.international_number, NumberingPlan.ISDN, "22229");
////			LMSI newLmsi = new LMSIImpl(getDataLmsi());
////			long mapProtocolVersion=3;
////
//		CancelLocationRequestImpl asc = null;//new CancelLocationRequestImpl(imsi, null, cancellationType, extensionContainer, typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi, mapProtocolVersion);
////
////			AsnOutputStream asnOS = new AsnOutputStream();
////			//asc.encodeAll(asnOS);
////			
////			byte[] encodedData = null;//asnOS.toByteArray();
//			byte[] rawData = null;// getEncodedData();		
////			System.out.println(" 0  = " + Arrays.toString(encodedData));
////			//assertTrue( Arrays.equals(rawData,encodedData));
////			
////			
////			
////			asc = new CancelLocationRequestImpl(null, imsiWithLmsi, cancellationType, extensionContainer, typeOfUpdate, mtrfSupportedAndAuthorized, mtrfSupportedAndNotAuthorized, newMSCNumber, newVLRNumber, newLmsi, mapProtocolVersion);
////
////			asnOS = new AsnOutputStream();
////			//asc.encodeAll(asnOS);
////			
////			encodedData = asnOS.toByteArray();
////			System.out.println(" 1  = " + Arrays.toString(encodedData));
////			
////			//////////////////////////
////			mapProtocolVersion=2;
////			asc = new CancelLocationRequestImpl(null, imsiWithLmsi, null, null, null, false, false, null, null, null, mapProtocolVersion);
////
////			asnOS = new AsnOutputStream();
////			asc.encodeAll(asnOS);
////			
////			encodedData = asnOS.toByteArray();
////			System.out.println(" 2 = " + Arrays.toString(encodedData));
//			rawData = getEncodedData3();		
//		//	assertTrue( Arrays.equals(rawData,encodedData));
//			
//			/////////////////////
//		//	rawData = getEncodedData1();
//			AsnInputStream asn = new AsnInputStream(rawData);
//			
//			int tag = asn.readTag();
//			asc = new CancelLocationRequestImpl(3);
//			asc.decodeAll(asn);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	private static byte[] getEncodedData() {
//		return new byte[] { -93, 72, 4, 5, 17, 17, 33, 34, 34, 2, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -125, 4, -111, 34, 34, -8, -124, 4, -111, 34, 34, -7, -123, 4, 0, 3, 98, 39};
//	}
//	
//	private static byte[] getEncodedData1() {
//		return new byte[] { -93, 80, 16, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, 0, 3, 98, 39, 2, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -125, 4, -111, 34, 34, -8, -124, 4, -111, 34, 34, -7, -123, 4, 0, 3, 98, 39 };
//	}
//	
//	
//	public static byte[] getDataLmsi() {
//		return new byte[] { 0, 3, 98, 39 };
//	}
//	
//	public static MAPExtensionContainer GetTestExtensionContainer() {
//		MAPParameterFactory mapServiceFactory = new MAPParameterFactoryImpl(); 
//		
//		ArrayList<MAPPrivateExtension> al = new ArrayList<MAPPrivateExtension>();
//		al.add(mapServiceFactory
//				.createMAPPrivateExtension(new long[] { 1, 2, 3, 4 }, new byte[] { 11, 12, 13, 14, 15 }));
//		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 6 }, null));
//		al.add(mapServiceFactory.createMAPPrivateExtension(new long[] { 1, 2, 3, 5 }, new byte[] { 21, 22, 23, 24, 25,
//				26 }));
//
//		MAPExtensionContainer cnt = mapServiceFactory.createMAPExtensionContainer(al, new byte[] { 31, 32, 33 });
//
//		return cnt;
//	}
//
//	private static byte[] getEncodedData2() {
//		return new byte[] {36, 5, 17, 17, 33, 34, 34};
//	}
//
//	private static byte[] getEncodedData3() {
//		return new byte[] { -93, 80, 48, 13, 4, 5, 17, 17, 33, 34, 34, 4, 4, 0, 3, 98, 39, 2, 1, 1, 48, 39, -96, 32, 48, 10, 6, 3, 42, 3, 4, 11, 12, 13, 14, 15, 48, 5, 6, 3, 42, 3, 6, 48, 11, 6, 3, 42, 3, 5, 21, 22, 23, 24, 25, 26, -95, 3, 31, 32, 33, -128, 1, 0, -125, 4, -111, 34, 34, -8, -124, 4, -111, 34, 34, -7, -123, 4, 0, 3, 98, 39};
//	}
}
