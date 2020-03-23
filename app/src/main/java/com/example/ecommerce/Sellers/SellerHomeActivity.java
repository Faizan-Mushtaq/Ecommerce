package com.example.ecommerce.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Buyers.MainActivity;
import com.example.ecommerce.Model.Products;
import com.example.ecommerce.R;
import com.example.ecommerce.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SellerHomeActivity extends AppCompatActivity {
        private TextView mTextMessage;

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private DatabaseReference unverifiedProducts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
         BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);//added this line to make button work

        unverifiedProducts= FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=findViewById(R.id.seller_home_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


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
                      Intent intentCate=new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                      startActivity(intentCate);
                      return true;


                  case R.id.navigation_logout:
                                  final FirebaseAuth mAuth;
                                  mAuth=FirebaseAuth.getInstance();
                                  mAuth.signOut();
                                  Intent intentMain=new Intent(SellerHomeActivity.this, MainActivity.class);
                                  intentMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  startActivity(intentMain);
                                  finish();
                                  return true;

              }
              return false;
          }
      };


    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(unverifiedProducts.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class)
                        .build();
        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options)
                {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i, @NonNull final Products products)
                    {
                        itemViewHolder.txtProductName.setText(products.getPname());
                        itemViewHolder.txtProductDescription.setText(products.getDescription());
                        itemViewHolder.txtProductPrice.setText("Price = " + products.getPrice() + "$");
                       itemViewHolder.txtProductStatus.setText(products.getProductState());
                        Picasso.get().load(products.getImage()).into(itemViewHolder.imageView);

                        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                final String productID= products.getPid();
                                CharSequence[]  options=new CharSequence[]
                                        {
                                                "Yes",
                                                "No"
                                        };
                                AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Do you want to Delete this product.Are You sure?");
                                builder.setItems(options, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        if(which==0)// yes position
                                        {
                                            //changeProductState(productID);
                                        }
                                        if(which==1)
                                        {

                                        }
                                    }
                                });

                                builder.show();

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                    {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                        ItemViewHolder holder = new ItemViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
