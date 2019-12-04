package com.example.droidbarv1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.droidbarv1.Model.Data.Comanda;
import com.example.droidbarv1.Model.Data.Empleado;
import com.example.droidbarv1.Model.Data.Factura;

import com.example.droidbarv1.Model.WaitResponseServer;
import com.example.droidbarv1.View.HistorialItemTouchHelper;
import com.example.droidbarv1.View.HistorialAdapter;
import com.example.droidbarv1.View.MainViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity implements HistorialItemTouchHelper.RecyclerItemTouchHelperListener {
    private List<Factura> fact;
    private ArrayList<Factura> factTerminadas;
    private MainViewModel viewModel;
    private HistorialAdapter adapter;
    //CREAR LIST DE COMANDAS Y PONERLAS BIEN EN EL ONSWIPE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setTitle(R.string.tituloHistorial);
        setSupportActionBar(toolbar);

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        adapter= new HistorialAdapter(this);
        cargaDatos();

    }

    private void cargaDatos() {
        viewModel.getFacturaList(new WaitResponseServer() {
            @Override
            public void waitingResponse(boolean success, List list) {
                if(success){
                    fact =list;
                    recuperaFactTerminadas();
                    viewModel.getEmpleadosList(new WaitResponseServer() {
                        @Override
                        public void waitingResponse(boolean success, List list) {
                            if (success){
                                adapter.setFacturaEmpleadoList(factTerminadas,list);
                                initComponents();
                            }
                        }
                    });
                }
            }
        });
    }

    private void recuperaFactTerminadas() {
        factTerminadas=new ArrayList<>();
        for (Factura f:fact) {
            if(f.getId_employee_finish()!=4){
                factTerminadas.add(f);
            }
        }
    }

    private void initComponents() {
        RecyclerView rvList = findViewById(R.id.rvHistorial);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new HistorialItemTouchHelper(0, ItemTouchHelper.LEFT, (HistorialItemTouchHelper.RecyclerItemTouchHelperListener) this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvList);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof HistorialAdapter.AdapterViewHolder) {

            int nFact= (int)factTerminadas.get(viewHolder.getAdapterPosition()).getId();

            final Factura deletedFactura = factTerminadas.get(viewHolder.getAdapterPosition());
            // remove the command from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());
            //remove command from database and refresh recycler list
            for (Comanda c:Ticket.comandas
                 ) {
                if(c.getId_ticket()==deletedFactura.getId()){
                    viewModel.deleteCommand(c);
                }
            }
            viewModel.deleteFactura(deletedFactura);
            // showing snack bar
            Snackbar snackbar = Snackbar
                    .make(this.findViewById(R.id.clHistorial), "Factura "+nFact + " borrada", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
