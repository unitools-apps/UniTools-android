package com.github.ali77gh.unitools.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.ui.adapter.StoragePackPdfListViewAdapter;
import com.github.ali77gh.unitools.ui.dialogs.FileActionDialog;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLConnection;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.PDF_PATH_NAME;
import static com.github.ali77gh.unitools.ui.activities.FilePackActivity.Path;

public class FilePackPdfFragment extends Fragment {

    private ListView listView;

    public FilePackPdfFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View cView = inflater.inflate(R.layout.fragment_filepack_pdf, null);

        listView = cView.findViewById(R.id.list_file_pack_pdf);
        listView.setEmptyView(cView.findViewById(R.id.text_file_pack_empty));
        RefreshList();

        return cView;
    }

    public void RefreshList() {
        File[] pdfs = new File(Path + File.separator + PDF_PATH_NAME).listFiles();


        listView.setAdapter(new StoragePackPdfListViewAdapter(getActivity(), pdfs));

        listView.setOnItemClickListener((parent, view, position, id) -> {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            File file = new File(pdfs[position].getPath());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            new FileActionDialog(getActivity(), pdfs[position], new FileActionDialog.FileActionDialogListener() {
                @Override
                public void onDelete() {
                    pdfs[position].delete();
                    RefreshList();
                }

                @Override
                public void onShare() {
                    shareFile(pdfs[position]);
                    RefreshList();
                }
            }).show();
            return true;
        });
    }

    private void shareFile(File file) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://" + file.getAbsolutePath()));

        startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }
}
