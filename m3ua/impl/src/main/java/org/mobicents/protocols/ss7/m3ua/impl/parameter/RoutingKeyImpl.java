package org.mobicents.protocols.ss7.m3ua.impl.parameter;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;
import javolution.util.FastList;

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
public class RoutingKeyImpl extends ParameterImpl implements RoutingKey {

    private LocalRKIdentifier localRkId;
    private RoutingContext rc;
    private TrafficModeType trafMdTy;
    private NetworkAppearance netApp;
    private DestinationPointCode[] dpc;
    private ServiceIndicators[] servInds;
    private OPCList[] opcList;

    private ByteBuffer buffer = ByteBuffer.allocate(256);

    private byte[] value;

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
            ((LocalRKIdentifierImpl) this.localRkId).write(buffer);
        }

        if (this.rc != null) {
            ((RoutingContextImpl) rc).write(buffer);
        }

        if (this.trafMdTy != null) {
            ((TrafficModeTypeImpl) trafMdTy).write(buffer);
        }

        if (this.netApp != null) {
            ((NetworkAppearanceImpl) this.netApp).write(buffer);
        }

        for (int i = 0; i < this.dpc.length; i++) {
            ((DestinationPointCodeImpl) this.dpc[i]).write(buffer);

            if (this.servInds != null) {
                ((ServiceIndicatorsImpl) this.servInds[i]).write(buffer);
            }

            if (this.opcList != null) {
                ((OPCListImpl) this.opcList[i]).write(buffer);
            }
        }

        this.value = new byte[buffer.position()];
        buffer.flip();
        buffer.get(this.value);
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
}
