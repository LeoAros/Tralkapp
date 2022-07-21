package com.example.tralkapp.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tralkapp.DB.DBHelper;
import com.example.tralkapp.ENTIDADES.Especie;
import com.example.tralkapp.ENTIDADES.Estado;
import com.example.tralkapp.ENTIDADES.Genero;
import com.example.tralkapp.ListAdapter;
import com.example.tralkapp.ListElement;
import com.example.tralkapp.MainActivity;
import com.example.tralkapp.R;
import com.example.tralkapp.SessionManagement;

import java.util.ArrayList;
import java.util.List;

public class EjemplarFragment extends Fragment {
    DBHelper DB;
    List<ListElement> elements;
    Spinner spinnerEspecie,spinnerEstado,spinnerGenero;
    //Alert dialog (Modal) para guardar ejemplares
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    //Componentes del dialog (edit text y botones)
    private Button btnGuardarEjemplar,btnCancelarEjemplar;
    private EditText edtNombreEjemplar,edtPesoEjemplar,edtRadioEjemplar;
    private ImageButton btnAgregarEjemplar;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_ejemplar,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        btnAgregarEjemplar = view.findViewById(R.id.btnAgregarEjem);
        btnAgregarEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        SessionManagement sessionManagement = new SessionManagement(getContext());
        int userID = sessionManagement.getSession();

        DB = new DBHelper(getContext());
        Cursor resUser = DB.getData("id_tipo","user","id",userID);
        resUser.moveToFirst();
        //Ocultar botón de agregar ejemplar dependiendo del tipo de usuario
        if(resUser.getInt(0) != 1){
            btnAgregarEjemplar.setVisibility(View.INVISIBLE);
        }
    }

    public void init(){
        elements = new ArrayList<>();
        DB = new DBHelper(getContext());
        Cursor res = DB.getAll("ejemplar");
        while (res.moveToNext()){
            elements.add(new ListElement(res.getInt(0),res.getString(1),res.getString(2)+"kg","Ver ficha",res.getInt(3),res.getInt(6)));
        }

        ListAdapter listAdapter = new ListAdapter(elements, getContext(), new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ListElement item) {
                verFicha(item);
            }
        });
        RecyclerView recyclerView = getActivity().findViewById(R.id.listRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapter);
    }

    //crear dialog
    public void createDialog(){
        DB = new DBHelper(getContext());
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View ejemplarPopUpView = getLayoutInflater().inflate(R.layout.popup,null);

        btnGuardarEjemplar = ejemplarPopUpView.findViewById(R.id.btnGuardar);
        btnCancelarEjemplar = ejemplarPopUpView.findViewById(R.id.btnCancelar);
        edtNombreEjemplar = ejemplarPopUpView.findViewById(R.id.edtNombreEjemplar);
        edtPesoEjemplar = ejemplarPopUpView.findViewById(R.id.edtPesoEjemplar);
        edtRadioEjemplar = ejemplarPopUpView.findViewById(R.id.edtRadioCollar);

        dialogBuilder.setView(ejemplarPopUpView);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 1400);

        //spinner especie
        spinnerEspecie = ejemplarPopUpView.findViewById(R.id.spinnerEspecie);
        ArrayList<Especie> especies = new ArrayList<>();
        Cursor res = DB.getAll("especie");
        while(res.moveToNext()){
            especies.add(new Especie(res.getInt(0),res.getString(1)));
        }
        ArrayAdapter<Especie> adapterEspecies = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,especies);
        spinnerEspecie.setAdapter(adapterEspecies);

        //spinner estado
        spinnerEstado = ejemplarPopUpView.findViewById(R.id.spinnerEstado);
        ArrayList<Estado> estados = new ArrayList<>();
        Cursor resEstado = DB.getAll("estado");
        while(resEstado.moveToNext()){
            estados.add(new Estado(resEstado.getInt(0),resEstado.getString(1)));
        }
        ArrayAdapter<Estado> adapterEstado = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,estados);
        spinnerEstado.setAdapter(adapterEstado);

        //spinner genero
        spinnerGenero = ejemplarPopUpView.findViewById(R.id.spinnerGenero);
        ArrayList<Genero> generos = new ArrayList<>();
        Cursor resGenero = DB.getAll("genero");
        while(resGenero.moveToNext()){
            generos.add(new Genero(resGenero.getInt(0),resGenero.getString(1)));
        }
        ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,generos);
        spinnerGenero.setAdapter(adapterGenero);


        btnGuardarEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Especie especie = (Especie) spinnerEspecie.getSelectedItem();
                int id_especie = especie.getId();
                Estado estado = (Estado) spinnerEstado.getSelectedItem();
                int id_estado = estado.getId();
                Genero genero = (Genero) spinnerGenero.getSelectedItem();
                int id_genero = genero.getId();
                String nombreEjemplar = edtNombreEjemplar.getText().toString();
                String pesoEjemplar = edtPesoEjemplar.getText().toString();
//                double pesoEjemplar = Double.parseDouble(edtPesoEjemplar.getText().toString());
                String radioEjemplar = edtRadioEjemplar.getText().toString();
                //validación de campos
                Boolean sw = validar(nombreEjemplar,pesoEjemplar,radioEjemplar);
                if(sw){
                    int checkInsert = DB.insertarEjemplar(nombreEjemplar,pesoEjemplar,id_especie,id_estado,radioEjemplar,id_genero);
                    switch (checkInsert){
                        case 0: {
                            Toast.makeText(getContext(), "Radio collar en uso.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 1: {
                            Toast.makeText(getContext(), "Ocurrió un error, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        case 2: {
                            Toast.makeText(getContext(), "Se agregó un ejemplar correctamente.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            init();
                            break;
                        }
                        default:{
                            Toast.makeText(getContext(), "Ocurrió un error, vuelva a intentarlo.", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Toast.makeText(getContext(),"Rellene todos los campos.",Toast.LENGTH_LONG).show();
                }
                DB.close();
            }
        });

        btnCancelarEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public Boolean validar(String nombre,String peso,String radio){
        Boolean sw = true;
        if(nombre.isEmpty() || peso.isEmpty() || radio.isEmpty()){
            sw = false;
        }
        return sw;
    }

    public void verFicha(ListElement item){
        Bundle datosAEnviar = new Bundle();
        datosAEnviar.putInt("id_ejemplar", item.id);
        datosAEnviar.putInt("id_especie", item.id_especie);
        // Crea el nuevo fragmento y la transacción.
        Fragment nuevoFragmento = new FichaFragment();
        nuevoFragmento.setArguments(datosAEnviar);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, nuevoFragmento);
        transaction.addToBackStack(null);

        // Commit a la transacción
        transaction.commit();

    }

}