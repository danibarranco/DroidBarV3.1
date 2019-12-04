package com.example.droidbarv1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.example.droidbarv1.Model.Data.Comanda;
import com.example.droidbarv1.Model.Data.Factura;
import com.example.droidbarv1.Model.Data.Producto;
import com.example.droidbarv1.Model.WaitResponseServer;
import com.example.droidbarv1.View.ComandaAdapter;
import com.example.droidbarv1.View.ComandaItemTouchHelper;
import com.example.droidbarv1.View.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ticket extends AppCompatActivity implements ComandaItemTouchHelper.RecyclerItemTouchHelperListener{
    private MainViewModel viewModel;
    private int idMesa;
    private long idEmpleado;
    private Button btFinTicket;
    private TextView tvFactura,tvMesa,tvTotal;
    private float total=0;
    public static Factura facturaActual;
    private View fragmento;
    public static List<Factura> facturas;
    public static List<Comanda> comandas= new ArrayList<>();
    public static List<Comanda> comandasTicket;
    private ComandaAdapter adapter;
    private boolean yaExiste;
    private Factura finF;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        facturaActual=getIntent().getParcelableExtra("factura");

        //Obtencion de datos de Intent o del Bundle
        if(savedInstanceState!=null){
            idMesa=savedInstanceState.getInt("idMesa");
            idEmpleado=savedInstanceState.getLong("idEmpleado");
            yaExiste=savedInstanceState.getBoolean("yaExiste");
            total=savedInstanceState.getFloat("total");

        }else {
            idMesa=facturaActual.getTable();
            idEmpleado=facturaActual.getId_employee_start();
            yaExiste=getIntent().getBooleanExtra("yaExiste",false);
        }

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        tvFactura=findViewById(R.id.tvFactura);
        tvFactura.setText("Factura Nº "+facturaActual.getId());



        adapter = new ComandaAdapter(new ComandaAdapter.OnItemClickListenner() {
            @Override
            public void onItemClick(Comanda comanda, View v) {
                if(v.getId()==findViewById(R.id.ivMenos).getId()){
                    //ivMenos restar comandas
                    if (comanda.getUnits() == 1){
                        //No se pueden quitar mas Poner sanck bar que diga desliza comanda hacia la izq para borrar.
                    }else {
                        comanda.setUnits(comanda.getUnits() - 1);
                        double priceProduct=0;
                        for (Producto p:OrdenaComanda.productos) {
                            if(p.getId()==comanda.getId_product()){
                                priceProduct=p.getPrice();
                            }
                        }
                        comanda.setPrice(comanda.getPrice()-priceProduct);
                        viewModel.updateComanda(comanda);
                        recuperaComandas();
                        adapter.setComandaList(comandasTicket);


                    }
                }else{
                    //ivMas añadir unidades a comanda
                    comanda.setUnits(comanda.getUnits() + 1);
                    double priceProduct=0;
                    for (Producto p:OrdenaComanda.productos) {
                        if(p.getId()==comanda.getId_product()){
                            priceProduct=p.getPrice();
                        }
                    }
                    comanda.setPrice(comanda.getPrice()+priceProduct);
                    viewModel.updateComanda(comanda);
                    recuperaComandas();
                    adapter.setComandaList(comandasTicket);

                }

            }
        }, this);
    /*
        viewModel.getComandaList().observe(this, new Observer<List<Comanda>>() {
            @Override
            public void onChanged(List<Comanda> comandas) {
                Ticket.comandas=comandas;
                recuperaComandas();
                adapter.setComandaList(comandasTicket);
            }
        });*/

        initComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        //facturas=MainActivity.facturas;
       /* viewModel.getComandaList().observe(this, new Observer<List<Comanda>>() {
            @Override
            public void onChanged(List<Comanda> comandas) {
                Ticket.comandas=comandas;
                recuperaComandas();
                adapter.setComandaList(comandasTicket);
            }
        });*/

    }

    private void recuperaComandas() {
        comandasTicket= new ArrayList<>();
        total=0;
        for (Comanda c:comandas
             ) {
            if(c.getId_ticket()==facturaActual.getId()){
                double priceProduct=0;
                for (Producto p:OrdenaComanda.productos) {
                    if(p.getId()==c.getId_product()){
                        priceProduct=p.getPrice();
                    }
                }
                Log.v("xxxx recupera",c.toString());
                comandasTicket.add(c);
                total+=priceProduct*c.getUnits();
            }
        }
        tvTotal=findViewById(R.id.tvTotal);
        tvTotal.setText("Total: "+total+"€");
    }

