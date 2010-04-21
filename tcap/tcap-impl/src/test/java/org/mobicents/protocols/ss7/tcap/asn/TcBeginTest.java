package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ComponentType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;

public class TcBeginTest extends TestCase {

	private void compareArrays(byte[] expected, byte[] encoded) {
		boolean same = Arrays.equals(expected, encoded);
		assertTrue("byte[] dont match, expected|encoded \n"
				+ Arrays.toString(expected) + "\n" + Arrays.toString(encoded),
				same);
	}

	@org.junit.Test
	public void testBasicTCBeginEncode() throws IOException, ParseException {
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage();
		tcm.setOriginatingTransactionId(1358955064L);

		// Create Dialog Portion
		DialogPortion dp = TcapFactory.createDialogPortion();

		dp.setOid(true);
		dp.setOidValue(new long[] { 0, 0, 17, 773, 1, 1, 1 });

		dp.setAsn(true);

		DialogRequestAPDUImpl diRequestAPDUImpl = new DialogRequestAPDUImpl();

		ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
		acn.setOid(new long[] { 0, 4, 0, 0, 1, 0, 19, 2 });

		diRequestAPDUImpl.setApplicationContextName(acn);

		UserInformation userInfo = new UserInformationImpl();
		byte[] userData = new byte[] { 0x28, 0x23, 0x06, 0x07, 0x04, 0x00,
				0x00, 0x01, 0x01, 0x01, 0x01, (byte) 0xa0, 0x18, (byte) 0xa0,
				(byte) 0x80, (byte) 0x80, 0x09, (byte) 0x96, 0x02, 0x24,
				(byte) 0x80, 0x03, 0x00, (byte) 0x80, 0x00, (byte) 0xf2,
				(byte) 0x81, 0x07, (byte) 0x91, 0x13, 0x26, (byte) 0x98,
				(byte) 0x86, 0x03, (byte) 0xf0, 0x00, 0x00 };
		userInfo.setUserData(userData);

		diRequestAPDUImpl
				.setUserInformation(new UserInformation[] { userInfo });

		dp.setDialogAPDU(diRequestAPDUImpl);

		tcm.setDialogPortion(dp);

		AsnOutputStream aos = new AsnOutputStream();

		// INVOKE Component

		Invoke invComp = TcapFactory.createComponentInvoke();
		invComp.setInvokeId(-128l);

		// Operation Code
		OperationCode oc = TcapFactory.createOperationCode(false, 59l);
		invComp.setOperationCode(oc);

		// Sequence of Parameter
		Parameter p1 = TcapFactory.createParameter();
		p1.setTagClass(Tag.CLASS_UNIVERSAL);
		p1.setTag(0x04);
		p1.setData(new byte[] { 0x0f });

		Parameter p2 = TcapFactory.createParameter();
		p2.setTagClass(Tag.CLASS_UNIVERSAL);
		p2.setTag(0x04);
		p2.setData(new byte[] { (byte) 0xaa, (byte) 0x98, (byte) 0xac,
				(byte) 0xa6, 0x5a, (byte) 0xcd, 0x62, 0x36, 0x19, 0x0e, 0x37,
				(byte) 0xcb, (byte) 0xe5, 0x72, (byte) 0xb9, 0x11 });

		Parameter p3 = TcapFactory.createParameter();
		p3.setTagClass(Tag.CLASS_CONTEXT_SPECIFIC);
		p3.setTag(0x00);
		p3.setData(new byte[] { (byte) 0x91, 0x13, 0x26, (byte) 0x88,
				(byte) 0x83, 0x00, (byte) 0xf2 });
		
		
		Parameter[] params = new Parameter[]{p1,p2,p3};
		
		
		invComp.setParameters(params);

		tcm.setComponent(new Component[] { invComp });

		tcm.encode(aos);

		byte[] encodedData = aos.toByteArray();

		String s = dump(encodedData, encodedData.length, false);

		System.out.println(s);

	}

	@org.junit.Test
	public void testBasicTCBegin() throws IOException, ParseException {

		// no idea how to check rest...?

		// trace
		byte[] b = new byte[] {
		// TCBegin
				0x62, 38,
				// oidTx
				// OrigTran ID (full)............ 145031169
				0x48, 4, 8, (byte) 0xA5, 0, 1,
				// dialog portion
				0x6B, 30,
				// extrnal tag
				0x28, 28,
				// oid
				0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) // asn
				160, 17,
				// DialogPDU - Request
				0x60, 15, (byte) // protocol version
				0x80, 2, 7, (byte) 0x80, (byte) // acn
				161, 9,
				// oid
				6, 7, 4, 0, 1, 1, 1, 3, 0

		};

		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCInvoke", TCBeginMessage._TAG, tag);
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage(ais);

