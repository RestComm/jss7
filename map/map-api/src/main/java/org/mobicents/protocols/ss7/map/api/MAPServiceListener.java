package org.mobicents.protocols.ss7.map.api;

import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSConfirm;
import org.mobicents.protocols.ss7.map.api.service.supplementary.ProcessUnstructuredSSIndication;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSConfirm;
import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSIndication;

/**
 * 
 * @author amit bhayani
 *
 */
public interface MAPServiceListener {

	
	public void onProcessUnstructuredSSIndication(ProcessUnstructuredSSIndication procUnstrInd);
	
	public void onProcessUnstructuredSSConfirm(ProcessUnstructuredSSConfirm procUnstrCnfrm);
	
	public void onUnstructuredSSIndication(UnstructuredSSIndication unstrInd);
	
	public void onUnstructuredSSConfirm(UnstructuredSSConfirm unstrCnfrm);
	
}
