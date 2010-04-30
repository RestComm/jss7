package org.mobicents.protocols.ss7.map.api;

import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MapServiceFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequest(
			byte ussdDataCodingScheme, USSDString ussdString);

	public ProcessUnstructuredSSResponse createProcessUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, USSDString ussdString);

	public UnstructuredSSRequest createUnstructuredSSRequest(
			byte ussdDataCodingScheme, USSDString ussdString);

	public UnstructuredSSResponse createUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, USSDString ussdString);

	public USSDString createUSSDString(String ussdString, Charset charSet);

	public USSDString createUSSDString(byte[] ussdString, Charset charSet);
	
	public AddressString createAddressString(AddressNature addNature, NumberingPlan numPlan, String address);

}
