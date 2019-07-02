package com.service.Worker.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.service.Worker.R;
import com.service.Worker.fragment.EditProfile;
import com.service.Worker.fragment.OrderRequest;
import com.service.Worker.fragment.Report;
import com.service.Worker.fragment.YourOrder;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    FirebaseAuth auth;
    public static String name,category="";
    Uri a_image;
    DatabaseReference mref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      toolbar = findViewById(R.id.toolbar);

      setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
      final FirebaseUser user = auth.getCurrentUser();
        final String a = user.getUid().toString();
        mref = FirebaseDatabase.getInstance().getReference().child("UrbanClap").child("serviceProvider").child("worker").child(a);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerTitle(Gravity.LEFT,"");
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        final TextView mNameTextView = (TextView) header.findViewById(R.id.name_drawer);
        final TextView category_drawer = (TextView) header.findViewById(R.id.category_drawer);
        final CircleImageView  imageView1 = (CircleImageView) header.findViewById(R.id.image_drawer);
//        mNameTextView.setText(name);

        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                a_image = Uri.parse(String.valueOf(dataSnapshot.child("image").getValue()));
                Glide.with(getApplicationContext()).load(a_image).into(imageView1);
                name =dataSnapshot.child("name").getValue().toString();
                category = dataSnapshot.child("category").getValue().toString();
                category_drawer.setText(category);
                mNameTextView.setText(name);

//                category=(String)dataSnapshot.child("category").getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
                return false;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

           // return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_order) {
            toolbar.setTitle("Complete Order");
            fragmentManager.beginTransaction().replace(R.id.container,new YourOrder()).commit();
            // Handle the camera action
        } else if (id == R.id.nav_request) {
            toolbar.setTitle("Order Request");
            fragmentManager.beginTransaction().replace(R.id.container,new OrderRequest()).commit();

        } else if (id == R.id.nav_editProfile) {
            toolbar.setTitle("Your Profile");
            fragmentManager.beginTransaction().replace(R.id.container,new EditProfile()).commit();

        } else if (id == R.id.nav_report) {

            toolbar.setTitle("Report ");
            fragmentManager.beginTransaction().replace(R.id.container,new Report()).commit();

        } else if (id == R.id.nav_logout) {
            toolbar.setTitle("");
            auth.signOut();
            finishAffinity();

        }else{}


        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
