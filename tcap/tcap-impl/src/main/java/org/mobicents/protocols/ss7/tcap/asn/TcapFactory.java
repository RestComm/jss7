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
			}else
			{
				//craete UNIPDU
				return null;
			}
			
		} else {

			if(tag == DialogAPDU._TAG_REQUEST )
			{
				DialogRequestAPDUImpl d = new DialogRequestAPDUImpl();
				d.decode(ais);
				return d;
			}
			if (tag == DialogAPDU._TAG_RESPONSE ) {
				
				return null;
				
			}
			
			if(tag == DialogAPDU._TAG_ABORT)
			{
				return null;
			}
			
			throw new ParseException("Wrong tag for APDU, found: " + tag);
		}

	}
	public static ProtocolVersion createProtocolVersion()
	{
		return new ProtocolVersionImpl();
	}
	public static ProtocolVersion createProtocolVersion(AsnInputStream ais) throws ParseException
	{
		ProtocolVersionImpl pv = new ProtocolVersionImpl();
		pv.decode(ais);
		return pv;
	}
	public static ApplicationContextName createApplicationContextName()  {
		ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
	
		return acn;
	}
	public static ApplicationContextName createApplicationContextName(AsnInputStream ais) throws ParseException {
		ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
		acn.decode(ais);
		return acn;
	}
	public static UserInformation createUserInformation() {
		return new UserInformationImpl();
	}
	public static UserInformation createUserInformation(AsnInputStream localAis) throws ParseException {
		UserInformationImpl ui = new UserInformationImpl();
		ui.decode(localAis);
		return ui;
	}
}
