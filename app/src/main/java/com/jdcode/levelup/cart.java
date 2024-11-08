package com.jdcode.levelup;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class cart extends AppCompatActivity implements CartAdapter.TotalAmountListener{


    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private CartManager cartManager;
    private TextView totalAmountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartManager = CartManager.getInstance();

        recyclerView = findViewById(R.id.recycleViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Product> cartProducts = cartManager.getCartProducts();
        cartAdapter = new CartAdapter(this, cartProducts, this); // Sin listener de "añadir al carrito" en esta vista
        recyclerView.setAdapter(cartAdapter);

        totalAmountTextView = findViewById(R.id.totalAmount);
        updateTotalAmount();

        //Funcionamieto boton de cierre
        Button buttonClose = findViewById(R.id.button_close);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizarCarrito();
            }
        });
    }

    @Override
    public void updateTotalAmount() {
        double totalAmount = cartAdapter.getTotalPrice();
        totalAmountTextView.setText("Total: $" + String.format("%.2f", totalAmount));
    }




    public void finalizarCarrito(){
        finish();
    }
}