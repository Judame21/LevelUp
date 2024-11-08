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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> cartProductList;
    private Context context;
    private TotalAmountListener listener;

    public CartAdapter(Context context, List<Product> cartProductList, TotalAmountListener listener) {
        this.cartProductList = cartProductList;
        this.context = context;
        this.listener = listener;
    }

    public interface TotalAmountListener {
        void updateTotalAmount(); // Método que llamará la actividad/fragmento
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity, productDescription;
        ImageView productImage;
        Button removeButton;
        Button addButton;

        public CartViewHolder(View itemView) {
            super(itemView);

            productDescription = itemView.findViewById(R.id.descriptionText);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            removeButton = itemView.findViewById(R.id.removeButton);
            addButton = itemView.findViewById(R.id.addButton);
            productQuantity = itemView.findViewById(R.id.productQuantity);
        }
    }




    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_product, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        Product product = cartProductList.get(position);
        holder.productName.setText(product.getNombre());
        holder.productPrice.setText(String.valueOf(product.getPrecio()));
        holder.productDescription.setText(String.valueOf(product.getDescripcion()));
        holder.productQuantity.setText(String.valueOf(product.getCantidad()));



        Glide.with(context)
                .load(product.getImageUrl())
                .into(holder.productImage);

        //Boton de añadir
        holder.addButton.setOnClickListener(v -> {
            product.setCantidad(product.getCantidad() + 1);
            holder.productQuantity.setText(String.valueOf(product.getCantidad()));
            listener.updateTotalAmount();
        });

        //Boton de remover
        holder.removeButton.setOnClickListener(v -> {
            if (product.getCantidad() > 1) {
                product.setCantidad(product.getCantidad() - 1);
                holder.productQuantity.setText(String.valueOf(product.getCantidad()));
            } else {
                cartProductList.remove(position);
                notifyItemRemoved(position);
            }
            listener.updateTotalAmount();
        });

    }




    public double getTotalPrice() {
        double total = 0;
        for (Product product : cartProductList) {
            total += product.getPrecio() * product.getCantidad();
        }
        return total;
    }


    @Override
    public int getItemCount() {
        return cartProductList.size();
    }
}
