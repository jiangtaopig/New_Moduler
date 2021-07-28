package com.zjt.startmodepro;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjt.base.BaseActivity;
import com.zjt.router.RouteHub;
import com.zjt.startmodepro.concurrent.TestThreadPoolActivity;
import com.zjt.startmodepro.cpu_info.BlinkCpuInfo;
import com.zjt.startmodepro.schedule.ScheduleActivity;
import com.zjt.startmodepro.singleinstance.DataManager;
import com.zjt.startmodepro.viewmodel.NameViewModel;
import com.zjt.startmodepro.widget.RangeSeekBar;
import com.zjt.user_api.UserInfo;
import com.zjt.user_api.UserProvider;
import com.zjt.user_api.UserProxy;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.operators.observable.ObservableObserveOn;
import io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private TextView mTv;
    private TextView mToUserTxt;
    private RangeSeekBar mRangeSeekBar;
    private TextView mShowDialog;
    private Button mJump2FileActivity;
    private AlertDialog mDialog;
    private NameViewModel mNameViewModel;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TestExceptionActivity.Companion.setMydata(new MyData("zhujiangtao", "hhhhhh"));

        mTv = findViewById(R.id.txt_rx);
        mToUserTxt = findViewById(R.id.txt_user);
        mShowDialog = findViewById(R.id.txt_show_dialog);
        mJump2FileActivity = findViewById(R.id.jump_2_file_activity);

        mJump2FileActivity.setOnClickListener(v -> {
            FileActivity.Companion.enter(this);
        });



        mNameViewModel = new ViewModelProvider(this).get(NameViewModel.class);
        Log.e("MainActivity", "mNameViewModel ==== > " + mNameViewModel);
        mNameViewModel.getCurrentName().observe(this, data -> {
            Log.e("MainActivity", "data ==== > " + data);
        });

        Observable.interval(5000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Throwable {
                        mNameViewModel.getCurrentName().setValue(aLong+" .. ZZ..");
                    }
                });

        mTv.setOnClickListener(v -> {
//            test1();
//            test2();

            TestBuilder testBuilder = new TestBuilder.Builder()
                    .setName("aaa")
                    .setAge(233)
                    .build();


            Log.e("zjt", "name = " + testBuilder.name + ", age = " + testBuilder.age);


            // 测试 ARouter 的 Provider 的使用
            UserProvider provider = UserProxy.getInstance().getUserProvider();
            UserInfo userInfo = provider.getUserInfo();
            Log.e("zjt", "name = " + userInfo.getName() + " , age = " + userInfo.getAge());


            UserProvider userProvider = (UserProvider) ARouter.getInstance().build(RouteHub.User.USER_PROVIDER_PATH).navigation();
            userProvider.getUserInfo();
            Log.e("zjt", "获取 ARouter 服务的方式2 name = " + userInfo.getName() + " , age = " + userInfo.getAge());

            mNameViewModel.setCurrentName("测试ViewModel页面服用");

//            JetPackActivity.enter(this);
        });

        mToUserTxt.setOnClickListener(v -> {
            ARouter.getInstance()
                    .build(RouteHub.User.USER_MAIN_PATH)
                    .navigation(this);

//            UserProvider userProvider = (UserProvider) ARouter.getInstance().build(RouteHub.User.USER_PROVIDER_PATH).navigation();
//            UserInfo userInfo = userProvider.getUserInfo();
//            Log.e("zjt", "获取 ARouter 服务的方式2 name = " + userInfo.getName() + " , age = " + userInfo.getAge());

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
            MyKotlinDialog myKotlinDialog = MyKotlinDialog.Companion.getInstance("编辑对话框");
            myKotlinDialog.setTitle("123456");
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
                    TestRefactorActivity.Companion.enter(this);
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

                            handler.postAtFrontOfQueue(() -> {
                                Log.e("zjt", "runnable 3 start");
                            });
                        }
                );

        findViewById(R.id.btn_thread_pool)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, TestThreadPoolActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.btn_exception)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, TestExceptionActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.btn_coroutine)
                .setOnClickListener(v -> {
                    TestCoroutineActivity.Companion.enter(this);
                });

        findViewById(R.id.btn_permission)
                .setOnClickListener(v -> {
//                    Intent intent = new Intent(this, TestPermissionActivity.class);
//                    startActivity(intent);
                    Semaphore semaphore = new Semaphore(0);
                    try {
                        semaphore.acquire();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("xxxxx", "num = " + semaphore.availablePermits());
                    semaphore.release();
                    Log.e("xxxxx", "num = " +semaphore.availablePermits());
                });

        findViewById(R.id.btn_edit)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, TestEditActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.btn_thread_local)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, TestThreadLocalActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.btn_schedule)
                .setOnClickListener(v -> {
//                    ScheduleActivity.Companion.enter(this);

                    String data = "?streamname=live_25489630_8896947&key=6f85a209ffa9d775b0a8d3635128b0a5";
                    Uri uri = Uri.parse(data);
                    String stream = uri.getQueryParameter("streamname");
                    String key = uri.getQueryParameter("key");

                    DataManager.Companion.getInstance(this).doSth();
                    DataManager.Companion.getInstance(this).doSth();

                    BlinkCpuInfo blinkCpuInfo = BlinkCpuInfo.parseCpuInfo();
                    if (blinkCpuInfo != null && blinkCpuInfo.mRawInfoMap != null && !blinkCpuInfo.mRawInfoMap.isEmpty()){
                        String cpu = blinkCpuInfo.mRawInfoMap.get("hardware");
                        Log.e("cpucpu", "cpuinfo = "+cpu);
                    }
                    Intent intent = new Intent(this, TextFolderActivity2.class);
                    startActivity(intent);

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

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", " ----- onPause --------");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MainActivity", " ----- onStop --------");
    }

    /**
     * 严格模式下，MainActivity 跳转到其他的界面那么 会走到 MainActivity 的 onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MainActivity", " ----- onDestroy --------");
//        TestExceptionActivity.Companion.setMydata(null);
    }
}