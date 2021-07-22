package com.zjt.startmodepro

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.zjt.startmodepro.utils.DisplayUtil

/**

 * @Author : zhujiangtao01

 * @Time : On 2021/4/27 4:48 下午

 * @Description : TestEditActivity

 */


class TestEditActivity : AppCompatActivity() {

    private lateinit var mTitleEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_layout)
        initView()
    }

    private fun initView() {
        mTitleEditText = findViewById(R.id.edit_tv)

        mTitleEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    v?.text.toString().let {
                        if (it.isEmpty()) {
                            Toast.makeText(this@TestEditActivity, "请输入标题", Toast.LENGTH_LONG).show()
                            return false;
                        }
                        clearTitleFocus()
                        DisplayUtil.hideSoftInput(mTitleEditText)
                        // todo ... 调接口更新标题
                    }
                    return true
                }
                return false
            }
        })

        mTitleEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mTitleEditText.let {
//                    it.isSingleLine = false
//                    it.maxLines = 2
                    requestFocus()
                }

            } else {
                mTitleEditText.let {
                    it.maxLines = 1
                    it.isSingleLine = true
                }
            }
        }
    }

    private fun clearTitleFocus() {
        mTitleEditText?.clearFocus()
    }

    private fun requestFocus() {
        val content = mTitleEditText?.text.toString()
        mTitleEditText?.isFocusable = true
        mTitleEditText?.isFocusableInTouchMode = true
        mTitleEditText?.requestFocus()
        mTitleEditText?.postDelayed({
            mTitleEditText?.setSelection(content.length)
        }, 300)
        DisplayUtil.showSoftInput(mTitleEditText)
    }

}