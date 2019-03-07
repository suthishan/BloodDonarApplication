package com.suthishan.blooddonar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suthishan.blooddonar.R;
import com.suthishan.blooddonar.constants.AppVariables;
import com.suthishan.blooddonar.utils.PreferenceData;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    PreferenceData preferenceData;
    LinearLayout blood_transfer,donor_list,seekerlist;

    TextView username,txt_mobile,txt_age;
    ImageView imageView;
    TextView username_nav,email;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setupNavigation();

        preferenceData = new PreferenceData(this);
        blood_transfer = (LinearLayout) findViewById(R.id.blood_transfer);
        donor_list = (LinearLayout) findViewById(R.id.donor_list);
        seekerlist = (LinearLayout) findViewById(R.id.seekerlist);
        username = (TextView) findViewById(R.id.username);
        txt_mobile = (TextView) findViewById(R.id.txt_mobile);
        txt_age = (TextView) findViewById(R.id.txt_age);
        onClicklistiner();
        if (AppVariables.isMainActivityOpen) {
            if (preferenceData.getMainScreenOpen().equalsIgnoreCase("0")) {
                preferenceData.setMainScreenOpen(1);
            }
        }
        if(preferenceData.getAdminLoginDetails()){
            blood_transfer.setVisibility(View.VISIBLE);
            username.setText("Welcome Admin");
        }
    }



    private void setupNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        imageView = (ImageView) headerView.findViewById(R.id.imageView);
        username_nav = (TextView) headerView.findViewById(R.id.username);
        email = (TextView) headerView.findViewById(R.id.email);
        username_nav.setText("Welcome Admin");
        email.setText("Welcome Admin@donor.com");

    }

    private void onClicklistiner() {
        donor_list.setOnClickListener(this);
        seekerlist.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(doubleBackToExitPressedOnce){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
            startActivity(intent);
            finish();
            System.exit(0);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_donor_list) {
            startActivity(new Intent(getApplicationContext(), DonorListActivity.class));
            finish();
            // Handle the camera action
        } else if (id == R.id.nav_seekar_list) {
            startActivity(new Intent(getApplicationContext(), SeekerListActivity.class));
            finish();

        }else if (id == R.id.log_out) {
            preferenceData.setAdminLogin(false);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.donor_list:
                startActivity(new Intent(getApplicationContext(), DonorListActivity.class));
                break;
            case R.id.seekerlist:
                startActivity(new Intent(getApplicationContext(),SeekerListActivity.class));
                break;
        }
    }

}
