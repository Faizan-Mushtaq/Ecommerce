package com.example.ecommerce;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecommerce.Model.Cart;
import com.example.ecommerce.Prevalent.Prevalent;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private Button NextProcessBtn;
    private TextView txtTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);

        NextProcessBtn=(Button)findViewById(R.id.next_btn);
        txtTotalAmount=(TextView)findViewById(R.id.total_price);
    }
    @Override
    protected  void onStart()
    {
        super.onStart();
        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options= new FirebaseRecyclerOptions.Builder<Cart>()
                                                .setQuery(cartListRef.child("User View")
                                                .child(Prevalent.currentOnlineUser.getPhone())
                                                .child("Products"),Cart.class)
                                                .build();
    }
}
