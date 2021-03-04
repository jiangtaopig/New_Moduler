package com.zjt.startmodepro;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zjt.user_api.UserInfo;
import com.zjt.user_api.UserProvider;
import com.zjt.user_api.UserProxy;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.observable.ObservableObserveOn;
import io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTv = findViewById(R.id.txt_rx);
        mTv.setOnClickListener(v -> {
//                test1();
//            test2();

            UserProvider provider = UserProxy.getInstance().getUserProvider();
            UserInfo userInfo = provider.getUserInfo();
            Log.e("zjt", "name = " + userInfo.getName()+" , age = "+userInfo.getAge());


            JetPackActivity.enter(this);
        });

    }

    private void test2() {
        Observable<Integer> createOb = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                Log.e("RX_JAVA", "subscribe threadName = " + Thread.currentThread().getName());
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("RX_JAVA", "onSubscribe threadName = " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e("RX_JAVA", "integer = " + integer + ", threadNmae = " + Thread.currentThread().getName());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("RX_JAVA", "onError");
            }

            @Override
            public void onComplete() {
                Log.e("RX_JAVA", "onComplete");
            }
        };

        ObservableSubscribeOn<Integer> ioSchedulerOb = (ObservableSubscribeOn<Integer>) createOb
                .subscribeOn(Schedulers.io());

        ObservableObserveOn<Integer> mainOb = (ObservableObserveOn<Integer>) ioSchedulerOb
                .observeOn(AndroidSchedulers.mainThread());

        mainOb.subscribe(observer);
    }

    private void test1() {
        Observable<Integer> crateOb = Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onComplete();
            }
        });

        Observable<String> mapOb = crateOb.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Throwable {
                return "map_" + integer;
            }
        });

        Observable<String> flatMapOb = mapOb.flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(final String s) throws Throwable {
                return Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        emitter.onNext("flat_" + s);
                    }
                });
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("RX_JAVA", "onSubscribe threadName = " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1_000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNext(@NonNull String integer) {
                Log.e("RX_JAVA", "integer = " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("RX_JAVA", "onError");
            }

            @Override
            public void onComplete() {
                Log.e("RX_JAVA", "onComplete");
            }
        };

        flatMapOb.subscribe(observer);
    }
}