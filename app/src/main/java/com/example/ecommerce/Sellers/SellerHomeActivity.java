package com.example.ecommerce.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SellerHomeActivity extends AppCompatActivity {
        private TextView mTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
         BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//added this line to make button work
       /* // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder
        //   (R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
        //  .build();
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(navView, navController);*/
    }
     private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
              =new BottomNavigationView.OnNavigationItemSelectedListener()
     {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item)
          {
              switch (item.getItemId())
              {
                  case R.id.navigation_home:

                      //  mTextMessage.setText(R.string.title_home);
                        return true;


                  case R.id.navigation_add:
                     // mTextMessage.setText(R.string.title_dashboard);
                      return true;


                  case R.id.navigation_logout:
                                  final FirebaseAuth mAuth;
                                  mAuth=FirebaseAuth.getInstance();
                                  mAuth.signOut();
                                  Intent intent=new Intent(SellerHomeActivity.this, MainActivity.class);
                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  startActivity(intent);
                                  finish();
                                  return true;

              }
              return false;
          }
      };



}