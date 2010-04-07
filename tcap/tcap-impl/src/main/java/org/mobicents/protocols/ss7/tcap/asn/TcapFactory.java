package org.mobicents.protocols.ss7.tcap.asn;


import java.io.IOException;

import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

public final class TcapFactory {

	
	public static DialogPortion createDialogPortion(AsnInputStream ais) throws ParseException
	{
		DialogPortionImpl dpi = new DialogPortionImpl();
		dpi.decode(ais);
		return dpi;
	}
	
	public static DialogPortion createDialogPortion() 
	{
		return new DialogPortionImpl();
	}

	public static DialogAPDU createDialogAPDU(AsnInputStream ais) throws ParseException {
		return new DialogAPDU(){

			public void decode(AsnInputStream ais) throws ParseException {
				// TODO Auto-generated method stub
				
			}

			public void encode(AsnOutputStream aos) throws ParseException {

				byte[] b = new byte[]{
						60, 15, (byte) 128, 2, 7, 1, (byte) 161, 9, 6, 7, 4, 0, 1, 1, 1, 3, 0
					
				};
				try {
					aos.write(b);
				} catch (IOException e) {
					throw new ParseException(e);
				}
			}};
	}
}
