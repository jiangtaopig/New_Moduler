package com.zjt.startmodepro;

import android.Manifest;
import android.icu.util.TimeUnit;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjt.router.RouteHub;
import com.zjt.startmodepro.widget.RangeSeekBar;
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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private TextView mTv;
    private TextView mToUserTxt;
    private RangeSeekBar mRangeSeekBar;
    private TextView mShowDialog;
    private Button mJump2FileActivity;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        mTv = findViewById(R.id.txt_rx);
        mToUserTxt = findViewById(R.id.txt_user);
        mShowDialog = findViewById(R.id.txt_show_dialog);
        mJump2FileActivity = findViewById(R.id.jump_2_file_activity);

        mJump2FileActivity.setOnClickListener(v -> {
            FileActivity.Companion.enter(this);
        });


        mTv.setOnClickListener(v -> {
//            test1();
//            test2();

            // 测试 ARouter 的 Provider 的使用
            UserProvider provider = UserProxy.getInstance().getUserProvider();
            UserInfo userInfo = provider.getUserInfo();
            Log.e("zjt", "name = " + userInfo.getName() + " , age = " + userInfo.getAge());


            UserProvider userProvider = (UserProvider) ARouter.getInstance().build(RouteHub.User.USER_PROVIDER_PATH).navigation();
            userProvider.getUserInfo();
            Log.e("zjt", "获取 ARouter 服务的方式2 name = " + userInfo.getName() + " , age = " + userInfo.getAge());
            JetPackActivity.enter(this);
        });

        mToUserTxt.setOnClickListener(v -> {
            ARouter.getInstance().build(RouteHub.User.USER_MAIN_PATH)
                    .navigation(this);
        });

        mRangeSeekBar = findViewById(R.id.range_seek_bar);
        // 在seekbar 的左上角显示0， 右上角现实 100
//        mRangeSeekBar.setUnit("0", "100");
        // 设置 seekbar 开始时的最小位置
        mRangeSeekBar.setMinValue(10);
        // 设置seekbar 开始时的 最大位置
        mRangeSeekBar.setMaxValue(100);

        mRangeSeekBar.setCallBack(new RangeSeekBar.DhdBarCallBack() {
            @Override
            public void onEndTouch(float minPercentage, float maxPercentage) {
                super.onEndTouch(minPercentage, maxPercentage);
                Log.e("seekbar", "minPercentage = " + minPercentage + " , maxPercentage = " + maxPercentage);
            }
        });


        mShowDialog.setOnClickListener(v -> {
//            NoticeDialog noticeDialog = NoticeDialog.getInstance("哈哈哈哈");
//            noticeDialog.show(getSupportFragmentManager(), "Notice_Dialog");
            MyKotlinDialog myKotlinDialog = MyKotlinDialog.Companion.getInstance("haha");
            myKotlinDialog.show(getSupportFragmentManager(), "MyKotlin_Dialog");
        });

        findViewById(R.id.btn_bitmap_clip).setOnClickListener(v -> {
            BitmapClipActivity.Companion.enter(this);
        });

        findViewById(R.id.btn_2_http).setOnClickListener(v -> {
            HttpActivity.Companion.enter(this);
        });

        findViewById(R.id.btn_2_kotlin_package)
                .setOnClickListener(v -> {
                    MyKotlinActivity.Companion.enter(this);
                    ZhuJtUtils.test();
                });



        findViewById(R.id.btn_test_handler_sync_barrier).
                setOnClickListener(
                        v -> {
                            Handler handler = new Handler();
                            handler.post(() -> {
                                Log.e("zjt", "runnable 1 start");
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Log.e("zjt", "runnable 1 end");
                            });

                            handler.post(() -> {
                                Log.e("zjt", "runnable 2 start");
                            });

                            handler.postAtFrontOfQueue(() ->{
                                Log.e("zjt", "runnable 3 start");
                            });

                        }
                );


        requestPermission();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Toast.makeText(this, "您已经申请了权限!", Toast.LENGTH_SHORT).show();
        }
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