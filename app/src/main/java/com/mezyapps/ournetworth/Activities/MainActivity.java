package com.mezyapps.ournetworth.Activities;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mezyapps.ournetworth.Common;
import com.mezyapps.ournetworth.Config;
import com.mezyapps.ournetworth.Fragments.Ask;
import com.mezyapps.ournetworth.Fragments.EditProfile;
import com.mezyapps.ournetworth.Fragments.Give;
import com.mezyapps.ournetworth.Fragments.GiveAndAsk;
import com.mezyapps.ournetworth.Fragments.Home;
import com.mezyapps.ournetworth.Fragments.SearchMember;
import com.mezyapps.ournetworth.MVP.Member;
import com.mezyapps.ournetworth.MVP.UserImageResponse;
import com.mezyapps.ournetworth.R;
import com.mezyapps.ournetworth.Retrofit.Api;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    public static LinearLayout toolbarContainer;
    public static View toolbar, searchLayout;
    @BindView(R.id.searchView)
    SearchView searchView;
    boolean doubleBackToExitPressedOnce = false;
    public static ImageView menu, back, profilelogo,search;
    public static DrawerLayout drawerLayout;
    public static String  userId;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    public static TextView title,searchTextView;

    public static FloatingActionButton fab;
    List<Member> memberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        getUserId();
        fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                loadFragment(new GiveAndAsk(), true);

            }
        });

        getimageUserwise();
        // customized searchView
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = (EditText) searchView.findViewById(id);
        searchEditText.setTextSize(12);
        searchLayout = findViewById(R.id.searchLayout);
        toolbarContainer = (LinearLayout) findViewById(R.id.toolbar_container);
        toolbar = findViewById(R.id.toolbar);
        searchTextView=findViewById(R.id.searchTextView);
       loadFragment(new Home(), false);



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.give_nav:
                            loadFragment(new Give(), true);

                        break;
                    case R.id.ask_nav:
                          loadFragment(new Ask(), true);
                        break;
                    case R.id.logout_nav:
                        logout();
                        break;
                    case R.id.meber_nav:
                        loadFragment(new Home(), true);
                        break;
                    case R.id.faq:
                        // loadFragment(new FAQ(), true);
                        break;
                    case R.id.appInfo:
                        // loadFragment(new AppInfo(), true);
                        break;
                    case R.id.share:
                        shareApp();
                        break;
                    case R.id.rateApp:
                        // perform click on Rate Item
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;
                    case R.id.email:
                        //   openGmail();
                        break;
                    case R.id.call:
                        //  call();
                        break;
                }
                return false;
            }
        });




    }

    private void getimageUserwise() {
        // sending gcm token to server
        Api.getClient().getUserImage(userId,
                new Callback<UserImageResponse>() {
                    @Override
                    public void success(UserImageResponse userImageResponse, Response response) {
                        if (userImageResponse.getSuccess().equalsIgnoreCase("true")) {
                            try {
                                Picasso.with(MainActivity.this)
                                        .load(userImageResponse.getImage())
                                        // .resize(75,75)
                                        .placeholder(R.drawable.defaultimage)
                                        .into(profilelogo);
                            } catch (Exception e) {
                                Log.d("exception", e.toString());
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                        Log.e("error", error.toString());
                    }
                });
    }


    public void shareApp() {
        // share app with your friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this " + getResources().getString(R.string.app_name) + " App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        title = (TextView) findViewById(R.id.title);
        menu = (ImageView) findViewById(R.id.menu);
        profilelogo = (ImageView) findViewById(R.id.profilelogo);
        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search);

    }


    @Override
    public void onBackPressed() {
        // double press to exit
        if (menu.getVisibility() == View.VISIBLE) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }


    public void showToolbar() {
        toolbarContainer.clearAnimation();
        toolbarContainer
                .animate()
                .translationY(0)
                .start();

    }

    public void lockUnlockDrawer(int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            searchLayout.setVisibility(View.GONE);
            showToolbar();

        } else {
            menu.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            searchLayout.setVisibility(View.VISIBLE);

        }

    }


    @OnClick({R.id.menu, R.id.back, R.id.profilelogo,R.id.searchTextView,R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!Home.swipeRefreshLayout.isRefreshing())
                if (!MainActivity.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    MainActivity.drawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.back:
                removeCurrentFragmentAndMoveBack();
                break;
            case R.id.searchTextView:
            case R.id.search:
                if (!Home.swipeRefreshLayout.isRefreshing())
                    loadFragment(new SearchMember(), true);
                break;
            case R.id.profilelogo:

                if (!Home.swipeRefreshLayout.isRefreshing())
                    loadFragment(new EditProfile(), true);

                break;

        }
    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.remove(fragment);
        trans.commit();*/
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }



    private void logout() {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("Are you sure you want to logout?");
        alertDialog.setCancelText("Cancel");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setText("Logout");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.saveUserData(MainActivity.this, "mobile", "");
                Common.saveUserData(MainActivity.this, "userId", "");
                Config.moveTo(MainActivity.this, LoginActivity.class);
                finish();

            }
        });
    }


    private void getUserId() {
        if (Common.getSavedUserData(MainActivity.this, "userId").equalsIgnoreCase("")) {
            userId = "";
        } else {
            userId = Common.getSavedUserData(MainActivity.this, "userId");
            Log.d("userId", userId);
        }

    }
//    search code start

// search code end
}
