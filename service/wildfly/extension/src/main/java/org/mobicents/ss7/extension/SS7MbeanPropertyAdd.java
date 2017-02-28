package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.dmr.ModelNode;

import static org.mobicents.ss7.extension.SS7MbeanPropertyDefinition.PROPERTY_ATTRIBUTES;

class SS7MbeanPropertyAdd extends AbstractAddStepHandler {

    public static final SS7MbeanPropertyAdd INSTANCE = new SS7MbeanPropertyAdd();

    private SS7MbeanPropertyAdd() {
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        SS7MbeanPropertyDefinition.NAME_ATTR.validateAndSet(operation, model);
        for (SimpleAttributeDefinition def : PROPERTY_ATTRIBUTES) {
            def.validateAndSet(operation, model);
        }
    }
}