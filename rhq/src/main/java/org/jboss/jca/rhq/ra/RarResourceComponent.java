/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jca.rhq.ra;

import org.jboss.jca.rhq.core.AbstractResourceComponent;
import org.jboss.jca.rhq.core.Deploy;
import org.jboss.jca.rhq.util.ContainerHelper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.content.PackageType;
import org.rhq.core.domain.content.transfer.DeployPackageStep;
import org.rhq.core.domain.content.transfer.DeployPackagesResponse;
import org.rhq.core.domain.content.transfer.RemovePackagesResponse;
import org.rhq.core.domain.content.transfer.ResourcePackageDetails;
import org.rhq.core.pluginapi.content.ContentFacet;
import org.rhq.core.pluginapi.content.ContentServices;
import org.rhq.core.pluginapi.inventory.DeleteResourceFacet;

/**
 * RarResourceComponent
 * 
 * @author <a href="mailto:yyang@gmail.com">Young Yang</a>
 * @author <a href="mailto:jeff.zhang@jboss.org">Jeff Zhang</a> 
 */
public class RarResourceComponent extends AbstractResourceComponent implements ContentFacet, DeleteResourceFacet
{
   /**
    * loadResourceConfiguration
    * 
    * @return Configuration Configuration
    * @throws Exception exception
    */
   @Override
   public Configuration loadResourceConfiguration() throws Exception
   {
      Configuration config = new Configuration();

      /*
      ManagementRepository mr = ManagementRepositoryManager.getManagementRepository();
      Connector connector = ManagementRepositoryHelper.getConnectorByUniqueId(mr, getRarUniqueId());

      ResourceAdapter manResAdapter = connector.getResourceAdapter();
      List<ConfigProperty> manResConfigProps = manResAdapter.getConfigProperties();
      PropertyList configList = getConfigPropertiesList(manResAdapter.getResourceAdapter(), manResConfigProps);
      config.put(configList);
      */
      return config;
   }

   @Override
   public DeployPackagesResponse deployPackages(Set<ResourcePackageDetails> arg0, ContentServices arg1)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Set<ResourcePackageDetails> discoverDeployedPackages(PackageType arg0)
   {
      Set<ResourcePackageDetails> details = new HashSet<ResourcePackageDetails>();
      // TODO Auto-generated method stub
      return details;
   }

   @Override
   public List<DeployPackageStep> generateInstallationSteps(ResourcePackageDetails arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public RemovePackagesResponse removePackages(Set<ResourcePackageDetails> arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public InputStream retrievePackageBits(ResourcePackageDetails arg0)
   {
      // TODO Auto-generated method stub
      return null;
   }

   /**
    * Deletes the RAR resource.
    * @throws Exception the exception
    */
   @Override
   public void deleteResource() throws Exception
   {
      Deploy deployer = (Deploy)ContainerHelper.getEmbeddedDiscover();
      final String rarUniqueId = getRarUniqueId();
      String uploadDir = getUploadedDir();
      File dir = new File(uploadDir);
      File[] rarFiles = dir.listFiles(new FilenameFilter()
      {
         
         @Override
         public boolean accept(File dir, String name)
         {
            return name.equals(rarUniqueId);
         }
         
      });
      if (rarFiles.length == 0)
      {
         throw new IllegalStateException("Can not find associated file of the connector: " + rarUniqueId);
      }
      File rarFile = rarFiles[0];
      
      try
      {
         deployer.undeploy(rarFile.toURI().toURL());
      }
      catch (Throwable e)
      {
         throw new Exception(e);
      }
   }
}
