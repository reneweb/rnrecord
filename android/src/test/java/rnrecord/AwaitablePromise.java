package rnrecord;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.PromiseImpl;
import com.facebook.react.common.futures.SimpleSettableFuture;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by reweber on 22/01/2017
 */
public class AwaitablePromise<T> {

    private final SimpleSettableFuture<T> resolveFuture;
    private final SimpleSettableFuture rejectFuture;
    private final Promise promise;

    public AwaitablePromise() {
        this.resolveFuture = new SimpleSettableFuture<>();
        this.rejectFuture = new SimpleSettableFuture();

        promise = new PromiseImpl(new Callback() {
            @Override
            public void invoke(Object... args) {
                resolveFuture.set((T)args[0]);
            }
        }, new Callback() {
            @Override
            public void invoke(Object... args) {
                rejectFuture.set(args[0]);
            }
        });
    }

    public Promise promise() {
        return promise;
    }

    public T awaitResolve() {
        try {
            return resolveFuture.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new AwaitTimeoutException(e);
        }
    }

    public <E> E awaitReject() {
        try {
            return (E)rejectFuture.get(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new AwaitTimeoutException(e);
        }
    }

    public static class AwaitTimeoutException extends RuntimeException {
        public AwaitTimeoutException() {}

        public AwaitTimeoutException(String detailMessage) {
            super(detailMessage);
        }

        public AwaitTimeoutException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public AwaitTimeoutException(Throwable throwable) {
            super(throwable);
        }
    }
}
