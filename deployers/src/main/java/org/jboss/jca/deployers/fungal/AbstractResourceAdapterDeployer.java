/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008-2010, Red Hat Middleware LLC, and individual contributors
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

package org.jboss.jca.deployers.fungal;

import org.jboss.jca.common.api.metadata.ra.ConfigProperty;
import org.jboss.jca.core.api.bootstrap.CloneableBootstrapContext;
import org.jboss.jca.core.spi.naming.JndiStrategy;
import org.jboss.jca.validator.Failure;
import org.jboss.jca.validator.FailureHelper;
import org.jboss.jca.validator.Validate;
import org.jboss.jca.validator.Validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.ResourceAdapter;
import javax.resource.spi.ResourceAdapterAssociation;

import org.jboss.logging.Logger;

import com.github.fungal.api.util.Injection;
import com.github.fungal.api.util.JarFilter;
import com.github.fungal.spi.deployers.DeployException;

/**
 * An abstract resource adapter deployer which contains common functionality
 * for all resource adapter archive based deployers.
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public abstract class AbstractResourceAdapterDeployer
{
   private static Logger log = Logger.getLogger(AbstractResourceAdapterDeployer.class);

   private static boolean trace = log.isTraceEnabled();

   /** The configuration */
   private RAConfiguration raConfiguration = null;

   /**
    * Constructor
    */
   AbstractResourceAdapterDeployer()
   {
   }

   /**
    * Set the configuration
    * @param value The value
    */
   public void setConfiguration(RAConfiguration value)
   {
      raConfiguration = value;
   }

   /**
    * Get the configuration
    * @return The value
    */
   public RAConfiguration getConfiguration()
   {
      return raConfiguration;
   }

   /**
    * validate archive
    *
    * @param url of the archive
    * @param archiveValidation classes and/or to validate.
    * @return The list of failures gotten. Null in case of no failures or if validation is not run according to
    *   {@link #getArchiveValidation()} Settin
    */
   //IT IS PACKAGE PROTECTED ONLY FOR TESTS ACCESSIBILITY
   List<Failure> validateArchive(URL url, List<Validate> archiveValidation)
   {
      // Archive validation
      if (!getConfiguration().getArchiveValidation())
      {
         return null;
      }
      Validator validator = new Validator();
      List<Failure> failures = validator.validate(archiveValidation);

      return failures;
   }

   /**
    * print Failures into Log files.
    *
    * @param urlFileName filename Of deployed rar
    * @param validator validator instance used to run validation rules
    * @param failures the list of Failures to be printed
    * @param reportDirectory where to put various logs
    * @param fhInput optional parameter. Normally used only for test or in case of
    *   FailureHelper already present in context
    * @return the error Text
    *
    */
   //IT IS PACKAGE PROTECTED ONLY FOR TESTS ACCESSIBILITY
   String printFailuresLog(String urlFileName, Validator validator, Collection<Failure> failures,
         File reportDirectory, FailureHelper... fhInput)
   {
      String errorText = "";
      FailureHelper fh = null;
      if (fhInput.length == 0)
         fh = new FailureHelper(failures);
      else
         fh = fhInput[0];

      if (failures != null && failures.size() > 0)
      {
         if (reportDirectory == null)
         {
            reportDirectory = new File(SecurityActions.getSystemProperty("iron.jacamar.home"), "/log/");
         }
         if (reportDirectory.exists())
         {
            int lastSlashIndex = urlFileName.lastIndexOf("/");
            int lastSepaIndex = urlFileName.lastIndexOf(File.separator);

            int lastIndex = lastSlashIndex > lastSepaIndex ? lastSlashIndex : lastSepaIndex;
            if (lastIndex != -1)
               urlFileName = urlFileName.substring(lastIndex + 1);
            urlFileName += ".log";

            File report = new File(reportDirectory, urlFileName);
            FileWriter fw = null;
            BufferedWriter bw = null;
            try
            {
               fw = new FileWriter(report);
               bw = new BufferedWriter(fw, 8192);
               bw.write(fh.asText(validator.getResourceBundle()));
               bw.flush();

               errorText = "Validation failures - see: " + report.getAbsolutePath();
            }
            catch (IOException ioe)
            {
               log.warn(ioe.getMessage(), ioe);
            }
            finally
            {
               if (bw != null)
               {
                  try
                  {
                     bw.close();
                  }
                  catch (IOException ignore)
                  {
                     // Ignore
                  }
               }
               if (fw != null)
               {
                  try
                  {
                     fw.close();
                  }
                  catch (IOException ignore)
                  {
                     // Ignore
                  }
               }
            }
         }
         else
         {
            errorText = fh.asText(validator.getResourceBundle());
         }
      }
      return errorText;
   }

   /**
    * Cehck for failures at a certain level
    * @param failures The failures
    * @param severity The level
    * @return True if a failure is found with the specified severity; otherwise false
    */
   protected boolean hasFailuresLevel(Collection<Failure> failures, int severity)
   {
      if (failures != null)
      {
         for (Failure failure : failures)
         {
            if (failure.getSeverity() == severity)
            {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Start the resource adapter
    * @param resourceAdapter The resource adapter
    * @param bootstrapIdentifier The bootstrap context identifier; may be <code>null</code>
    * @throws DeployException Thrown if the resource adapter cant be started
    */
   @SuppressWarnings("unchecked")
   protected void startContext(ResourceAdapter resourceAdapter, String bootstrapIdentifier) throws DeployException
   {
      try
      {
         Class clz = resourceAdapter.getClass();
         Method start = clz.getMethod("start", new Class[] {BootstrapContext.class});

         CloneableBootstrapContext cbc = null;

         if (bootstrapIdentifier != null && getConfiguration().getBootstrapContexts() != null)
         {
            CloneableBootstrapContext bc = getConfiguration().getBootstrapContexts().get(bootstrapIdentifier);

            if (bc != null)
               cbc = bc.clone();
         }

         if (cbc == null)
            cbc = getConfiguration().getDefaultBootstrapContext().clone();

         start.invoke(resourceAdapter, new Object[]
         {cbc});
      }
      catch (InvocationTargetException ite)
      {
         throw new DeployException("Unable to start " +
                                   resourceAdapter.getClass().getName(), ite.getTargetException());
      }
      catch (Throwable t)
      {
         throw new DeployException("Unable to start " + resourceAdapter.getClass().getName(), t);
      }
   }

   /**
    * Associate resource adapter with ojects if they implement ResourceAdapterAssociation
    * @param resourceAdapter The resource adapter
    * @param object The of possible association object
    * @throws DeployException Thrown if the resource adapter cant be started
    */
   @SuppressWarnings("unchecked")
   protected void associateResourceAdapter(ResourceAdapter resourceAdapter, Object object)
      throws DeployException
   {
      if (resourceAdapter != null && object != null)
      {
         if (object instanceof ResourceAdapterAssociation)
         {
            try
            {
               Class clz = object.getClass();

               Method setResourceAdapter = clz.getMethod("setResourceAdapter",
                                                         new Class[]
                                                         {ResourceAdapter.class});

               setResourceAdapter.invoke(object, new Object[]
               {resourceAdapter});
            }
            catch (Throwable t)
            {
               throw new DeployException("Unable to associate " + object.getClass().getName(), t);
            }
         }
      }
   }

   /**
    * Initialize and inject configuration properties
    * @param className The fully qualified class name
    * @param configs The configuration properties
    * @param cl The class loader
    * @return The object
    * @throws DeployException Thrown if the object cant be initialized
    */
   protected Object initAndInject(String className,
                                  List<? extends ConfigProperty> configs,
                                  ClassLoader cl)
      throws DeployException
   {
      try
      {
         Class clz = Class.forName(className, true, cl);
         Object o = clz.newInstance();

         if (configs != null)
         {
            Injection injector = new Injection();
            for (ConfigProperty cpmd : configs)
            {
               if (cpmd.isValueSet())
                  injector.inject(cpmd.getConfigPropertyType().getValue(), cpmd.getConfigPropertyName().getValue(),
                        cpmd.getConfigPropertyValue().getValue(), o);
            }
         }

         return o;
      }
      catch (Throwable t)
      {
         throw new DeployException("Deployment " + className + " failed", t);
      }
   }

   /**
    * Get the URLs for the directory and all libraries located in the directory
    * @param directory The directory
    * @return The URLs
    * @exception MalformedURLException MalformedURLException
    * @exception IOException IOException
    */
   protected URL[] getUrls(File directory) throws MalformedURLException, IOException
   {
      List<URL> list = new LinkedList<URL>();

      if (directory.exists() && directory.isDirectory())
      {
         // Add directory
         list.add(directory.toURI().toURL());

         // Add the contents of the directory too
         File[] jars = directory.listFiles(new JarFilter());

         if (jars != null)
         {
            for (int j = 0; j < jars.length; j++)
            {
               list.add(jars[j].getCanonicalFile().toURI().toURL());
            }
         }
      }
      return list.toArray(new URL[list.size()]);
   }


   /**
    * Bind connection factory into JNDI
    * @param deployment The deployment name
    * @param cf The connection factory
    * @return The JNDI names bound
    * @exception Throwable Thrown if an error occurs
    */
   protected String[] bindConnectionFactory(String deployment, Object cf) throws Throwable
   {
      JndiStrategy js = getConfiguration().getJndiStrategy().clone();

      return js.bindConnectionFactories(deployment, new Object[] {cf});
   }

   /**
    * Start
    */
   public void start()
   {
      if (raConfiguration == null)
         throw new IllegalStateException("Configuration not defined");
   }
}