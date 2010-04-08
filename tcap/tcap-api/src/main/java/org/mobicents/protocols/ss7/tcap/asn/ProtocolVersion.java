package org.mobicents.protocols.ss7.tcap.asn;

import org.mobicents.protocols.asn.Tag;

public interface ProtocolVersion extends Encodable{
	public static final int _TAG_PROTOCOL_VERSION = 0x00;
	public static final int _TAG_PROTOCOL_VERSION_CLASS = Tag.CLASS_CONTEXT_SPECIFIC;
	public static final boolean _TAG_PROTOCOL_VERSION_PRIMITIVE = true;
	
	public int getProtocolVersion();
	
}
