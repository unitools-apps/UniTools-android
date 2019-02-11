package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.activities.FilePackActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.IMAGE_PATH_NAME;

public class ExportPdfDialog extends BaseDialog {

    private ExportPdfDialogListener listener;
    private LinearLayout list;
    private File[] images;

    public ExportPdfDialog(@NonNull Activity activity, ExportPdfDialogListener listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_export_pdf);


        list = findViewById(R.id.linear_export_pdf_dialog_list);
        EditText name = findViewById(R.id.text_export_pdf_dialog_name);
        Button cancel = findViewById(R.id.btn_export_pdf_dialog_cancel);
        Button export = findViewById(R.id.btn_export_pdf_dialog_export);

        InitList();

        cancel.setOnClickListener(v -> dismiss());

        export.setOnClickListener(v -> {

            File[] selected = getSelectedFiles();

            if (selected.length == 0) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.select_somethings), Toast.LENGTH_SHORT).show();
                return;
            }

            if (name.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                return;
            }

            listener.onSelect(name.getText().toString(), selected);
            dismiss();
        });
    }

    private void InitList() {
        images = new File(FilePackActivity.Path + File.separator + IMAGE_PATH_NAME).listFiles();

        for (File f : images) {
            CheckBox checkBox = new CheckBox(getActivity());
            checkBox.setText(f.getName());
            checkBox.setChecked(true);
            list.addView(checkBox);
        }
    }

    private File[] getSelectedFiles() {
        List<File> selected = new ArrayList<>();

        for (int i = 0; i <= list.getChildCount() - 1; i++) {
            if (((CheckBox) list.getChildAt(i)).isChecked()) selected.add(images[i]);
        }

        return selected.toArray(new File[selected.size()]);
    }


    public interface ExportPdfDialogListener {
        void onSelect(String name, File[] images);
    }
}
