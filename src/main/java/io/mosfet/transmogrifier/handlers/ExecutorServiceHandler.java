package io.mosfet.transmogrifier.handlers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Created with love.
 * User: mosfet
 * Date: 25/02/20
 * github: kmos
 * twitter: nmosf
 */
public class ExecutorServiceHandler<S> extends DecoratorHandler<S> {

  private ExecutorService pool;
  private Thread.UncaughtExceptionHandler exceptionHandler;

  public ExecutorServiceHandler(Handler<S> other, ExecutorService pool, Thread.UncaughtExceptionHandler exceptionHandler) {
    super(other);
    this.pool = pool;
    this.exceptionHandler = exceptionHandler;
  }

  public ExecutorServiceHandler(Handler<S> other, ExecutorService pool) {
    this(other, pool, (t, e) -> System.out.println("uncaught: " + t + " error: " + e));
  }

  @Override
  public void handle(S s) {
    pool.submit(new FutureTask<>(
                  () -> {
                    super.handle(s);
                    return null;
                  }) {
                  @Override
                  protected void setException(Throwable t) {
                    exceptionHandler.uncaughtException(Thread.currentThread(), t);
                  }
                }
    );
  }
}
