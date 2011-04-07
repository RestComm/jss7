/**
 * 
 */
package org.mobicents.protocols.ss7.tcap.asn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 * Class with some utility methods.
 * @author baranowb
 *
 */
final class Utils {

	
	public static Long readTransactionId(AsnInputStream ais) throws AsnException, IOException
	{
		//here  we have AIS, with txid - this is integer, but its coded as 
		//octet string so no extra byte is added....
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ais.readOctetString(bos);
		//this contains LSBs of Long number
		byte[] data = bos.toByteArray();
		byte[] longRep = new byte[8];
		//copy data so longRep = {0,0,0,...,data};
		System.arraycopy(data, 0, longRep, longRep.length-data.length, data.length);
		ByteBuffer bb = ByteBuffer.wrap(longRep);
		return bb.getLong();
		
	}
	
	public static void writeTransactionId(AsnOutputStream aos,Long txId, int tagClass, int tag) throws AsnException, IOException
	{
		//txId may only be up to 4 bytes, that is 0xFF FF FF FF
		byte[] data = new byte[4];
		long ll = txId.longValue();
		data[3] = (byte) ll;
		data[2] = (byte) (ll>> 8);
		data[1] = (byte) (ll>>16);
		data[0] = (byte) (ll >> 24);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		//now fast forward, we wont encode 0x00s!
		//FIXME: check if TxId = 0, is coded as zero len octet string...
		//now leacing atleast ONE B, it can be 0x00
		//for(int index = 0;index<4;index++)
		
//Amit : Let Tx Id be always 4 octets		
//		for(int index = 0;index<3;index++)
//		{
//			if(data[index] == 0)
//			{
//				bais.read();
//			}else
//			{
//				break;
//			}
//		}
		aos.writeStringOctet(tagClass, tag, bais);
		
		
		
	}
}
