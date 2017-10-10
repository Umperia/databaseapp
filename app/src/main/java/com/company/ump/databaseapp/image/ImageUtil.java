package com.company.ump.databaseapp.image;

import android.content.Context;

import com.company.ump.databaseapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageUtil {

    private ImageLoader imageLoader;
    private DisplayImageOptions optionsPhoto;

    public com.nostra13.universalimageloader.core.ImageLoader getImageLoader() {
        return imageLoader;
    }

    public DisplayImageOptions getOptionsPhoto() {
        return optionsPhoto;
    }


    public ImageUtil(Context context) {

        if (imageLoader == null) {
            imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        }
        if (optionsPhoto == null) {

            optionsPhoto = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading(true)
                    .showImageForEmptyUri(R.drawable.ic_account_circle_24dp)
                    .showImageOnFail(R.drawable.ic_account_circle_24dp)
                    .showImageOnLoading(R.drawable.ic_account_circle_24dp)
                    .cacheInMemory(false)
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                    .build();
        }
    }

    public void clearCache() {

        if (imageLoader != null) {
            imageLoader.clearMemoryCache();
            imageLoader.clearDiskCache();
        }

    }
}
