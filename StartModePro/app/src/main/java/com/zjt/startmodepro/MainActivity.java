package com.zjt.startmodepro;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.alibaba.android.arouter.launcher.ARouter;
import com.zjt.router.RouteHub;
import com.zjt.startmodepro.concurrent.TestThreadPoolActivity;
import com.zjt.startmodepro.widget.RangeSeekBar;
import com.zjt.user_api.UserInfo;
import com.zjt.user_api.UserProvider;
import com.zjt.user_api.UserProxy;

import org.jetbrains.annotations.NotNull;

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
    private AlertDialog mDialog;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTv = findViewById(R.id.txt_rx);
        mToUserTxt = findViewById(R.id.txt_user);
        mShowDialog = findViewById(R.id.txt_show_dialog);
        mJump2FileActivity = findViewById(R.id.jump_2_file_activity);

//        requestPermission();

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
                    TestRefactorActivity.Companion.enter(this);
                    ZhuJtUtils.test();
                });

        findViewById(R.id.btn_test_handler_sync_barrier).
                setOnClickListener(
                        v -> {
                            Integer.valueOf("这种");
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

                            new Handler().postDelayed(() -> {

                            }, 300);
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
//                    requestCamera();
                });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
//        requestStorage();
        requestCamera();
//
//        else {
//            Toast.makeText(this, "您已经申请了权限!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull @NotNull String[] permissions, @androidx.annotation.NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                //已授权
                if (grantResults[i] == 0) {
                    continue;
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                    //选择禁止/拒绝
//                    request();
                    int a = 1;
                } else {
                    //选择拒绝并不再询问
                    int b = 2;
//                    jump2Setting();
                }
            }
        }
    }

    private void request(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("授权");
        builder.setMessage("需要允许授权才可使用");
        builder.setPositiveButton("去允许", (dialog, id) -> {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void jump2Setting(String txt){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("授权" + ": "+txt);
        builder.setMessage("需要允许授权才可使用");
        builder.setPositiveButton("去授权", (dialog, id) -> {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            //调起应用设置页面
            startActivityForResult(intent, 2);
        });
        mDialog = builder.create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCamera() {
        if (this.checkSelfPermission(Manifest.permission.CAMERA) != PERMISSION_GRANTED) {
            // 从来没申请过权限的时候返回false
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }else {
                jump2Setting("相机权限");
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStorage() {
        if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }else {
                jump2Setting("读写SD卡权限");
            }

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