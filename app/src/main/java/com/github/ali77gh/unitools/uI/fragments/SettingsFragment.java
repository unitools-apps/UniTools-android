package com.github.ali77gh.unitools.uI.fragments;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.Model.UserInfo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;

import java.util.Locale;

/**
 * Created by ali on 10/3/18.
 */

public class SettingsFragment extends Fragment {

    private UserInfoRepo userInfoRepo;

    private Spinner languageSpinner;
    private Spinner notificationSpinner;
    private ImageView notifiState;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cView = inflater.inflate(R.layout.fragment_settings, container, false);

        userInfoRepo = new UserInfoRepo(getActivity());

        languageSpinner = cView.findViewById(R.id.spinner_settings_language);
        notificationSpinner = cView.findViewById(R.id.spinner_settings_notification);
        notifiState = cView.findViewById(R.id.image_settings_notification_state);


        SetupLanguage();
        SetupNotification();
        LoadCurrentSettings();

        return cView;
    }

    private void LoadCurrentSettings() {
        UserInfo ui = userInfoRepo.getUserInfo();

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

        switch (ui.NotificationMode) {
            case UserInfo.NOTIFICATION_WITH_SOUND:
                notificationSpinner.setSelection(0);
                notifiState.setImageDrawable(getResources().getDrawable(R.drawable.settings_notifi_sound));
                break;
            case UserInfo.NOTIFICATION_JUST_NOTIFI:
                notificationSpinner.setSelection(1);
                notifiState.setImageDrawable(getResources().getDrawable(R.drawable.settings_notifi));
                break;
            case UserInfo.NOTIFICATION_NOTHING:
                notificationSpinner.setSelection(2);
                notifiState.setImageDrawable(getResources().getDrawable(R.drawable.settings_notifi_mute));
                break;
            default:
                throw new IllegalArgumentException("invalid Notification mode: " + ui.NotificationMode);
        }
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

    private void SetupNotification() {

        String modes[] = getResources().getStringArray(R.array.notofication_modes);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, modes);
        notificationSpinner.setAdapter(adapter);

        ((View) notificationSpinner.getParent()).setOnClickListener(view -> {
            notificationSpinner.performClick();
        });

        notificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                UserInfo ui = userInfoRepo.getUserInfo();
                switch (i) {
                    case 0:
                        ui.NotificationMode = UserInfo.NOTIFICATION_WITH_SOUND;
                        notifiState.setImageDrawable(getResources().getDrawable(R.drawable.settings_notifi_sound));
                        break;
                    case 1:
                        ui.NotificationMode = UserInfo.NOTIFICATION_JUST_NOTIFI;
                        notifiState.setImageDrawable(getResources().getDrawable(R.drawable.settings_notifi));
                        break;
                    case 2:
                        ui.NotificationMode = UserInfo.NOTIFICATION_NOTHING;
                        notifiState.setImageDrawable(getResources().getDrawable(R.drawable.settings_notifi_mute));
                        break;
                }
                userInfoRepo.setUserInfo(ui);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //todo destroy
    }


    private boolean IsFirstTimeL = false;

    private void SetLang(String lang) {

        if (!IsFirstTimeL) {
            IsFirstTimeL = true;
            return;
        }

        if (getResources().getString(R.string.LangID).equals(lang)) return;

        UserInfo ui = userInfoRepo.getUserInfo();
        ui.LangId = lang;
        userInfoRepo.setUserInfo(ui);

        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        getActivity().recreate();
    }
}
