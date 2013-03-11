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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXParseException;

public class XmlParseException extends IllegalArgumentException {
  private static final long serialVersionUID = -7205923022085794222L;

  private int lineNumber = -1, columnNumber = -1;
  private String missingElement = "";

  public XmlParseException(final String message) {
    super(message);
  }

  public XmlParseException(final Throwable cause) {
    super(cause);

    init();
  }

  public XmlParseException(final String message, final Throwable cause) {
    super(message, cause);

    init();
  }

  private final void init() {
    if (getCause() instanceof SAXParseException) {
      final SAXParseException exception = (SAXParseException) getCause();

      lineNumber = exception.getLineNumber();
      columnNumber = exception.getColumnNumber();

      // Search for an {element name} in the message
      // Using a custom error handler in XmlMarshaller would be better, but extending com.sun.org.apache.xerces.internal.util.ErrorHandlerWrapper is unsafe
      final Pattern pattern = Pattern.compile("\\{(.*?)\\}");
      final Matcher matcher = pattern.matcher(getCause().getMessage());

      if (matcher.find()) {
        missingElement = matcher.group(1);
      }
    }
  }

  @Override
  public String toString() {
    final StringBuffer buffer = new StringBuffer();

    if (lineNumber >= 0) {
      buffer.append("line number: ").append(lineNumber).append(' ');
    }

    if (columnNumber >= 0) {
      buffer.append("column number: ").append(columnNumber).append(' ');
    }

    if (!StringUtils.isBlank(missingElement)) {
      buffer.append("missing element '").append(missingElement).append("' ");
    }

    return buffer.toString();
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public int getColumnNumber() {
    return columnNumber;
  }

  public String getMissingElement() {
    return missingElement;
  }
}
