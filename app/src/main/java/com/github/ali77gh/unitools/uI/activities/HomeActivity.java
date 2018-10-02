package com.github.ali77gh.unitools.uI.activities;

import android.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.fragments.WallFragment;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView drawerToggle = findViewById(R.id.btn_home_drawer_toggle);
        title = findViewById(R.id.text_home_title);
        drawer = findViewById(R.id.drawer_home);

        drawerToggle.setOnClickListener(view -> drawer.openDrawer(Gravity.START));
        SetupNavigationService();
    }

    private void SetupNavigationService() {
        //init -> load home as default
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.home_frag_place_holder, new WallFragment(), "tag");
        ft.commit();

        LinearLayout baygani = findViewById(R.id.linear_drawer_bayegani);
        LinearLayout transfer = findViewById(R.id.linear_drawer_transfer);
        LinearLayout friends = findViewById(R.id.linear_drawer_friends);
        LinearLayout help = findViewById(R.id.linear_drawer_help);
        LinearLayout settings = findViewById(R.id.linear_drawer_settings);
        LinearLayout info = findViewById(R.id.linear_drawer_info);


        baygani.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
        });

        transfer.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
        });

        friends.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
        });

        help.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
        });

        settings.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
        });

        info.setOnClickListener(view -> {
            Toast.makeText(this, "به زودی", Toast.LENGTH_SHORT).show();
        });

        //todo: setup click listeners and
        //todo: switch fragment and
        //todo: change title
        //todo: refresh toolbar
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
