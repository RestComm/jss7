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

            SS7MbeanDefinition.NAME_ATTR.marshallAsAttribute(mbeanEntry, true, writer);
            SS7MbeanDefinition.TYPE_ATTR.marshallAsAttribute(mbeanEntry, true, writer);
            SS7MbeanDefinition.CLASS_ATTR.marshallAsAttribute(mbeanEntry, true, writer);
            SS7MbeanDefinition.INTERFACE_ATTR.marshallAsAttribute(mbeanEntry, true, writer);
            SS7MbeanDefinition.ENABLED_ATTR.marshallAsAttribute(mbeanEntry, true, writer);
            SS7MbeanDefinition.REFLECTION_ATTR.marshallAsAttribute(mbeanEntry, true, writer);

            final ModelNode constructor = mbeanEntry.get(SS7MbeanConstructorDefinition.CONSTRUCTOR);
            if (constructor != null && constructor.isDefined()) {
                for (Property constructorProp : constructor.asPropertyList()) {
                    writer.writeStartElement(SS7MbeanConstructorDefinition.CONSTRUCTOR);

                    final ModelNode constructorEntry = constructorProp.getValue();

                    final ModelNode parameter = constructorEntry.get(SS7MbeanConstructorParameterDefinition.PARAMETER);
                    if (parameter != null && parameter.isDefined()) {
                        for (Property parameterProp : parameter.asPropertyList()) {
                            writer.writeStartElement(SS7MbeanConstructorParameterDefinition.PARAMETER);

                            final ModelNode parameterEntry = parameterProp.getValue();
                            System.out.println("parameterProp: " + parameterProp.getName());
                            System.out.println("parameterEntry: " + parameterEntry.asString());
                            writer.writeCharacters(parameterProp.getName());

                            writer.writeEndElement();
                        }
                    }

                    writer.writeEndElement();
                }
            }

            final ModelNode property = mbeanEntry.get(SS7MbeanPropertyDefinition.PROPERTY);
            if (property != null && property.isDefined()) {
                for (Property propertyProp : property.asPropertyList()) {
                    writer.writeStartElement(SS7MbeanPropertyDefinition.PROPERTY);

                    final ModelNode propertyEntry = propertyProp.getValue();

                    SS7MbeanPropertyDefinition.NAME_ATTR.marshallAsAttribute(propertyEntry, true, writer);
                    SS7MbeanPropertyDefinition.TYPE_ATTR.marshallAsAttribute(propertyEntry, true, writer);
                    SS7MbeanPropertyDefinition.VALUE_ATTR.marshallAsAttribute(propertyEntry, true, writer);

                    final ModelNode entry = propertyEntry.get(SS7MbeanPropertyEntryDefinition.ENTRY);
                    if (entry != null && entry.isDefined()) {
                        for (Property entryProp : entry.asPropertyList()) {
                            writer.writeStartElement(SS7MbeanPropertyEntryDefinition.ENTRY);

                            final ModelNode entryEntry = entryProp.getValue();

                            SS7MbeanPropertyEntryDefinition.KEY_ATTR.marshallAsAttribute(entryEntry, true, writer);
                            SS7MbeanPropertyEntryDefinition.TYPE_ATTR.marshallAsAttribute(entryEntry, true, writer);
                            SS7MbeanPropertyEntryDefinition.VALUE_ATTR.marshallAsAttribute(entryEntry, true, writer);

                            writer.writeEndElement();
                        }
                    }

                    writer.writeEndElement();
                }
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
        list.add(subsystem);

        // mbean elements
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

        // MBean Attributes
        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            final String value = reader.getAttributeValue(i);
            switch (SS7MbeanDefinition.Element.of(attribute)) {
                case NAME: {
                    name = value;
                    SS7MbeanDefinition.NAME_ATTR.parseAndSetParameter(value, mbean, reader);
                    break;
                }
                case TYPE: {
                    SS7MbeanDefinition.TYPE_ATTR.parseAndSetParameter(value, mbean, reader);
                    break;
                }
                case CLASS: {
                    SS7MbeanDefinition.CLASS_ATTR.parseAndSetParameter(value, mbean, reader);
                    break;
                }
                case INTERFACE: {
                    SS7MbeanDefinition.INTERFACE_ATTR.parseAndSetParameter(value, mbean, reader);
                    break;
                }
                case ENABLED: {
                    SS7MbeanDefinition.ENABLED_ATTR.parseAndSetParameter(value, mbean, reader);
                    break;
                }
                case REFLECTION: {
                    SS7MbeanDefinition.REFLECTION_ATTR.parseAndSetParameter(value, mbean, reader);
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
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(SS7MbeanDefinition.MBEAN, name));
        mbean.get(OP_ADDR).set(address.toModelNode());
        list.add(mbean);

        // constructor/properties elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case SS7_1_0: {
                    final String tagName = reader.getLocalName();
                    switch (tagName) {
                        case SS7MbeanConstructorDefinition.CONSTRUCTOR: {
                            parseConstructor(reader, address, list);
                            break;
                        }
                        case SS7MbeanPropertyDefinition.PROPERTY: {
                            parseProperty(reader, address, list);
                            break;
                        }
                        default: {
                            break;
                        }
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
                case TYPE: {
                    SS7MbeanPropertyDefinition.TYPE_ATTR.parseAndSetParameter(value, property, reader);
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

        //ParseUtils.requireNoContent(reader);

        if (name == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        property.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(SS7MbeanPropertyDefinition.PROPERTY, name));
        property.get(OP_ADDR).set(address.toModelNode());
        list.add(property);

        parsePropertyEntries(reader, address, list);
    }

    static void parsePropertyEntries(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {

        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case SS7_1_0: {
                    final String tagName = reader.getLocalName();
                    switch (tagName) {
                        case SS7MbeanPropertyEntryDefinition.ENTRY: {
                            parseEntry(reader, parent, list);
                            break;
                        }
                        default: {
                            throw unexpectedElement(reader);
                        }
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }

    }

    static void parseEntry(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        String key = null;
        final ModelNode entry = new ModelNode();

        final int count = reader.getAttributeCount();
        for (int i = 0; i < count; i++) {
            requireNoNamespaceAttribute(reader, i);
            final String attribute = reader.getAttributeLocalName(i);
            final String value = reader.getAttributeValue(i);
            switch (SS7MbeanPropertyEntryDefinition.Element.of(attribute)) {
                case KEY: {
                    key = value;
                    SS7MbeanPropertyEntryDefinition.KEY_ATTR.parseAndSetParameter(value, entry, reader);
                    break;
                }
                case TYPE: {
                    SS7MbeanPropertyEntryDefinition.TYPE_ATTR.parseAndSetParameter(value, entry, reader);
                    break;
                }
                case VALUE: {
                    SS7MbeanPropertyEntryDefinition.VALUE_ATTR.parseAndSetParameter(value, entry, reader);
                    break;
                }
                default: {
                    throw unexpectedAttribute(reader, i);
                }
            }
        }

        ParseUtils.requireNoContent(reader);

        if (key == null) {
            throw missingRequired(reader, Collections.singleton(Attribute.NAME));
        }

        entry.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(SS7MbeanPropertyEntryDefinition.ENTRY, key));
        entry.get(OP_ADDR).set(address.toModelNode());

        list.add(entry);
    }

    static void parseConstructor(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        ParseUtils.requireNoAttributes(reader);
        final ModelNode constructor = new ModelNode();

        constructor.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(Constants.CONSTRUCTOR, Constants.CLASSIC));
        constructor.get(OP_ADDR).set(address.toModelNode());
        list.add(constructor);

        parseConstructorParams(reader, address, list);
    }

    static void parseConstructorParams(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {

        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            switch (Namespace.forUri(reader.getNamespaceURI())) {
                case SS7_1_0: {
                    final String tagName = reader.getLocalName();
                    switch (tagName) {
                        case SS7MbeanConstructorParameterDefinition.PARAMETER: {
                            parseParameter(reader, parent, list);
                            break;
                        }
                        default: {
                            throw unexpectedElement(reader);
                        }
                    }
                    break;
                }
                default: {
                    throw unexpectedElement(reader);
                }
            }
        }
    }

    static void parseParameter(XMLExtendedStreamReader reader, PathAddress parent, List<ModelNode> list)
            throws XMLStreamException {
        ParseUtils.requireNoAttributes(reader);
        final ModelNode parameter = new ModelNode();

        String value = reader.getElementText();
        if (value != null) {
            value = value.trim();
        }

        parameter.get(OP).set(ADD);
        PathAddress address = PathAddress.pathAddress(parent,
                PathElement.pathElement(Constants.PARAMETER, value));
        parameter.get(OP_ADDR).set(address.toModelNode());
        list.add(parameter);
    }

}