package com.github.ali77gh.unitools.ui.activities;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.audio.VoiceRecorder;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.core.pdf.PDFGen;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.ui.adapter.ViewPagerAdapter;
import com.github.ali77gh.unitools.ui.dialogs.EditDocDialog;
import com.github.ali77gh.unitools.ui.dialogs.ExportPdfDialog;
import com.github.ali77gh.unitools.ui.fragments.FilePackNotesFragment;
import com.github.ali77gh.unitools.ui.fragments.FilePackPdfFragment;
import com.github.ali77gh.unitools.ui.fragments.FilePackPicsFragment;
import com.github.ali77gh.unitools.ui.fragments.FilePackVoicesFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.IMAGE_PATH_NAME;
import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.PDF_PATH_NAME;

;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackActivity extends AppCompatActivity {

    public static String Path;
    public String docName;

    private FilePackPicsFragment filePackPicsFragment;
    private FilePackVoicesFragment filePackVoicesFragment;
    private FilePackNotesFragment filePackNoteFragment;
    private FilePackPdfFragment filePackPdfFragment;

    private final int CAMERA_REQUEST_CODE = 0;
    private final int GALLERY_REQUEST_CODE = 1;
    private final int PDF_REQUEST_CODE = 2;

    private final int PICS = 0;
    private final int VOICES = 1;
    private final int NOTE = 2;
    private final int PDF = 3;
    private int _currentPage = PICS; // first page

    private FloatingActionButton cFab;
    private FloatingActionButton rFab;
    private FloatingActionButton lFab;

    private FrameLayout zoomableParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CH.initStatics(this);
        setContentView(R.layout.activity_file_pack);

        zoomableParent = findViewById(R.id.zoomview_file_pack_activity);

        Path = getIntent().getStringExtra("path");
        docName = getIntent().getStringExtra("docName");

        SetupViewPager();
        SetupFabsClicks();
    }

    private void SetupFabsClicks() {

        cFab = findViewById(R.id.center_fab_file_pack_activity);
        rFab = findViewById(R.id.right_fab_file_pack_activity);
        lFab = findViewById(R.id.left_fab_file_pack_activity);

        rFab.setOnClickListener(view -> ShowMenu()); //same for all pages

        switch (_currentPage) {
            case PICS:
                cFab.setOnClickListener(view -> OpenCamera());
                lFab.setOnClickListener(view -> OpenGallery());
                break;

            case VOICES:
                cFab.setOnClickListener(view -> {
                    if (VoiceRecorder.isRecording()) {
                        VoiceRecorder.Stop();
                        filePackVoicesFragment.RefreshList();
                        cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_mic));
                    } else {
                        VoiceRecorder.Record(Path + File.separator + FilePackProvider.VOICE_PATH_NAME + File.separator + FilePackProvider.getMaxVoiceCode(Path) + ".mp3");
                        filePackVoicesFragment.RefreshList();
                        cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_voices_pause));
                    }
                });
                break;

            case NOTE:
                cFab.setOnClickListener(view -> {
                    filePackNoteFragment.Save();
                });
                break;

            case PDF:
                cFab.setOnClickListener(v -> {
                    ImportPdf();
                });
                lFab.setOnClickListener(v -> {

                    if (new File(FilePackActivity.Path + File.separator + IMAGE_PATH_NAME).listFiles().length == 0) {
                        CH.toast(R.string.first_take_some_picture);
                        return;
                    }

                    new ExportPdfDialog(this, (name, images) -> {
                        GeneratePdf(images, name);
                    }).show();
                });
                break;

        }
    }

    private void OpenCamera() {

        //to fix android 7+ crash
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + FilePackProvider.getMaxPicCode(Path) + ".jpg");
        Uri imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void OpenGallery() {
        Intent intent = new Intent();
        // Update only images, no videos or anything else
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    private void ImportPdf() {
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("application/pdf");

        startActivityForResult(intent, PDF_REQUEST_CODE);

    }

    private void GeneratePdf(File[] images, String name) {
        File pdfPath = new File(FilePackActivity.Path + File.separator + PDF_PATH_NAME + File.separator + name + ".pdf");

        CH.toast(R.string.start_generate_pdf);
        PDFGen.Generate(images, pdfPath, new Promise() {
            @Override
            public void onFailed(String msg) {
                runOnUiThread(() -> CH.toast(R.string.pdf_generate_failed));
            }

            @Override
            public void onSuccess(Object ignore) {
                runOnUiThread(() -> {
                    CH.toast(R.string.pdf_saved_to_pdf_list);
                    filePackPdfFragment.RefreshList();
                });
            }
        });
    }

    private void ShowMenu() {
        new EditDocDialog(this, docName, () -> finish()).show();
    }

    private void SetupViewPager() {

        TabLayout tabLayout = findViewById(R.id.tab_file_pack_activity);
        ViewPager viewPager = findViewById(R.id.viewpager_file_pack_activity);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filePackPicsFragment = new FilePackPicsFragment();
        filePackVoicesFragment = new FilePackVoicesFragment();
        filePackNoteFragment = new FilePackNotesFragment();
        filePackPdfFragment = new FilePackPdfFragment();

        filePackPicsFragment.setOnZoomableRequest(path -> ShowZoomable(path));

        adapter.AddFragment(filePackPicsFragment, getString(R.string.pics));
        adapter.AddFragment(filePackVoicesFragment, getString(R.string.voices));
        adapter.AddFragment(filePackNoteFragment, getString(R.string.note));
        adapter.AddFragment(filePackPdfFragment, getString(R.string.pdf));

        //get stop\start recording event to change fab icon and refresh list
        VoiceRecorder.setCallback(recording -> {
            if (_currentPage == VOICES) {
                if (recording)
                    cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_voices_pause));
                else
                    cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_mic));
            }
            filePackVoicesFragment.RefreshList(); //list should refresh anyway
        });

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case PICS:
                        cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_camera));
                        lFab.setImageDrawable(getResources().getDrawable(R.drawable.file_pack_gallery));
                        lFab.hide();//you can remove this when update to next support library (current 28.0.0) (support library bug)
                        lFab.show();
                        _currentPage = PICS;
                        SetupFabsClicks();
                        break;
                    case VOICES:
                        if (VoiceRecorder.isRecording())
                            cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_voices_pause));
                        else
                            cFab.setImageDrawable(getResources().getDrawable(R.drawable.storage_mic));
                        lFab.hide();
                        _currentPage = VOICES;
                        SetupFabsClicks();
                        break;
                    case NOTE:
                        cFab.setImageDrawable(getResources().getDrawable(R.drawable.note_save));
                        lFab.hide();
                        _currentPage = NOTE;
                        SetupFabsClicks();
                        break;
                    case PDF:
                        cFab.setImageDrawable(getResources().getDrawable(R.drawable.filepack_pdf_import));
                        lFab.setImageDrawable(getResources().getDrawable(R.drawable.fliepack_pdf_generate));
                        lFab.hide();//you can remove this when update to next support library (current 28.0.0) (support library bug)
                        lFab.show();
                        _currentPage = PDF;
                        SetupFabsClicks();
                        break;
                    default:
                        throw new RuntimeException("invalid tab");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case GALLERY_REQUEST_CODE:
                MyDataBeen.onNewPhoto();
                if (data == null) return;

                if (data.getClipData() != null) {

                    // handle multiple photo
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri from = clipData.getItemAt(i).getUri();
                        File to = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + FilePackProvider.getMaxPicCode(Path) + ".jpg");
                        CopyFileUsingStream(from, to);
                    }
                } else if(data.getData() != null) {

                    // handle single photo
                    Uri from = data.getData();
                    File to = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + FilePackProvider.getMaxPicCode(Path) + ".jpg");
                    CopyFileUsingStream(from, to);
                } else {
                    CH.toast("can't import file :(");
                }

                filePackPicsFragment.RefreshList();
                break;
            case CAMERA_REQUEST_CODE:
                MyDataBeen.onNewPhoto();
                filePackPicsFragment.RefreshList();
                break;
            case PDF_REQUEST_CODE:
                MyDataBeen.onNewPdfImport();
                if (data == null || data.getData() == null) return;
                Uri fromPdf = data.getData();
                File toPdf = new File(Path + File.separator + FilePackProvider.PDF_PATH_NAME + File.separator + getFileName(fromPdf)); // already have .pdf
                CopyFileUsingStream(fromPdf, toPdf);
                filePackPdfFragment.RefreshList();
                break;
            default:
                throw new RuntimeException("invalid code");
        }
    }

    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst())
            return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        else return "unnamed";
    }

    private void CopyFileUsingStream(Uri source, File dest) {
        InputStream is;
        OutputStream os;
        try {
            is = getContentResolver().openInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[2048];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            Log.d("self", e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetupLang();
    }

    private void SetupLang() {

        String lang = UserInfoRepo.getUserInfo().LangId;

        if (lang.equals(getString(R.string.LangID))) return;

        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        recreate();
    }

    private void ShowZoomable(String path) {


        SubsamplingScaleImageView imageZomable = (SubsamplingScaleImageView) zoomableParent.getChildAt(1);
        ImageView back = (ImageView) zoomableParent.getChildAt(2);
        ImageView rotateRight = (ImageView) zoomableParent.getChildAt(3);
        ImageView rotateLeft = (ImageView) zoomableParent.getChildAt(4);

        imageZomable.setImage(ImageSource.uri(path));

        rotateRight.setOnClickListener(v ->
                imageZomable.setRotation(imageZomable.getRotation() + 90)
        );

        rotateLeft.setOnClickListener(v ->
                imageZomable.setRotation(imageZomable.getRotation() - 90)
        );

        zoomableParent.setVisibility(View.VISIBLE);

        back.setOnClickListener(view -> {
            zoomableParent.setVisibility(View.GONE);
            imageZomable.recycle(); // this will releases all resources the view is using
        });
    }

    @Override
    public void onBackPressed() {
        if (zoomableParent.getVisibility() == View.VISIBLE) {
            zoomableParent.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
