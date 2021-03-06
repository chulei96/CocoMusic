package com.jiwenjie.cocomusic.ui.activity

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.KeyEvent
import com.jiwenjie.basepart.adapters.BaseFragmentPagerAdapter
import com.jiwenjie.basepart.utils.ToastUtils
import com.jiwenjie.basepart.views.BaseActivity
import com.jiwenjie.cocomusic.R
import com.jiwenjie.cocomusic.test.TestFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_toolbar.*
import kotlin.collections.ArrayList

/**
 *  author:Jiwenjie
 *  email:278630464@qq.com
 *  time:2019/05/12
 *  desc:
 *  version:1.0
 */
@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {

   private val CURRENT_ITEM_MINE = 0     // 我的
   private val CURRENT_ITEM_FIND = 1     // 发现
   private val CURRENT_ITEM_FRIEND = 2   // 朋友
   private val CURRENT_ITEM_VIDEO = 3    // 视频

   private val ORIGINAL_FONT_SIZE = 16f   // 原始字体大小
   private val SELECT_FONT_SIZE = 18f   // 点击选中字体大小

   private var currentIndex = 0     // 标识 toolbar 的当前下标

   companion object {
      @JvmStatic
      fun runActivity(activity: Activity) {
         val intent = Intent(activity, MainActivity::class.java)
         activity.startActivity(intent)
      }
   }

   override fun initActivity(savedInstanceState: Bundle?) {
      initView()
      initEvent()
      // 设置默认的显示界面
      activity_viewPager.currentItem = currentIndex
   }

   private fun initView() {
      val fragmentList = ArrayList<Fragment>()
      fragmentList.add(TestFragment.newInstance("one"))
      fragmentList.add(TestFragment.newInstance("two"))
      fragmentList.add(TestFragment.newInstance("three"))
      fragmentList.add(TestFragment.newInstance("four"))

      val titleList = ArrayList<String>()
      titleList.add("one")
      titleList.add("two")
      titleList.add("three")
      titleList.add("four")

      activity_viewPager.adapter = BaseFragmentPagerAdapter(supportFragmentManager, fragmentList, titleList)
   }

   private fun initEvent() {
      common_menu.setOnClickListener {
         activity_drawerLyt.openDrawer(Gravity.START)
      }
      common_mine.setOnClickListener {
         // 我的
         updateUI(currentIndex, CURRENT_ITEM_MINE)
      }
      common_find.setOnClickListener {
         // 发现
         updateUI(currentIndex, CURRENT_ITEM_FIND)
      }
      common_friend.setOnClickListener {
         // 朋友
         updateUI(currentIndex, CURRENT_ITEM_FRIEND)
      }
      common_video.setOnClickListener {
         // 视频
         updateUI(currentIndex, CURRENT_ITEM_VIDEO)
      }
      common_search.setOnClickListener {
         // 点击搜索按钮，跳转搜索界面
         SearchActivity.runActivity(this)
      }

      activity_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
         override fun onPageScrollStateChanged(p0: Int) {
         }

         override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
         }

         override fun onPageSelected(position: Int) {
            updateUI(currentIndex, position)
         }
      })
   }

   private fun updateUI(lastIndex: Int, currentItem: Int) {
      currentIndex = currentItem

      if (lastIndex == currentIndex) return

      // 设置被选中的 item 的选中动画
      changeItem(currentItem, true)
      // 设置之前选中的 item 的还原动画
      changeItem(lastIndex, false)

      activity_viewPager.setCurrentItem(currentItem, true)
   }

   // 改变选择 item 的方法
   private fun changeItem(index: Int, isSelect: Boolean) {
      when (index) {
         CURRENT_ITEM_MINE -> {
            startAnim(common_mine, "textSize", "textColor", isSelect)
         }
         CURRENT_ITEM_FIND -> {
            startAnim(common_find, "textSize", "textColor", isSelect)
         }
         CURRENT_ITEM_FRIEND -> {
            startAnim(common_friend, "textSize", "textColor", isSelect)
         }
         CURRENT_ITEM_VIDEO -> {
            startAnim(common_video, "textSize", "textColor", isSelect)
         }
      }
   }

   // 动画方法
   private fun startAnim(target: Any, onePropertyName: String, twoPropertyName: String, isSelect: Boolean) {
      val objAnim: ObjectAnimator
      val colorAnim: ObjectAnimator
      if (isSelect) {   // 被选中的动画
         objAnim = ObjectAnimator.ofFloat(target, onePropertyName, ORIGINAL_FONT_SIZE, SELECT_FONT_SIZE)      // 缩放
         colorAnim = ObjectAnimator.ofObject(target, twoPropertyName, ArgbEvaluator(),
                 resources.getColor(R.color.alpha_80_white), resources.getColor(R.color.white))     // 字体颜色
      } else {    // 未被选中的动画
         objAnim = ObjectAnimator.ofFloat(target, onePropertyName, SELECT_FONT_SIZE, ORIGINAL_FONT_SIZE)      // 缩放
         colorAnim = ObjectAnimator.ofObject(target, twoPropertyName, ArgbEvaluator(),
                 resources.getColor(R.color.white), resources.getColor(R.color.alpha_80_white))     // 字体颜色
      }

      val animSet = AnimatorSet()
      animSet.playTogether(objAnim, colorAnim)
      animSet.duration = 300
      animSet.start()
   }

   // 双击退出程序
   var prePressTime = 0.toLong()

   override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
         if (System.currentTimeMillis() - prePressTime > 2000) {
            prePressTime = System.currentTimeMillis()
            ToastUtils.showToast(this, resources.getString(R.string.exit_tip))
         } else {
            finish()
            android.os.Process.killProcess(android.os.Process.myUid())
            System.exit(0)
         }
         return false
      } else {
         // 点击音量键加减的时候也会响应该方法，所以在这里处理，防止点击音量键会导致应用退出
         return super.onKeyDown(keyCode, event)
      }
   }

   override fun getLayoutId(): Int = R.layout.activity_main
}