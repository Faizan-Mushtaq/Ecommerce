package com.example.ecommerce.Sellers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ecommerce.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerLoginBegin,registerButton;
    private EditText nameInput,phoneInput,emailInput,passwordInput,addressInput;

    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth=FirebaseAuth.getInstance();

        sellerLoginBegin=(Button)findViewById(R.id.seller_already_have_account_btn);
        nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        emailInput=findViewById(R.id.seller_email);
        passwordInput=findViewById(R.id.seller_password);
        addressInput=findViewById(R.id.seller_address);
        registerButton=findViewById(R.id.seller_register_btn);

        loadingBar =new ProgressDialog(this);



        sellerLoginBegin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent =new Intent(SellerRegistrationActivity.this,SellerLoginActivity.class);
                startActivity(intent);

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                registerSeller();
            }
        });

    }

    private void registerSeller()
    {
        final String name=nameInput.getText().toString();
        final String phone=phoneInput.getText().toString();
        final String email=emailInput.getText().toString();
        String password=passwordInput.getText().toString();
        final String address=addressInput.getText().toString();

        if(!name.equals("") && !phone.equals("") && !email.equals("") && !password.equals("") && !address.equals(""))
        {
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait while we are checking credentials..");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();


            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(task.isSuccessful())
                            {
                                final DatabaseReference rootRef;
                                rootRef= FirebaseDatabase.getInstance().getReference();

                                String sid= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                                HashMap<String ,Object>sellerMap=new HashMap<>();
                                sellerMap.put("sid",sid);
                                sellerMap.put("phone",phone);
                                sellerMap.put("email",email);
                                sellerMap.put("address",address);
                                sellerMap.put("name",name);
                               // sellerMap.put("password",password);

                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                loadingBar.dismiss();
                                                Toast.makeText(SellerRegistrationActivity.this, "You Are Registered Successfully.", Toast.LENGTH_SHORT).show();

                                                Intent intent=new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                            }

                         else  if (!task.isSuccessful()) {
                                Log.e(String.valueOf(SellerRegistrationActivity.this), "onComplete: Failed=" + task.getException().getMessage());
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please complete Registration Form", Toast.LENGTH_SHORT).show();
        }
    }
}
