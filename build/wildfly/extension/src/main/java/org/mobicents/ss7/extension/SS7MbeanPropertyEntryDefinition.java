package org.mobicents.ss7.extension;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

import java.util.HashMap;
import java.util.Map;

public class SS7MbeanPropertyEntryDefinition extends SimpleResourceDefinition {

    public enum Element {
        UNKNOWN(null),
        KEY("key"),
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
            final Map<String, Element> map = new HashMap<String, SS7MbeanPropertyEntryDefinition.Element>();
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

    public static final SimpleAttributeDefinition KEY_ATTR = new SimpleAttributeDefinition(
            Element.KEY.localName(), ModelType.STRING, true);
    public static final SimpleAttributeDefinition TYPE_ATTR = new SimpleAttributeDefinition(
            Element.TYPE.localName(), ModelType.STRING, true);
    public static final SimpleAttributeDefinition VALUE_ATTR = new SimpleAttributeDefinition(
            Element.VALUE.localName(), ModelType.STRING, true);

    public static final String ENTRY = "entry";
    public static final PathElement ENTRY_PATH = PathElement.pathElement(ENTRY);
    public static final SS7MbeanPropertyEntryDefinition INSTANCE = new SS7MbeanPropertyEntryDefinition();

    protected static final SimpleAttributeDefinition[] ENTRY_ATTRIBUTES = {
            //KEY_ATTR, // key is read-only
            TYPE_ATTR,
            VALUE_ATTR
    };

    private SS7MbeanPropertyEntryDefinition() {
        super(ENTRY_PATH,
                SS7Extension.getResourceDescriptionResolver(SS7MbeanPropertyDefinition.PROPERTY + "." + ENTRY),
                SS7MbeanPropertyEntryAdd.INSTANCE,
                SS7MbeanPropertyEntryRemove.INSTANCE);
    }

    @Override
    public void registerAttributes(final ManagementResourceRegistration properties) {
        //super.registerAttributes(resourceRegistration);
        properties.registerReadOnlyAttribute(KEY_ATTR, null);
        for (SimpleAttributeDefinition def : ENTRY_ATTRIBUTES) {
            properties.registerReadWriteAttribute(def, null, new ReloadRequiredWriteAttributeHandler(def));
        }
    }
}