package org.mobicents.ss7.extension;

import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

/**
 * @author <a href="mailto:sergey.povarnin@telestax.com">Sergey Povarnin</a>
 */
public class SubsystemDefinition extends SimpleResourceDefinition {
  public static final SubsystemDefinition INSTANCE = new SubsystemDefinition();

  private SubsystemDefinition() {
    super(SS7Extension.SUBSYSTEM_PATH,
        SS7Extension.getResourceDescriptionResolver(null),
        //We always need to add an 'add' operation
        SubsystemAdd.INSTANCE,
        //Every resource that is added, normally needs a remove operation
        SubsystemRemove.INSTANCE);
  }

  @Override
  public void registerOperations(ManagementResourceRegistration resourceRegistration) {
    super.registerOperations(resourceRegistration);
    //you can register aditional operations here
  }

  @Override
  public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
    //you can register attributes here
  }
}
