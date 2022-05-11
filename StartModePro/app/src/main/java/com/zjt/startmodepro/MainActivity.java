package com.zjt.startmodepro;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjt.base.BaseActivity;
import com.zjt.router.RouteHub;
import com.zjt.startmodepro.concurrent.TestThreadPoolActivity;
import com.zjt.startmodepro.cpu_info.BlinkCpuInfo;
import com.zjt.startmodepro.cpu_info.MemoryMeter;
import com.zjt.startmodepro.cpu_info.Unit;
import com.zjt.startmodepro.float_window.FloatWindowView;
import com.zjt.startmodepro.pagerSnapHelper.TestPagerSnapHelperActivity;
import com.zjt.startmodepro.scroll_conflict.TestInnerInterceptActivity;
import com.zjt.startmodepro.singleinstance.DataManager;
import com.zjt.startmodepro.soloader.TestLoadSoActivity;
import com.zjt.startmodepro.viewmodel.NameViewModel;
import com.zjt.startmodepro.widget.RangeSeekBar;
import com.zjt.startmodepro.widget.TestDefineViewActivity;
import com.zjt.startmodepro.widget.TestPostByMultiThread;
import com.zjt.user_api.UserInfo;
import com.zjt.user_api.UserProvider;
import com.zjt.user_api.UserProxy;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

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

public class MainActivity extends BaseActivity {

    private TextView mTv;
    private TextView mToUserTxt;
    private RangeSeekBar mRangeSeekBar;
    private TextView mShowDialog;
    private Button mJump2FileActivity;
    private ImageView img;
    private AlertDialog mDialog;
    private NameViewModel mNameViewModel;

