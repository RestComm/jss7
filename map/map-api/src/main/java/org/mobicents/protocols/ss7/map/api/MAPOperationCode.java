package org.mobicents.protocols.ss7.map.api;

/**
 * Standard Operation Code included in Invoke. ETS 300 974: December 2000 (GSM
 * 09.02 version 5.15.1)
 * 
 * @author amit bhayani
 * 
 */
public interface MAPOperationCode {

	// -- supplementary service handling operation codes
	public static final int registerSS = 10;
	public static final int eraseSS = 11;
	public static final int activateSS = 12;
	public static final int deactivateSS = 13;
	public static final int interrogateSS = 14;

	public static final int processUnstructuredSS_Request = 59;
	public static final int unstructuredSS_Request = 60;
	public static final int unstructuredSS_Notify = 61;
	public static final int registerPassword = 17;
	public static final int getPassword = 18;
	
	
	//-- short message service operation codes
	public static final int  sendRoutingInfoForSM = 45;
	public static final int  mo_forwardSM = 46;
	public static final int  mt_forwardSM = 44;
	public static final int  reportSM_DeliveryStatus = 47;
	public static final int  informServiceCentre = 63;
	public static final int  alertServiceCentre = 64;
	public static final int  readyForSM = 66;

}
