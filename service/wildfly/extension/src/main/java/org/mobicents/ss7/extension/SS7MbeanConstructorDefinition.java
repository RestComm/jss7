package org.mobicents.ss7.extension;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

import java.util.HashMap;
import java.util.Map;

public class SS7MbeanConstructorDefinition extends SimpleResourceDefinition {

    public enum Element {
        UNKNOWN(null),

        PARAMETER("parameter");

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

    public static final String CONSTRUCTOR = "constructor";
    public static final PathElement CONSTRUCTOR_PATH = PathElement.pathElement(CONSTRUCTOR);
    public static final SS7MbeanConstructorDefinition INSTANCE = new SS7MbeanConstructorDefinition();

    protected static final SimpleAttributeDefinition PARAMETER =
            new SimpleAttributeDefinitionBuilder(Element.PARAMETER.localName(), ModelType.STRING)
                    .setXmlName(Element.PARAMETER.localName())
                    .setAllowNull(true) // todo should be false, but 'add' won't validate then
                    .build();

    private SS7MbeanConstructorDefinition() {
        super(CONSTRUCTOR_PATH,
                SS7Extension.getResourceDescriptionResolver(SS7MbeanDefinition.MBEAN + "." + CONSTRUCTOR),
                SS7MbeanConstructorAdd.INSTANCE,
                SS7MbeanConstructorRemove.INSTANCE);
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        super.registerChildren(resourceRegistration);
        resourceRegistration.registerSubModel(SS7MbeanConstructorParameterDefinition.INSTANCE);
    }
}