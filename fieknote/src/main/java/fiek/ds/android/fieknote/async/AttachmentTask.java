

package fiek.ds.android.fieknote.async;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.models.Attachment;
import fiek.ds.android.fieknote.models.listeners.OnAttachingFileListener;
import fiek.ds.android.fieknote.utils.StorageHelper;


public class AttachmentTask extends AsyncTask<Void, Void, Attachment> {

    private final WeakReference<Fragment> mFragmentWeakReference;
    private OnAttachingFileListener mOnAttachingFileListener;
    private Uri uri;
    private String fileName;


    public AttachmentTask(Fragment mFragment, Uri uri, OnAttachingFileListener mOnAttachingFileListener) {
        this(mFragment, uri, null, mOnAttachingFileListener);
    }


    public AttachmentTask(Fragment mFragment, Uri uri, String fileName,
                          OnAttachingFileListener mOnAttachingFileListener) {
        mFragmentWeakReference = new WeakReference<>(mFragment);
        this.uri = uri;
        this.fileName = TextUtils.isEmpty(fileName) ? "" : fileName;
        this.mOnAttachingFileListener = mOnAttachingFileListener;
    }


    @Override
    protected Attachment doInBackground(Void... params) {
        return StorageHelper.createAttachmentFromUri(FiekNote.getAppContext(), uri);
    }


    @Override
    protected void onPostExecute(Attachment mAttachment) {
        if (isAlive()) {
            if (mAttachment != null) {
                mOnAttachingFileListener.onAttachingFileFinished(mAttachment);
            } else {
                mOnAttachingFileListener.onAttachingFileErrorOccurred(null);
            }
        } else {
            if (mAttachment != null) {
                StorageHelper.delete(FiekNote.getAppContext(), mAttachment.getUri().getPath());
            }
        }
    }


    private boolean isAlive() {
        return mFragmentWeakReference != null
                && mFragmentWeakReference.get() != null
                && mFragmentWeakReference.get().isAdded()
                && mFragmentWeakReference.get().getActivity() != null
                && !mFragmentWeakReference.get().getActivity().isFinishing();
    }

}