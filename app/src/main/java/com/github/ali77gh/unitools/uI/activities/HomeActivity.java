package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.data.Repo.EventRepo;
import com.github.ali77gh.unitools.data.Repo.FileRepo;
import com.github.ali77gh.unitools.data.Repo.FriendRepo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.fragments.BayganiFragment;
import com.github.ali77gh.unitools.uI.fragments.ToolsFragment;
import com.github.ali77gh.unitools.uI.fragments.HelpFragment;
import com.github.ali77gh.unitools.uI.fragments.JozveFragment;
import com.github.ali77gh.unitools.uI.fragments.SettingsFragment;
import com.github.ali77gh.unitools.uI.fragments.TransferFragment;
import com.github.ali77gh.unitools.uI.fragments.WallFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private TextView title;
    private WallFragment wallFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatics();

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
        LinearLayout tools = findViewById(R.id.linear_drawer_tools);
        LinearLayout help = findViewById(R.id.linear_drawer_help);
        LinearLayout settings = findViewById(R.id.linear_drawer_settings);

        //init -> load Wall as default
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        wallFragment = new WallFragment();
        ft.replace(R.id.home_frag_place_holder, wallFragment, "tag");
        ft.commit();
        SelectMenuVisually(wall);


        wall.setOnClickListener(view -> {
            wallFragment = new WallFragment();
            switchFragment(wallFragment);
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.home));
            SelectMenuVisually(wall);
        });

        jozve.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true) return;
            switchFragment(new JozveFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.doc_founder));
            SelectMenuVisually(jozve);
        });

        baygani.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true) return;
            switchFragment(new BayganiFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.storage));
            SelectMenuVisually(baygani);
        });

        transfer.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true) return;
            switchFragment(new TransferFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.transfer));
            SelectMenuVisually(transfer);
        });

        tools.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true) return;
            switchFragment(new ToolsFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.friends));
            SelectMenuVisually(tools);
        });

        help.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
            if (true) return;
            switchFragment(new HelpFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.guide));
            SelectMenuVisually(help);
        });

        settings.setOnClickListener(view -> {
            switchFragment(new SettingsFragment());
            drawer.closeDrawer(Gravity.START);
            title.setText(getResources().getString(R.string.settings));
            SelectMenuVisually(settings);
        });
    }

    public void switchFragment(Fragment newFragment) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            if (getSupportFragmentManager().findFragmentById(R.id.home_frag_place_holder) == null) {
                ft.add(R.id.home_frag_place_holder, newFragment);
            } else {
                ft.replace(R.id.home_frag_place_holder, newFragment);
            }
            ft.addToBackStack(null);
            ft.commit();
            clearBackStack();
        } catch (Exception e) {
            throw e;
        }
    }

    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer(Gravity.START);
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                wallFragment.OnBarcodeReaded(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initStatics(){
        EventRepo.init(this);
        FileRepo.init(this);
        FriendRepo.init(this);
        UserInfoRepo.init(this);
        ContextHolder.init(this);
    }
}
