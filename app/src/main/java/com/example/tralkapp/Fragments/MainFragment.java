package com.example.tralkapp.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tralkapp.DB.DBHelper;
import com.example.tralkapp.ExpLVAdapter;
import com.example.tralkapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {
    DBHelper DB;
    ImageView imgFondo;
    TextView txt1;
    EditText edtMasa;

    //List view de procesos expandible
    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String>> mapChild;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_main,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        Cursor res = DB.getWhere("especie","id",1);
        res.moveToFirst();

//        txt1 = view.findViewById(R.id.textAnimal);
//        txt1.setText(res.getString(1));

        edtMasa = view.findViewById(R.id.edtMasa);

        imgFondo = view.findViewById(R.id.imgFondo);
        imgFondo.setImageResource(R.drawable.fondo_texto);

        //expandible LV
        expLV = (ExpandableListView) view.findViewById(R.id.expLV);
        listCategorias = new ArrayList<>();
        mapChild = new HashMap<>();

        cargarDatos(res.getInt(0),0);

        edtMasa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!edtMasa.getText().toString().isEmpty()){
                    listCategorias.clear();
                    cargarDatos(res.getInt(0),Double.parseDouble(edtMasa.getText().toString()));
                }else{
                    listCategorias.clear();
                    cargarDatos(res.getInt(0),0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void cargarDatos(int id, double masa){

        Cursor res = DB.getWhere("proceso","id_especie",id);

        while(res.moveToNext()){
            listCategorias.add(res.getString(1));
            Cursor resultado = DB.getDosificacion(res.getInt(0));
            ArrayList<String> list = new ArrayList<>();
            while (resultado.moveToNext()){
                if(masa != 0){
                    list.add(resultado.getString(4)+": "+dosificacion(masa,resultado.getString(2),resultado.getString(3))+"ml");
                }else {
                    list.add(resultado.getString(4));
                }
            }
            int lastId = listCategorias.size() - 1;
            mapChild.put(listCategorias.get(lastId), list);
        }

        adapter = new ExpLVAdapter(getContext(), listCategorias, mapChild);
        expLV.setAdapter(adapter);
    }

    private String dosificacion(double masa,String StrConcentracion,String StrDosis){
        Double concentracion = Double.parseDouble(StrConcentracion);
        Double dosis = Double.parseDouble(StrDosis);

        DecimalFormat formato = new DecimalFormat("#.###");

        Double resultado = (masa*dosis)/concentracion;
        return formato.format(resultado);
    }

}