/*
    @Override
    public void onBackPressed() {
        //Si se presiona atras con una factura nueva, que se actualice en el main
        if(!yaExiste){
            viewModel.getFacturaList(this).observe(this, new Observer<List<Factura>>() {
                @Override
                public void onChanged(List<Factura> facturas) {
                    //MainActivity.facturas=facturas;
                    Ticket.facturas=facturas;
                }
            });
            new LoadViewTask().execute();
        }
        finish();
    }*/

    private void initComponents() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* viewModel.getFacturaList(Ticket.this).observe(Ticket.this, new Observer<List<Factura>>() {
                    @Override
                    public void onChanged(List<Factura> facturas) {
                        Ticket.facturas=facturas;
                    }
                });*/
                startActivity(new Intent(Ticket.this,OrdenaComanda.class).putExtra("idMesa",idMesa).putExtra("idFactura", facturaActual.getId()).putExtra("idEmpleado", idEmpleado));
            }
        });

        RecyclerView rvList = findViewById(R.id.rvComandas);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ComandaItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvList);

        tvMesa=findViewById(R.id.tvMesa);
        tvMesa.setText(tvMesa.getText()+" Nº "+idMesa);

        btFinTicket=findViewById(R.id.btFin);
        btFinTicket.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                secureCancel();
            }
        });

        fragmento = findViewById(R.id.fragmentLoadingTicket);
        fragmento.setVisibility(View.INVISIBLE);

    }

    private void secureCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_secure);
        builder.setMessage(R.string.message_secure);
        builder.setPositiveButton(R.string.respSi, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finTicket();
                if(!comandasTicket.isEmpty()){
                    printRequest();
                }else{
                    new LoadViewTask().execute();
                }

            }
        });
        builder.setNegativeButton(R.string.respNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void printRequest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_print);
        builder.setMessage(R.string.message_print);

        builder.setPositiveButton(R.string.respSi, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Añado intent a actividad de impresion de factura. 22/11
                Intent printIntent = new Intent(Ticket.this, PrintTicket.class);
                printIntent.putExtra("finF", finF);
                startActivity(printIntent);
                finish();
            }
        });

        builder.setNegativeButton(R.string.respNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new LoadViewTask().execute();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void finTicket() {
        boolean fin=false;
        finF=null;
        for (Factura f:facturas) {
            if(f.getId()==facturaActual.getId() && f.getId_employee_finish()==4){
                fin=true;
                finF=f;
            }
        }
        if(fin){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            finF.setFinish_time(LocalDateTime.now().format(formatter));
            finF.setId_employee_finish(idEmpleado);
            finF.setTotal(total);
            viewModel.updateFactura(finF);
            //Falta calcular el total de todas las comandas de ese ticket
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("idMesa",idMesa);
        outState.putLong("idEmpleado",idEmpleado);
        outState.putBoolean("yaExiste",yaExiste);
        outState.putFloat("total",total);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof ComandaAdapter.MyViewHolder) {
            // get the removed product name to display it in snack bar
            int posProduct=0;
            for (int i = 0; i <OrdenaComanda.productos.size() ; i++) {
                if(OrdenaComanda.productos.get(i).getId()==comandasTicket.get(viewHolder.getAdapterPosition()).getId_product()){
                    posProduct=i;
                }
            }

            String nameProduct =  OrdenaComanda.productos.get(posProduct).getName();
            int units= comandasTicket.get(viewHolder.getAdapterPosition()).getUnits();
            // backup of removed command for undo purpose
            final Comanda deletedComand = comandasTicket.get(viewHolder.getAdapterPosition());
            // remove the command from recycler view
            System.out.println("ITEM BORRADO  "+deletedComand.toString()+ "  id producto"+(int)comandasTicket.get(viewHolder.getAdapterPosition()).getId_product());
            adapter.removeItem(viewHolder.getAdapterPosition());
            //remove command from database and refresh recycler list
            viewModel.deleteCommand(deletedComand);
            // showing snack bar
            Snackbar snackbar = Snackbar
                    .make(this.findViewById(R.id.ticket), "Borrado "+nameProduct + " unidades: "+units, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }



    private class LoadViewTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fragmento.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            synchronized (this){
                //Si se cierra una factura nueva, que se actualice en el main
                if(!yaExiste){
                   // MainActivity.facturas=viewModel.getFacturaList().getValue();
                }
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
            fragmento.setVisibility(View.INVISIBLE);
        }
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Si se presiona atras con una factura nueva, que se actualice en el main
        if(!yaExiste){
            viewModel.getFacturaList(this).observe(this, new Observer<List<Factura>>() {
                @Override
                public void onChanged(List<Factura> facturas) {
                    //MainActivity.facturas=facturas;
                    Ticket.facturas=facturas;
                }
            });
            new LoadViewTask().execute();
        }
        finish();
        return true;
    }*/
}
