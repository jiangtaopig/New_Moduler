package com.zjt.startmodepro

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class TestFragment : Fragment() {

    lateinit var titleTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleTv = view.findViewById(R.id.txt_title_2)
        titleTv.setOnClickListener {
            CallAnchorDialog.show(this)
        }
    }

    companion object {
        private const val TAG = "TestFragment"

        fun show(activity: AppCompatActivity) {
            try {
                val previousFrag = activity.supportFragmentManager.findFragmentByTag(TAG)
                if (previousFrag != null) {
                    activity.supportFragmentManager.beginTransaction().remove(previousFrag)
                        .commitAllowingStateLoss()
                }
                val testFragment = TestFragment()
                activity.supportFragmentManager.beginTransaction().add(testFragment, TAG)
                    .commitAllowingStateLoss()
            } catch (e: Exception) {
                Log.e(TAG, "static show() occursException", e)
            }
        }
    }
}