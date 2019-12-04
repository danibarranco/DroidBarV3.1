package com.example.droidbarv1.Model;

import android.util.Log;


import com.example.droidbarv1.MainActivity;
import com.example.droidbarv1.Model.Data.Comanda;
import com.example.droidbarv1.Model.Data.Empleado;
import com.example.droidbarv1.Model.Data.Factura;
import com.example.droidbarv1.Model.Data.Producto;
import com.example.droidbarv1.Model.Rest.DroidBarClient;
import com.example.droidbarv1.Ticket;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.droidbarv1.MainActivity.TAG;

public class Repository {


    private DroidBarClient apiClient;
    private MutableLiveData<List<Factura>> mutableFacturaList =new MutableLiveData<>();
    private MutableLiveData<List<Comanda>> mutableComandaList =new MutableLiveData<>();
    private MutableLiveData<List<Producto>> mutableProductoList =new MutableLiveData<>();

    public Repository() {
        retrieveApiClient();
        //fetchFacturaList();
        //fetchComandaList();
        //fetchProductoList();
    }
    private void retrieveApiClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://informatica.ieszaidinvergeles.org:8046/DroidBar/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiClient=retrofit.create(DroidBarClient.class);

    }

    private void fetchProductoList(final WaitResponseServer wait) {
        Call<ArrayList<Producto>> call = apiClient.getProducto();
        call.enqueue(new Callback<ArrayList<Producto>>() {
            @Override
            public void onResponse(Call<ArrayList<Producto>> call, Response<ArrayList<Producto>> response) {
                if(response.body()!=null) {
                    Log.v(TAG, response.body().toString());
                    mutableProductoList.setValue(response.body());
                    wait.waitingResponse(true,response.body());
                }else{
                    wait.waitingResponse(false,null);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Producto>> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
                mutableProductoList=new MutableLiveData<>();
            }
        });
    }

    private void fetchComandaList(final WaitResponseServer wait) {
        Call<ArrayList<Comanda>> call = apiClient.getComanda();
        call.enqueue(new Callback<ArrayList<Comanda>>() {
            @Override
            public void onResponse(Call<ArrayList<Comanda>> call, Response<ArrayList<Comanda>> response) {
                if(response.body()!=null){
                    Log.v(TAG, response.body().toString());
                    mutableComandaList.setValue(response.body());
                    //Ticket.comandas = mutableComandaList.getValue();
                    wait.waitingResponse(true,response.body());
                }else{
                    wait.waitingResponse(false,null);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comanda>> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
                mutableComandaList=new MutableLiveData<>();
            }
        });
    }

    public void fetchFacturaList(final WaitResponseServer wait) {
        Call<ArrayList<Factura>> call = apiClient.getFactura();
        call.enqueue(new Callback<ArrayList<Factura>>() {
            @Override
            public void onResponse(Call<ArrayList<Factura>> call, Response<ArrayList<Factura>> response) {
                if(response.body()!=null){
                    Log.v(TAG, response.body().toString());
                    mutableFacturaList.setValue(response.body());
                    //MainActivity.facturas=mutableFacturaList.getValue();
                    wait.waitingResponse(true,response.body());
                }else{
                    wait.waitingResponse(false,null);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Factura>> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
                mutableFacturaList=new MutableLiveData<>();
            }
        });
    }


    public MutableLiveData<List<Empleado>> getLiveEmpleadoList(final WaitResponseServer wait){
        final MutableLiveData<List<Empleado>> mutableEmpleadoList =new MutableLiveData<>();
        Call<ArrayList<Empleado>> call = apiClient.getEmpleado();
        call.enqueue(new Callback<ArrayList<Empleado>>() {
            @Override
            public void onResponse(Call<ArrayList<Empleado>> call, Response<ArrayList<Empleado>> response) {
                if(response.body()!=null){
                    Log.v(TAG, response.body().toString());
                    mutableEmpleadoList.setValue(response.body());
                    wait.waitingResponse(true,response.body());
                }else{
                    wait.waitingResponse(false,null);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Empleado>> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
        return mutableEmpleadoList;
    }

    public MutableLiveData<List<Factura>> getLiveFacturaList(final WaitResponseServer wait){
        fetchFacturaList(wait);

        return mutableFacturaList;
    }
    public MutableLiveData<List<Comanda>> getLiveComandaList(final WaitResponseServer wait) {
        fetchComandaList(wait);
        return mutableComandaList;
    }
    public MutableLiveData<List<Producto>> getLiveProductoList(final WaitResponseServer wait) {
        fetchProductoList(wait);
        return mutableProductoList;
    }

    public void addFactura(Factura factura, final WaitResponseServer wait) {
        Call<Long> call = apiClient.postFactura(factura);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                    System.out.println("Añade");
                    wait.waitingResponse(true,null);
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void updateFactura(Factura finF) {
        Call<Integer> call = apiClient.putFactura(finF.getId(),finF);
        //fetchFacturaList();
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                //fetchFacturaList();
                System.out.println("Actualiza");

            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void addComanda(Comanda comanda){
        Call<Long> call = apiClient.postComanda(comanda);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                //fetchComandaList();
                System.out.println("Añade");
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });

    }

    public void updateComanda(Comanda finC) {
        Call<Integer> call = apiClient.putComanda(finC.getId(),finC);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                //fetchComandaList();
                System.out.println("Actualiza");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }



    public void deleteCommand(Comanda deletedComand) {
        //Borrar comanda
        Call<Boolean> call = apiClient.deleteComanda(deletedComand.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                //fetchComandaList();
                System.out.println("Borrado");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void deleteFactura(Factura deletedFactura) {
        Call<Boolean> call = apiClient.deleteFactura(deletedFactura.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                //fetchFacturaList();
                System.out.println("Borrado");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }
}
