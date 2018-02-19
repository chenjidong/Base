package com.kuky.baselib.baseUtils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author Kuky
 */
public class DataBindingAuxiliaries {

    @BindingAdapter({"image_url", "place_image", "error_image"})
    public static void imageLoader(ImageView image, String url, Drawable place, Drawable error) {
        Glide.with(image.getContext())
                .load(url)
                .thumbnail(0.7f)
                .apply(new RequestOptions()
                        .centerCrop()
                        .placeholder(place)
                        .fallback(error))
                .into(image);
    }
}
