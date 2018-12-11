package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali77gh on 12/11/18.
 */

public class AddFilePackDialog extends Dialog {

    private Activity context;
    private Promise<List<String>> callback;

    public AddFilePackDialog(@NonNull Context context, Promise<List<String>> callback) {
        super(context);
        this.context = (Activity) context;
        this.callback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_file_pack);

        LinearLayout lst = findViewById(R.id.linear_add_file_pack_dialog_lst_parent);
        ImageView add = findViewById(R.id.btn_add_file_pack_dialog_add);
        EditText addText = findViewById(R.id.text_add_file_pack_dialog_add);
        Button cancel = findViewById(R.id.btn_add_file_pack_dialog_cancel);
        Button addAll = findViewById(R.id.btn_add_file_pack_dialog_add_all);


        //todo dont add if exist
        for (UClass uClass : UserInfoRepo.getUserInfo().Classes) {
            ViewGroup item = (ViewGroup) context.getLayoutInflater().inflate(R.layout.item_add_file_pack_dialog, null);
            TextView name = (TextView) item.getChildAt(1);
            name.setText(uClass.what);
            lst.addView(item);
        }

        add.setOnClickListener(view -> {
            if (addText.getVisibility() == View.GONE) {
                addText.setVisibility(View.VISIBLE);
            } else {
                if (addText.getText().toString().equals("")){
                    Toast.makeText(context, context.getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                    return;
                }
                ViewGroup item = (ViewGroup) context.getLayoutInflater().inflate(R.layout.item_add_file_pack_dialog, null);
                TextView name = (TextView) item.getChildAt(1);
                CheckBox checkBox = (CheckBox) item.getChildAt(0);
                checkBox.setChecked(true);
                name.setText(addText.getText().toString());
                lst.addView(item);
                addText.setText("");
                addText.setVisibility(View.GONE);
            }
        });

        addAll.setOnClickListener(view -> {
            List<String> selected = new ArrayList<>();

            for (int i = 0; i < lst.getChildCount(); i++) {
                ViewGroup item = (ViewGroup) lst.getChildAt(i);
                CheckBox checkBox = (CheckBox) item.getChildAt(0);
                if (checkBox.isChecked()) {
                    TextView name = (TextView) item.getChildAt(1);
                    selected.add(name.getText().toString());
                }
            }

            if (selected.size() == 0) {
                Toast.makeText(context, context.getString(R.string.select_some_things), Toast.LENGTH_SHORT).show();
                return;
            }
            callback.onSuccess(selected);
            dismiss();
        });


        cancel.setOnClickListener(view -> dismiss());

    }
}
