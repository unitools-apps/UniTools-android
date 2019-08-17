package com.github.ali77gh.unitools.ui.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.model.FilePack;
import com.github.ali77gh.unitools.ui.adapter.StoragePacksListViewAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.PDF_PATH_NAME;

;

public class InputPdfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_file);
        ListView listView = findViewById(R.id.list_input_file);
        CH.initStatics(this);

        Uri from = getIntent().getData();

        List<FilePack> filePacks = FilePackProvider.getFilePacks();
        if (filePacks != null) {
            listView.setAdapter(new StoragePacksListViewAdapter(this, filePacks));
        }
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String to = FilePackProvider.getPathOfPack(filePacks.get(position).getName() + File.separator + PDF_PATH_NAME + File.separator + getNameOfUriFile(from));
            try {
                CopyFileUsingStream(from, new File(to));
                CH.toast(R.string.successfully_done);

                finish();
            } catch (IOException e) {
                CH.toast(R.string.pdf_copy_failed);
            }

        });

        listView.addFooterView(getLayoutInflater().inflate(R.layout.layout_list_footer, null));
        listView.setEmptyView(findViewById(R.id.text_storage_nothing_to_show));
    }

    private String getNameOfUriFile(Uri uri){
        String parts[] = uri.toString().split("/");
        return parts[parts.length - 1];
    }

    private void CopyFileUsingStream(Uri source, File dest) throws IOException {
        InputStream is;
        OutputStream os;

        is = getContentResolver().openInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[2048];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }
}
