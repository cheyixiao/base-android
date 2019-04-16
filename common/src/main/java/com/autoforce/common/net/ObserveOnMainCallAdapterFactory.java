package com.autoforce.common.net;

import android.support.annotation.NonNull;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by xlh on 2017/7/3.
 */

public class ObserveOnMainCallAdapterFactory extends CallAdapter.Factory {

    private final Scheduler scheduler;

    public ObserveOnMainCallAdapterFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != Flowable.class) {
            return null; // Ignore non-Observable types.
        }

        // Look up the next call adapter which would otherwise be used if this one was not present.
        //noinspection unchecked returnType checked above to be Observable.
        final CallAdapter<Object, Flowable<?>> delegate =
                (CallAdapter<Object, Flowable<?>>) retrofit.nextCallAdapter(this, returnType,
                        annotations);

        return new CallAdapter<Object, Object>() {
            @Override
            public Object adapt(@NonNull Call<Object> call) {
                // Delegate to get the normal Observable...
                Flowable<?> o = delegate.adapt(call);
                // ...and change it to send notifications to the observer on the specified scheduler.
                return o.observeOn(AndroidSchedulers.mainThread());
            }

            @Override
            public Type responseType() {
                return delegate.responseType();
            }
        };
    }
}

