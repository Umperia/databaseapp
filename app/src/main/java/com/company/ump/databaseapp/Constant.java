package com.company.ump.databaseapp;

import android.util.Log;

public class Constant {

    public final static int ACTION_PICK = 0;
    public final static int ACTION_IMAGE_CAPTURE = 1;

    public final static int EXTERNAL_STORAGE_PERMISSION_REQUEST = 9999;
    public final static int CAMERA_PERMISSION_REQUEST = 8888;

    public final static String NAME_IMAGE_FILE = "IMG";
    public final static String LOGGER = "LOGGER";

    //Logger
    public static void D(String e) {
        Log.d(LOGGER, e);
    }

    public static void E(String e) {
        Log.e(LOGGER, e);
    }
}
