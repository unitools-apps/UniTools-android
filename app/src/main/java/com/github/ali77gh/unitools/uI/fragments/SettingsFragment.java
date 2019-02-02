package com.github.ali77gh.unitools.uI.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.activities.GuideActivity;
import com.github.ali77gh.unitools.uI.dialogs.SettingsAlarmConfigDialog;

/**
 * Created by ali on 10/3/18.
 */

public class SettingsFragment extends Fragment implements Backable {

    private Spinner languageSpinner;
    private LinearLayout aboutUsBtn;
    private LinearLayout autoSilentBtn;
    private Switch autoSilentSwitch;
    private FrameLayout aboutUs;

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
        autoSilentBtn = cView.findViewById(R.id.linear_settings_auto_silent);
        autoSilentSwitch = (Switch) autoSilentBtn.getChildAt(3);
        aboutUsBtn = cView.findViewById(R.id.linear_settings_about_us);
        aboutUs = cView.findViewById(R.id.layout_settings_about);
        LinearLayout reminder = cView.findViewById(R.id.linear_settings_reminder);
        LinearLayout guide = cView.findViewById(R.id.linear_settings_guide);
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


        SetupLanguage();
        LoadCurrentSettings();
        SetupAboutUs();

        return cView;
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

        autoSilentSwitch.setChecked(ui.AutoSilent);
    }

    private void SetupLanguage() {

        String langs[] = getResources().getStringArray(R.array.languages);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, langs);
        languageSpinner.setAdapter(adapter);

        ((View) languageSpinner.getParent()).setOnClickListener(view -> {
            languageSpinner.performClick();
        });

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
        getActivity().finishAndRemoveTask();
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
