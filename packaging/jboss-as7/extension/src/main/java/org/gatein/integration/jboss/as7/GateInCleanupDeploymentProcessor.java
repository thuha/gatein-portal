/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.gatein.integration.jboss.as7;

import org.gatein.integration.jboss.as7.deployment.GateInConfigurationKey;
import org.gatein.integration.jboss.as7.deployment.GateInEarKey;
import org.gatein.integration.jboss.as7.deployment.GateInExtKey;
import org.gatein.integration.jboss.as7.deployment.PortletWarKey;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;

/**
 * @author <a href="mailto:mstrukel@redhat.com">Marko Strukelj</a>
 */
public class GateInCleanupDeploymentProcessor implements DeploymentUnitProcessor
{
   @Override
   public void deploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException
   {
      final DeploymentUnit du = phaseContext.getDeploymentUnit();
      Object config = du.removeAttachment(GateInConfigurationKey.KEY);
      if (config != null)
      {
         du.removeAttachment(PortletWarKey.INSTANCE);
         du.removeAttachment(GateInEarKey.KEY);
         du.removeAttachment(GateInExtKey.KEY);
      }
   }

   @Override
   public void undeploy(DeploymentUnit context)
   {
   }
}
