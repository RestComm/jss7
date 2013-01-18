package org.mobicents.protocols.ss7.cap.service.gprs;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.primitives.AppendFreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.FreeFormatData;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.CAMELFCIGPRSBillingChargingCharacteristicsImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.FCIBCCCAMELsequence1Impl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.FreeFormatDataImpl;
import org.mobicents.protocols.ss7.cap.service.gprs.primitive.PDPIDImpl;
import org.testng.annotations.Test;

public class FurnishChargingInformationGPRSRequestTest {
	
	public byte[] getData() {
		return new byte[] {48, 20, 48, 18, -96, 16, -128, 8, 48, 6, -128, 1, 5, -127, 1, 2, -127, 1, 2, -126, 1, 1};
	};
	
	public byte[] getFreeFormatData() {
		return new byte[] {48, 6, -128, 1, 5, -127, 1, 2};
	};
	
	@Test(groups = { "functional.decode", "primitives" })
	public void testDecode() throws Exception {
		byte[] data = this.getData();
		AsnInputStream asn = new AsnInputStream(data);
		int tag = asn.readTag();
		FurnishChargingInformationGPRSRequestImpl prim = new FurnishChargingInformationGPRSRequestImpl();
		prim.decodeAll(asn);
		
		assertEquals(tag, Tag.SEQUENCE);
		assertEquals(asn.getTagClass(), Tag.CLASS_UNIVERSAL);

		assertEquals(prim.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getFreeFormatData().getData(),this.getFreeFormatData());
		assertEquals(prim.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getPDPID().getId(),2);
		assertEquals(prim.getFCIGPRSBillingChargingCharacteristics().getFCIBCCCAMELsequence1().getAppendFreeFormatData(), AppendFreeFormatData.append);
	}
	
	@Test(groups = { "functional.encode", "primitives" })
	public void testEncode() throws Exception {
		
		FreeFormatData freeFormatData = new FreeFormatDataImpl(this.getFreeFormatData());
		PDPID pdpID = new PDPIDImpl(2);
		FCIBCCCAMELsequence1Impl fcIBCCCAMELsequence1 = new FCIBCCCAMELsequence1Impl(freeFormatData, pdpID, AppendFreeFormatData.append);
		
		CAMELFCIGPRSBillingChargingCharacteristicsImpl fciGPRSBillingChargingCharacteristics = new 
				CAMELFCIGPRSBillingChargingCharacteristicsImpl(fcIBCCCAMELsequence1);
		
		FurnishChargingInformationGPRSRequestImpl prim = new FurnishChargingInformationGPRSRequestImpl(fciGPRSBillingChargingCharacteristics);
		AsnOutputStream asn = new AsnOutputStream();
		prim.encodeAll(asn);

		assertTrue(Arrays.equals(asn.toByteArray(), this.getData()));
	}
	
}
