package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.audio.VoiceRecorder;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.adapter.ViewPagerAdapter;
import com.github.ali77gh.unitools.uI.dialogs.EditDocDialog;
import com.github.ali77gh.unitools.uI.fragments.FilePackNotesFragment;
import com.github.ali77gh.unitools.uI.fragments.FilePackPicsFragment;
import com.github.ali77gh.unitools.uI.fragments.FilePackVoicesFragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Locale;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackActivity extends AppCompatActivity {

    public static String Path;
    public String docName;
    public static VoiceRecorder _voiceRecorder = new VoiceRecorder();

    private FilePackVoicesFragment filePackVoicesFragment;
    private FilePackNotesFragment filePackNoteFragment;

    private final int CAMERA_REQUEST_CODE = 0;
    private final int GALLERY_REQUEST_CODE = 1;

    private final int PICS = 0;
    private final int VOICES = 1;
    private final int NOTE = 2;
    private int _currentPage = PICS;//first page

    private FloatingActionButton cFab;
    private FloatingActionButton rFab;
    private FloatingActionButton lFab;

    private FrameLayout zoomableParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ContextHolder.initStatics(this);
        setContentView(R.layout.activity_file_pack);

        zoomableParent = findViewById(R.id.zoomview_file_pack_activity);

        Path = getIntent().getStringExtra("path");
        docName = getIntent().getStringExtra("docName");

        SetupViewPager();
        SetupFabs();
    }

    private void SetupFabs() {

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
                    if (_voiceRecorder.isRecording()) {
                        _voiceRecorder.Stop();
                        filePackVoicesFragment.RefreshList();
                        cFab.setImageDrawable(getDrawable(R.drawable.storage_mic));
                    } else {
                        _voiceRecorder.Record(Path + File.separator + FilePackProvider.VOICE_PATH_NAME + File.separator + FilePackProvider.getMaxVoiceCode(Path) + ".mp3");
                        cFab.setImageDrawable(getDrawable(R.drawable.storage_voices_pause));
                    }
                });
                lFab.setOnClickListener(view -> {
                    //dont delete this
                });
                break;

            case NOTE:
                cFab.setOnClickListener(view -> {
                    filePackNoteFragment.Save();
                });
                lFab.setOnClickListener(view -> {
                    //dont delete this
                });
                break;
        }
    }

    private void OpenCamera() {

        //to fix android 7+ crash
        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + FilePackProvider.getMaxPicCode(Path) + ".bmp");
        Uri imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void OpenGallery() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    private void ShowMenu() {
        EditDocDialog editDocDialog = new EditDocDialog(this, docName);
        editDocDialog.show();
        editDocDialog.setOnDismissListener(dialogInterface -> finish());
    }

    private void SetupViewPager() {

        TabLayout tabLayout = findViewById(R.id.tab_file_pack_activity);
        ViewPager viewPager = findViewById(R.id.viewpager_file_pack_activity);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FilePackPicsFragment filePackPicsFragment = new FilePackPicsFragment();

        filePackPicsFragment.setOnZoomableRequest(path -> ShowZoomable(path));

        filePackVoicesFragment = new FilePackVoicesFragment();
        filePackNoteFragment = new FilePackNotesFragment();

        adapter.AddFragment(filePackPicsFragment, getString(R.string.pics));
        adapter.AddFragment(filePackVoicesFragment, getString(R.string.voices));
        adapter.AddFragment(filePackNoteFragment, getString(R.string.note));

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case PICS:
                        cFab.setImageDrawable(getDrawable(R.drawable.storage_camera));
                        cFab.show();
                        lFab.show();
                        rFab.show();
                        _currentPage = PICS;
                        SetupFabs();
                        break;
                    case VOICES:
                        if (_voiceRecorder.isRecording())
                            cFab.setImageDrawable(getDrawable(R.drawable.storage_voices_pause));
                        else
                            cFab.setImageDrawable(getDrawable(R.drawable.storage_mic));
                        cFab.show();
                        lFab.hide();
                        rFab.show();
                        _currentPage = VOICES;
                        SetupFabs();
                        break;
                    case NOTE:
                        cFab.setImageDrawable(getDrawable(R.drawable.note_save));
                        cFab.show();
                        lFab.hide();
                        rFab.show();
                        _currentPage = NOTE;
                        SetupFabs();
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
        tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LOCALE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            //gallery
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                File file = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + FilePackProvider.getMaxPicCode(Path) + ".bmp");
                OutputStream os = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SetupViewPager();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //camera
            SetupViewPager();
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

        imageZomable.setImage(ImageSource.uri(path));

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
