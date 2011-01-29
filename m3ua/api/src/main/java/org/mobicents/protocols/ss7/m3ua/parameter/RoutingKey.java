package org.mobicents.protocols.ss7.m3ua.parameter;

/**
 * The routing key is used to associate traffic with the proper application
 * server and ASP. 
 * 
 * @author amit bhayani
 * 
 */
public interface RoutingKey extends Parameter {

    public LocalRKIdentifier getLocalRKIdentifier();

    public RoutingContext getRoutingContext();

    public TrafficModeType getTrafficModeType();

    public NetworkAppearance getNetworkAppearance();

    public DestinationPointCode[] getDestinationPointCodes();

    public ServiceIndicators[] getServiceIndicators();

    public OPCList[] getOPCLists();

}
