package com.jiwenjie.cocomusic.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jiwenjie.basepart.utils.ToastUtils
import com.jiwenjie.cocomusic.R
import com.jiwenjie.cocomusic.R.id.textView
import kotlinx.android.synthetic.main.activity_test.*

/**
 *  author:Jiwenjie
 *  email:278630464@qq.com
 *  time:2019/05/06
 *  desc:
 *  version:1.0
 */
class TestActivity : PlayBaseActivity() {

    override fun initActivity(savedInstanceState: Bundle?) {
        super.initActivity(savedInstanceState)

        textView.setOnClickListener {
            ToastUtils.showToast(this, "点击测试")
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }
}