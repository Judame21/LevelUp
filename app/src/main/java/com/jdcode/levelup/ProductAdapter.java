package com.jdcode.levelup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductoViewHolder> {

    private List<Product> productsList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productsList) {
        this.context = context;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Product product = productsList.get(position);
        holder.productTitle.setText(product.getNombre());
        holder.productPrice.setText("$" + product.getPrecio());

        // Cargar la imagen con Glide (asegúrate de tener Glide en tus dependencias)
        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);

        holder.addToCartButton.setOnClickListener(v -> {
            // Acción al presionar el botón "Añadir al carrito"
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productTitle;
        public TextView productPrice;
        public Button addToCartButton;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productTitle = itemView.findViewById(R.id.productTitle);
            productPrice = itemView.findViewById(R.id.productPrice);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }


}
