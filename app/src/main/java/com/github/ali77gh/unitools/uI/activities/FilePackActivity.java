package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ShortIdGenerator;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.uI.adapter.ViewPagerAdapter;
import com.github.ali77gh.unitools.uI.fragments.FilePackNotesFragment;
import com.github.ali77gh.unitools.uI.fragments.FilePackPicsFragment;
import com.github.ali77gh.unitools.uI.fragments.FilePackVoicesFragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackActivity extends AppCompatActivity {

    public static String Path;

    private final int CAMERA_REQUEST_CODE = 0;
    private final int GALLERY_REQUEST_CODE = 1;

    private final int PICS = 0;
    private final int VOICES = 1;
    private final int NOTE = 2;
    private int _currentPage = PICS;//first page

    private FloatingActionButton cfab;
    private FloatingActionButton rfab;
    private FloatingActionButton lfab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_pack);

        Path = getIntent().getStringExtra("path");

        SetupViewPager();
        SetupFabs();
    }

    private void SetupFabs() {

        cfab = findViewById(R.id.center_fab_file_pack_activity);
        rfab = findViewById(R.id.right_fab_file_pack_activity);
        lfab = findViewById(R.id.left_fab_file_pack_activity);

        switch (_currentPage) {
            case PICS:
                cfab.setOnClickListener(view -> OpenCamera());
                lfab.setOnClickListener(view -> OpenGallery());
                rfab.setOnClickListener(view -> ShowMenu());
                break;
            case VOICES:
                cfab.setOnClickListener(view -> {

                });
                lfab.setOnClickListener(view -> {

                });
                rfab.setOnClickListener(view -> {

                });
                break;

            case NOTE:
                cfab.setOnClickListener(view -> {

                });
                lfab.setOnClickListener(view -> {

                });
                rfab.setOnClickListener(view -> {

                });
                break;
        }
    }

    private void OpenCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + ShortIdGenerator.Generate(6));
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
        //todo open dialog for -> 1.rename 2.
    }


    private void SetupViewPager() {

        TabLayout tabLayout = findViewById(R.id.tab_file_pack_activity);
        ViewPager viewPager = findViewById(R.id.viewpager_file_pack_activity);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        FilePackPicsFragment filePackPicsFragment = new FilePackPicsFragment();
        adapter.AddFragment(filePackPicsFragment, getString(R.string.pics));
        adapter.AddFragment(new FilePackVoicesFragment(), getString(R.string.voices));
        adapter.AddFragment(new FilePackNotesFragment(), getString(R.string.note));

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case PICS:
                        cfab.setImageDrawable(getDrawable(R.drawable.storage_camera));
                        cfab.show();
                        lfab.show();
                        rfab.show();
                        _currentPage = PICS;
                        SetupFabs();
                        break;
                    case VOICES:
                        cfab.setImageDrawable(getDrawable(R.drawable.storage_mic));
                        cfab.show();
                        lfab.hide();
                        rfab.show();
                        _currentPage = VOICES;
                        SetupFabs();
                        break;
                    case NOTE:
                        cfab.setImageDrawable(getDrawable(R.drawable.note_save));
                        cfab.show();
                        lfab.hide();
                        rfab.show();
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
            File from = new File(getRealPathFromURI(uri));
            Log.d("uri", uri.getPath());
            File to = new File(Path + File.separator + FilePackProvider.IMAGE_PATH_NAME + File.separator + ShortIdGenerator.Generate(6));
            try {
                copyFileUsingStream(from, to);
            } catch (IOException e) {
                Toast.makeText(this, "error while copy file", Toast.LENGTH_LONG).show();
                throw new RuntimeException("err while copy file -> IOException:" + e.getMessage());
            }
            SetupViewPager();
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //camera
            SetupViewPager();
        }
    }

    public String getRealPathFromURI(Uri uri) {

        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, new String[]{id}, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;


    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {
        try (InputStream is = new FileInputStream(source); OutputStream os = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}
