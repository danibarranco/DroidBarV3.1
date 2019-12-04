package com.example.droidbarv1.View;

import android.app.Application;

import com.example.droidbarv1.Model.Data.Comanda;
import com.example.droidbarv1.Model.Data.Empleado;
import com.example.droidbarv1.Model.Data.Factura;
import com.example.droidbarv1.Model.Data.Producto;
import com.example.droidbarv1.Model.Repository;
import com.example.droidbarv1.Model.WaitResponseServer;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class MainViewModel extends AndroidViewModel {
    private Repository repository;

    public MainViewModel(@NonNull Application application){
        super(application);
        repository= new Repository();

    }

    public MutableLiveData<List<Empleado>>getEmpleadosList(final WaitResponseServer wait){
        return repository.getLiveEmpleadoList(wait);
    }
    public MutableLiveData<List<Factura>>getFacturaList(final WaitResponseServer wait){
        return repository.getLiveFacturaList(wait);
    }
    public MutableLiveData<List<Comanda>> getComandaList(final WaitResponseServer wait){
        return repository.getLiveComandaList(wait);
    }
    public MutableLiveData<List<Producto>>getProductoList(final WaitResponseServer wait){
        return repository.getLiveProductoList(wait);
    }
    public void addFactura(Factura factura,final WaitResponseServer wait){
        repository.addFactura(factura,wait);
    }

    public void updateFactura(Factura finF) {
        repository.updateFactura(finF);
    }

    public void addComanda(Comanda comanda){
        repository.addComanda(comanda);
    }

    public void updateComanda(Comanda finC){
        repository.updateComanda(finC);
    }

    public void deleteCommand(Comanda deletedComand) {
        repository.deleteCommand(deletedComand);
    }

    public void deleteFactura(Factura deletedFactura) {
        repository.deleteFactura(deletedFactura);
    }
}
