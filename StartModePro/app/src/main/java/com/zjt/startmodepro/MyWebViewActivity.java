package com.zjt.startmodepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class MyWebViewActivity extends AppCompatActivity {

    int redirectCount = 0;

    public static void enter(Context context, String dealerCode) {
        Intent intent = new Intent(context, MyWebViewActivity.class);
        intent.putExtra("dealerCode", dealerCode);
        if (!(context instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    private WebView webView;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        Log.e("TestWebViewActivity", "onNewIntent >> uri = " + uri.toString());
        if (uri != null) {
            if ((uri.getScheme() != null)) {
                Log.e("TestWebViewActivity", "onNewIntent >> scheme = " + uri.getScheme());
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_layout);

        webView = findViewById(R.id.web_view);
        initSetting(webView.getSettings());

        String dealerCode = getIntent().getStringExtra("dealerCode");

//        String url = "http://dimappsit.insaxxxic.com:8050/flutterh5/?path=clueFollowUpListScreen&screenParams=";
//
//        Map<String, String> map = new HashMap<>();
//        map.put("dealerCode", "BK20370");
//        if (!TextUtils.isEmpty(dealerCode)) {
//            map.put("dealerCode", dealerCode);
//        }
//        String mapJson = JSON.toJSONString(map);
//        url += mapJson;
//        url += "&ac=e63b77e94fa34b80b88c1a25bc33aaee&source=SVW_CLAIM";
//
//        String uu="http://dimapxxpsit.insxxaixxc.com:8050/flutterh5/?path=clueFollowUpListScreen&screenParams={\"dealerCode\": \"BK20370\"}&ac=e63b77e94fa34b80b88c1a25bc33aaee&source=SVW_CLAIM";
//        Log.e("url >>> " , url);
//        Log.e("uu  >>> " , uu);
//
//        // https://dimaxxpp.insxxaic.com:8443/flutterh5/?path=clueFollowUpListScreen&screenParams={"dealerCode": "BK20370"}&ac=e63b77e94fa34b80b88c1a25bc33aaee&source=SVW_CLAIM
        //




        webView.setWebViewClient(new WebViewClient() {
            String referer = "insurance-car-h5-web-v.xiaoccyuanins.com"; //
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("xxxxxwww", "shouldOverrideUrlLoading request url = " + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.d("xxxxxwww", "shouldOverrideUrlLoading  ----  url = " + url);
                if (url.startsWith("https://wx.tenpay.com") || url.startsWith("https://insurance-car-h5-web-v.xiaoyxxuanins.com")) {
                    Map headers = new HashMap();
                    headers.put("Referer", referer);
//                    if (url.contains("redirect_url")) {
//                        url = url.substring(0, url.indexOf("redirect_url") -1)  + "&redirect_url="+ URLEncoder.encode("mecsit.yinxzgketech.com://zjt:8888");
//                    }
                    Log.d("TestWebViewActivity", "url =  " + url);
                    String finalUrl = url;
                    new Handler(Looper.getMainLooper()).post(() -> view.loadUrl(finalUrl, headers));

                   return false;
                }
                referer = url;
                // 返回 true 表示交给系统处理，Webview 不用再处理，否则以 weixin:// 开头的 Scheme 再次被 WebView 打开报错
                // 位于 weixin://wap/pay?prepayid%... 的网页无法加载，因为 net::ERR_UNKNOWN_URL_SCHEME
               if (startPay(url)) {
                   return true;
               }

                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }

////                Log.e("TestWebViewActivity", "url = " + url);
//                if (url.startsWith("http://") || url.startsWith("https://")) { //加载的url是http/https协议地址
//                    view.loadUrl(url);
//                    return false; //返回false表示此url默认由系统处理,url未加载完成，会继续往下走
//
//                } else if (url.contains("baidu")) {
//                    view.loadUrl(url);
//                    return true;
//                } else if (!(url.startsWith("http:") || url.startsWith("https:"))) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e("xxxxxwww", "onPageFinished url = " + url + "  ,redirectCount = " + redirectCount);
                redirectCount++;
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("xxxxxwww", "onPageStarted url = " + url);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
//                Log.e("TestWebViewActivity", "shouldInterceptRequest url = " + url);
//                WebResourceResponse webResourceResponse = handleIntercept(request);

//                startPay(url);


                // 创建一个新的请求头 Map
//                Map<String, String> newHeaders = new HashMap<>(request.getRequestHeaders());
//                // 设置 Referer 头信息
//                newHeaders.put("Referer", "https://insurance-car-h5-web-v.xiaoyuanins.com/");
//                // 创建一个新的 WebResourceRequest 对象，使用新的请求头
//                WebResourceRequest newRequest = new WebResourceRequest() {
//                    @Override
//                    public Uri getUrl() {
//                        return Uri.parse(String.valueOf(request.getUrl()));
//                    }
//
//                    @Override
//                    public boolean isForMainFrame() {
//                        return request.isForMainFrame();
//                    }
//
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public boolean isRedirect() {
//                        return request.isRedirect();
//                    }
//
//                    @Override
//                    public boolean hasGesture() {
//                        return request.hasGesture();
//                    }
//
//                    @Override
//                    public String getMethod() {
//                        return request.getMethod();
//                    }
//
//                    @Override
//                    public Map<String, String> getRequestHeaders() {
//                        return newHeaders;
//                    }
//                };
//
//                // 继续处理请求
//                return super.shouldInterceptRequest(view, newRequest);



                return null;
            }
        });



//        webView.loadUrl("https://xyh5sit.yingxxketech.com/bmeinsurercarsalesweb/#/sales?token=fbef16e600334776a77bb93440e30801&licenseNo=%E4%BA%ACW235654");
//        Map<String, String> additionalMap = new HashMap<>();
//        additionalMap.put("Referer", "https://insurance-car-h5-web-v.xiaoyxxuanins.com/");
//        webView.loadUrl("https://insxxurance-car-h5-web-v.xiaoyuxxanins.com/#/sales?token=3c9094fde61842d9ba33d169b36be786", additionalMap);

        webView.loadUrl("https://mecsit.yingkxxetech.com/svwService/#/pay/index?token=609e6ea35b67412ea561940256694440&payOrderNo=RBS2500050715");
    }



    private boolean startPay(String url) {
        Log.e("TestWebViewActivity", "startPay  >>> " + url);
        try {
            if (url.startsWith("weixin://")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            } else if(url.startsWith("mecsit.yingketech.com")){ // 回调我这个APP里面定义的 scheme
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }else if (parseScheme(url)) { // 跳转支付宝支付
                Intent intent;
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                intent.addCategory("android.intent.category.BROWSABLE");
                intent.setComponent(null);
                startActivity(intent);
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    // 支付宝的
    public boolean parseScheme(String url) {
        if (url.contains("platformapi/startApp") || url.contains("platformapi/startapp")) {
            return true;
        } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M) && (url.contains("platformapi") && (url.contains("startApp") || url.contains("startpp")))) {
            return true;
        } else {
            return false;
        }
    }



    private void initSetting(WebSettings settings) {
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);

        settings.setUseWideViewPort(true); // 关键点
        settings.setDefaultTextEncodingName("utf-8"); //设置编码
        settings.setAllowFileAccess(true);// 支持文件流
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);// 调整到适合webview大小
        settings.setLoadWithOverviewMode(true);// 调整到适合webview大小
        settings.setBlockNetworkImage(false);

        String cachePath = getApplicationContext().getCacheDir().getPath(); // 把内部私有缓存目录'/data/data/包名/cache/'作为WebView的AppCache的存储路径
        settings.setAppCachePath(cachePath);
        settings.setAppCacheMaxSize(5 * 1024 * 1024);

        //开启缓存机制
        settings.setAppCacheEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);// true 表示允许Chrome://inspect/#devices
        }

        // 设置可以访问文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);


        //允许H5本地存储(localStorage & sessionStorage)
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);

        String app = " ;app/zuifuli/" + "1.0.0/";
        String userAgent = settings.getUserAgentString() + app;
        Log.d("TestWebviewActivity", "userAgent = " + userAgent);
        settings.setUserAgentString(userAgent);

    }
}
