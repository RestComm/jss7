package org.mobicents.protocols.ss7.sccp;

import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

public interface SccpParameterFactory {

	//FIXME: add methods with params.
	public GlobalTitle getGlobalTitle100();
	public Importance getImportance();
	public ProtocolClass getProtocolClass();
	public SccpAddress getSccpAddress();
	public Segmentation getSegmentation();
	
	
}
