package io.vertx.it;

import io.vertx.impl.core.VertxThread;

import java.util.concurrent.TimeUnit;

public class CustomVertxThread extends VertxThread  {
  public CustomVertxThread(Runnable target, String name, boolean worker, long maxExecTime, TimeUnit maxExecTimeUnit) {
    super(target, name, worker, maxExecTime, maxExecTimeUnit);
  }
}