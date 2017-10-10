package com.company.ump.databaseapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.company.ump.databaseapp.image.ImagePhotoUtil;
import com.company.ump.databaseapp.image.ImageUtil;
import com.company.ump.databaseapp.profile.ItemListener;
import com.company.ump.databaseapp.profile.Profile;
import com.company.ump.databaseapp.profile.ProfileItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.company.ump.databaseapp.Constant.NAME_IMAGE_FILE;

public class ProfilesView extends LinearLayout {

    @BindView(R2.id.edit_text_name)
    EditText etName;

    @BindView(R2.id.edit_text_email)
    EditText etEmail;

    @BindView(R2.id.edit_text_phone)
    EditText etPhone;

    @BindView(R2.id.btn_save)
    Button btnSave;

    @BindView(R2.id.btn_gallery)
    ImageButton btnGallery;

    @BindView(R2.id.btn_camera)
    ImageButton btnCamera;

    @BindView(R2.id.recycle_list_view)
    RecyclerView recyclerView;

    Context context;
    ItemListener addListener;
    ItemListener removeListener;
    ResViewAdapter adapter;
    ImageUtil imageUtil;
    ImagePhotoUtil imagePhotoUtil;
    File storageDir;
    ClickInterface clickInterface;
    String path;

    public ProfilesView(Context context) {
        super(context);
        init(context);
    }

    public ProfilesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        View view = LayoutInflater.from(context).inflate(R.layout.profiles_view, this);
        this.context = context;

        ButterKnife.bind(this, view);

        imageUtil = new ImageUtil(context);

        LinearLayoutManager llm = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(llm);

        adapter = new ResViewAdapter(new ArrayList<>(), imageUtil, this::remove);
        recyclerView.setAdapter(adapter);

        btnGallery.setOnClickListener(view1 -> clickInterface.clickButton(ButtonEnum.GALLERY));
        btnCamera.setOnClickListener(view1 -> clickInterface.clickButton(ButtonEnum.CAMERA));

        btnSave.setOnClickListener(view1 -> add());
    }

    private void add() {
        if (!isEmpty(etName) && !isEmpty(etEmail) && !isEmpty(etPhone)) {
            ProfileItem profile = new ProfileItem();
            profile.setName(etName.getText().toString());
            profile.setEmail(etEmail.getText().toString());
            profile.setPhone(etPhone.getText().toString());
            profile.setPath(path);
            clearEditTexts();
            adapter.addProfile(profile);
            addListener.getProfile(profile);
        } else {
            Toast.makeText(context, "Input error.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addALL(List<Profile> profiles) {
        for (Profile profile : profiles) {
            ProfileItem profileItem = new ProfileItem();
            profileItem.setName(profile.getName());
            profileItem.setEmail(profile.getEmail());
            profileItem.setPhone(profile.getPhone());
            profileItem.setPath(profile.getPath());
            adapter.addProfile(profileItem);
        }
    }

    private void remove(Profile profile) {
        adapter.getProfiles().remove(profile);
        removeListener.getProfile(profile);
        adapter.notifyDataSetChanged();
    }

    public void setImgUri (String path) {
        this.path = path;
    }

    public void getImgUri(ClickInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    private void clearEditTexts() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
    }

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }

    private File newFile(Bitmap bitmap, String name) {
        File f;
        try {
            //create a file to write bitmap data
            f = new File(storageDir, NAME_IMAGE_FILE + name + ".jpg");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] bitMapData = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitMapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Constant.E("file name " + e.toString());
            return null;
        }

        return f;
    }

    public ImagePhotoUtil getImagePhotoUtil() {
        return imagePhotoUtil;
    }

    public void setRemoveListener(ItemListener removeListener) {
        this.removeListener = removeListener;
    }

    public void setAddListener(ItemListener addListener) {
        this.addListener = addListener;
    }
}
