package com.github.ali77gh.unitools.uI.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.Tools;
import com.itextpdf.text.pdf.PdfReader;

import java.io.File;
import java.io.IOException;

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

        ViewGroup cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_storag_pack_pdf, null);
        File pdf = files[i];

        TextView name = cview.findViewById(R.id.text_item_storage_pdf_name);
        TextView pages = cview.findViewById(R.id.text_item_storage_pdf_pages);

        name.setText(pdf.getName().substring(0, pdf.getName().indexOf("."))); //todo remove .pdf
        new PDFPageCounter(pdf, pages).start();

        //last item
        if (i == files.length - 1) {
            cview.setPadding(0, 0, 0, Tools.DpToPixel(90));
        }

        return cview;
    }

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

            try {
                PdfReader reader = new PdfReader(file.getPath());
                String count = String.valueOf(reader.getNumberOfPages()) + " " + _activity.getString(R.string.pages);
                textView.post(() -> {
                    textView.setText(count);
                });
            } catch (IOException e) {
                textView.post(() -> {
                    textView.setText("unknown");
                });
            }
        }
    }
}
