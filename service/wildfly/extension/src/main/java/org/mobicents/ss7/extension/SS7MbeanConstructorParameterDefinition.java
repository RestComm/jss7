package org.mobicents.ss7.extension;

import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleResourceDefinition;

public class SS7MbeanConstructorParameterDefinition extends SimpleResourceDefinition {

    public static final String PARAMETER = "parameter";
    public static final PathElement PARAMETER_PATH = PathElement.pathElement(PARAMETER);
    public static final SS7MbeanConstructorParameterDefinition INSTANCE = new SS7MbeanConstructorParameterDefinition();

    private SS7MbeanConstructorParameterDefinition() {
        super(PARAMETER_PATH,
                SS7Extension.getResourceDescriptionResolver(SS7MbeanDefinition.MBEAN + "." + PARAMETER),
                SS7MbeanConstructorParameterAdd.INSTANCE,
                SS7MbeanConstructorParameterRemove.INSTANCE);
    }
}