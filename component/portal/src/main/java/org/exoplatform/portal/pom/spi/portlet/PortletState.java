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

package org.exoplatform.portal.pom.spi.portlet;

import org.chromattic.api.annotations.Create;
import org.chromattic.api.annotations.PrimaryType;
import org.chromattic.api.annotations.OneToMany;
import org.chromattic.api.annotations.OneToOne;
import org.chromattic.api.annotations.RelatedMappedBy;
import org.gatein.mop.core.api.workspace.content.AbstractCustomization;

import java.util.Map;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
@PrimaryType(name = PortletState.MOP_NODE_NAME)
public abstract class PortletState
{

   /** . */
   private Portlet payload;
   static final String MOP_NODE_NAME = "mop:portletpreferences";

   @OneToMany
   public abstract Map<String, PreferenceState> getChildren();

   @Create
   public abstract PreferenceState create();

   @OneToOne
   @RelatedMappedBy("state")
   public abstract AbstractCustomization getCustomization();

   private void setPayload(Portlet payload)
   {

      this.payload = payload;

      //
      Map<String, PreferenceState> entries = getChildren();
      entries.clear();

      for (Preference pref : payload)
      {
         PreferenceState prefState = create();
         entries.put(pref.getName(), prefState);
         prefState.setValue(pref.getValues());
         prefState.setReadOnly(pref.isReadOnly());
      }
   }

   public void setPayload(Object payload)
   {
      setPayload((Portlet)payload);
   }

   public Object getPayload()
   {
      if (payload == null)
      {
         PortletBuilder builder = new PortletBuilder();
         for (PreferenceState entry : getChildren().values())
         {
            builder.add(entry.getName(), entry.getValues(), entry.getReadOnly());
         }
         payload = builder.build();
      }
      return payload;
   }
}
