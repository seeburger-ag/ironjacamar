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
package org.jboss.jca.codegenerator;

import java.io.IOException;
import java.io.Writer;

/**
 * A Cci connection factory class CodeGen.
 * 
 * @author Jeff Zhang
 * @version $Revision: $
 */
public class CciConnFactoryCodeGen extends AbstractCodeGen
{
   /**
    * Output class code
    * @param def definition
    * @param out Writer
    * @throws IOException ioException
    */
   @Override
   public void writeClassBody(Definition def, Writer out) throws IOException
   {

      out.write("public class " + getClassName(def) + " implements ConnectionFactory");
      writeLeftCurlyBracket(out, 0);
      int indent = 1;
      
      writeDefaultConstructor(def, out, indent);

      writeConnection(def, out, indent);
      writeMetaData(def, out, indent);
      writeRecordFactory(def, out, indent);
      writeReference(def, out, indent);
      writeRightCurlyBracket(out, 0);
   }
   
   /**
    * Output class import
    * @param def definition
    * @param out Writer
    * @throws IOException ioException
    */
   @Override
   public void writeImport(Definition def, Writer out) throws IOException
   {
      out.write("package " + def.getRaPackage() + ";");
      writeEol(out);
      writeEol(out);
      out.write("import javax.naming.NamingException;");
      writeEol(out);
      out.write("import javax.naming.Reference;");
      writeEol(out);
      writeEol(out);
      out.write("import javax.resource.ResourceException;");
      writeEol(out);
      out.write("import javax.resource.cci.Connection;");
      writeEol(out);
      out.write("import javax.resource.cci.ConnectionFactory;");
      writeEol(out);
      out.write("import javax.resource.cci.ConnectionSpec;");
      writeEol(out);
      out.write("import javax.resource.cci.RecordFactory;");
      writeEol(out);
      out.write("import javax.resource.cci.ResourceAdapterMetaData;");
      writeEol(out);
      writeEol(out);
   }
   
   /**
    * get this class name
    * @param def definition
    * @return String class name
    */
   @Override
   public String getClassName(Definition def)
   {
      return def.getCciConnFactoryClass();
   }
   
   /**
    * Output Connection method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeConnection(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("@Override");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public Connection getConnection() throws ResourceException");
      writeLeftCurlyBracket(out, indent);

      writeIndent(out, indent + 1);
      out.write("return new MyCciConnection();");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
      
      writeIndent(out, indent);
      out.write("@Override");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public Connection getConnection(ConnectionSpec properties) throws ResourceException");
      writeLeftCurlyBracket(out, indent);

      writeIndent(out, indent + 1);
      out.write("return new MyCciConnection();");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }

   /**
    * Output MetaData method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeMetaData(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("@Override");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public ResourceAdapterMetaData getMetaData() throws ResourceException");
      writeLeftCurlyBracket(out, indent);

      writeIndent(out, indent + 1);
      out.write("return null;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   
   /**
    * Output RecordFactory method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeRecordFactory(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("@Override");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public RecordFactory getRecordFactory() throws ResourceException");
      writeLeftCurlyBracket(out, indent);

      writeIndent(out, indent + 1);
      out.write("return null;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
   
   /**
    * Output Reference method
    * @param def definition
    * @param out Writer
    * @param indent space number
    * @throws IOException ioException
    */
   private void writeReference(Definition def, Writer out, int indent) throws IOException
   {
      writeIndent(out, indent);
      out.write("@Override");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public Reference getReference() throws NamingException");
      writeLeftCurlyBracket(out, indent);
      writeIndent(out, indent + 1);
      out.write("return null;");
      writeRightCurlyBracket(out, indent);
      writeEol(out);

      writeIndent(out, indent);
      out.write("@Override");
      writeEol(out);
      writeIndent(out, indent);
      out.write("public void setReference(Reference reference)");
      writeLeftCurlyBracket(out, indent);
      writeRightCurlyBracket(out, indent);
      writeEol(out);
   }
}