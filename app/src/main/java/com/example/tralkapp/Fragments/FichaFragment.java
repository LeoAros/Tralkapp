package com.example.tralkapp.Fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tralkapp.DB.DBHelper;
import com.example.tralkapp.ENTIDADES.Especie;
import com.example.tralkapp.ENTIDADES.Estado;
import com.example.tralkapp.ENTIDADES.Genero;
import com.example.tralkapp.ListAdapter;
import com.example.tralkapp.ListAdapterProce;
import com.example.tralkapp.ListElement;
import com.example.tralkapp.ListProcedimientos;
import com.example.tralkapp.R;
import com.example.tralkapp.SessionManagement;

import java.util.ArrayList;
import java.util.List;


public class FichaFragment extends Fragment {
    DBHelper DB;
    TextView tvNombre,tvPeso,tvGenero,tvEspecie,tvEstado,tvRadio;
    List<ListProcedimientos> procedimientos;
    private ImageView imgFicha,imgGender;
    private ImageButton btnAgregarProce,btnEditarEjemplar;
    //Alert dialog (Modal) para ingresar procedimientos
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnGuardarProce,btnCancelarProce,btnCancelarEditEjemplar,btnModificarEjemplar;
    private EditText edtNombreProce,edtNombreEjemplarEdit,edtPesoEjemplarEditar,edtRadioEjemplarEditar;
    private Spinner spinnerEspecie,spinnerEstado,spinnerGenero;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ficha, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvNombre = view.findViewById(R.id.tvNombreFicha);
        tvEspecie = view.findViewById(R.id.tvEspecieFicha);
        tvPeso = view.findViewById(R.id.tvPesoFicha);
        tvEstado = view.findViewById(R.id.tvEstadoFicha);
        tvRadio = view.findViewById(R.id.tvRadioFicha);
        btnAgregarProce = view.findViewById(R.id.btnAgregarProce);
        btnEditarEjemplar = view.findViewById(R.id.btnEditarFicha);

        imgFicha = view.findViewById(R.id.imgFicha);
        imgFicha.setImageResource(R.drawable.huemul);

        imgGender = view.findViewById(R.id.imageGender);

        Bundle datosRecuperados = getArguments();
        int id_ejemplar = datosRecuperados.getInt("id_ejemplar");
        int id_especie = datosRecuperados.getInt("id_especie");

        DB = new DBHelper(getContext());
        Cursor res = DB.getFicha(id_ejemplar);
        res.moveToFirst();

