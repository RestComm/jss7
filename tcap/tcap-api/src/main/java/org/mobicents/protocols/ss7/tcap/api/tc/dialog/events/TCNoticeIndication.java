package org.mobicents.protocols.ss7.tcap.api.tc.dialog.events;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

public interface TCNoticeIndication extends DialogIndication{

	
	public SccpAddress getOriginationAddress();
	public SccpAddress getDestinationAddress();
	public ReportCause getReportCause();
}
