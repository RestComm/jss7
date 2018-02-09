/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javolution.text.TextBuilder;
import javolution.util.FastList;
import javolution.xml.XMLFormat;
import javolution.xml.XMLSerializable;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.ss7.m3ua.parameter.DestinationPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.LocalRKIdentifier;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.OPCList;
import org.mobicents.protocols.ss7.m3ua.parameter.Parameter;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingKey;
import org.mobicents.protocols.ss7.m3ua.parameter.ServiceIndicators;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 *
 * @author amit bhayani
 *
 */
public class RoutingKeyImpl extends ParameterImpl implements RoutingKey, XMLSerializable {
    private static final String LOCAL_RK_ID = "localRkId";
    private static final String ROUTING_CONTEXT = "rc";
    private static final String TRAFFIC_MODE = "trafficMode";
    private static final String NETWORK_APPEARANCE = "netAppearance";
    private static final String DPCS = "dpcs";
    private static final String DPC_ARRAY_SIZE = "dpcsSize";
    private static final String SIS = "sis";
    private static final String SI_ARRAY_SIZE = "sisSize";
    private static final String OPC_LIST = "opcList";
    private static final String OPC_ARRAY_SIZE = "opcSize";

    private LocalRKIdentifier localRkId;
    private RoutingContext rc;
    private TrafficModeType trafMdTy;
    private NetworkAppearance netApp;
    private DestinationPointCode[] dpc;
    private ServiceIndicators[] servInds;
    private OPCList[] opcList;

    private ByteBuf buf = Unpooled.buffer(256);

    private byte[] value;

    public RoutingKeyImpl() {
        this.tag = Parameter.Routing_Key;
    }

    protected RoutingKeyImpl(byte[] value) {
        this.tag = Parameter.Routing_Key;
        this.value = value;

        this.decode(value);

        this.value = value;
    }

    protected RoutingKeyImpl(LocalRKIdentifier localRkId, RoutingContext rc, TrafficModeType trafMdTy,
            NetworkAppearance netApp, DestinationPointCode[] dpc, ServiceIndicators[] servInds, OPCList[] opcList) {
        this.tag = Parameter.Routing_Key;
        this.localRkId = localRkId;
        this.rc = rc;
        this.trafMdTy = trafMdTy;
        this.netApp = netApp;
        this.dpc = dpc;
        this.servInds = servInds;
        this.opcList = opcList;

        this.encode();
    }

    private void decode(byte[] data) {
        int pos = 0;
        FastList<DestinationPointCode> dpcList = new FastList<DestinationPointCode>();
        FastList<ServiceIndicators> serIndList = new FastList<ServiceIndicators>();
        FastList<OPCList> opcListList = new FastList<OPCList>();

        while (pos < data.length) {
            short tag = (short) ((data[pos] & 0xff) << 8 | (data[pos + 1] & 0xff));
            short len = (short) ((data[pos + 2] & 0xff) << 8 | (data[pos + 3] & 0xff));

            byte[] value = new byte[len - 4];

            System.arraycopy(data, pos + 4, value, 0, value.length);
            pos += len;
            // parameters.put(tag, factory.createParameter(tag, value));
            switch (tag) {
                case ParameterImpl.Local_Routing_Key_Identifier:
                    this.localRkId = new LocalRKIdentifierImpl(value);
                    break;

                case ParameterImpl.Routing_Context:
                    this.rc = new RoutingContextImpl(value);
                    break;

                case ParameterImpl.Traffic_Mode_Type:
                    this.trafMdTy = new TrafficModeTypeImpl(value);
                    break;

                case ParameterImpl.Network_Appearance:
                    this.netApp = new NetworkAppearanceImpl(value);
                    break;

                case ParameterImpl.Destination_Point_Code:
                    dpcList.add(new DestinationPointCodeImpl(value));
                    break;
                case ParameterImpl.Service_Indicators:
                    serIndList.add(new ServiceIndicatorsImpl(value));
                    break;
                case ParameterImpl.Originating_Point_Code_List:
                    opcListList.add(new OPCListImpl(value));
                    break;
            }

            // The Parameter Length does not include any padding octets. We have
            // to consider padding here
            pos += (pos % 4);
        }// end of while

        this.dpc = new DestinationPointCode[dpcList.size()];
        this.dpc = dpcList.toArray(this.dpc);

        if (serIndList.size() > 0) {
            this.servInds = new ServiceIndicators[serIndList.size()];
            this.servInds = serIndList.toArray(this.servInds);
        }

        if (opcListList.size() > 0) {
            this.opcList = new OPCList[opcListList.size()];
            this.opcList = opcListList.toArray(this.opcList);
        }
    }

