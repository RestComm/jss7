/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.protocols.ss7.m3ua.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.m3ua.As;
import org.mobicents.protocols.ss7.m3ua.Asp;
import org.mobicents.protocols.ss7.m3ua.ExchangeType;
import org.mobicents.protocols.ss7.m3ua.Functionality;
import org.mobicents.protocols.ss7.m3ua.IPSPType;
import org.mobicents.protocols.ss7.m3ua.RouteAs;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.DeregistrationResultImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.DeregistrationStatusImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RegistrationResultImpl;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.RegistrationStatusImpl;
import org.mobicents.protocols.ss7.m3ua.message.MessageClass;
import org.mobicents.protocols.ss7.m3ua.message.MessageType;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationRequest;
import org.mobicents.protocols.ss7.m3ua.message.rkm.DeregistrationResponse;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationRequest;
import org.mobicents.protocols.ss7.m3ua.message.rkm.RegistrationResponse;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.DeregistrationStatus;
import org.mobicents.protocols.ss7.m3ua.parameter.RegistrationResult;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 *
 */
public class RoutingKeyManagementHandler extends MessageHandler {
    private static final Logger logger = Logger.getLogger(RoutingKeyManagementHandler.class);

    private static final String KEY_SEPARATOR = ":";
    private static final String AS_NAME = "asRkm";

    private AtomicInteger asCounter = new AtomicInteger(1);

    public RoutingKeyManagementHandler(AspFactoryImpl aspFactoryImpl) {
        super(aspFactoryImpl);
    }

    public void handleRegistrationRequest(RegistrationRequest registrationRequest) {

        String asName = null;
        RoutingKey routingKey = registrationRequest.getRoutingKey();
        TrafficModeType trafficMode = routingKey.getTrafficModeType();
        RoutingContext rc = routingKey.getRoutingContext();
        int dpc = routingKey.getDestinationPointCodes()[0].getPointCode();
        int [] opcs = null;
        short [] sis = null;

        if(routingKey.getOPCLists() == null) {
            opcs = new int[]{-1};
        } else {
            opcs = routingKey.getOPCLists()[0].getPointCodes();
        }

        if(routingKey.getServiceIndicators() == null) {
            sis = new short []{-1};
        } else {
            sis = routingKey.getServiceIndicators()[0].getIndicators();
        }
        RouteAs routeAs = compareRoutingKeys(dpc, opcs, sis);
        if(routeAs != null) {
            //if the ASP is not currently included in the list of ASPs for the related Application Server, the SGP MAY authorize the ASP to be added to the AS
            for(As as : routeAs.getAsArray()) {
                if(as!=null) {
                List<Asp> currList = as.getAspList();
                    boolean found = false;
                      for(Asp subAsp : currList) {
                          if(subAsp.getName().equals(aspFactoryImpl.getName())) {
                              found = true;
                              break;
                          }
                      }
                      if(!found) {
                          try {
                              aspFactoryImpl.m3UAManagementImpl.assignAspToAs(as.getName(), aspFactoryImpl.getName());
                          } catch (Exception e) {
                              logger.error("Can not assign ASP to AS after register request", e);
                              sendRegisterError(routingKey, 1);
                              return;
                          }
                      }
                }
            }
        } else {
            //create AS
            Functionality functionality = null;
            ExchangeType exchangeType = null;
            IPSPType ipspType = null;
            int minAspActiveForLoadbalance = 0;
            if(!aspFactoryImpl.m3UAManagementImpl.appServers.isEmpty()) {
                for(As as : aspFactoryImpl.m3UAManagementImpl.appServers) {
                    if(as != null) {
                        functionality = as.getFunctionality();
                        exchangeType = as.getExchangeType();
                        ipspType = as.getIpspType();
                        minAspActiveForLoadbalance = as.getMinAspActiveForLb();
                    }
                }
                try {
                    asName = AS_NAME+asCounter.getAndIncrement();
                    aspFactoryImpl.m3UAManagementImpl.createAs(asName, functionality, exchangeType, ipspType, rc, trafficMode, minAspActiveForLoadbalance, routingKey.getNetworkAppearance());
                } catch (Exception e) {
                    logger.error("Can not create AS after register request", e);
                    sendRegisterError(routingKey, 1);
                    return;
                }
            } else {
                logger.error("Can not create AS because of empty AS list after register request. Creation new one based on exited one.");
                sendRegisterError(routingKey, 1);
                return;
            }
            //assign ASP to AS
            try {
                aspFactoryImpl.m3UAManagementImpl.assignAspToAs(asName, this.aspFactoryImpl.getName());
            } catch (Exception e) {
                logger.error("Can not assign ASP to AS after register request", e);
                sendRegisterError(routingKey, 1);
                return;
            }
            //create KEY(route)
            try {
                addRoute(asName, dpc, opcs, sis, trafficMode.getMode());
            } catch (Exception e) {
                logger.error("Can not add Route after register request", e);
                sendRegisterError(routingKey, 1);
                return;
            }
        }
        //send response
        RegistrationResponse registrationResponse = (RegistrationResponse) this.aspFactoryImpl.messageFactory.createMessage(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_RESPONSE);
        RegistrationResult rslt =  new RegistrationResultImpl(routingKey.getLocalRKIdentifier(), new RegistrationStatusImpl(0), rc);
        registrationResponse.setRegistrationResult(rslt);
        this.aspFactoryImpl.write(registrationResponse);
    }

