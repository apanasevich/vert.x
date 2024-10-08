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
package io.vertx.core.http.impl;

import io.vertx.core.Future;
import io.vertx.core.internal.ContextInternal;
import io.vertx.core.net.impl.endpoint.Endpoint;
import io.vertx.core.spi.metrics.PoolMetrics;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
abstract class ClientHttpEndpointBase<C> extends Endpoint {

  private final PoolMetrics metrics;

  ClientHttpEndpointBase(PoolMetrics metrics, Runnable dispose) {
    super(dispose);
    this.metrics = metrics;
  }

  public Future<C> requestConnection(ContextInternal ctx, long timeout) {
    Future<C> fut = requestConnection2(ctx, timeout);
    if (metrics != null) {
      Object metric = metrics.enqueue();
      fut = fut.andThen(ar -> {
        metrics.dequeue(metric);
      });
    }
    return fut;
  }

  protected abstract Future<C> requestConnection2(ContextInternal ctx, long timeout);

  @Override
  protected void dispose() {
    if (metrics != null) {
      metrics.close();
    }
  }
}