    public void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            ((Activity) context).startActivityForResult(intent, 1001);
        }
    }

    public boolean checkFloatWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 表示的是系统开机到现在的具体时间，单位为毫秒，不算系统休眠时间即息屏等
        Log.e("xxxx", "time = >> " + SystemClock.uptimeMillis());

        HandlerThread handlerThread = new HandlerThread("DownLoadResource");

        setContentView(R.layout.activity_main);
        TestExceptionActivity.Companion.setMydata(new MyData("zhujiangtao", "hhhhhh"));

        mTv = findViewById(R.id.txt_rx);
        mToUserTxt = findViewById(R.id.txt_user);
        mShowDialog = findViewById(R.id.txt_show_dialog);
        mJump2FileActivity = findViewById(R.id.jump_2_file_activity);
        img = findViewById(R.id.image);

        mJump2FileActivity.setOnClickListener(v -> {
            if (checkFloatWindowPermission(this)) {
                FloatWindowView floatWindowView = new FloatWindowView(this);
                floatWindowView.show();
            } else {
                applyPermission(this);
            }

            Queue<String> queue = new ArrayDeque<>(3);
            queue.poll();

//            FileActivity.Companion.enter(this);

        });


        SingleModel.INSTANCE.getTestVal().observe(this, stringStringPair -> {
            String v1 = stringStringPair.component1();
            String v2 = stringStringPair.component2();
        });

        mTv.setOnClickListener(v -> {
//            test1();
//            test2();
            JetPackActivity.enter(this);
//            Glide.with(this).load(R.drawable.a2).into(img);

            // 跳转手势 activity
//            Intent intent = new Intent(this, TestGestureDetectorActivity.class);
//            startActivity(intent);

            NumberFormat moneyFormat = NumberFormat.getCurrencyInstance();
            moneyFormat.setGroupingUsed(false);
            moneyFormat.setMaximumFractionDigits(0);
            moneyFormat.setRoundingMode(RoundingMode.HALF_UP);   //四舍五入
            // 如果你在手机系统里面设置地区比如说是中国台湾，那么下面就会返回 NT$4, NT$ 表示的是 new TaiWan Dollar
            String result = moneyFormat.format(4); //

            String money = StringFormatter.format("%s", result.substring(1));
            System.out.println("money = "+money);

//            ARouter.getInstance()
//                    .build(RouteHub.User.USER_FLOAT_ACTIVITY)
//                    .navigation(this);

//            CallAnchorDialog.Companion.show(this, new CallAnchorDialog.OnCallDialogDismiss() {
//                @Override
//                public void onDismiss() {
//                    CallAnchorDialog.Companion.hide(MainActivity.this);
//                }
//            });

            mNameViewModel = new ViewModelProvider(this).get(NameViewModel.class);
            Log.e("MainActivity", "mNameViewModel ==== > " + mNameViewModel);

            mNameViewModel.setCurrentName("测试ViewModel页面服用");

            // 演示livedata 数据倒灌，就是先设置 livedata 的值，后监听依然能够收到数据回调
            mNameViewModel.getCurrentName().observe(this, data -> {
                Log.e("MainActivity", "data ==== > " + data);
            });

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

        findViewById(R.id.btn_so).setOnClickListener(v -> {
            Intent intent = new Intent(this, TestLoadSoActivity.class);
            startActivity(intent);
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

                            handler.postDelayed(() -> {
                                Log.e("zjt", "delay runnable");
                            }, 3_000);
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
                    Intent intent = new Intent(this, TestPermissionActivity.class);
                    startActivity(intent);
//                    Semaphore semaphore = new Semaphore(0);
//                    try {
//                        semaphore.acquire();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Log.e("xxxxx", "num = " + semaphore.availablePermits());
//                    semaphore.release();
//                    Log.e("xxxxx", "num = " +semaphore.availablePermits());

                    String brand = android.os.Build.BRAND;
                    String model = android.os.Build.MODEL;
                    int cores = Runtime.getRuntime().availableProcessors();
                    BlinkCpuInfo blinkCpuInfo = BlinkCpuInfo.parseCpuInfo();
                    if (blinkCpuInfo != null && blinkCpuInfo.mRawInfoMap != null && !blinkCpuInfo.mRawInfoMap.isEmpty()) {
                        String cpu = blinkCpuInfo.mRawInfoMap.get("hardware");
                        Log.e("cpucpu", "cpuinfo = " + cpu);
                    }
                    MemoryMeter memory = new MemoryMeter(this);
                    long totalMemory = memory.getSystemTotalMem(Unit.MB);

                    Log.e("xxxxx", "brand = " + brand + " ，model = " + model + "， cores = " + cores + " , totalMemory = " + totalMemory);
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


                    Intent intent = new Intent(this, TextFolderActivity2.class);
                    startActivity(intent);

                });

        findViewById(R.id.btn_scroll_conflict)
                .setOnClickListener(v -> {
                    String file = this.getDir("mod_resource", Context.MODE_PRIVATE).getAbsolutePath();
                    Intent intent = new Intent(this, TestInnerInterceptActivity.class);
                    startActivity(intent);

//                    TestHandlerThread handlerThread = new TestHandlerThread();
//                    for (int i = 1; i < 5; i++) {
//                        handlerThread.addMsg("消息"+i);
//                    }
//                    testPost();
                });

        findViewById(R.id.btn_coroutine_2)
                .setOnClickListener(v -> {
                    com.zjt.startmodepro.widget.TestCoroutineActivity.Companion.enter(this);
                });

        findViewById(R.id.btn_canvas)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, TestDefineViewActivity.class);
                    startActivity(intent);

//                    new Thread("thread_zhu") {
//                        @Override
//                        public void run() {
//                            super.run();
//                            String a = null;
//                            a.length();
//                        }
//                    }.start();
                }); //

        findViewById(R.id.btn_pop_window)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, TestPopWindowActivity.class);
                    startActivity(intent);

                });

        findViewById(R.id.btn_pager_snap_helper)
                .setOnClickListener(v -> {
                    TestPagerSnapHelperActivity.enter(this);

                });

    }


    private void testPost() {
        TestPostByMultiThread postByMultiThread = new TestPostByMultiThread();
        for (int i = 5; i < 10; i++) {
            int finalI = i;
            new Thread("thread " + finalI) {
                @Override
                public void run() {
                    char[] tmp = new char[1];
                    tmp[0] = (char) (97 + finalI);
                    postByMultiThread.addTask(finalI, new String(tmp));
                }
            }.start();
        }

    }


    private int sum(int x, int y) {
        return x + y;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // onResume 生命周期中是可以在子线程中更新UI的。因为线程检查是在 ViewRootImpl 类中的，而 ViewRootImpl 类是在onResume执行完成之后才创建的。
//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                mTv.setText("我是在onResume生命周期的子线程中更新的UI");
//            }
//        }.start();
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