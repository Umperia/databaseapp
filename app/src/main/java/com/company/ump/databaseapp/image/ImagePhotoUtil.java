package com.company.ump.databaseapp.image;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.company.ump.databaseapp.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ImagePhotoUtil {

    private Context context;
    private File file;
    private Uri uri;
    private String path;
    private File storageDir = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM), "Camera");

    public ImagePhotoUtil(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        try {
            file = new File(storageDir,
                    Constant.NAME_IMAGE_FILE + getNameFromDate() + ".jpg");

            uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            path = file.getAbsolutePath();
        } catch (Exception e) {
            Constant.E("file name " + e.toString());
        }
    }

    public String getNameFromDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
