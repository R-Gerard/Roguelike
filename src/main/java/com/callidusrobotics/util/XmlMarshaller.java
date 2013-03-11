/**
 * Copyright (C) 2013 Rusty Gerard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.callidusrobotics.util;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

/**
 * An XML marshaller and unmarshaller.
 *
 * @author Rusty
 * @since 0.0.1
 */
public class XmlMarshaller {
  protected JAXBContext jaxb;
  protected boolean fragment = true;
  protected boolean prettyPrint = true;
  protected boolean validated = true;
  protected String encoding = "US-ASCII";

  private static final String PARSE_ERROR = "Unable to parse";

  /**
   * Default constructor.
   *
   * @param clazz
   *          The class to manage
   * @throws XmlParseException
   *           If the resource can not be parsed
   */
  public XmlMarshaller(final Class<?> clazz) {
    try {
      jaxb = JAXBContext.newInstance(clazz);
    } catch (final JAXBException e) {
      throw new XmlParseException(e);
    }
  }

  /**
   * Deserializes an XML blob contained in a system resource file using
   * {@link java.lang.Class#getResourceAsStream(String)}.
   *
   * @param fileName
   *          The name of the file to unmarshal
   * @return The unmarshalled object
   * @see #unmarshal(String)
   * @throws XmlParseException
   *           If the resource can not be parsed
   */
  public Object unmarshalSystemResource(final String fileName) {
    String xml;
    try {
      xml = IOUtils.toString(getClass().getResourceAsStream(fileName));
    } catch (IOException e) {
      throw new XmlParseException(PARSE_ERROR + "'" + fileName + "'.", e);
    }

    try {
      return unmarshal(xml);
    } catch (final XmlParseException e) {
      throw new XmlParseException(PARSE_ERROR + "'" + fileName + "'.", e);
    }
  }

  /**
   * Deserializes an XML blob contained in a file.
   *
   * @param fileName
   *          The name of the file to unmarshal
   * @return The unmarshalled object
   * @see #unmarshal(String)
   * @throws XmlParseException
   *           If the resource can not be parsed
   */
  public Object unmarshalFile(final String fileName) {
    FileInputStream file;
    try {
      file = new FileInputStream(fileName);
    } catch (final FileNotFoundException e) {
      throw new XmlParseException(PARSE_ERROR + "'" + fileName + "'.", e);
    }

    String xml;
    try {
      xml = IOUtils.toString(file);
    } catch (final IOException e) {
      throw new XmlParseException(PARSE_ERROR + "'" + fileName + "'.", e);
    }

    try {
      return unmarshal(xml);
    } catch (final XmlParseException e) {
      throw new XmlParseException(PARSE_ERROR + "'" + fileName + "'.", e);
    }
  }

  /**
   * Deserializes an XML blob.
   *
   * @param xml
   *          The content to unmarshal
   * @return The unmarshalled object
   * @throws XmlParseException
   *           If the resource can not be parsed
   */
  public Object unmarshal(final String xml) {
    if (validated) {
      try {
        final String xsd = generateSchema();
        validate(xml, xsd);
      } catch (final IOException | SAXException e) {
        throw new XmlParseException(e);
      }
    }

    final StreamSource streamSource = new StreamSource(new StringReader(xml));
    try {
      final Unmarshaller unmarshaller = jaxb.createUnmarshaller();
      final Object object = unmarshaller.unmarshal(streamSource);

      return object;
    } catch (final JAXBException e) {
      throw new XmlParseException(e);
    }
  }

  /**
   * Serializes an object into an XML blob.
   *
   * @param object
   *          The object to serialize
   * @return The serialized XML blob, never null
   * @throws XmlParseException
   *           If the resource can not be parsed
   */
  public String marshal(final Object object) {
    final StringWriter writer = new StringWriter();

    try {
      final Marshaller marshaller = jaxb.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, fragment);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, prettyPrint);
      marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

      marshaller.marshal(object, writer);
    } catch (final JAXBException e) {
      throw new XmlParseException(e);
    }

    try {
      writer.flush();
      writer.close();
    } catch (final IOException e) {
      throw new XmlParseException(e);
    }

    final String xml = writer.toString();
    if (validated) {
      try {
        final String xsd = generateSchema();
        validate(xml, xsd);
      } catch (final IOException | SAXException e) {
        throw new XmlParseException(e);
      }
    }

    return xml;
  }

  /**
   * Fragment mutator
   *
   * @param fragment
   *          If true, the XML declaration will be suppressed
   * @return this, for method chaining
   */
  public XmlMarshaller setFragment(final boolean fragment) {
    this.fragment = fragment;
    return this;
  }

  /**
   * PrettyPrint mutator
   *
   * @param prettyPrint
   *          If true, the marshalled output will be pretty-printed
   * @return this, for method chaining
   */
  public XmlMarshaller setPrettyPrint(final boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
    return this;
  }

  /**
   * Validated mutator
   *
   * @param validated
   *          If true, the schema will be validated before unmarshalling and
   *          after marshalling
   * @return this, for method chaining
   */
  public XmlMarshaller setValidated(final boolean validated) {
    this.validated = validated;
    return this;
  }

  /**
   * Encoding mutator
   *
   * @param encoding
   *          The output encoding of the marshalled XML data, not null
   * @return this, for method chaining
   */
  public XmlMarshaller setEncoding(final String encoding) {
    this.encoding = encoding;
    return this;
  }

  /**
   * Validates an XML fragment against a supplied schema.
   *
   * @param xml
   *          The fragment to validate
   * @param xsd
   *          The schema used for validation
   * @throws SAXException
   * @throws IOException
   */
  protected void validate(final String xml, final String xsd) throws SAXException, IOException {
    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    final Schema schema = schemaFactory.newSchema(new StreamSource(new ByteArrayInputStream(xsd.getBytes(Charset.forName(encoding)))));
    final Validator validator = schema.newValidator();

    validator.validate(new StreamSource(new ByteArrayInputStream(xml.getBytes(Charset.forName(encoding)))));
  }

  /**
   * Generates an XSD for the managed class.
   *
   * @return The XSD string
   */
  protected String generateSchema() throws IOException {
    final XmlOutputResolver outputResolver = new XmlOutputResolver();
    jaxb.generateSchema(outputResolver);

    return outputResolver.getOutput(encoding);
  }
}
