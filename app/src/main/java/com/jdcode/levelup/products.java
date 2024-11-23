package com.jdcode.levelup;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class products extends AppCompatActivity {


    private RecyclerView recyclerView;

    private List<Product> productList;

    private ProductAdapter adapter;

    private FirebaseFirestore db;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;  // Código para la solicitud de permisos


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseApp.initializeApp(this);


        //ASPECTO VISUAL PARA BOTON DE BUSQUEDA
        TextInputEditText fieldBusqueda = findViewById(R.id.field_busqueda);

        fieldBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    String buscar = "Buscar";
                    if(fieldBusqueda.getText().toString().equals("Buscar")){
                        fieldBusqueda.setText(""); // Ocultar la guía al hacer foco
                    }
                } else {
                    if (fieldBusqueda.getText().toString().isEmpty()) {
                        fieldBusqueda.setText("Buscar"); // Mostrar la guía si está vacío
                    }
                }
            }
        });

        //FUNCION DEL BOTON DE BUSQUEDA
        Button buttonCart = findViewById(R.id.button_cart);
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCart();
            }
        });

        //BOTON CAMARA



        Button buttonCamera = findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (ContextCompat.checkSelfPermission(products.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Si no tenemos permisos, solicitarlos
                    ActivityCompat.requestPermissions(products.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    openCamera();
                }


            }
        });

        //Funcion de cargar y mostrar productos
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productList = new ArrayList<>();
        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        cargarProductos();

    }

    public void cargarProductos(){
        CollectionReference productosRef = db.collection("productos");
        System.out.println("PRUEBA DE ENTRADA AL METODO");
        productosRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("TASK SUCESSFULL");
                        QuerySnapshot querySnapshot = task.getResult();
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Product product = document.toObject(Product.class);
                            productList.add(product);
                            Log.d("FirebaseProductData", "ID: " + document.getId() +
                                    ", Nombre: " + product.getNombre() +
                                    ", Precio: " + product.getPrecio() +
                                    ", Imagen URL: " + product.getImageUrl()+
                                    ", Descripcion: " + product.getDescripcion());

                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void openCamera(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No hay cámara disponible en este dispositivo", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "No se ha encontrado un producto similar", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al tomar la foto o la acción fue cancelada", Toast.LENGTH_SHORT).show();
        }
    }



    public void openCart (){
        Intent intent = new Intent(products.this, cart.class);
        startActivity(intent);
    }

}