    public void handleRegistrationResponse(RegistrationResponse registrationResponse) {
        logger.error(String.format("Received REGRES=%s. Handling of RKM message is not supported", registrationResponse));
    }

    public void handleDeregistrationRequest(DeregistrationRequest deregistrationRequest) {
        RoutingContext rc = deregistrationRequest.getRoutingContext();
        for(long currRc : rc.getRoutingContexts()) {
            AspImpl aspImpl = aspFactoryImpl.getAsp(currRc);
            if(aspImpl!=null) {
                //ASP to inactive state
                try {
                    AsImpl as = aspImpl.asImpl;
                    //destroyAspFactory
                    aspFactoryImpl.m3UAManagementImpl.stopAsp(aspImpl.getName());
                    aspFactoryImpl.m3UAManagementImpl.unassignAspFromAs(as.getName(), aspImpl.getName());
                    //if AS does not have ASP remove it
                    if(as.appServerProcs.isEmpty()) {
                        for(Entry<String, RouteAs> entry : aspFactoryImpl.m3UAManagementImpl.getRoute().entrySet()) {
                            if(((RouteAsImpl)entry.getValue()).hasAs(as)) {
                                String key = entry.getKey();
                                String[] keys = key.split(KEY_SEPARATOR);
                                aspFactoryImpl.m3UAManagementImpl.removeRoute(Integer.valueOf(keys[0]),Integer.valueOf(keys[1]),Integer.valueOf(keys[2]), as.getName());
                            }
                        }
                        aspFactoryImpl.m3UAManagementImpl.destroyAs(as.getName());
                    }
                    aspFactoryImpl.m3UAManagementImpl.startAsp(aspImpl.getName());

                    DeregistrationResponse deregistrationResponse = (DeregistrationResponse) this.aspFactoryImpl.messageFactory.createMessage(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_RESPONSE);
                    DeregistrationResult derslt =  new DeregistrationResultImpl(rc, new DeregistrationStatusImpl(0));
                    deregistrationResponse.setDeregistrationResult(derslt);
                    this.aspFactoryImpl.write(deregistrationResponse);
                    return;
                } catch (Exception e) {
                    logger.error("Can not add Route after register request", e);
                    sendDeregisterError(rc, 1);
                    return;
                }
            } else {
                logger.error("Can not find ASP for RC : "+currRc+" register request");
                sendDeregisterError(rc, 1);
                return;
            }
        }
    }

    public void handleDeregistrationResponse(DeregistrationResponse deregistrationResponse) {
        logger.error(String.format("Received DEREGRES=%s. Handling of RKM message is not supported", deregistrationResponse));
    }

    private RouteAs compareRoutingKeys(int dpc, int[] opcIntArr, short[] siShortArr) {
        RouteAs route = null;
        for (int i = 0; i < opcIntArr.length; i++) {
            for (int j = 0; j < siShortArr.length; j++) {
                String sKey = (new StringBuffer().append(dpc).append(KEY_SEPARATOR).append(opcIntArr[i]).append(KEY_SEPARATOR).append(siShortArr[j])).toString();
                route = aspFactoryImpl.m3UAManagementImpl.getRoute().get(sKey);
                if(route !=null)
                    return route;
            }
        }
        return route;
    }

    private void addRoute(String asName, int dpc, int[] opcIntArr, short[] siShortArr, int trafficMode) throws Exception {
        for (int i = 0; i < opcIntArr.length; i++) {
            for (int j = 0; j < siShortArr.length; j++) {
                aspFactoryImpl.m3UAManagementImpl.addRoute(dpc, opcIntArr[i], siShortArr[j], asName, trafficMode);
            }
        }
    }

    private void sendRegisterError(RoutingKey routingKey, int errorCode) {
        RegistrationResponse registrationResponse = (RegistrationResponse) this.aspFactoryImpl.messageFactory.createMessage(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.REG_RESPONSE);
        RegistrationResult rslt =  new RegistrationResultImpl(routingKey.getLocalRKIdentifier(), new RegistrationStatusImpl(errorCode), routingKey.getRoutingContext());
        registrationResponse.setRegistrationResult(rslt);
        this.aspFactoryImpl.write(registrationResponse);
    }

    private void sendDeregisterError(RoutingContext rc, int errorCode) {
        DeregistrationResponse deregistrationResponse = (DeregistrationResponse) this.aspFactoryImpl.messageFactory.createMessage(MessageClass.ROUTING_KEY_MANAGEMENT, MessageType.DEREG_RESPONSE);
        DeregistrationStatus status = new DeregistrationStatusImpl(1);
        DeregistrationResult rslt = new DeregistrationResultImpl(rc, status);
        deregistrationResponse.setDeregistrationResult(rslt);
        this.aspFactoryImpl.write(deregistrationResponse);
    }

}
