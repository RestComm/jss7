package org.mobicents.ss7.extension;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sergey.povarnin@telestax.com
 */
public class SS7MbeanDefinition extends SimpleResourceDefinition {

    public enum Element {
        // must be first
        UNKNOWN(null),
        NAME("name"),
        TYPE("type"),
        CLASS("class"),
        INTERFACE("interface"),
        ENABLED("enabled"),
        REFLECTION("reflection");

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
                if (name != null)
                    map.put(name, element);
            }
            MAP = map;
        }

        public static Element of(final String localName) {
            final Element element = MAP.get(localName);
            return element == null ? UNKNOWN : element;
        }

    }

    protected static final SimpleAttributeDefinition NAME_ATTR =
            new SimpleAttributeDefinitionBuilder(Element.NAME.localName(), ModelType.STRING)
                    .setXmlName(Element.NAME.localName())
                    .setAllowNull(true) // todo should be false, but 'add' won't validate then
                    .build();

    protected static final SimpleAttributeDefinition TYPE_ATTR =
            new SimpleAttributeDefinitionBuilder(Element.TYPE.localName(), ModelType.STRING)
                    .setXmlName(Element.TYPE.localName())
                    .setAllowNull(true) // todo should be false, but 'add' won't validate then
                    .build();

    protected static final SimpleAttributeDefinition CLASS_ATTR =
            new SimpleAttributeDefinitionBuilder(Element.CLASS.localName(), ModelType.STRING)
                    .setXmlName(Element.CLASS.localName())
                    .setAllowNull(true) // todo should be false, but 'add' won't validate then
                    .build();

    protected static final SimpleAttributeDefinition INTERFACE_ATTR =
            new SimpleAttributeDefinitionBuilder(Element.INTERFACE.localName(), ModelType.STRING)
                    .setXmlName(Element.INTERFACE.localName())
                    .setAllowNull(true) // todo should be false, but 'add' won't validate then
                    .build();

    protected static final SimpleAttributeDefinition ENABLED_ATTR =
            new SimpleAttributeDefinitionBuilder(Element.ENABLED.localName(), ModelType.BOOLEAN)
                    .setXmlName(Element.ENABLED.localName())
                    .build();

    protected static final SimpleAttributeDefinition REFLECTION_ATTR =
            new SimpleAttributeDefinitionBuilder(Element.REFLECTION.localName(), ModelType.BOOLEAN)
                    .setXmlName(Element.REFLECTION.localName())
                    .build();

    public static final String MBEAN = "mbean";
    public static final PathElement MBEAN_PATH = PathElement.pathElement(MBEAN);
    public static final SS7MbeanDefinition INSTANCE = new SS7MbeanDefinition();

    protected static final SimpleAttributeDefinition[] MBEAN_ATTRIBUTES = {
            //NAME, // name is read-only
            TYPE_ATTR,
            CLASS_ATTR,
            INTERFACE_ATTR,
            ENABLED_ATTR,
            REFLECTION_ATTR
    };

    private SS7MbeanDefinition() {
        super(MBEAN_PATH,
                SS7Extension.getResourceDescriptionResolver(MBEAN),
                SS7MbeanAdd.INSTANCE,
                SS7MbeanRemove.INSTANCE);
    }

    @Override
    public void registerChildren(ManagementResourceRegistration resourceRegistration) {
        super.registerChildren(resourceRegistration);
        resourceRegistration.registerSubModel(SS7MbeanConstructorDefinition.INSTANCE);
        resourceRegistration.registerSubModel(SS7MbeanPropertyDefinition.INSTANCE);
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration mbeans) {
        mbeans.registerReadOnlyAttribute(NAME_ATTR, null);
        for (SimpleAttributeDefinition def : MBEAN_ATTRIBUTES) {
            mbeans.registerReadWriteAttribute(def, null, new ReloadRequiredWriteAttributeHandler(def));
        }
    }

}
