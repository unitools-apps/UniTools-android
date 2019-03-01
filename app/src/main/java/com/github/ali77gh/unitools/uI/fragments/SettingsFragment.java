package com.github.ali77gh.unitools.uI.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.Backup;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.activities.GuideActivity;
import com.github.ali77gh.unitools.uI.dialogs.BackupDialog;
import com.github.ali77gh.unitools.uI.dialogs.SettingsAlarmConfigDialog;

/**
 * Created by ali on 10/3/18.
 */

public class SettingsFragment extends Fragment implements Backable {

    private Spinner languageSpinner;
    private Spinner calendarSpinner;
    private LinearLayout aboutUsBtn;
    private LinearLayout autoSilentBtn;
    private Switch autoSilentSwitch;
    private FrameLayout aboutUs;
    private BackupDialog backupDialog;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View cView = inflater.inflate(R.layout.fragment_settings, container, false);

        languageSpinner = cView.findViewById(R.id.spinner_settings_language);
        calendarSpinner = cView.findViewById(R.id.spinner_settings_calendar);

        autoSilentBtn = cView.findViewById(R.id.linear_settings_auto_silent);
        autoSilentSwitch = (Switch) autoSilentBtn.getChildAt(3);
        aboutUsBtn = cView.findViewById(R.id.linear_settings_about_us);
        aboutUs = cView.findViewById(R.id.layout_settings_about);
        LinearLayout reminder = cView.findViewById(R.id.linear_settings_reminder);
        LinearLayout guide = cView.findViewById(R.id.linear_settings_guide);
        LinearLayout backup = cView.findViewById(R.id.linear_settings_auto_backup);
//        LinearLayout donateUs = cView.findViewById(R.id.linear_settings_donate_us);
//        LinearLayout sendFeedback = cView.findViewById(R.id.linear_settings_feedback);

        reminder.setOnClickListener(view -> new SettingsAlarmConfigDialog(getActivity()).show());

        autoSilentBtn.setOnClickListener(view -> autoSilentSwitch.toggle());
        autoSilentSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            UserInfo ui = UserInfoRepo.getUserInfo();
            ui.AutoSilent = b;
            UserInfoRepo.setUserInfo(ui);
        });

        guide.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), GuideActivity.class));
            getActivity().overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        });

//        donateUs.setOnClickListener(view -> new DonateUsDialog(getActivity()).show());
//
//        sendFeedback.setOnClickListener(view -> new SendFeedbackDialog(getActivity()).show());

        backup.setOnClickListener(v -> {
            if (CheckFilePermission()){
                backupDialog = new BackupDialog(getActivity());
                backupDialog.show();
            }else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }, 1754);
            }

        });


        SetupLanguageAndCalendar();
        LoadCurrentSettings();
        SetupAboutUs();

        return cView;
    }

    public boolean CheckFilePermission() {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return true;
        }

        boolean storageR = getContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean storageW = getContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;


        if (storageR && storageW) {
            return true;
        }
        return false;
    }

    private void LoadCurrentSettings() {
        UserInfo ui = UserInfoRepo.getUserInfo();

        switch (ui.LangId) {
            case "fa":
                languageSpinner.setSelection(1);
                break;
            case "en":
                languageSpinner.setSelection(0);
                break;
            default:
                throw new IllegalArgumentException("invalid language: " + ui.LangId);
        }

        switch (ui.Calendar) {
            case 'g': // Gregorian
                calendarSpinner.setSelection(0);
                break;
            case 'j': // Jalali
                calendarSpinner.setSelection(1);
                break;
            default: // default
                SetCalendar('j');
                calendarSpinner.setSelection(1);
        }

        autoSilentSwitch.setChecked(ui.AutoSilent);
    }

    private void SetupLanguageAndCalendar() {

        //load lang
        String langs[] = getResources().getStringArray(R.array.languages);
        languageSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, langs));

        //load calendar
        String calendars[] = getResources().getStringArray(R.array.calendar);
        calendarSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_spinner, calendars));

        //setup on prent click
        ((View) languageSpinner.getParent()).setOnClickListener(view -> languageSpinner.performClick());
        ((View) calendarSpinner.getParent()).setOnClickListener(view -> calendarSpinner.performClick());

        // on settings changed
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view == null) return;
                switch (((TextView) view).getText().toString()) {
                    case "فارسی":
                        SetLang("fa");
                        break;
                    case "english":
                        SetLang("en");
                        break;
                    default:
                        throw new IllegalArgumentException("invalid language: " + ((TextView) view).getText().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (view == null) return;
                switch (i) {
                    case 0:
                        SetCalendar('g');
                        break;
                    case 1:
                        SetCalendar('j');
                        break;
                    default:
                        throw new IllegalArgumentException("invalid calendar");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void SetupAboutUs() {

        LinearLayout github = aboutUs.findViewById(R.id.linear_about_github);
        LinearLayout website = aboutUs.findViewById(R.id.linear_about_website);
        github.setOnClickListener(view -> OpenGithub());
        website.setOnClickListener(view -> OpenWebsite());
        aboutUsBtn.setOnClickListener(view -> {
            aboutUs.setAlpha(0);
            aboutUs.setVisibility(View.VISIBLE);
            aboutUs.animate().alpha(1).start();
        });
    }

    private boolean IsFirstTimeL = false;

    private void SetLang(String lang) {

        if (!IsFirstTimeL) {
            IsFirstTimeL = true;
            return;
        }

        if (getResources().getString(R.string.LangID).equals(lang)) return;

        UserInfo ui = UserInfoRepo.getUserInfo();
        ui.LangId = lang;
        UserInfoRepo.setUserInfo(ui);

        Toast.makeText(getActivity(),getString(R.string.open_app_again_reverse),Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().finishAndRemoveTask();
        }else {
            getActivity().finish();
        }
    }

    private void SetCalendar(char calendarId) {
        UserInfo ui = UserInfoRepo.getUserInfo();
        ui.Calendar = calendarId;
        UserInfoRepo.setUserInfo(ui);
    }

    private void OpenGithub() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ali77gh/UniTools"));
        startActivity(browserIntent);
    }

    private void OpenWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unitools-apps.github.io/Website/"));
        startActivity(browserIntent);
    }

    @Override
    public boolean onBack() {
        if (aboutUs.getVisibility() == View.VISIBLE) {
            aboutUs.setAlpha(1);
            aboutUs.animate().alpha(0).start();
            aboutUs.postDelayed(() -> aboutUs.setVisibility(View.GONE), 500);
            return true;
        }
        return false;
    }
}
