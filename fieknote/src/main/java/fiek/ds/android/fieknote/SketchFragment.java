

package fiek.ds.android.fieknote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.afollestad.materialdialogs.MaterialDialog;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import fiek.ds.android.fieknote.models.ONStyle;
import fiek.ds.android.fieknote.models.listeners.OnDrawChangedListener;
import fiek.ds.android.fieknote.models.views.SketchView;
import fiek.ds.android.fieknote.utils.Constants;
import it.feio.android.checklistview.utils.AlphaManager;


public class SketchFragment extends Fragment implements OnDrawChangedListener {

    @Bind(fiek.ds.android.fieknote.R.id.sketch_stroke) ImageView stroke;
    @Bind(fiek.ds.android.fieknote.R.id.sketch_eraser) ImageView eraser;
    @Bind(fiek.ds.android.fieknote.R.id.drawing)
    SketchView mSketchView;
    @Bind(fiek.ds.android.fieknote.R.id.sketch_undo) ImageView undo;
    @Bind(fiek.ds.android.fieknote.R.id.sketch_redo) ImageView redo;
    @Bind(fiek.ds.android.fieknote.R.id.sketch_erase) ImageView erase;
    private int seekBarStrokeProgress, seekBarEraserProgress;
    private View popupLayout, popupEraserLayout;
    private ImageView strokeImageView, eraserImageView;
    private int size;
    private ColorPicker mColorPicker;
    private int oldColor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(false);
    }


    @Override
    public void onStart() {
		((FiekNote)getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());

		super.onStart();
    }


    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(fiek.ds.android.fieknote.R.layout.fragment_sketch, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getMainActivity().getToolbar().setNavigationOnClickListener(v -> getActivity().onBackPressed());

        mSketchView.setOnDrawChangedListener(this);

        Uri baseUri = getArguments().getParcelable("base");
        if (baseUri != null) {
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(baseUri));
                mSketchView.setBackgroundBitmap(getActivity(), bmp);
            } catch (FileNotFoundException e) {
                Log.e(Constants.TAG, "Error replacing sketch bitmap background", e);
            }
        }

        // Show the Up button in the action bar.
        if (getMainActivity().getSupportActionBar() != null) {
            getMainActivity().getSupportActionBar().setDisplayShowTitleEnabled(true);
            getMainActivity().getSupportActionBar().setTitle(fiek.ds.android.fieknote.R.string.title_activity_sketch);
            getMainActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        stroke.setOnClickListener(v -> {
            if (mSketchView.getMode() == SketchView.STROKE) {
                showPopup(v, SketchView.STROKE);
            } else {
                mSketchView.setMode(SketchView.STROKE);
                AlphaManager.setAlpha(eraser, 0.4f);
                AlphaManager.setAlpha(stroke, 1f);
            }
        });

        AlphaManager.setAlpha(eraser, 0.4f);
        eraser.setOnClickListener(v -> {
			if (mSketchView.getMode() == SketchView.ERASER) {
				showPopup(v, SketchView.ERASER);
			} else {
				mSketchView.setMode(SketchView.ERASER);
				AlphaManager.setAlpha(stroke, 0.4f);
				AlphaManager.setAlpha(eraser, 1f);
			}
		});

        undo.setOnClickListener(v -> mSketchView.undo());

        redo.setOnClickListener(v -> mSketchView.redo());

        erase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                askForErase();
            }

			private void askForErase() {
				new MaterialDialog.Builder(getActivity())
						.content(fiek.ds.android.fieknote.R.string.erase_sketch)
						.positiveText(fiek.ds.android.fieknote.R.string.confirm)
						.callback(new MaterialDialog.ButtonCallback() {
							@Override
							public void onPositive(MaterialDialog dialog) {
								mSketchView.erase();
							}
						})
						.build().show();
			}
		});


        // Inflate the popup_layout.xml
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(ActionBarActivity
                .LAYOUT_INFLATER_SERVICE);
        popupLayout = inflater.inflate(fiek.ds.android.fieknote.R.layout.popup_sketch_stroke, null);
        // And the one for eraser
        LayoutInflater inflaterEraser = (LayoutInflater) getActivity().getSystemService(ActionBarActivity
                .LAYOUT_INFLATER_SERVICE);
        popupEraserLayout = inflaterEraser.inflate(fiek.ds.android.fieknote.R.layout.popup_sketch_eraser, null);

        // Actual stroke shape size is retrieved
        strokeImageView = (ImageView) popupLayout.findViewById(fiek.ds.android.fieknote.R.id.stroke_circle);
        final Drawable circleDrawable = getResources().getDrawable(fiek.ds.android.fieknote.R.drawable.circle);
        size = circleDrawable.getIntrinsicWidth();
        // Actual eraser shape size is retrieved
        eraserImageView = (ImageView) popupEraserLayout.findViewById(fiek.ds.android.fieknote.R.id.stroke_circle);
        size = circleDrawable.getIntrinsicWidth();

        setSeekbarProgress(SketchView.DEFAULT_STROKE_SIZE, SketchView.STROKE);
        setSeekbarProgress(SketchView.DEFAULT_ERASER_SIZE, SketchView.ERASER);

        // Stroke color picker initialization and event managing
        mColorPicker = (ColorPicker) popupLayout.findViewById(fiek.ds.android.fieknote.R.id.stroke_color_picker);
        mColorPicker.addSVBar((SVBar) popupLayout.findViewById(fiek.ds.android.fieknote.R.id.svbar));
        mColorPicker.addOpacityBar((OpacityBar) popupLayout.findViewById(fiek.ds.android.fieknote.R.id.opacitybar));
        mColorPicker.setOnColorChangedListener(mSketchView::setStrokeColor);
        mColorPicker.setColor(mSketchView.getStrokeColor());
        mColorPicker.setOldCenterColor(mSketchView.getStrokeColor());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void save() {
        Bitmap bitmap = mSketchView.getBitmap();
        if (bitmap != null) {

            try {
                Uri uri = getArguments().getParcelable(MediaStore.EXTRA_OUTPUT);
                File bitmapFile = new File(uri.getPath());
                FileOutputStream out = new FileOutputStream(bitmapFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
                if (bitmapFile.exists()) {
                    getMainActivity().sketchUri = uri;
                } else {
                    getMainActivity().showMessage(fiek.ds.android.fieknote.R.string.error, ONStyle.ALERT);
                }

            } catch (Exception e) {
                Log.e(Constants.TAG, "Error writing sketch image data", e);
            }
        }
    }


    // The method that displays the popup.
    private void showPopup(View anchor, final int eraserOrStroke) {

        boolean isErasing = eraserOrStroke == SketchView.ERASER;

        oldColor = mColorPicker.getColor();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Creating the PopupWindow
        PopupWindow popup = new PopupWindow(getActivity());
        popup.setContentView(isErasing ? popupEraserLayout : popupLayout);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setFocusable(true);
        popup.setOnDismissListener(() -> {
			if (mColorPicker.getColor() != oldColor)
				mColorPicker.setOldCenterColor(oldColor);
		});

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets (transformed 
        // dp to pixel to support multiple screen sizes)
        popup.showAsDropDown(anchor);

        // Stroke size seekbar initialization and event managing
        SeekBar mSeekBar;
        mSeekBar = (SeekBar) (isErasing ? popupEraserLayout
                .findViewById(fiek.ds.android.fieknote.R.id.stroke_seekbar) : popupLayout
                .findViewById(fiek.ds.android.fieknote.R.id.stroke_seekbar));
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // When the seekbar is moved a new size is calculated and the new shape
                // is positioned centrally into the ImageView
                setSeekbarProgress(progress, eraserOrStroke);
            }
        });
        int progress = isErasing ? seekBarEraserProgress : seekBarStrokeProgress;
        mSeekBar.setProgress(progress);
    }


    protected void setSeekbarProgress(int progress, int eraserOrStroke) {
        int calcProgress = progress > 1 ? progress : 1;

        int newSize = Math.round((size / 100f) * calcProgress);
        int offset = Math.round((size - newSize) / 2);
        Log.v(Constants.TAG, "Stroke size " + newSize + " (" + calcProgress + "%)");

        LayoutParams lp = new LayoutParams(newSize, newSize);
        lp.setMargins(offset, offset, offset, offset);
        if (eraserOrStroke == SketchView.STROKE) {
            strokeImageView.setLayoutParams(lp);
            seekBarStrokeProgress = progress;
        } else {
            eraserImageView.setLayoutParams(lp);
            seekBarEraserProgress = progress;
        }

        mSketchView.setSize(newSize, eraserOrStroke);
    }


    @Override
    public void onDrawChanged() {
        // Undo
        if (mSketchView.getPaths().size() > 0)
            AlphaManager.setAlpha(undo, 1f);
        else
            AlphaManager.setAlpha(undo, 0.4f);
        // Redo
        if (mSketchView.getUndoneCount() > 0)
            AlphaManager.setAlpha(redo, 1f);
        else
            AlphaManager.setAlpha(redo, 0.4f);
    }


    private MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }


}
