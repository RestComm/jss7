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
        SubsystemAdd.INSTANCE,
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
