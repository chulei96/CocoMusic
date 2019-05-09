package com.jiwenjie.cocomusic.ui.presenter

import android.graphics.Bitmap
import com.jiwenjie.basepart.mvp.BaseMvpPresenter
import com.jiwenjie.cocomusic.aidl.Music
import com.jiwenjie.cocomusic.common.PlayProgressListener
import com.jiwenjie.cocomusic.play.playservice.MusicPlayerService
import com.jiwenjie.cocomusic.ui.contract.PlayContract
import com.jiwenjie.cocomusic.utils.CommonUtils
import com.jiwenjie.cocomusic.utils.CoverLoader
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 *  author:Jiwenjie
 *  email:278630464@qq.com
 *  time:2019/05/09
 *  desc:
 *  version:1.0
 */
class PlayControlPresenter(view: PlayContract.View) :
        BaseMvpPresenter<PlayContract.View>(view),
        PlayContract.Presenter,
        PlayProgressListener {

   override fun attachView(view: PlayContract.View) {
      MusicPlayerService.addProgressListener(this)
   }

   override fun detachView() {
      MusicPlayerService.removeProgressListener(this)
   }

   override fun onProgressUpdate(position: Long, duration: Long) {
      mView?.updateProgress(position, duration)
   }

   override fun updateNowPlaying(music: Music?, isInit: Boolean?) {
      mView?.showNowPlaying(music)
      CoverLoader.loadBigImageView(mView!!.getContext(), music, object : CoverLoader.BitmapCallBack {
         override fun showBitmap(bitmap: Bitmap?) {
            doAsync {
               val blur = CommonUtils.createBlurredImageFromBitmap(bitmap!!, 12)
               uiThread {
                  mView?.setPlayingBg(blur, isInit)
               }
         }
      }})
   }
}