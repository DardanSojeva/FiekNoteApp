package fiek.ds.android.fieknote.factory;

import android.provider.MediaStore;

import android.net.Uri;

public class MediaStoreFactory {
    public Uri createURI(String type){
        switch (type) {
            case "image":
                return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            case "video":
                return  MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            case "audio":
                return  MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        return null;
    }
}
