package org.mobicents.protocols.ss7.map;

import java.util.ArrayList;
import java.util.List;

import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.ServiceRequest;
import org.mobicents.protocols.ss7.map.api.ServiceResponse;

/**
 * 
 * @author amit bhayani
 * 
 */
public class MAPDialogImpl implements MAPDialog {
	
	private MAPDialogState state = MAPDialogState.OPEN;

	// Queue to hold the MAP Primitives
	private List<ServiceRequest> serviceReqQueue = new ArrayList<ServiceRequest>();
	private List<ServiceResponse> serviceRespQueue = new ArrayList<ServiceResponse>();

	// Application Context of this Dialog
	private MAPApplicationContext appCntx;

	public MAPDialogImpl(MAPApplicationContext appCntx) {
		this.appCntx = appCntx;
	}

	public void abort(int userReason) {
		// TODO Auto-generated method stub

	}

	public void addServiceRequest(ServiceRequest serviceRequest) {
		if(this.state == MAPDialogState.UNCONFIRMED_CONTINUE){
			//TODO Do not accept the primitive here yet
		}
		serviceReqQueue.add(serviceRequest);
	}

	public void addServiceResponse(ServiceResponse serviceResponse) {
		serviceRespQueue.add(serviceResponse);
	}

	public void close(boolean prearrangedEnd) {

	}

	public void send() {
		if(this.state == MAPDialogState.OPEN){
			//TODO this TC_BEGIN
		} else if(this.state == MAPDialogState.CONFIRMED_CONTINUE){
			//TODO this is TC_CONTINUE
		}
		
		//TODO state where it should raise exception?
	}

	public void sendOpenServiceResponse() {

	}

}
