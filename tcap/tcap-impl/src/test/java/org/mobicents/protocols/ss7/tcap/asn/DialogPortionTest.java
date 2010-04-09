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
	}
	

}
