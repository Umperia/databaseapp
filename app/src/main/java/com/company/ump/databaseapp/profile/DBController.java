package com.company.ump.databaseapp.profile;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DBController {

    private Context context;
    private ProfileRealm profileRealm;

    public DBController(Context context) {
        this.context = context;
        profileRealm = new ProfileRealm();
    }

    public void saveDB(Profile profile) {
        ProfileItem profileItem = new ProfileItem();
        profileItem.setName(profile.getName());
        profileItem.setEmail(profile.getEmail());
        profileItem.setPhone(profile.getPhone());
        profileItem.setPath(profile.getPath());
        profileRealm.saveProfile(context, profileItem);
    }

    public List<Profile> getAll() {
        List<Profile> profiles = new ArrayList<>();
        for (ProfileItem item : profileRealm.readProfiles(context)) {
            Profile profile = new ProfileItem();
            profile.setName(item.getName());
            profile.setEmail(item.getEmail());
            profile.setPhone(item.getPhone());
            profile.setPath(item.getPath());
            profiles.add(profile);
        }
        return profiles;
    }

    public void remove(String name) {
        profileRealm.removeProfile(context, name);
    }
}
