package com.example.winebarcodereader.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.winebarcodereader.util.ManageImageSizeScaleUtil
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import com.example.winebarcodereader.R
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder




object ContentsBindingAdapter {

    /**
     * WineInfoResultViewModel의 url값이 변경되면 Glide로 이미지 처리
     */
    @BindingAdapter(value = ["bind_image","device_width","wine_price_view","wine_event_msg"], requireAll = false)
    @JvmStatic
    fun loadWineInfoImage(imageView : ImageView, url : String?, device_width:Int?,winePriceTextView: TextView, wineEventMsgTextView:TextView){
        if (!url.isNullOrEmpty()) {
            val animation:AnimationDrawable = imageView.background as AnimationDrawable
            animation.start()
            Glide.with(imageView.context)
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(object: CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // 아래 resource가 들어간 뷰가 사라지는 등의 경우의 처리
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        Log.d("onResourceReady","onLoadFailed :: ")
                        animation.stop()
                        imageView.background = null

                        imageView.setImageDrawable(imageView.context.getDrawable(R.drawable.wine_info_ready))
                    }

                    override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                        Log.d("onResourceReady","onResourceReady original bitmap width :: " + bitmap.width)
                        Log.d("onResourceReady","onResourceReady original bitmap height :: " + bitmap.height)

                        animation.stop()
                        imageView.background = null

                        // 이미지가 있는 경우 price TextView도 visible
                        winePriceTextView.visibility = View.VISIBLE

                        // 이미지가 있는 경우 wine Event Message TextView도 visible
                        wineEventMsgTextView.visibility = View.VISIBLE

                        if(device_width == 0){
                            imageView.setImageBitmap(bitmap)
                        }else{
                            // set resized image bitmap
                            imageView.setImageBitmap(ManageImageSizeScaleUtil.getResizedImage(bitmap, device_width))
                        }
                    }
                })
        }
    }

    /**
     * AdvertisementViewModel의 video uri가 수정되면 start video 혹은 stop video
     */
    @BindingAdapter("bind_video")
    @JvmStatic
    fun loadAdvertisementVideo(videoView : VideoView , uri : Uri?){
        if(uri != null){
            videoView.setOnPreparedListener{
                // 반복 재생
                it.isLooping = true
            }
            videoView.setVideoURI(uri)
            videoView.start()
        }else{
            videoView.stopPlayback()
        }
    }

    /**
     * Barcode Generator Adapter
     */
    @BindingAdapter("bind_barcode_image")
    @JvmStatic
    fun setPocketCUWineBarBarcodeImage(imageView : ImageView , barcode : String?){
        if(!barcode.isNullOrEmpty()){
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap(barcode, BarcodeFormat.QR_CODE, 400, 400)
            imageView.setImageBitmap(bitmap)
        }else{

        }
    }
}