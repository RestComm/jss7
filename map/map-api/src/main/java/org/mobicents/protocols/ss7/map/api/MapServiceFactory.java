package org.mobicents.protocols.ss7.map.api;

public interface MapServiceFactory {
	
	public ServiceRequest createServiceRequest(int servicePrimitive);
	
	public ServiceResponse createServiceResponse(int servicePrimitive);
	
	public ServiceIndication createServiceIndication(int servicePrimitive);
	
	public ServiceConfirm createServiceConfirm(int servicePrimitive);

}
