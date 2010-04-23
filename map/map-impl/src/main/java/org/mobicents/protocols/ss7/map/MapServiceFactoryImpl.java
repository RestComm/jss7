package org.mobicents.protocols.ss7.map;

import org.mobicents.protocols.ss7.map.api.MapServiceFactory;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.ProcessUnstructuredSSResponseImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSRequestImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.UnstructuredSSResponseImpl;

/**
 * 
 * @author amit bhayani
 *
 */
public class MapServiceFactoryImpl implements MapServiceFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequest(
			byte ussdDataCodingScheme, byte[] ussdString) {

		ProcessUnstructuredSSRequest request = new ProcessUnstructuredSSRequestImpl(
				ussdDataCodingScheme, ussdString);
		return request;
	}

	public ProcessUnstructuredSSResponse createProcessUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, byte[] ussdString) {
		ProcessUnstructuredSSResponse response = new ProcessUnstructuredSSResponseImpl(
				ussdDataCodingScheme, ussdString);
		return response;
	}

	public UnstructuredSSRequest createUnstructuredSSRequest(
			byte ussdDataCodingScheme, byte[] ussdString) {
		UnstructuredSSRequest request = new UnstructuredSSRequestImpl(ussdDataCodingScheme, ussdString);
		return request;
	}

	public UnstructuredSSResponse createUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, byte[] ussdString) {
		UnstructuredSSResponse response = new UnstructuredSSResponseImpl(ussdDataCodingScheme, ussdString);
		return response;
	}

}
