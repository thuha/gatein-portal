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

package org.exoplatform.portal.application;

import org.exoplatform.container.ExoContainer;
import org.exoplatform.portal.mop.SiteType;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.UserProfile;
import org.exoplatform.web.application.Application;
import org.exoplatform.web.application.ApplicationLifecycle;
import org.exoplatform.web.application.RequestFailure;
import org.exoplatform.web.login.LogoutControl;
import org.exoplatform.web.url.navigation.NavigationResource;
import org.exoplatform.web.url.navigation.NodeURL;
import org.exoplatform.webui.application.WebuiRequestContext;

public class UserProfileLifecycle implements ApplicationLifecycle<WebuiRequestContext>
{
   public static final String USER_PROFILE_ATTRIBUTE_NAME = "PortalUserProfile";

   private final static ThreadLocal<UserProfile> currentUserProfile = new ThreadLocal<UserProfile>();

   @SuppressWarnings("unused")
   public void onInit(Application app)
   {
      // do nothing for now
   }

   @SuppressWarnings("unused")
   public void onStartRequest(final Application app, final WebuiRequestContext context) throws Exception
   {
      String user = context.getRemoteUser();
      UserProfile userProfile = null;
      if (user != null)
      {
         ExoContainer exoContainer = app.getApplicationServiceContainer();
         if (exoContainer != null)
         {
            OrganizationService organizationService =
               (OrganizationService)exoContainer.getComponentInstanceOfType(OrganizationService.class);
            userProfile = organizationService.getUserProfileHandler().findUserProfileByName(user);
         }

         if (userProfile == null)
         {
            LogoutControl.wantLogout();
            PortalRequestContext prContext = (PortalRequestContext)context;
            NodeURL createURL = prContext.createURL(NodeURL.TYPE);
            createURL.setResource(new NavigationResource(SiteType.PORTAL, prContext.getPortalOwner(), null));
            prContext.sendRedirect(createURL.toString());
         }
      }

      currentUserProfile.set(userProfile);
      context.setAttribute(this.USER_PROFILE_ATTRIBUTE_NAME, userProfile);
   }

   @SuppressWarnings("unused")
   public void onFailRequest(Application app, WebuiRequestContext context, RequestFailure failureType) throws Exception
   {

   }

   @SuppressWarnings("unused")
   public void onEndRequest(Application app, WebuiRequestContext context) throws Exception
   {
      currentUserProfile.remove();
   }

   @SuppressWarnings("unused")
   public void onDestroy(Application app)
   {
      // do nothing for now
   }

}