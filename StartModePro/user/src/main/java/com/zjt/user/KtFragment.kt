package com.zjt.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zjt.user.viewmodel.MeViewModel

class KtFragment : Fragment() {

    lateinit var mTitleTv : TextView
    lateinit var mMeViewModel :MeViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mMeViewModel = ViewModelProvider(this).get(MeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_me_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTitleTv = view.findViewById(R.id.txt_me_kt)
        // 直接修改服务器代码
         var a = "123
        var b = 22

        mMeViewModel.apply {
            mMeViewModel.mData.observe(viewLifecycleOwner, object : Observer<String>{
                override fun onChanged(t: String?) {
                    mTitleTv.text = t
                }

            })
        }

        mTitleTv.setOnClickListener {
            mMeViewModel.doSth()
        }

    }


}
