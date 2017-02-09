package org.mobicents.ss7.extension;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

import java.util.HashMap;
import java.util.Map;

public class SS7MbeanPropertyDefinition extends SimpleResourceDefinition {

    public enum Element {
        UNKNOWN(null),
        NAME("name"),
        TYPE("type"),
        VALUE("value");

        private final String name;

        Element(final String name) {
            this.name = name;
        }

        public String localName() {
            return name;
        }

        private static final Map<String, Element> MAP;

        static {
            final Map<String, Element> map = new HashMap<String, Element>();
            for (Element element : values()) {
                final String name = element.localName();
                if (name != null) map.put(name, element);
            }
            MAP = map;
        }

        public static Element of(final String localName) {
            final Element element = MAP.get(localName);
            return element == null ? UNKNOWN : element;
        }

    }

    public static final SimpleAttributeDefinition NAME_ATTR = new SimpleAttributeDefinition(
            Element.NAME.localName(), ModelType.STRING, true);
    public static final SimpleAttributeDefinition TYPE_ATTR = new SimpleAttributeDefinition(
            Element.TYPE.localName(), ModelType.STRING, true);
    public static final SimpleAttributeDefinition VALUE_ATTR = new SimpleAttributeDefinition(
            Element.VALUE.localName(), ModelType.STRING, true);

    public static final String PROPERTY = "property";
    public static final PathElement PROPERTY_PATH = PathElement.pathElement(PROPERTY);
    public static final SS7MbeanPropertyDefinition INSTANCE = new SS7MbeanPropertyDefinition();

    protected static final SimpleAttributeDefinition[] PROPERTY_ATTRIBUTES = {
            //NAME_ATTR, // name is read-only
            TYPE_ATTR,
            VALUE_ATTR
    };

    private SS7MbeanPropertyDefinition() {
        super(PROPERTY_PATH,
                SS7Extension.getResourceDescriptionResolver(SS7MbeanDefinition.MBEAN + "." + PROPERTY),
                SS7MbeanPropertyAdd.INSTANCE,
                SS7MbeanPropertyRemove.INSTANCE);
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        super.registerChildren(resourceRegistration);
        resourceRegistration.registerSubModel(SS7MbeanPropertyEntryDefinition.INSTANCE);
    }

    @Override
    public void registerAttributes(final ManagementResourceRegistration properties) {
        //super.registerAttributes(resourceRegistration);
        properties.registerReadOnlyAttribute(NAME_ATTR, null);
        for (SimpleAttributeDefinition def : PROPERTY_ATTRIBUTES) {
            properties.registerReadWriteAttribute(def, null, new ReloadRequiredWriteAttributeHandler(def));
        }
    }

}