package org.mobicents.protocols.ss7.map;

import java.nio.charset.Charset;

import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.dialog.AddressNature;
import org.mobicents.protocols.ss7.map.api.dialog.AddressString;
import org.mobicents.protocols.ss7.map.api.dialog.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.dialog.AddressStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.USSDStringImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MapServiceFactoryImpl implements MapServiceFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequest(
			byte ussdDataCodingScheme, USSDString ussdString) {

		ProcessUnstructuredSSRequest request = new ProcessUnstructuredSSRequestImpl(
				ussdDataCodingScheme, ussdString);
		return request;
	}

	public ProcessUnstructuredSSResponse createProcessUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, USSDString ussdString) {
		ProcessUnstructuredSSResponse response = new ProcessUnstructuredSSResponseImpl(
				ussdDataCodingScheme, ussdString);
		return response;
	}

	public UnstructuredSSRequest createUnstructuredSSRequest(
			byte ussdDataCodingScheme, USSDString ussdString) {
		UnstructuredSSRequest request = new UnstructuredSSRequestImpl(
				ussdDataCodingScheme, ussdString);
		return request;
	}

	public UnstructuredSSResponse createUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, USSDString ussdString) {
		UnstructuredSSResponse response = new UnstructuredSSResponseImpl(
				ussdDataCodingScheme, ussdString);
		return response;
	}

	public USSDString createUSSDString(String ussdString, Charset charset) {
		return new USSDStringImpl(ussdString, charset);
	}

	public USSDString createUSSDString(byte[] ussdString, Charset charset) {
		return new USSDStringImpl(ussdString, charset);
	}

	public AddressString createAddressString(AddressNature addNature,
			NumberingPlan numPlan, String address) {
		return new AddressStringImpl(addNature, numPlan, address);
	}

}
