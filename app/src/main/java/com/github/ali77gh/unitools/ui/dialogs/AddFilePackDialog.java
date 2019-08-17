package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UClassRepo;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by ali77gh on 12/11/18.
 */

public class AddFilePackDialog extends BaseDialog {

    private final Promise<String> listener;

    public AddFilePackDialog(Activity activity, Promise<String> listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_file_pack);

        AutoCompleteTextView input = findViewById(R.id.text_add_file_pack_dialog_input);
        Button done = findViewById(R.id.btn_add_file_pack_dialog_add_all);
        Button cancel = findViewById(R.id.btn_add_file_pack_dialog_cancel);

        List<String> autoCompleteValues = getNamesOfClasses(UClassRepo.getAll());
        List<String> filePacks = FilePackProvider.getFilePacksNames();

        for (String filePackName : filePacks) {
            if (autoCompleteValues.indexOf(filePackName) != -1) {
                autoCompleteValues.remove(filePackName);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, autoCompleteValues);
        input.setAdapter(adapter);

        done.setEnabled(false);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString().equals("")) {
                    done.setEnabled(false);
                } else {
                    done.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancel.setOnClickListener(v -> dismiss());

        done.setOnClickListener(v -> {

            if (filePacks.indexOf(input.getText().toString()) != -1) {
                //exist
                CH.toast(R.string.exists);
                return;
            }

            listener.onSuccess(input.getText().toString());

            MyDataBeen.onNewDoc();
            dismiss();
        });
    }

    private List<String> getNamesOfClasses(List<UClass> uClasses) {
        List<String> names = new ArrayList<>();
        for (UClass uClass : uClasses)
            names.add(uClass.what);
        return names;
    }

    public interface OnDoneClick {
        void onDone(String[] input);
    }
}
