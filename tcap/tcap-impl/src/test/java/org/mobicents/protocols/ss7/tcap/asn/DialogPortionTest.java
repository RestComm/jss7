package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.ss7.tcap.asn.DialogPortionImpl;
import org.mobicents.protocols.ss7.tcap.asn.ParseException;

import junit.framework.TestCase;

public class DialogPortionTest extends TestCase {

	@org.junit.Test
	public void testDialogPortion_DialogRequestAPDU() throws IOException, ParseException {
		//trace
		byte[] b = new byte[] { 107,30, 40, 28, 6, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, 17, 0x60, 15, (byte) 128, 2, 7, (byte) 128, (byte) 161, 9, 6, 7, 4, 0, 1, 1, 1, 3, 0 };
		AsnInputStream asin = new AsnInputStream(new ByteArrayInputStream(b));
		asin.readTag();
		DialogPortionImpl dpi = new DialogPortionImpl();
		dpi.decode(asin);

		AsnOutputStream aso = new AsnOutputStream();
		dpi.encode(aso);
		byte[] encoded = aso.toByteArray();
		assertTrue(Arrays.equals(b, encoded));
		
		try{
			dpi.getEncodeBitStringType();
			fail();
		}catch(UnsupportedOperationException e)
		{
			
		} catch (AsnException e) {
			fail();
			e.printStackTrace();
		}
		try{
			encoded = null;
			encoded = dpi.getEncodeType();
			assertNotNull(encoded);
			
		}catch(UnsupportedOperationException e)
		{
			fail();
			e.printStackTrace();
		} catch (AsnException e) {
			fail();
			e.printStackTrace();
		}
		assertTrue(dpi.isAsn());
		assertTrue(dpi.isOid());
		assertFalse(dpi.isUnidirectional());
		
		assertFalse(dpi.isArbitrary());
		assertFalse(dpi.isOctet());
		assertFalse(dpi.isObjDescriptor());
		assertFalse(dpi.isInteger());
		
		
		DialogAPDU _apid = dpi.getDialogAPDU();
		assertEquals(DialogAPDUType.Request, _apid.getType());
		assertFalse(_apid.isUniDirectional());
		DialogRequestAPDU apdu = (DialogRequestAPDU) _apid;
		
		//no idea how to check rest...?
		
	}
	@org.junit.Test
	public void testDialogPortion_DialogAbortAPDU() throws IOException, ParseException {
		
		//trace
		byte[] b = new byte[] { 0x6B, 0x12, 0x28, 0x10, 0x06, 0x07, 0x00, 0x11, (byte) 0x86, 0x05, 0x01, 0x01, 0x01, (byte) 0xA0, 0x05, 0x64, 0x03, (byte) 0x80, 0x01, 0x01 };
		AsnInputStream asin = new AsnInputStream(new ByteArrayInputStream(b));
		asin.readTag();
		DialogPortionImpl dpi = new DialogPortionImpl();
		dpi.decode(asin);
		AsnOutputStream aso = new AsnOutputStream();
		dpi.encode(aso);
		byte[] encoded = aso.toByteArray();
		assertTrue(Arrays.equals(b, encoded));
		
		try{
			dpi.getEncodeBitStringType();
			fail();
		}catch(UnsupportedOperationException e)
		{
			
		} catch (AsnException e) {
			fail();
			e.printStackTrace();
		}
		try{
			encoded = null;
			encoded = dpi.getEncodeType();
			assertNotNull(encoded);
			
		}catch(UnsupportedOperationException e)
		{
			fail();
			e.printStackTrace();
		} catch (AsnException e) {
			fail();
			e.printStackTrace();
		}
		assertTrue(dpi.isAsn());
		assertTrue(dpi.isOid());
		assertFalse(dpi.isUnidirectional());
		
		assertFalse(dpi.isArbitrary());
		assertFalse(dpi.isOctet());
		assertFalse(dpi.isObjDescriptor());
		assertFalse(dpi.isInteger());
		
		
		DialogAPDU _apid = dpi.getDialogAPDU();
		assertEquals(DialogAPDUType.Abort, _apid.getType());
		assertFalse(_apid.isUniDirectional());
		DialogAbortAPDU apdu = (DialogAbortAPDU) _apid;
	}
	
	
	
//	@org.junit.Test
//	public void testDialogPortion_DialogResponseAPDU() throws IOException, ParseException {
//		
//
//		byte[] b = new byte[] { 0x6B, 0x2A, /*EXT_T*/0x28, 0x28, /*OID_T*/0x06, 0x07, 0x00, 0x11, (byte)0x86, 0x05, 0x01, 0x01, 0x01, /*ASN_T*/(byte)0xA0, /*29*/0x1D, /*REsponse_T*/0x61, 0x1B, /*rest of trace is gone... ech*/ };
//		System.err.println(b.length);
//		AsnInputStream asin = new AsnInputStream(new ByteArrayInputStream(b));
//		asin.readTag();
//		DialogPortionImpl dpi = new DialogPortionImpl();
//		dpi.decode(asin);
//		AsnOutputStream aso = new AsnOutputStream();
//		dpi.encode(aso);
//		byte[] encoded = aso.toByteArray();
//		assertTrue(Arrays.equals(b, encoded));
//		
//		try{
//			dpi.getEncodeBitStringType();
//			fail();
//		}catch(UnsupportedOperationException e)
//		{
//			
//		} catch (AsnException e) {
//			fail();
//			e.printStackTrace();
//		}
//		try{
//			encoded = null;
//			encoded = dpi.getEncodeType();
//			assertNotNull(encoded);
//			
//		}catch(UnsupportedOperationException e)
//		{
//			fail();
//			e.printStackTrace();
//		} catch (AsnException e) {
//			fail();
//			e.printStackTrace();
//		}
//		assertTrue(dpi.isAsn());
//		assertTrue(dpi.isOid());
//		assertFalse(dpi.isUnidirectional());
//		
//		assertFalse(dpi.isArbitrary());
//		assertFalse(dpi.isOctet());
//		assertFalse(dpi.isObjDescriptor());
//		assertFalse(dpi.isInteger());
//		
//		
////		DialogAPDU _apid = dpi.getDialogAPDU();
////		assertEquals(DialogAPDUType.Abort, _apid.getType());
////		assertFalse(_apid.isUniDirectional());
////		DialogAbortAPDU apdu = (DialogAbortAPDU) _apid;
//	}
	
	
}
