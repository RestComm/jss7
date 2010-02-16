/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * 
 */
package org.mobicents.protocols.ss7.isup.impl.message;

import org.mobicents.protocols.ss7.isup.message.CallProgressMessage;
import org.mobicents.protocols.ss7.isup.message.ISUPMessage;
import org.mobicents.protocols.ss7.isup.message.parameter.BackwardCallIndicators;
import org.mobicents.protocols.ss7.isup.message.parameter.ConnectedNumber;
import org.mobicents.protocols.ss7.isup.message.parameter.EventInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.TransmissionMediumUsed;

/**
 * Start time:15:07:07 2009-07-17<br>
 * Project: mobicents-isup-stack<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CPGTest extends MessageHarness{


	public void testTwo_Parameters() throws Exception
	{
		byte[] message = getDefaultBody();

		//CallProgressMessage cpg=new CallProgressMessageImpl(this,message);
		CallProgressMessage cpg=super.messageFactory.createCPG();
		cpg.decodeElement(message);
		assertNotNull(cpg.getParameter(EventInformation._PARAMETER_CODE));
		assertNotNull(cpg.getParameter(BackwardCallIndicators._PARAMETER_CODE));
		assertNotNull(cpg.getParameter(TransmissionMediumUsed._PARAMETER_CODE));
		assertNotNull(cpg.getParameter(ConnectedNumber._PARAMETER_CODE));
		
		EventInformation ei=(EventInformation) cpg.getParameter(EventInformation._PARAMETER_CODE);
		assertEquals("EventInformation has wrong value: ",0x02 ,ei.getEventIndicator());
		
		BackwardCallIndicators bci=(BackwardCallIndicators) cpg.getParameter(BackwardCallIndicators._PARAMETER_CODE);
		
		assertEquals("BackwardCallIndicators value getChargeIndicator  does not match:",bci._CHARGE_INDICATOR_CHARGE,bci.getChargeIndicator());
		assertEquals("BackwardCallIndicators value getCalledPartysStatusIndicator  does not match:",bci._CPSI_SUBSCRIBER_FREE,bci.getCalledPartysStatusIndicator());
		assertEquals("BackwardCallIndicators value getCalledPartysCategoryIndicator  does not match:",bci._CPCI_PAYPHONE,bci.getCalledPartysCategoryIndicator());
		assertEquals("BackwardCallIndicators value getEndToEndMethodIndicator  does not match:",bci._ETEMI_SCCP,bci.getEndToEndMethodIndicator());
		assertEquals("BackwardCallIndicators value isInterworkingIndicator  does not match:",bci._II_IE,bci.isInterworkingIndicator());
		assertEquals("BackwardCallIndicators value isEndToEndInformationIndicator  does not match:",bci._ETEII_NO_IA,bci.isEndToEndInformationIndicator());
		assertEquals("BackwardCallIndicators value isIsdnUserPartIndicator  does not match:",bci._ISDN_UPI_UATW,bci.isIsdnUserPartIndicator());
		assertEquals("BackwardCallIndicators value isHoldingIndicator  does not match:",bci._HI_REQUESTED,bci.isHoldingIndicator());
		assertEquals("BackwardCallIndicators value isIsdnAccessIndicator  does not match:",bci._ISDN_AI_TA_ISDN,bci.isIsdnAccessIndicator());
		assertEquals("BackwardCallIndicators value isEchoControlDeviceIndicator  does not match:",bci._ECDI_IECD_NOT_INCLUDED,bci.isEchoControlDeviceIndicator());
		assertEquals("BackwardCallIndicators value getSccpMethodIndicator  does not match:",bci._SCCP_MI_CONNECTION_ORIENTED,bci.getSccpMethodIndicator());
		
		
		
		TransmissionMediumUsed tmu=(TransmissionMediumUsed) cpg.getParameter(TransmissionMediumUsed._PARAMETER_CODE);
		assertEquals("TransmissionMediumUsed value getTransimissionMediumUsed  does not match:",0x03,tmu.getTransimissionMediumUsed());
		
		ConnectedNumber cn=(ConnectedNumber) cpg.getParameter(ConnectedNumber._PARAMETER_CODE);
		
		//XXX: note this can fail, once we decide when APRI is done
		
		assertEquals("ConnectedNumber value getNatureOfAddressIndicator  does not match:",cn._NAI_SUBSCRIBER_NUMBER,cn.getNatureOfAddressIndicator());
		assertEquals("ConnectedNumber value getNumberingPlanIndicator  does not match:",cn._NPI_TELEX,cn.getNumberingPlanIndicator());
		assertEquals("ConnectedNumber value getAddressRepresentationRestrictedIndicator  does not match:",cn._APRI_NOT_AVAILABLE,cn.getAddressRepresentationRestrictedIndicator());
		assertEquals("ConnectedNumber value getScreeningIndicator  does not match:",cn._SI_NETWORK_PROVIDED,cn.getScreeningIndicator(),cn._SI_NETWORK_PROVIDED);
		assertEquals("ConnectedNumber value getAddress  does not match:","380683",cn.getAddress());
		
		
		
	}
	
	@Override
	protected byte[] getDefaultBody() {
		//FIXME: for now we strip MTP part
		byte[] message={
				0x0C
				,(byte) 0x0B
				,CallProgressMessage._MESSAGE_CODE_CPG
				//EventInformation
				,0x02
				//no mandatory varialbe part, no pointer
				//pointer to optioanl part
				,0x01
				
				//backward call idnicators
				,BackwardCallIndicators._PARAMETER_CODE
				,0x02
				,(byte) 0xA6
				,(byte) 0x9D
				
				//TransmissionMedium Used
				,TransmissionMediumUsed._PARAMETER_CODE
				,0x01
				,0x03
				
				//Connected Number
				,ConnectedNumber._PARAMETER_CODE
				,0x05
				,0x01
				,0x4B
				,(byte) (0x83&0xFF)
				,0x60
				,0x38
				
				
				//End of Opt Part
				,0x00

		};
		return message;
	}

	@Override
	protected ISUPMessage getDefaultMessage() {
		return super.messageFactory.createCPG();
	}
}
