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

	public static DialogAPDU createDialogAPDU(AsnInputStream ais, int tag, boolean unidirectional) throws ParseException {
		
		if (unidirectional) {
			// only one
			if (tag != DialogAPDU._TAG_UNIDIRECTIONAL) {
				throw new ParseException("Wrong tag for APDU, found: " + tag);
			}
			//craete UNIPDU
		} else {

			if (tag != DialogAPDU._TAG_REQUEST && tag != DialogAPDU._TAG_RESPONSE && tag != DialogAPDU._TAG_ABORT) {
				throw new ParseException("Wrong tag for APDU, found: " + tag);
			}
			//create one of directional
		}
		return new DialogAPDU(){

			public void decode(AsnInputStream ais) throws ParseException {
				// TODO Auto-generated method stub
				
			}

			public void encode(AsnOutputStream aos) throws ParseException {

				byte[] b = new byte[]{0x60, 15, (byte) 128, 2, 7, 1, (byte) 161, 9, 6, 7, 4, 0, 1, 1, 1, 3, 0
					
				};
				try {
					aos.write(b);
				} catch (IOException e) {
					throw new ParseException(e);
				}
			}

			public DialogAPDUType getType() {
				// TODO Auto-generated method stub
				return null;
			}

			public boolean isUniDirectional() {
				// TODO Auto-generated method stub
				return false;
			}};
	}
}
