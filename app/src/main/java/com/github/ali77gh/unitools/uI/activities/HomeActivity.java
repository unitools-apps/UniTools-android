package com.github.ali77gh.unitools.uI.activities;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.fragments.AboutUsFragment;
import com.github.ali77gh.unitools.uI.fragments.BayganiFragment;
import com.github.ali77gh.unitools.uI.fragments.FriendsFragment;
import com.github.ali77gh.unitools.uI.fragments.HelpFragment;
import com.github.ali77gh.unitools.uI.fragments.JozveFragment;
import com.github.ali77gh.unitools.uI.fragments.SettingsFragment;
import com.github.ali77gh.unitools.uI.fragments.TransferFragment;
import com.github.ali77gh.unitools.uI.fragments.WallFragment;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MakeItPersian();

        ImageView drawerToggle = findViewById(R.id.btn_home_drawer_toggle);
        title = findViewById(R.id.text_home_title);
        drawer = findViewById(R.id.drawer_home);

        drawerToggle.setOnClickListener(view -> drawer.openDrawer(Gravity.START));
        SetupNavigationService();
    }

    private void SetupNavigationService() {

        LinearLayout wall = findViewById(R.id.linear_drawer_wall);
        LinearLayout jozve = findViewById(R.id.linear_drawer_jozve);
        LinearLayout baygani = findViewById(R.id.linear_drawer_bayegani);
        LinearLayout transfer = findViewById(R.id.linear_drawer_transfer);
        LinearLayout friends = findViewById(R.id.linear_drawer_friends);
        LinearLayout help = findViewById(R.id.linear_drawer_help);
        LinearLayout settings = findViewById(R.id.linear_drawer_settings);
        LinearLayout info = findViewById(R.id.linear_drawer_info);

        //init -> load Wall as default
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.home_frag_place_holder, new WallFragment(), "tag");
        ft.commit();
        SelectMenuVisually(wall);


        wall.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            switchFragment(new WallFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("خانه");
            SelectMenuVisually(wall);
        });

        jozve.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true)return;
            switchFragment(new JozveFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("جزوه یاب");
            SelectMenuVisually(jozve);
        });

        baygani.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true)return;
            switchFragment(new BayganiFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("بایگانی");
            SelectMenuVisually(baygani);
        });

        transfer.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true)return;
            switchFragment(new TransferFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("رفت و آمد");
            SelectMenuVisually(transfer);
        });

        friends.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true)return;
            switchFragment(new FriendsFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("دوستان");
            SelectMenuVisually(friends);
        });

        help.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true)return;
            switchFragment(new HelpFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("راهنما");
            SelectMenuVisually(help);
        });

        settings.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            switchFragment(new SettingsFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("تنظیمات");
            SelectMenuVisually(settings);
        });

        info.setOnClickListener(view -> {
            switchFragment(new AboutUsFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText("درباره ی ما");
            SelectMenuVisually(info);
        });

        //todo: setup click listeners           done
        //todo: switch fragment                 done
        //todo: change title                    done
        //todo: refresh toolbar
        //todo:selected item effect
    }

    public void switchFragment(Fragment newFragment) {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            if (getSupportFragmentManager().findFragmentById(R.id.home_frag_place_holder) == null) {
                ft.add(R.id.home_frag_place_holder, newFragment);
            } else {
                ft.replace(R.id.home_frag_place_holder, newFragment);
            }
            ft.addToBackStack(null);
            ft.commit();
        } catch (Exception e) {
            throw e;
        }
    }

    private void SelectMenuVisually(LinearLayout selectedMenu) {

        //deselect all
        LinearLayout parent = findViewById(R.id.linear_drawer_menu_parent);
        for (int i = 0; i != parent.getChildCount(); i++) {
            View row = parent.getChildAt(i);

            if (row instanceof LinearLayout) {

                LinearLayout lRow = (LinearLayout) row;
                ImageView icon = (ImageView) lRow.getChildAt(0);
                TextView lable = (TextView) lRow.getChildAt(1);

                lRow.setBackgroundColor(getResources().getColor(R.color.background));
                lable.setTextColor(getResources().getColor(R.color.accent));
                icon.setColorFilter(getResources().getColor(R.color.accent), PorterDuff.Mode.SRC_IN);
            }
        }

        //select

        ImageView icon = (ImageView) selectedMenu.getChildAt(0);
        TextView lable = (TextView) selectedMenu.getChildAt(1);

        selectedMenu.setBackgroundColor(getResources().getColor(R.color.background_darker));
        lable.setTextColor(getResources().getColor(R.color.primary));
        icon.setColorFilter(getResources().getColor(R.color.primary), PorterDuff.Mode.SRC_IN);
    }

    private void MakeItPersian() {
        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale("fa".toLowerCase())); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
            return;
        }

        super.onBackPressed();
    }
}