		assertEquals("Originating transaction id does not match", new Long(
				145031169L), tcm.getOriginatingTransactionId());
		assertNotNull("Dialog portion should not be null", tcm
				.getDialogPortion());

		assertNull("Component portion should not be present", tcm
				.getComponent());

		DialogPortion dp = tcm.getDialogPortion();

		assertFalse("Dialog should not be Uni", dp.isUnidirectional());

		DialogAPDU dapdu = dp.getDialogAPDU();
		assertNotNull("APDU should not be null", dapdu);
		assertEquals("Wrong APDU type", DialogAPDUType.Request, dapdu.getType());
		DialogRequestAPDU dr = (DialogRequestAPDU) dapdu;
		// rest is checked in dialog portion type!
		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();

		compareArrays(b, encoded);

	}

	@org.junit.Test
	public void testTCBeginMessage_No_Dialog() throws IOException,
			ParseException {

		// no idea how to check rest...?

		// created by hand
		byte[] b = new byte[] {
		// TCBegin
				0x62, 68,
				// org txid
				// OrigTran ID (full)............ 145031169
				0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,
				// dtx

				// dialog portion
				// empty
				// comp portion
				0x6C, 60,
				// invoke
				(byte) 0xA1, 6,
				// invoke ID
				0x02, 0x01, 0x01,
				// op code
				0x02, 0x01, 0x37,
				// return result
				(byte) 0xA7, 50,
				// inoke id
				0x02, 0x01, 0x02,
				// sequence start
				0x30, 7,
				// local operation
				0x02, 0x01, 0x01,
				// global operation
				0x06, 0x02, 0x00, (byte) 0xFF,
				// parameter
				0x30, 36, (byte) 0xA0,// some tag.1
				17, (byte) 0x80,// some tag.1.1
				2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
				04, (byte) 0x82, // some tag.1.3 ?
				2, 0x00, 0x00, (byte) 0x82,
				// some tag.1.4
				1, 12, (byte) 0x83, // some tag.1.5
				2, 0x33, 0x33, (byte) 0xA1,// some trash here
				// tension indicator 2........ ???
				// use value.................. ???
				// some tag.2
				3, (byte) 0x80,// some tag.2.1
				1, -1, (byte) 0xA2, // some tag.3
				3, (byte) 0x80,// some tag.3.1
				1, -1, (byte) 0xA3,// some tag.4
				5, (byte) 0x82,// some tag.4.1
				3, (byte) 0xAB,// - 85 serviceKey................... 123456 //
				// dont care about this content, lets just make
				// len correct
				(byte) 0xCD, (byte) 0xEF };

		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCBegin", TCBeginMessage._TAG, tag);
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage(ais);

		assertNull("Dialog portion should not be present", tcm
				.getDialogPortion());

		assertEquals("Originating transaction id does not match", new Long(
				145031169L), tcm.getOriginatingTransactionId());
		// comp portion
		assertNotNull("Component portion should be present", tcm.getComponent());
		assertEquals("Component count is wrong", 2, tcm.getComponent().length);
		Component c = tcm.getComponent()[0];
		assertEquals("Wrong component type", ComponentType.Invoke, c.getType());
		Invoke i = (Invoke) c;
		assertEquals("Wrong invoke ID", new Long(1), i.getInvokeId());
		assertNull("Linked ID is not null", i.getLinkedId());

		c = tcm.getComponent()[1];
		assertEquals("Wrong component type", ComponentType.ReturnResult, c
				.getType());
		ReturnResult rr = (ReturnResult) c;
		assertEquals("Wrong invoke ID", new Long(2), rr.getInvokeId());
		assertNotNull("Operation code should not be null", rr
				.getOperationCode());
		assertEquals("Operation Code count is wrong ", 2,
				rr.getOperationCode().length);

		OperationCode[] ocs = rr.getOperationCode();

		assertEquals("Wrong Operation Code type", OperationCodeType.Local,
				ocs[0].getOperationType());
		assertEquals("Wrong Operation Code", new Long(1), ocs[0].getCode());
		assertEquals("Wrong Operation Code type", OperationCodeType.Global,
				ocs[1].getOperationType());
		assertEquals("Wrong Operation Code", new Long(0x00FF), ocs[1].getCode());

		assertNotNull("Parameter should not be null", rr.getParameter());

		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();

		compareArrays(b, encoded);

	}

	@org.junit.Test
	public void testTCBeginMessage_No_Component() throws IOException,
			ParseException {

		// created by hand
		byte[] b = new byte[] {
		// TCBegin
				0x62, 50,
				// org txid
				// OrigTran ID (full)............ 145031169
				0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,

				// dialog portion
				0x6B, 42,
				// extrnal tag
				0x28, 40,
				// oid
				0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

				29, 0x61, // dialog response
				27,
				// protocol version
				(byte) 0x80, // protocol version

				2, 7, (byte) 0x80, (byte) 161,// acn
				9,
				// oid
				6, 7, 4, 0, 1, 1, 1, 3, 0,
				// result
				(byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x0,
				// result source diagnostic
				(byte) 0xA3, 5, (byte) 0x0A2, // provider
				3, 0x02,// int 2
				0x01, (byte) 0x2
		// no user info?
		};

		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCBegin", TCBeginMessage._TAG, tag);
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage(ais);
		assertNull("Component portion should not be present", tcm
				.getComponent());
		assertNotNull("Dialog portion should not be null", tcm
				.getDialogPortion());
		assertEquals("Originating transaction id does not match", new Long(
				145031169L), tcm.getOriginatingTransactionId());

		assertFalse("Dialog should not be Uni", tcm.getDialogPortion()
				.isUnidirectional());
		DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
		assertEquals("Wrong dialog APDU type!", DialogAPDUType.Response, _dapd
				.getType());

		DialogResponseAPDU dapd = (DialogResponseAPDU) _dapd;

		// check nulls first
		assertNull("UserInformation should not be present", dapd
				.getUserInformation());

		// not nulls
		assertNotNull("Result should not be null", dapd.getResult());
		Result r = dapd.getResult();
		assertEquals("Wrong result", ResultType.Accepted, r.getResultType());

		assertNotNull("Result Source Diagnostic should not be null", dapd
				.getResultSourceDiagnostic());

		ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
		assertNull("User diagnostic should not be present", rsd
				.getDialogServiceUserType());
		assertEquals("Wrong provider diagnostic type",
				DialogServiceProviderType.NoCommonDialogPortion, rsd
						.getDialogServiceProviderType());

		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();

		compareArrays(b, encoded);

	}

	@org.junit.Test
	public void testTCBeginMessage_No_Nothing() throws IOException,
			ParseException {

		// no idea how to check rest...?

		// created by hand
		byte[] b = new byte[] {
		// TCBegin
				0x62, 6,
				// org txid
				// OrigTran ID (full)............ 145031169
				0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,
		// didTx

		};

		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCBegin", TCBeginMessage._TAG, tag);
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage(ais);

		assertNull("Dialog portion should be null", tcm.getDialogPortion());
		assertNull("Component portion should not be present", tcm
				.getComponent());
		assertEquals("Originating transaction id does not match", new Long(
				145031169L), tcm.getOriginatingTransactionId());

		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();

		compareArrays(b, encoded);

	}

	@org.junit.Test
	public void testTCBeginMessage_All() throws IOException, ParseException {

		// no idea how to check rest...?

		// created by hand
		byte[] b = new byte[] {
		// TCBegin
				0x62, 68 + 44,
				// org txid
				// OrigTran ID (full)............ 145031169
				0x48, 0x04, 0x08, (byte) 0xA5, 0, 0x01,

				// dialog portion
				0x6B, 42,
				// extrnal tag
				0x28, 40,
				// oid
				0x06, 7, 0, 17, (byte) 134, 5, 1, 1, 1, (byte) 160, // asn

				29, 0x61, // dialog response
				27,
				// protocol version
				(byte) 0x80, // protocol version

				2, 7, (byte) 0x80, (byte) 161,// acn
				9,
				// oid
				6, 7, 4, 0, 1, 1, 1, 3, 0,
				// result
				(byte) 0xA2, 0x03, 0x2, 0x1, (byte) 0x01,
				// result source diagnostic
				(byte) 0xA3, 5, (byte) 0x0A2, // provider
				3, 0x02,// int 2
				0x01, (byte) 0x00,
				// no user info?
				// comp portion
				0x6C, 60,
				// invoke
				(byte) 0xA1, 6,
				// invoke ID
				0x02, 0x01, 0x01,
				// op code
				0x02, 0x01, 0x37,
				// return result last
				(byte) 0xA2, 50,
				// inoke id
				0x02, 0x01, 0x02,
				// sequence start
				0x30, 7,
				// local operation
				0x02, 0x01, 0x01,
				// global operation
				0x06, 0x02, 0x00, (byte) 0xFF,
				// parameter
				0x30, 36, (byte) 0xA0,// some tag.1
				17, (byte) 0x80,// some tag.1.1
				2, 0x11, 0x11, (byte) 0xA1,// some tag.1.2
				04, (byte) 0x82, // some tag.1.3 ?
				2, 0x00, 0x00, (byte) 0x82,
				// some tag.1.4
				1, 12, (byte) 0x83, // some tag.1.5
				2, 0x33, 0x33, (byte) 0xA1,// some trash here
				// tension indicator 2........ ???
				// use value.................. ???
				// some tag.2
				3, (byte) 0x80,// some tag.2.1
				1, -1, (byte) 0xA2, // some tag.3
				3, (byte) 0x80,// some tag.3.1
				1, -1, (byte) 0xA3,// some tag.4
				5, (byte) 0x82,// some tag.4.1
				3, (byte) 0xAB,// - 85 serviceKey................... 123456 //
				// dont care about this content, lets just make
				// len correct
				(byte) 0xCD, (byte) 0xEF };

		AsnInputStream ais = new AsnInputStream(new ByteArrayInputStream(b));
		int tag = ais.readTag();
		assertEquals("Expected TCBegin", TCBeginMessage._TAG, tag);
		TCBeginMessage tcm = TcapFactory.createTCBeginMessage(ais);

		// universal
		assertEquals("Originating transaction id does not match", new Long(
				145031169L), tcm.getOriginatingTransactionId());

		// dialog portion
		assertNotNull("Dialog portion should not be null", tcm
				.getDialogPortion());

		assertFalse("Dialog should not be Uni", tcm.getDialogPortion()
				.isUnidirectional());
		DialogAPDU _dapd = tcm.getDialogPortion().getDialogAPDU();
		assertEquals("Wrong dialog APDU type!", DialogAPDUType.Response, _dapd
				.getType());

		DialogResponseAPDU dapd = (DialogResponseAPDU) _dapd;

		// check nulls first
		assertNull("UserInformation should not be present", dapd
				.getUserInformation());

		// not nulls
		assertNotNull("Result should not be null", dapd.getResult());
		Result r = dapd.getResult();
		assertEquals("Wrong result", ResultType.RejectedPermanent, r
				.getResultType());

		assertNotNull("Result Source Diagnostic should not be null", dapd
				.getResultSourceDiagnostic());

		ResultSourceDiagnostic rsd = dapd.getResultSourceDiagnostic();
		assertNull("User diagnostic should not be present", rsd
				.getDialogServiceUserType());
		assertEquals("Wrong provider diagnostic type",
				DialogServiceProviderType.Null, rsd
						.getDialogServiceProviderType());

		// comp portion
		assertNotNull("Component portion should be present", tcm.getComponent());
		assertEquals("Component count is wrong", 2, tcm.getComponent().length);
		Component c = tcm.getComponent()[0];
		assertEquals("Wrong component type", ComponentType.Invoke, c.getType());
		Invoke i = (Invoke) c;
		assertEquals("Wrong invoke ID", new Long(1), i.getInvokeId());
		assertNull("Linked ID is not null", i.getLinkedId());

		c = tcm.getComponent()[1];
		assertEquals("Wrong component type", ComponentType.ReturnResultLast, c
				.getType());
		ReturnResultLast rrl = (ReturnResultLast) c;
		assertEquals("Wrong invoke ID", new Long(2), rrl.getInvokeId());
		assertNotNull("Operation code should not be null", rrl
				.getOperationCode());
		assertEquals("Operation Code count is wrong ", 2, rrl
				.getOperationCode().length);

		OperationCode[] ocs = rrl.getOperationCode();

		assertEquals("Wrong Operation Code type", OperationCodeType.Local,
				ocs[0].getOperationType());
		assertEquals("Wrong Operation Code", new Long(1), ocs[0].getCode());
		assertEquals("Wrong Operation Code type", OperationCodeType.Global,
				ocs[1].getOperationType());
		assertEquals("Wrong Operation Code", new Long(0x00FF), ocs[1].getCode());

		assertNotNull("Parameter should not be null", rrl.getParameter());

		AsnOutputStream aos = new AsnOutputStream();
		tcm.encode(aos);
		byte[] encoded = aos.toByteArray();

		compareArrays(b, encoded);

	}

	public final static String dump(byte[] buff, int size, boolean asBits) {
		String s = "";
		for (int i = 0; i < size; i++) {
			String ss = null;
			if (!asBits) {
				ss = Integer.toHexString(buff[i] & 0xff);
			} else {
				ss = Integer.toBinaryString(buff[i] & 0xff);
			}
			ss = fillInZeroPrefix(ss, asBits);
			s += " " + ss;
		}
		return s;
	}

	public final static String fillInZeroPrefix(String ss, boolean asBits) {
		if (asBits) {
			if (ss.length() < 8) {
				for (int j = ss.length(); j < 8; j++) {
					ss = "0" + ss;
				}
			}
		} else {
			// hex
			if (ss.length() < 2) {

				ss = "0" + ss;
			}
		}

		return ss;
	}

}
