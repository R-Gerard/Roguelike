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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

class XmlOutputResolver extends SchemaOutputResolver {
  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

  @Override
  public Result createOutput(final String namespaceUri, final String suggestedFileName) throws IOException {
    final Result result = new StreamResult(outputStream);
    result.setSystemId(Long.toString(System.nanoTime()));
    return result;
  }

  public String getOutput(final String encoding) {
    try {
      return outputStream.toString(encoding);
    } catch (final UnsupportedEncodingException e) {
      return null;
    }
  }
}
