/**
 * Copyright (C) 2009 eXo Platform SAS.
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

package org.exoplatform.portal.resource;

import javax.jcr.RepositoryException;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;
import org.exoplatform.services.jcr.core.ManageableRepository;

/**
 * A key for skin config lookup.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
class SkinKey {

    private final String module;

    private final String name;

    private final String repositoryName;

    private final int hashCode;

    /**
     * Creates a new skin key.
     *
     * @param module the skin base
     * @param name the skin name
     * @throws IllegalArgumentException if any argument is null
     */
    public SkinKey(String module, String name) throws IllegalArgumentException {
        if (module == null) {
            throw new IllegalArgumentException("No null base accepted");
        }
        if (name == null) {
            throw new IllegalArgumentException("No null skin name accepted");
        }
        this.repositoryName = getCurrentRepositoryName();
        this.module = module;
        this.name = name;
        this.hashCode = repositoryName.hashCode()*31 + module.hashCode() * 41 + name.hashCode();
    }

    public String getModule() {
        return module;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return hashCode;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof SkinKey) {
            SkinKey that = (SkinKey) obj;
            return that.repositoryName.equals(repositoryName) && that.module.equals(module) && that.name.equals(name);
        }
        return false;
    }

    public String toString() {
        return "SkinKey[base=" + module + ",name=" + name + "]";
    }

    /**
     * Gets the current repository name.
     */
    private static String getCurrentRepositoryName() {
      RepositoryService repositoryService = (RepositoryService) PortalContainer.getInstance()
                                                                               .getComponentInstanceOfType(RepositoryService.class);
      try {
        ManageableRepository repo = repositoryService.getCurrentRepository();
        return (repo != null) ? repo.getConfiguration().getName() : repositoryService.getDefaultRepository().getConfiguration().getName();
      } catch (RepositoryException e) {
        throw new RuntimeException(e);
      } catch (RepositoryConfigurationException e){
        throw new RuntimeException(e);
      }
    }
}
