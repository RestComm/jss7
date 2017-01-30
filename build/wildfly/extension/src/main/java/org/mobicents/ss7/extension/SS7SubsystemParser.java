package org.mobicents.ss7.extension;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.parsing.Attribute;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.util.Collections;
import java.util.List;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP_ADDR;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;
import static org.jboss.as.controller.parsing.ParseUtils.missingRequired;
import static org.jboss.as.controller.parsing.ParseUtils.requireNoNamespaceAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedAttribute;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

/**
/**
 * The subsystem parser, which uses stax to read and write to and from xml
 */
class SS7SubsystemParser implements XMLStreamConstants, XMLElementReader<List<ModelNode>>,
        XMLElementWriter<SubsystemMarshallingContext> {

    private static final SS7SubsystemParser INSTANCE = new SS7SubsystemParser();

    static SS7SubsystemParser getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContent(XMLExtendedStreamWriter writer, SubsystemMarshallingContext context) throws XMLStreamException {
        context.startSubsystemElement(Namespace.CURRENT.getUriString(), false);

        final ModelNode node = context.getModelNode();
        final ModelNode mbean = node.get(SS7MbeanDefinition.MBEAN);

        for (Property mbeanProp : mbean.asPropertyList()) {
            writer.writeStartElement(SS7MbeanDefinition.MBEAN);

            final ModelNode mbeanEntry = mbeanProp.getValue();

            final ModelNode property = mbeanEntry.get(SS7MbeanPropertyDefinition.PROPERTY);
            for (Property propertyProp : property.asPropertyList()) {
                writer.writeStartElement(SS7MbeanPropertyDefinition.PROPERTY);

                final ModelNode propertyEntry = propertyProp.getValue();

                SS7MbeanPropertyDefinition.NAME_ATTR.marshallAsAttribute(propertyEntry, true, writer);
                SS7MbeanPropertyDefinition.VALUE_ATTR.marshallAsAttribute(propertyEntry, true, writer);
                writer.writeEndElement();
            }

            writer.writeEndElement();
        }

        writer.writeEndElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readElement(XMLExtendedStreamReader reader, List<ModelNode> list) throws XMLStreamException {
        PathAddress address = PathAddress.pathAddress(PathElement.pathElement(SUBSYSTEM, SS7Extension.SUBSYSTEM_NAME));

        final ModelNode subsystem = new ModelNode();
        subsystem.get(OP).set(ADD);
        subsystem.get(OP_ADDR).set(address.toModelNode());
        final int count = reader.getAttributeCount();

        list.add(subsystem);

        // elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case SS7_1_0: {
                    final String tagName = reader.getLocalName();
                    if (tagName.equals(SS7MbeanDefinition.MBEAN)) {
                        parseMbean(reader, address, list);
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }

    }

    static void parseMbean(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        String name = null;
        final ModelNode mbean = new ModelNode();

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String value = reader.getAttributeValue(i);
            final Attribute attribute = Attribute.forName(reader.getAttributeLocalName(i));
            switch (attribute) {
                case NAME: {
                    name = value;
                    break;
                }
                default: {
                    throw unexpectedAttribute(reader, i);
                }
            }
        }

        //ParseUtils.requireNoContent(reader);

        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        mbean.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent, PathElement.pathElement(SS7MbeanDefinition.MBEAN, name));
        mbean.get(OP_ADDR).set(address.toModelNode());
        list.add(mbean);

        // Parse properties
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case SS7_1_0: {
                    final String tagName = reader.getLocalName();
                    if (tagName.equals(SS7MbeanPropertyDefinition.PROPERTY)) {
                        parseProperty(reader, address, list);
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }
    }

    static void parseProperty(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        String name = null;
        final ModelNode property = new ModelNode();

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            final String value = reader.getAttributeValue(i);
            switch (SS7MbeanPropertyDefinition.Element.of(attribute)) {
                case NAME: {
                    name = value;
                    SS7MbeanPropertyDefinition.NAME_ATTR.parseAndSetParameter(value, property, reader);
                    break;
                }
                case VALUE: {
                    SS7MbeanPropertyDefinition.VALUE_ATTR.parseAndSetParameter(value, property, reader);
                    break;
                }
                default: {
                    throw unexpectedAttribute(reader, i);
                }
            }
        }

        ParseUtils.requireNoContent(reader);

        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        property.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent, PathElement.pathElement(SS7MbeanPropertyDefinition.PROPERTY, name));
        property.get(OP_ADDR).set(address.toModelNode());

        list.add(property);
    }
}