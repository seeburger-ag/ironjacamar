/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jca.core.api.connectionmanager.pool;

/**
 * A pool.
 *
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a>
 */
public interface Pool
{
   /**
    * Gets pool name.
    * @return pool name
    */
   public String getName();

   /**
    * Flush idle connections from the pool
    */
   public void flush();

   /**
    * Flush the pool
    * @param kill Kill all connections
    */
   public void flush(boolean kill);

   /**
    * Test if a connection can be obtained
    * @return True if it was poosible to get a connection; otherwise false
    */
   public boolean testConnection();

   /**
    * Get the statistics
    * @return The value
    */
   public PoolStatistics getStatistics();
}
