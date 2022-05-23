package com.example.tralkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tralkapp.DB.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DBHelper DB;
    ImageView imgAnimal;
    TextView txt1;
    EditText edtMasa;

    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String>> mapChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new DBHelper(this);

        Cursor res = DB.getData("animal");
        res.moveToFirst();

        txt1 =findViewById(R.id.textAnimal);
        txt1.setText(res.getString(1));

        edtMasa = findViewById(R.id.edtMasa);

        imgAnimal = findViewById(R.id.imgAnimal);
        imgAnimal.setImageResource(R.drawable.huemul);

        //expandible LV
        expLV = (ExpandableListView) findViewById(R.id.expLV);
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

    private void cargarDatos(int id,double masa){

        Cursor res = DB.getProcesos(id);

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

        adapter = new ExpLVAdapter(this, listCategorias, mapChild);
        expLV.setAdapter(adapter);
    }

    private Double dosificacion(double masa,String StrConcentracion,String StrDosis){
        Double concentracion = Double.parseDouble(StrConcentracion);
        Double dosis = Double.parseDouble(StrDosis);
        Double resultado = (masa*dosis)/concentracion;
        return resultado;
    }
}