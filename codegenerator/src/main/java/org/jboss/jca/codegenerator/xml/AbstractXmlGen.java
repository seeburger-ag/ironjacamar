/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.jca.codegenerator.xml;

import org.jboss.jca.codegenerator.BaseGen;
import org.jboss.jca.codegenerator.Definition;

import java.io.IOException;
import java.io.Writer;

/**
 * A AbstractXmlGen.
 * 
 * @author Jeff Zhang
 * @version $Revision: $
 */
public abstract class AbstractXmlGen extends BaseGen
{
   /**
    * generate code
    * @param def Definition 
    * @param out Writer
    * @throws IOException ioException
    */
   public void generate(Definition def, Writer out) throws IOException
   {
      writeXmlBody(def, out);
   }
   
   /**
    * Output xml
    * @param def definition
    * @param out Writer
    * @throws IOException ioException
    */
   public abstract void writeXmlBody(Definition def, Writer out) throws IOException;
}
