package org.restcomm.protocols.ss7.map.service.mobility.subscriberInformation;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberInformation.RouteingNumber;
import org.restcomm.protocols.ss7.map.primitives.TbcdString;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class RouteingNumberImpl extends TbcdString implements RouteingNumber {

    private static final String NUMBER = "number";

    public RouteingNumberImpl() {
        super(1, 5, "RouteingNumber");
    }

    public RouteingNumberImpl(String data) {
        super(1, 5, "RouteingNumber", data);
    }

    public String getRouteingNumber() {
        return data;
    }

    /**
     * XML Serialization/Deserialization
     */
    protected static final XMLFormat<RouteingNumberImpl> ROUTEING_NUMBER_XML = new XMLFormat<RouteingNumberImpl>(RouteingNumberImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml, RouteingNumberImpl routeingNumber) throws XMLStreamException {
            routeingNumber.data = xml.getAttribute(NUMBER, "");
        }

        @Override
        public void write(RouteingNumberImpl routeingNumber, javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            xml.setAttribute(NUMBER, routeingNumber.data);
        }
    };
}
