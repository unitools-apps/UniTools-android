package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.Backup;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;

;

public class BackupDialog extends BaseDialog {

    private int mode = 0;
    private final int MODE_BACKUP = 1;
    private final int MODE_RESTORE = 2;

    public BackupDialog(@NonNull Activity activity){
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_backup);

        RadioGroup radioGroup = findViewById(R.id.radio_group_backup_dialog_mode);

        CheckBox classes = findViewById(R.id.check_backup_dialog_classes);
        CheckBox events = findViewById(R.id.check_backup_dialog_events);
        CheckBox friends = findViewById(R.id.check_backup_dialog_friend);

        CheckBox deleteCurrent = findViewById(R.id.check_home_backup_dialog_delete_current);

        Button cancel = findViewById(R.id.btn_home_backup_dialog_cancel);
        Button done = findViewById(R.id.btn_home_backup_dialog_ok);

        done.setVisibility(View.GONE);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            classes.setVisibility(View.VISIBLE);
            events.setVisibility(View.VISIBLE);
            friends.setVisibility(View.VISIBLE);
            done.setVisibility(View.VISIBLE);

            switch (checkedId){
                case R.id.radio_backup_dialog_backup:
                    done.setText(R.string.backup);
                    mode = MODE_BACKUP;
                    deleteCurrent.setVisibility(View.GONE);
                    break;
                case R.id.radio_backup_dialog_restore:
                    done.setText(R.string.restore);
                    mode = MODE_RESTORE;
                    deleteCurrent.setVisibility(View.VISIBLE);
                    break;
                default:
                    throw new RuntimeException("invalid state");
            }
        });

        cancel.setOnClickListener(v -> dismiss());

        done.setOnClickListener(v -> {

            if (!classes.isChecked() && !events.isChecked() && !friends.isChecked()){
                CH.toast(R.string.select_something);
                return;
            }

            switch (mode){
                case MODE_BACKUP:
                    if (!Backup.IsBackupPossible()){
                        CH.toast(R.string.nothing_to_take_backup);
                        return;
                    }
                    Backup.DoBackup(classes.isChecked(), events.isChecked(), friends.isChecked());
                    CH.toast(getActivity().getString(R.string.backup_saved_in) + " : " + Backup.backupFilePath);
                    MyDataBeen.onBackup();
                    break;
                case  MODE_RESTORE:
                    if (Backup.IsRestorePossible()){
                        if (deleteCurrent.isChecked()){
                            Backup.ClearCurrentAndRestore(classes.isChecked(), events.isChecked(), friends.isChecked());
                            CH.toast(R.string.successfully_done);
                        }

                        else{
                            Backup.Restore(classes.isChecked(), events.isChecked(), friends.isChecked());
                            CH.toast(R.string.successfully_done);
                        }

                    } else {
                        CH.toast(getActivity().getString(R.string.put_backup_file_in) + " : " + Backup.backupFilePath);
                    }
                    MyDataBeen.onRestore();
                    break;
                default:
                    throw new RuntimeException("invalid state");
            }
        });
    }
}
