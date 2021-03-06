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

package org.jboss.jca.core.connectionmanager.pool.strategy;

import javax.resource.spi.ConnectionRequestInfo;
import javax.security.auth.Subject;

/**
 * Reauthentication pool key based on {@link Subject} and {@link ConnectionRequestInfo}.
 * 
 * However, initial implementation only divides on separateNoTx.
 *
 * @author <a href="mailto:jesper.pedersen@jboss.org">Jesper Pedersen</a> 
 * @version $Rev: $
 */
class ReauthKey
{
   /** Identifies no subject */
   private static final Subject NOSUBJECT = new Subject();
   
   /** Identifies no connection request information */
   private static final Object NOCRI = new Object();

   /** The subject */
   private final Subject subject;
   
   /** The connection request information */
   private final Object cri;
   
   /** The cached hashCode */
   private int hashCode;

   /** Separate no tx */
   private boolean separateNoTx;

   /**
    * Constructor
    * @param subject subject instance
    * @param cri connection request info
    * @param separateNoTx seperateNoTx
    */
   ReauthKey(Subject subject, ConnectionRequestInfo cri, boolean separateNoTx)
   {
      this.subject = (subject == null) ? NOSUBJECT : subject;
      this.cri = (cri == null) ? NOCRI : cri;
      this.separateNoTx = separateNoTx;
      this.hashCode = separateNoTx ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      return hashCode;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;  
      }
      
      if (obj == null || !(obj instanceof ReauthKey))
      {
         return false;  
      }
      
      ReauthKey other = (ReauthKey)obj;
      
      return separateNoTx == other.separateNoTx;
   }
}
