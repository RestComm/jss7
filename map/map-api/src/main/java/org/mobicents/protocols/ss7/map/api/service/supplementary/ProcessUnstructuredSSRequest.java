package org.mobicents.protocols.ss7.map.api.service.supplementary;

import org.mobicents.protocols.ss7.map.api.ServiceRequest;

/**
 * This service is used between the MSC and the VLR and between the VLR and the
 * HLR to relay information in order to allow unstructured supplementary service
 * operation.
 * 
 * @author amit bhayani
 * 
 */
public interface ProcessUnstructuredSSRequest extends ServiceRequest, USSDService {
	public static final int SERVICE_PRIMITIVE = 1;
}
