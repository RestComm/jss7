package org.mobicents.ss7.extension;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.dmr.ModelNode;

import static org.mobicents.ss7.extension.SS7MbeanPropertyEntryDefinition.ENTRY_ATTRIBUTES;

class SS7MbeanPropertyEntryAdd extends AbstractAddStepHandler {

    public static final SS7MbeanPropertyEntryAdd INSTANCE = new SS7MbeanPropertyEntryAdd();

    private SS7MbeanPropertyEntryAdd() {
    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        SS7MbeanPropertyEntryDefinition.KEY_ATTR.validateAndSet(operation, model);
        for (SimpleAttributeDefinition def : ENTRY_ATTRIBUTES) {
            def.validateAndSet(operation, model);
        }
    }
}