package com.company.ump.databaseapp.profile;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

public class ProfileRealm {

    public ProfileRealm() {
    }

    private final String TAG = "RealmProfile";

    private Realm init(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("profiles.realm")
                .modules(new MyLibraryModule())
                .build();
        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Log.e(TAG, String.valueOf(e));
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                //No Realm file to remove.
                Log.e(TAG, String.valueOf(ex));
            }
        }
        return null;
    }

    public List<ProfileItem> readProfiles(Context context) {
        Realm realm = init(context);
        File realmFile = new File(context.getFilesDir(), "profiles.realm");
        try {
            assert realm != null;
            RealmResults<ProfileItem> list = realm.where(ProfileItem.class).findAll();
            Log.d(TAG, String.valueOf(realmFile.length()));
            if (list == null)
                return new ArrayList<>();
            return list;
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public void saveProfile(Context context, ProfileItem profile) {

        Realm realm = init(context);
        File realmFile = new File(context.getFilesDir(), "profiles.realm");
        assert realm != null;
        try {
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(profile);
            Log.d(TAG, String.valueOf(realmFile.length()));
            realm.commitTransaction();
        } catch (NullPointerException ignore) {
        }
    }

    public void removeProfile(Context context, String name) {
        Realm realm = init(context);
        File realmFile = new File(context.getFilesDir(), "profiles.realm");
        assert realm != null;
        try {
            ProfileItem profile = realm.where(ProfileItem.class)
                    .equalTo("name", name).findFirst();
            if(profile != null) {
                realm.beginTransaction();
                profile.deleteFromRealm();
                realm.commitTransaction();
            }
            Log.d(TAG, String.valueOf(realmFile.length()));
        } catch (NullPointerException ignore) {
        }
    }

    public void onDestroy(Context context) {
        Realm realm = init(context);
        assert realm != null;
        realm.close();
    }
}
