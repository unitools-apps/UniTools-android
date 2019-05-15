package com.github.ali77gh.unitools.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.ui.Tools;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class StoragePackPdfListViewAdapter extends BaseAdapter {

    private Activity _activity;
    private File[] files;

    public StoragePackPdfListViewAdapter(Activity activity, File[] filePacks) {

        this._activity = activity;
        this.files = filePacks;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewGroup cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_file_pack_pdf, null);
        File pdf = files[i];

        TextView name = cview.findViewById(R.id.text_item_storage_pdf_name);
        TextView pages = cview.findViewById(R.id.text_item_storage_pdf_pages);

        name.setText(pdf.getName().substring(0, pdf.getName().indexOf(".")));
        new PDFPageCounter(pdf, pages).start();

        //last item
        if (i == files.length - 1) {
            cview.setPadding(0, 0, 0, Tools.DpToPixel(90));
        }

        return cview;
    }

    private static HashMap<String, String> cache = new HashMap<>();
    private class PDFPageCounter extends Thread {

        private File file;
        private TextView textView;

        PDFPageCounter(File file, TextView textView) {
            this.file = file;
            this.textView = textView;
        }

        @Override
        public void run() {
            super.run();
            if (cache.containsKey(file.getName())) {
                textView.post(() -> textView.setText(cache.get(file.getName())));
                return;
            }

            try {
                PdfReader reader = new PdfReader(file.getPath());
                int pages = reader.getNumberOfPages();
                textView.post(() -> {
                    String count = String.valueOf(pages) + " " + CH.getString(R.string.pages);
                    textView.setText(count);
                    cache.put(file.getName(), count);
                });
            } catch (Exception e) {
                textView.post(() -> {
                    textView.setText("unknown");
                });
            }
        }
    }
}
