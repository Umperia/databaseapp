package com.company.ump.databaseapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.company.ump.databaseapp.image.ImagePhotoUtil;
import com.company.ump.databaseapp.image.ImageUtil;
import com.company.ump.databaseapp.profile.DBController;
import com.company.ump.databaseapp.profile.ProfileItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.company.ump.databaseapp.Constant.CAMERA_PERMISSION_REQUEST;
import static com.company.ump.databaseapp.Constant.EXTERNAL_STORAGE_PERMISSION_REQUEST;

public class MainActivity extends AppCompatActivity{

    ProfileItem profileItem;
    ImageUtil imageUtil;
    ImagePhotoUtil imagePhotoUtil;
    File storageDir;
    CircleImageView avatar;
    ProfilesView profilesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        imagePhotoUtil = new ImagePhotoUtil(this);
        imageUtil = new ImageUtil(this);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        profilesView = (ProfilesView) findViewById(R.id.profiles_view);

        storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        DBController dbController = new DBController(this);

        profilesView.getImgUri(button -> {
            switch (button) {
                case GALLERY:
                    checkPermissionsAndPickImage();
                    break;
                case CAMERA:
                    checkPermissionsAndTakePhoto();
                    break;
            }
        });

        ProfilesView profilesView = (ProfilesView) findViewById(R.id.profiles_view);
        profilesView.setAddListener(profile -> {
            profileItem = new ProfileItem();
            profileItem.setName(profile.getName());
            profileItem.setEmail(profile.getEmail());
            profileItem.setPhone(profile.getPhone());
            profileItem.setPath(profile.getPath());
            dbController.saveDB(profileItem);
        });

        profilesView.setRemoveListener(profile -> dbController.remove(profile.getName()));

        profilesView.addALL(dbController.getAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ImagePhotoUtil getImagePhotoUtil() {
        return imagePhotoUtil;
    }

    public void checkPermissionsAndPickImage() {
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_REQUEST);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, Constant.ACTION_PICK);
        }
    }

    public void checkPermissionsAndTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                grantUriPermission(packageName, getImagePhotoUtil().getUri(), Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getImagePhotoUtil().getUri());
            startActivityForResult(intent, Constant.ACTION_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == EXTERNAL_STORAGE_PERMISSION_REQUEST && grantResults[0] == PERMISSION_GRANTED) {
            try {
                checkPermissionsAndPickImage();
            } catch (Exception e) {
                Constant.E("pickImage() " + e.toString());
            }
        }

        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults[0] == PERMISSION_GRANTED) {
            try {
                checkPermissionsAndTakePhoto();
            } catch (Exception e) {
                Constant.E("takePhoto() " + e.toString());
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Constant.D(String.valueOf(requestCode) + " " + String.valueOf(resultCode));
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.ACTION_PICK || requestCode == Constant.ACTION_IMAGE_CAPTURE) {
                String filePath;
                if (requestCode == Constant.ACTION_PICK) {
                    Uri _uri = data.getData();
                    if (_uri != null && "content".equals(_uri.getScheme())) {
                        Cursor cursor = getContentResolver().query(_uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
                        assert cursor != null;
                        cursor.moveToFirst();
                        filePath = cursor.getString(0);
                        cursor.close();
                    } else {
                        assert _uri != null;
                        filePath = _uri.getPath();
                    }
                    if (filePath != null) {
                        imageUtil.getImageLoader().displayImage("file://" + filePath, avatar, imageUtil.getOptionsPhoto());
                        profilesView.setImgUri("file://" + filePath);
                    }

                }
                if (requestCode == Constant.ACTION_IMAGE_CAPTURE) {
                    filePath = getImagePhotoUtil().getPath();
                    if (filePath != null) {
                        imageUtil.getImageLoader().displayImage("file://" + filePath, avatar, imageUtil.getOptionsPhoto());
                        profilesView.setImgUri("file://" + filePath);
                    }

                }
            }
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
