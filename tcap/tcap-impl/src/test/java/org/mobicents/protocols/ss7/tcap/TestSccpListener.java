package org.mobicents.protocols.ss7.tcap;

import org.mobicents.protocols.ss7.sccp.NetworkIdState;
import org.mobicents.protocols.ss7.sccp.RemoteSccpStatus;
import org.mobicents.protocols.ss7.sccp.SccpListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SignallingPointStatus;
import org.mobicents.protocols.ss7.sccp.message.SccpDataMessage;
import org.mobicents.protocols.ss7.sccp.message.SccpNoticeMessage;

public class TestSccpListener implements SccpListener {

	private static final long serialVersionUID = 1L;

	private boolean congestedStatusReceived = false;
	
	@Override
	public void onMessage(SccpDataMessage message) {
		
	}

	@Override
	public void onNotice(SccpNoticeMessage message) {
		
	}

	@Override
	public void onCoordResponse(int ssn, int multiplicityIndicator) {
		
	}

	@Override
	public void onState(int dpc, int ssn, boolean inService,
			int multiplicityIndicator) {
		
	}

	@Override
	public void onPcState(int dpc, SignallingPointStatus status, Integer restrictedImportanceLevel, RemoteSccpStatus remoteSccpStatus) {
		if(status.equals(status.CONGESTED))
		    congestedStatusReceived = true;
	}

	@Override
	public void onNetworkIdState(int networkId, NetworkIdState networkIdState) {
		
	}

	public boolean isCongestedStatusReceived() {
		return congestedStatusReceived;
	}

}
