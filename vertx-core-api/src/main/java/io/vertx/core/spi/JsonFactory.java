/*
 * Copyright (c) 2011-2019 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
 * which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */

package io.vertx.core.spi;

import io.vertx.core.ServiceHelper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.core.spi.json.JsonCodec;
import io.vertx.core.streams.ReadStream;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A factory for the plug-able json SPI.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public interface JsonFactory {

  /**
   * <p> Load the JSON factory with the {@code ServiceLoader}
   *
   * <ul>
   *   <li>An attempt is made to load a factory using the service loader {@code META-INF/services} {@link JsonFactory}.</li>
   *   <li>Factories are sorted </li>
   *   <li>If not factory is resolved (which is usually the default case), an exception is thrown.</li>
   * </ul>
   *
   * <p> When the default Jackson codec is used and {@code jackson-databind} is available then a codec using it
   * will be used otherwise the codec will only use {@code jackson-core} and provide best effort mapping.
   */
  static JsonFactory load() {
    List<JsonFactory> factories = new ArrayList<>(ServiceHelper.loadFactories(io.vertx.core.spi.JsonFactory.class));
    factories.sort(Comparator.comparingInt(JsonFactory::order));
    if (!factories.isEmpty()) {
      return factories.iterator().next();
    } else {
      throw new IllegalStateException();
    }
  }

  /**
   * The order of the factory. If there is more than one matching factory they will be tried in ascending order.
   *
   * @implSpec returns {@link Integer#MAX_VALUE}
   *
   * @return  the order
   */
  default int order() {
    return Integer.MAX_VALUE;
  }

  JsonCodec codec();

  JsonParser parser(ReadStream<Buffer> stream);

}
