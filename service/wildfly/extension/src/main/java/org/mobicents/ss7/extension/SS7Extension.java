package org.mobicents.ss7.extension;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleOperationDefinitionBuilder;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.as.controller.registry.OperationEntry;
import org.jboss.logging.Logger;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.DESCRIBE;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

/**
 *
 * @author Eduardo Martins
 *
 */
public class SS7Extension implements Extension {

  private final Logger log = Logger.getLogger(SS7Extension.class);

  /**
   * The name of our subsystem within the model.
   */
  public static final String SUBSYSTEM_NAME = "restcomm-ss7";
  private static final int MAJOR_VERSION = 1;
  private static final int MINOR_VERSION = 0;

  protected static final PathElement SUBSYSTEM_PATH = PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);
  private static final String RESOURCE_NAME = SS7Extension.class.getPackage().getName() + ".LocalDescriptions";

  static StandardResourceDescriptionResolver getResourceDescriptionResolver(final String keyPrefix) {
    String prefix = SUBSYSTEM_NAME + (keyPrefix == null ? "" : "." + keyPrefix);
    return new StandardResourceDescriptionResolver(prefix, RESOURCE_NAME, SS7Extension.class.getClassLoader(), true, false);
  }

  @Override
  public void initializeParsers(ExtensionParsingContext context) {
    context.setSubsystemXmlMapping(SUBSYSTEM_NAME, Namespace.SS7_1_0.getUriString(), SS7SubsystemParser.getInstance());
  }

  @Override
  public void initialize(ExtensionContext context) {
    final SubsystemRegistration subsystem =
            context.registerSubsystem(SUBSYSTEM_NAME, ModelVersion.create(MAJOR_VERSION, MINOR_VERSION));

    final ManagementResourceRegistration registration = subsystem.registerSubsystemModel(SubsystemDefinition.INSTANCE);

    final OperationDefinition describeOp = new SimpleOperationDefinitionBuilder(DESCRIBE,
        getResourceDescriptionResolver(null))
        .setEntryType(OperationEntry.EntryType.PRIVATE)
        .build();
    registration.registerOperationHandler(describeOp, GenericSubsystemDescribeHandler.INSTANCE, false);

    subsystem.registerXMLElementWriter(SS7SubsystemParser.getInstance());

    // here we can register submodels
    final ManagementResourceRegistration mbeans = registration.registerSubModel(SS7MbeanDefinition.INSTANCE);
  }
}
