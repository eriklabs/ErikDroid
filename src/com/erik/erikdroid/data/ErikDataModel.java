package com.erik.erikdroid.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class ErikDataModel implements Serializable {

    private static final String IMAGE_SUFFIX = ".jpg";

    private static final long serialVersionUID = -680242904022731669L;

    public int ID;
    public String Title;
    public String MainImage;
    public String[] Images;
    public String Site;
    public String __type;

    public String[] getAvailableImages() {
        List<String> availableImages = new ArrayList<String>();
        for (String s : Images) {
            if (s != null && !TextUtils.isEmpty(s) && s.endsWith(IMAGE_SUFFIX)) {
                availableImages.add(s);
            }
        }
        return availableImages.toArray(new String[availableImages.size()]);
    }

}
