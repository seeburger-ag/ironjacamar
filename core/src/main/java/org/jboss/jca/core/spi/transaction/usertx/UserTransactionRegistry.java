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
package org.jboss.jca.core.spi.transaction.usertx;

/**
 * UserTransactionRegistry.
 * 
 * @author <a href="jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public interface UserTransactionRegistry
{
   /**
    * Add a listener
    * @param listener The listener
    */
   public void addListener(UserTransactionListener listener);
   
   /**
    * Remove a listener
    * @param listener The listener
    */
   public void removeListener(UserTransactionListener listener);

   /**
    * Add a provider
    * @param provider The provider
    */
   public void addProvider(UserTransactionProvider provider);
   
   /**
    * Remove a provider
    * @param provider The provider
    */
   public void removeProvider(UserTransactionProvider provider);

   /**
    * Fire a user transaction started event
    */
   public void userTransactionStarted();
}
