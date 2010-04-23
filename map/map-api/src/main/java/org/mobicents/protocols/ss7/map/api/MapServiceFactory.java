package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSResponse;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSRequest;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public interface MapServiceFactory {

	public ProcessUnstructuredSSRequest createProcessUnstructuredSSRequest(
			byte ussdDataCodingScheme, byte[] ussdString);

	public ProcessUnstructuredSSResponse createProcessUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, byte[] ussdString);

	public UnstructuredSSRequest createUnstructuredSSRequest(
			byte ussdDataCodingScheme, byte[] ussdString);

	public UnstructuredSSResponse createUnstructuredSsRequestResponse(
			int invokeID, byte ussdDataCodingScheme, byte[] ussdString);

}
