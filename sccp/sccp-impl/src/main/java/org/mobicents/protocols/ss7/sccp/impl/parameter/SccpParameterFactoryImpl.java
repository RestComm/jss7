package org.mobicents.protocols.ss7.sccp.impl.parameter;

import org.mobicents.protocols.ss7.sccp.SccpParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.Importance;
import org.mobicents.protocols.ss7.sccp.parameter.ProtocolClass;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.sccp.parameter.Segmentation;

public class SccpParameterFactoryImpl implements SccpParameterFactory{

	public GlobalTitle getGlobalTitle100() {
		// TODO Auto-generated method stub
		return new GT0100Impl();
	}

	public Importance getImportance() {
		// TODO Auto-generated method stub
		return new ImportanceImpl();
	}

	public ProtocolClass getProtocolClass() {
		// TODO Auto-generated method stub
		return new ProtocolClassImpl();
	}

	public SccpAddress getSccpAddress() {
		// TODO Auto-generated method stub
		return new SccpAddressImpl();
	}

	public Segmentation getSegmentation() {
		// TODO Auto-generated method stub
		return new SegmentationImpl();
	}

}
