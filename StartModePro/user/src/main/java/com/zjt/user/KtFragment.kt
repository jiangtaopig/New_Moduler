package com.zjt.user

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.base.BaseFragment
import com.zjt.user.viewmodel.MeViewModel

class KtFragment : BaseFragment() {

    lateinit var mTitleTv: TextView
    private var mMeViewModel: MeViewModel? = null
    private var mDialog: AlertDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mMeViewModel = activity?.let {
            ViewModelProvider(it).get(MeViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me_layout, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTitleTv = view.findViewById(R.id.txt_me_kt)

        mMeViewModel.apply {
            mMeViewModel?.mData?.observe(viewLifecycleOwner, Observer<String> { t -> mTitleTv.text = t })
        }

        Log.e("KtFragment", "mMeViewModel = $mMeViewModel")

        mTitleTv.setOnClickListener {
            mMeViewModel?.doSth()
//            val intent = Intent(activity, TestFloatActivity::class.java)
//            activity?.startActivity(intent)

//            requestCamera()

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun requestCamera() {
        if (checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 1)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            for (i in permissions.indices) {
                //已授权
                if (grantResults[i] == 0) {
                    continue
                }
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permissions[i])) {
                    //选择禁止/拒绝
                    request()
                } else {
                    //选择拒绝并不再询问
                    jump2Setting()
                }
            }
        }
    }

    private fun jump2Setting() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("授权")
        builder.setMessage("需要允许授权才可使用")
        builder.setPositiveButton("去授权") { dialog: DialogInterface?, id: Int ->
            if (mDialog != null && mDialog!!.isShowing()) {
                mDialog!!.dismiss()
            }
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", context!!.packageName, null)
            intent.data = uri
            //调起应用设置页面
            startActivityForResult(intent, 2)
        }
        mDialog = builder.create()
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }


    private fun request() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("授权")
        builder.setMessage("需要允许授权才可使用")
        builder.setPositiveButton("去允许") { dialog: DialogInterface?, id: Int ->
            if (mDialog != null && mDialog!!.isShowing) {
                mDialog!!.dismiss()
            }
            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), 1)
        }
        mDialog = builder.create()
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }


}



