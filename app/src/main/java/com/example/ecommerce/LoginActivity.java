package com.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecommerce.Model.Users;
import com.example.ecommerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {
    private EditText InputPhoneNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog loadingBar;
    private TextView AdminLink,NotAdminLink;

    private String parentDbName="Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton=(Button)findViewById(R.id.login_btn);
        InputPhoneNumber=(EditText)findViewById(R.id.login_phone_number_input);
        InputPassword=(EditText)findViewById(R.id.login_password_input);
        AdminLink=(TextView)findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel_link);

        loadingBar =new ProgressDialog(this);
        chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_chkb);

        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";
            }
        });

    }


    private void LoginUser()
    {
        String phone=InputPhoneNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(this,"Please write phone...",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Please write password...",Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setTitle("Logging In. ");
            loadingBar.setMessage("Please wait while we are checking credentials..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone,final String password)
    {

        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);//to write in phone memory paper lib is used
            Paper.book().write(Prevalent.UserPasswordKey,password);

        }

        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData=dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                                Toast.makeText(LoginActivity.this, "Welcome Admin Logged in Successfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,AdminCategoryActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("Users"))
                            {
                                Toast.makeText(LoginActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                Prevalent.currentOnlineUser=usersData;
                                startActivity(intent);
                            }

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password or Phone Is Incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this, "Account With this "+phone+" Does not exist.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Create a New Account", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}