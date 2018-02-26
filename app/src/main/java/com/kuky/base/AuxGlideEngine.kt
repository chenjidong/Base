package com.kuky.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.zhihu.matisse.engine.ImageEngine

/**
 * @author Kuky
 *
 * Let Matisse support Glide 4
 */
class AuxGlideEngine : ImageEngine {

    override fun loadAnimatedGifThumbnail(context: Context, resize: Int, placeholder: Drawable, imageView: ImageView, uri: Uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .override(resize, resize)
                        .placeholder(placeholder))
                .into(imageView)
    }

    override fun loadImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri) {
        Glide.with(context)
                .load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .override(resizeX, resizeY))
                .into(imageView)
    }

    override fun loadAnimatedGifImage(context: Context, resizeX: Int, resizeY: Int, imageView: ImageView, uri: Uri) {
        Glide.with(context)
                .asGif()
                .load(uri)
                .apply(RequestOptions()
                        .centerCrop()
                        .priority(Priority.HIGH)
                        .override(resizeX, resizeY))
                .into(imageView)
    }

    override fun loadThumbnail(context: Context, resize: Int, placeholder: Drawable?, imageView: ImageView, uri: Uri) {
        Glide.with(context)
                .asBitmap()
                .load(uri)
                .apply(RequestOptions()
                        .placeholder(placeholder)
                        .override(resize, resize)
                        .centerCrop())
                .into(imageView)
    }

    override fun supportAnimatedGif(): Boolean {
        return true
    }
}
