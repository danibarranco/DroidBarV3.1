package com.example.droidbarv1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.customview2.ValueSelector;
import com.example.droidbarv1.Model.Data.Comanda;
import com.example.droidbarv1.Model.Data.Factura;
import com.example.droidbarv1.Model.Data.Producto;
import com.example.droidbarv1.Model.WaitResponseServer;
import com.example.droidbarv1.View.MainViewModel;
import com.example.droidbarv1.View.ProductoAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class OrdenaComanda extends AppCompatActivity {

    private int idMesa;
    private long idFactura,idEmpleado;
    public static List<Producto> productos;
    public static ArrayList<Producto> productosFilter;
    private RecyclerView rvProductos;
    private MainViewModel viewModel;
    private GridLayoutManager layoutManager;
    private ImageView btComida, btBebida;
    private ProductoAdapter adapter;
    private int destino = 1;
    private View fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordena_comanda);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); Poner action bar????

        idMesa = getIntent().getIntExtra("idMesa", 0);
        idFactura = getIntent().getLongExtra("idFactura", 0);
        idEmpleado = getIntent().getLongExtra("idEmpleado", 4);

        adapter = new ProductoAdapter(new ProductoAdapter.OnItemClickListenner() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onItemClick(Producto producto, View v) {
                //Creamos comanda con producto pinchado
                creaComanda(producto,1);
                Snackbar.make(v, producto.getName(), Snackbar.LENGTH_LONG).show();
            }

        }, new ProductoAdapter.OnLongItemClickListenner() {
            @Override
            public void onLongItemClick(final Producto producto, View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrdenaComanda.this);
                final LayoutInflater layoutInflater = getLayoutInflater();
                View vistaAux = layoutInflater.inflate(R.layout.layout_alerta, null);
                builder.setView(vistaAux);
                final ValueSelector valueSelector= vistaAux.findViewById(R.id.valueSelector);
                valueSelector.setMinValue(0);
                valueSelector.setMaxValue(50);
                builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int unidades = valueSelector.getValue();
                        creaComanda(producto, unidades);
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }, this);

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getProductoList(new WaitResponseServer() {
            @Override
            public void waitingResponse(boolean success, List list) {
                if(success){
                    productosFilter=new ArrayList<>();
                    productos=list;
                    for (Producto p:productos) {
                        if(p.getTarget()==destino){
                            productosFilter.add(p);
                        }
                    }
                    adapter.setProductoList(productosFilter);
                    initComponents();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        new LoadViewTask().execute();
    }

    private void creaComanda(Producto producto, int uds) {
        Comanda newC = new Comanda();
        newC.setUnits(uds);
        newC.setId_employee(idEmpleado);
        newC.setId_ticket(idFactura);
        newC.setId_product(producto.getId());
        newC.setPrice(producto.getPrice()*uds);
        newC.setServed(0);
        Log.v("product",newC.toString());
        viewModel.addComanda(newC);
    }


    private void initComponents() {

        rvProductos = findViewById(R.id.rvProductos);
        layoutManager = new GridLayoutManager(this, 3);
        rvProductos.setLayoutManager(layoutManager);
        rvProductos.setAdapter(adapter);

        btComida = findViewById(R.id.btComida);
        btComida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destino = 1;
                viewModel.getProductoList(new WaitResponseServer() {
                    @Override
                    public void waitingResponse(boolean success, List list) {
                        ArrayList<Producto> comida = new ArrayList<>();
                        for (Producto p : productos
                        ) {
                            if (p.getTarget() == destino) {
                                comida.add(p);
                            }
                        }
                        productos = comida;
                        adapter.setProductoList(comida);
                        adapter.setIcon(destino);
                    }
                });
            }
        });

        btBebida = findViewById(R.id.btBebida);
        btBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destino = 0;
                viewModel.getProductoList(new WaitResponseServer() {
                    @Override
                    public void waitingResponse(boolean success, List list) {
                        ArrayList<Producto> bebida = new ArrayList<>();
                        for (Producto p : productos
                        ) {
                            if (p.getTarget() == destino) {
                                bebida.add(p);
                            }
                        }
                        productos = bebida;
                        adapter.setProductoList(bebida);
                        adapter.setIcon(destino);
                    }
                });
            }
        });

        fragment = findViewById(R.id.fragmentLoadingComanda);
        fragment.setVisibility(View.INVISIBLE);
    }

    private class LoadViewTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragment.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                try {
                    this.wait(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
            return null;
        }
        protected void onPostExecute(Void result){
            fragment.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        new LoadViewTask().execute();
        return true;
    }
}