        String[] arreglo_ejemplar = new String[]{res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)};

        if(Integer.valueOf(res.getString(6)) == 1){
            imgGender.setColorFilter(Color.parseColor("#02a4d3"), PorterDuff.Mode.SRC_ATOP);
            imgGender.setImageResource(R.drawable.ic_male);
        }else{
            imgGender.setImageResource(R.drawable.ic_female);
            imgGender.setColorFilter(Color.parseColor("#e6456f"), PorterDuff.Mode.SRC_ATOP);
        }

        rellenarCampos(arreglo_ejemplar);

        initRecyclerProce(id_ejemplar,id_especie);

        btnAgregarProce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogProce(id_ejemplar,id_especie);
            }
        });

        btnEditarEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor resDatosEjemplar = DB.getFicha(id_ejemplar);
                resDatosEjemplar.moveToFirst();

                String[] arreglo_ejemplar_editar = new String[]{resDatosEjemplar.getString(0),resDatosEjemplar.getString(1),resDatosEjemplar.getString(2),resDatosEjemplar.getString(3),resDatosEjemplar.getString(4),resDatosEjemplar.getString(5)};
                createDialogEditarEjemplar(arreglo_ejemplar_editar);
            }
        });

        SessionManagement sessionManagement = new SessionManagement(getContext());
        int userID = sessionManagement.getSession();

        DB = new DBHelper(getContext());
        Cursor resUser = DB.getData("id_tipo","user","id",userID);
        resUser.moveToFirst();

        //Ocultar botón de editar ejemplar y agregar procedimiento dependiendo del tipo de usuario
        if(resUser.getInt(0) != 1){
            btnAgregarProce.setVisibility(View.INVISIBLE);
            btnEditarEjemplar.setVisibility(View.INVISIBLE);
        }
    }

    public void rellenarCampos(String[] arreglo){
            tvNombre.setText(arreglo[1]);
            tvEspecie.setText(arreglo[2]);
            tvPeso.setText(arreglo[3]+"kg");
            tvEstado.setText(arreglo[4]);
            tvRadio.setText("#"+arreglo[5]);
    }

    public void initRecyclerProce(int id_ejemplar,int id_especie){
        procedimientos = new ArrayList<>();
        DB = new DBHelper(getContext());
        Cursor res = DB.getOrder("procedimiento","id_ejemplar",id_ejemplar,"desc");
        while (res.moveToNext()){
            String[] splitFecha = res.getString(2).split(" ");
            procedimientos.add(new ListProcedimientos(res.getInt(0),res.getString(1),splitFecha[0],"Ver detalle",res.getInt(3)));
        }

        ListAdapterProce listAdapterProce = new ListAdapterProce(procedimientos, getContext(), new ListAdapterProce.OnItemClickListener() {
            @Override
            public void onItemClick(ListProcedimientos item) {
                verProcedimiento(item,id_especie);
            }
        });

        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerProce);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterProce);
    }

    public void createDialogProce(int id_ejemplar,int id_especie){

        DB = new DBHelper(getContext());
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View procedimientoPopUp = getLayoutInflater().inflate(R.layout.popup_proce,null);
        btnCancelarProce = procedimientoPopUp.findViewById(R.id.btnCancelarProce);
        btnGuardarProce = procedimientoPopUp.findViewById(R.id.btnGuardarProce);
        edtNombreProce = procedimientoPopUp.findViewById(R.id.edtNombreProce);
        dialogBuilder.setView(procedimientoPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 800);

        btnCancelarProce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnGuardarProce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreProce = edtNombreProce.getText().toString();
                Boolean sw = validar(nombreProce);
                if(sw){
                    Boolean checkInsert = DB.insertarProcedimiento(nombreProce,id_ejemplar,getContext());
                    if(checkInsert){
                        Toast.makeText(getContext(),"Se agregó un procedimiento correctamente.",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        initRecyclerProce(id_ejemplar,id_especie);
                    }else{
                        Toast.makeText(getContext(),"Ocurrió un error, vuelva a intentarlo.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Rellene todos los campos.",Toast.LENGTH_LONG).show();
                }
                DB.close();
            }
        });
    }

    public void createDialogEditarEjemplar(String[] arreglo){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View editarEjemplarPopUp = getLayoutInflater().inflate(R.layout.popup_edit_ejemplar,null);
        dialogBuilder.setView(editarEjemplarPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 1400);
        btnCancelarEditEjemplar = editarEjemplarPopUp.findViewById(R.id.btnCancelarEditarEjemplar);
        edtNombreEjemplarEdit = editarEjemplarPopUp.findViewById(R.id.edtNombreEjemplarEditar);
        edtPesoEjemplarEditar = editarEjemplarPopUp.findViewById(R.id.edtPesoEjemplarEditar);
        edtRadioEjemplarEditar = editarEjemplarPopUp.findViewById(R.id.edtRadioCollarEditar);
        btnModificarEjemplar = editarEjemplarPopUp.findViewById(R.id.btnEditarEjemplar);
        btnCancelarEditEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        edtNombreEjemplarEdit.setText(arreglo[1]);
        edtPesoEjemplarEditar.setText(arreglo[3]);
        edtRadioEjemplarEditar.setText(arreglo[5]);
        //spinner especie
        spinnerEspecie = editarEjemplarPopUp.findViewById(R.id.spinnerEspecieEditar);
        ArrayList<Especie> especies = new ArrayList<>();
        Cursor res = DB.getAll("especie");
        while(res.moveToNext()){
            especies.add(new Especie(res.getInt(0),res.getString(1)));
        }
        ArrayAdapter<Especie> adapterEspecies = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,especies);
        spinnerEspecie.setAdapter(adapterEspecies);
        //spinner estado
        spinnerEstado = editarEjemplarPopUp.findViewById(R.id.spinnerEstadoEditar);
        ArrayList<Estado> estados = new ArrayList<>();
        Cursor resEstado = DB.getAll("estado");
        while(resEstado.moveToNext()){
            estados.add(new Estado(resEstado.getInt(0),resEstado.getString(1)));
        }
        ArrayAdapter<Estado> adapterEstado = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,estados);
        spinnerEstado.setAdapter(adapterEstado);
        //spinner genero
        spinnerGenero = editarEjemplarPopUp.findViewById(R.id.spinnerGeneroEditar);
        ArrayList<Genero> generos = new ArrayList<>();
        Cursor resGenero = DB.getAll("genero");
        while(resGenero.moveToNext()){
            generos.add(new Genero(resGenero.getInt(0),resGenero.getString(1)));
        }
        ArrayAdapter<Genero> adapterGenero = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,generos);
        spinnerGenero.setAdapter(adapterGenero);

        btnModificarEjemplar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Especie especie = (Especie) spinnerEspecie.getSelectedItem();
                int id_especie = especie.getId();
                Estado estado = (Estado) spinnerEstado.getSelectedItem();
                int id_estado = estado.getId();
                Genero genero = (Genero) spinnerGenero.getSelectedItem();
                int id_genero = genero.getId();
                String nombreEjemplar = edtNombreEjemplarEdit.getText().toString();
                double pesoEjemplar = Double.parseDouble(edtPesoEjemplarEditar.getText().toString());
                String radioEjemplar = edtRadioEjemplarEditar.getText().toString();
                //validación de campos
                Boolean sw = validarEjemplar(nombreEjemplar,pesoEjemplar,radioEjemplar);
                if(sw){
                    Boolean checkUpdate = DB.editarEjemplar(arreglo[0],nombreEjemplar,pesoEjemplar,id_especie,id_estado,radioEjemplar,id_genero);
                    if(checkUpdate){
                        Toast.makeText(getContext(),"Se modificó un ejemplar correctamente.",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        reload(arreglo[0]);
                    }else{
                        Toast.makeText(getContext(),"Ocurrió un error, vuelva a intentarlo.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(),"Rellene todos los campos.",Toast.LENGTH_LONG).show();
                }
                DB.close();
            }
        });
    }

    public Boolean validar(String nombre){
        Boolean sw = true;
        if(nombre.isEmpty()){
            sw = false;
        }
        return sw;
    }

    public Boolean validarEjemplar(String nombre,Double peso,String radio){
        Boolean sw = true;
        if(nombre.isEmpty() || peso.toString().isEmpty() || radio.isEmpty()){
            sw = false;
        }
        return sw;
    }

    public void verProcedimiento(ListProcedimientos item,int id_especie){
        Bundle datosAEnviar = new Bundle();
        datosAEnviar.putInt("id_procedimiento", item.id);
        datosAEnviar.putInt("id_ejemplar", item.id_ejemplar);
        datosAEnviar.putInt("id_especie",id_especie);
        // Crea el nuevo fragmento y la transacción.
        Fragment nuevoFragmento = new ProcedimientoFragment();
        nuevoFragmento.setArguments(datosAEnviar);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, nuevoFragmento);
        transaction.addToBackStack(null);

        // Commit a la transacción
        transaction.commit();
    }

    public void reload(String id_ejemplar){
        DB = new DBHelper(getContext());
        Cursor res = DB.getFicha(Integer.valueOf(id_ejemplar));
        res.moveToFirst();
        String[] arreglo = new String[]{res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)};
        tvNombre.setText(arreglo[1]);
        tvEspecie.setText(arreglo[2]);
        tvPeso.setText(arreglo[3]+"kg");
        tvEstado.setText(arreglo[4]);
        tvRadio.setText("#"+arreglo[5]);

        if(Integer.valueOf(res.getString(6)) == 1){
            imgGender.setColorFilter(Color.parseColor("#02a4d3"), PorterDuff.Mode.SRC_ATOP);
            imgGender.setImageResource(R.drawable.ic_male);
        }else{
            imgGender.setImageResource(R.drawable.ic_female);
            imgGender.setColorFilter(Color.parseColor("#e6456f"), PorterDuff.Mode.SRC_ATOP);
        }
    }
}