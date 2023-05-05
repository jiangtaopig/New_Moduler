package com.zjt.startmodepro.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zjt.startmodepro.Constants

class WXEntryActivity :Activity() , IWXAPIEventHandler{

    lateinit var api :IWXAPI;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api =WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);

        try {
            val intent = intent
            api.handleIntent(intent, this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {

    }

    override fun onResp(p0: BaseResp?) {
        Toast.makeText(this, "code = ${p0?.errCode} , msg = ${p0?.errStr}", Toast.LENGTH_LONG).show();
    }
}