    private void encode() {
        if (this.localRkId != null) {
            ((LocalRKIdentifierImpl) this.localRkId).write(buf);
        }

        if (this.rc != null) {
            ((RoutingContextImpl) rc).write(buf);
        }

        if (this.trafMdTy != null) {
            ((TrafficModeTypeImpl) trafMdTy).write(buf);
        }

        if (this.netApp != null) {
            ((NetworkAppearanceImpl) this.netApp).write(buf);
        }

        for (int i = 0; i < this.dpc.length; i++) {
            ((DestinationPointCodeImpl) this.dpc[i]).write(buf);

            if (this.servInds != null) {
                ((ServiceIndicatorsImpl) this.servInds[i]).write(buf);
            }

            if (this.opcList != null) {
                ((OPCListImpl) this.opcList[i]).write(buf);
            }
        }

        int length = buf.readableBytes();
        value = new byte[length];
        buf.getBytes(buf.readerIndex(), value);
    }

    @Override
    protected byte[] getValue() {
        return this.value;
    }

    public DestinationPointCode[] getDestinationPointCodes() {
        return this.dpc;
    }

    public LocalRKIdentifier getLocalRKIdentifier() {
        return this.localRkId;
    }

    public NetworkAppearance getNetworkAppearance() {
        return this.netApp;
    }

    public OPCList[] getOPCLists() {
        return this.opcList;
    }

    public RoutingContext getRoutingContext() {
        return this.rc;
    }

    public ServiceIndicators[] getServiceIndicators() {
        return this.servInds;
    }

    public TrafficModeType getTrafficModeType() {
        return this.trafMdTy;
    }

    @Override
    public String toString() {
        TextBuilder tb = TextBuilder.newInstance();
        tb.append("RoutingKey(");
        if (localRkId != null) {
            tb.append(localRkId.toString());
        }

        if (rc != null) {
            tb.append(rc.toString());
        }

        if (trafMdTy != null) {
            tb.append(trafMdTy.toString());
        }

        if (netApp != null) {
            tb.append(netApp.toString());
        }

        if (dpc != null) {
            tb.append(dpc.toString());
        }

        if (servInds != null) {
            tb.append(servInds.toString());
        }

        if (opcList != null) {
            tb.append(opcList.toString());
        }
        tb.append(")");
        return tb.toString();
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<RoutingKeyImpl> RC_XML = new XMLFormat<RoutingKeyImpl>(RoutingKeyImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RoutingKeyImpl routingKey) throws XMLStreamException {
            int dpcArraySize = xml.getAttribute(DPC_ARRAY_SIZE).toInt();
            int opcArraySize = xml.getAttribute(OPC_ARRAY_SIZE).toInt();
            int siArraySize = xml.getAttribute(SI_ARRAY_SIZE).toInt();

            routingKey.localRkId = xml.get(LOCAL_RK_ID);
            routingKey.rc = xml.get(ROUTING_CONTEXT);
            routingKey.trafMdTy = xml.get(TRAFFIC_MODE);
            routingKey.netApp = xml.get(NETWORK_APPEARANCE);

            if (dpcArraySize != -1) {
                routingKey.dpc = new DestinationPointCodeImpl[dpcArraySize];
                for (int i = 0; i < dpcArraySize; i++) {
                    routingKey.dpc[i] = xml.get(DPCS);
                }
            }

            if (opcArraySize != -1) {
                routingKey.opcList = new OPCList[opcArraySize];
                for (int i = 0; i < opcArraySize; i++) {
                    routingKey.opcList[i] = xml.get(OPC_LIST);
                }
            }

            if (siArraySize != -1) {
                routingKey.servInds = new ServiceIndicators[siArraySize];
                for (int i = 0; i < siArraySize; i++) {
                    routingKey.servInds[i] = xml.get(SIS);
                }
            }

            routingKey.encode();
        }

        @Override
        public void write(RoutingKeyImpl routingKey, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (routingKey.dpc != null) {
                xml.setAttribute(DPC_ARRAY_SIZE, routingKey.dpc.length);
            } else {
                xml.setAttribute(DPC_ARRAY_SIZE, -1);
            }

            if (routingKey.opcList != null) {
                xml.setAttribute(OPC_ARRAY_SIZE, routingKey.opcList.length);
            } else {
                xml.setAttribute(OPC_ARRAY_SIZE, -1);
            }

            if (routingKey.servInds != null) {
                xml.setAttribute(SI_ARRAY_SIZE, routingKey.servInds.length);
            } else {
                xml.setAttribute(SI_ARRAY_SIZE, -1);
            }

            xml.add(routingKey.localRkId, LOCAL_RK_ID);
            xml.add(routingKey.rc, ROUTING_CONTEXT);
            xml.add(routingKey.trafMdTy, TRAFFIC_MODE);
            xml.add(routingKey.netApp, NETWORK_APPEARANCE);

            if (routingKey.dpc != null) {
                for (int i = 0; i < routingKey.dpc.length; i++) {
                    xml.add(routingKey.dpc[i], DPCS);
                }
            }

            if (routingKey.opcList != null) {
                for (int i = 0; i < routingKey.opcList.length; i++) {
                    xml.add(routingKey.opcList[i], OPC_LIST);
                }
            }

            if (routingKey.servInds != null) {
                for (int i = 0; i < routingKey.servInds.length; i++) {
                    xml.add(routingKey.servInds[i], SIS);
                }
            }
        }
    };
}
