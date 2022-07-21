package com.example.tralkapp.Fragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tralkapp.DB.DBHelper;
import com.example.tralkapp.ExpLVAdapter;
import com.example.tralkapp.ListAdapterNota;
import com.example.tralkapp.ListAdapterProce;
import com.example.tralkapp.ListNotas;
import com.example.tralkapp.ListProcedimientos;
import com.example.tralkapp.MainActivity;
import com.example.tralkapp.R;
import com.example.tralkapp.SessionManagement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcedimientoFragment extends Fragment {
    DBHelper DB;
    //List view de procesos expandible
    private ExpandableListView expLV;
    private ExpLVAdapter adapter;
    private ArrayList<String> listCategorias;
    private Map<String, ArrayList<String>> mapChild;
    //Alert dialog (Modal) para ingresar notas
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageButton btnAgregarNota, btnPDF,btnEditarProcedimiento;
    private Button btnCancelarNota, btnGuardarNota;
    private EditText edtNombreNota;
    private TextView tvNombreProce, tvNombreEjemplar, tvFechaProce;

    private File pdfFile;

    List<ListNotas> notas;

    //PDF
    private int pageHeight = 1800;
    private int pagewidth = 1000;
    // creating a bitmap variable
    // for storing our images
    Bitmap bmp, scaledbmp;
    // constant code for runtime permissions
    private static final int PERMISSION_REQUEST_CODE = 200;

    String[] informacionFicha = new String[]{"Encargado:", "Fecha:", "Hora inicio:", "Hora término:", "Nombre ejemplar:"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_procedimiento, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        DB = new DBHelper(getContext());
        btnAgregarNota = view.findViewById(R.id.btnAgregarNota);
        btnCancelarNota = view.findViewById(R.id.btnCancelarProce);
        btnEditarProcedimiento = view.findViewById(R.id.btnEditarProcedimiento);
        tvNombreProce = view.findViewById(R.id.tvNombreProcedimiento);
        tvNombreEjemplar = view.findViewById(R.id.tvEjemplarProcedimiento);
        tvFechaProce = view.findViewById(R.id.tvFechaProcedimiento);
        //expandible LV
        expLV = (ExpandableListView) view.findViewById(R.id.expandibleLV);
        listCategorias = new ArrayList<>();
        mapChild = new HashMap<>();

        Bundle datosRecuperados = getArguments();
        int id_ejemplar = datosRecuperados.getInt("id_ejemplar");
        int id_procedimiento = datosRecuperados.getInt("id_procedimiento");
        int id_especie = datosRecuperados.getInt("id_especie");

        Cursor resPeso = DB.getPeso("ejemplar", "id", id_ejemplar);

        resPeso.moveToFirst();

        Double peso = resPeso.getDouble(0);

        cargarDatos(id_especie, peso);

        btnAgregarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialogNota(id_procedimiento);
            }
        });

        initRecyclerNota(id_procedimiento);

        DB = new DBHelper(getContext());
        Cursor res = DB.getDatosProcePdf(id_procedimiento);
        Cursor resEjemplar = DB.getData("nombre", "ejemplar", "id", id_ejemplar);
        res.moveToFirst();
        resEjemplar.moveToFirst();

        String nombreProce = res.getString(0);
        String fechaProce = res.getString(1);
        String nombreEjemplar = resEjemplar.getString(0);

        rellenarCampos(nombreProce, fechaProce, nombreEjemplar);

        SessionManagement sessionManagement = new SessionManagement(getContext());
        int userID = sessionManagement.getSession();

        DB = new DBHelper(getContext());
        Cursor resUser = DB.getData("id_tipo,nombre,apellido","user","id",userID);
        resUser.moveToFirst();

        //Ocultar botón de editar ejemplar y agregar procedimiento si el usuario es asistente
        if(resUser.getInt(0) != 1){
            btnEditarProcedimiento.setVisibility(View.INVISIBLE);
        }

        //PDF
        btnPDF = view.findViewById(R.id.btnPDFProcedimiento);
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.huemul);
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false);

        // Se checkean los permisos de lectura y escritura
        if (!checkPermission()) {
            requestPermission();

        }

        String[] splitDate = fechaProce.split(" ");

        String[] arreglo = new String[]{res.getString(2)+" "+res.getString(3), splitDate[0], splitDate[1], "Por definir", nombreEjemplar};

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePDF(arreglo,id_especie,peso,id_procedimiento);
            }
        });
    }

    private void cargarDatos(int id, double masa) {
        Cursor res = DB.getWhere("proceso", "id_especie", id);
        while (res.moveToNext()) {
            listCategorias.add(res.getString(1));
            Cursor resultado = DB.getDosificacion(res.getInt(0));
            ArrayList<String> list = new ArrayList<>();
            while (resultado.moveToNext()) {
                if (masa != 0) {
                    list.add(resultado.getString(4) + ": " + dosificacion(masa, resultado.getString(2), resultado.getString(3)) + "ml");
                } else {
                    list.add(resultado.getString(4));
                }
            }
            int lastId = listCategorias.size() - 1;
            mapChild.put(listCategorias.get(lastId), list);
        }

        adapter = new ExpLVAdapter(getContext(), listCategorias, mapChild);
        expLV.setAdapter(adapter);
    }

    private String dosificacion(double masa, String StrConcentracion, String StrDosis) {
        Double concentracion = Double.parseDouble(StrConcentracion);
        Double dosis = Double.parseDouble(StrDosis);
        Double resultado = (masa * dosis) / concentracion;
        DecimalFormat formato = new DecimalFormat("#.###");
        return formato.format(resultado);
    }

    public void initRecyclerNota(int id_procedimiento) {
        notas = new ArrayList<>();
        DB = new DBHelper(getContext());
        Cursor res = DB.getOrder("nota", "id_procedimiento", id_procedimiento,"desc");
        while (res.moveToNext()) {
            String[] splitHora = res.getString(2).split(" ");
            notas.add(new ListNotas(res.getInt(0), res.getString(1), splitHora[1], "Ver detalle", res.getInt(3)));
        }

        ListAdapterNota listAdapterNota = new ListAdapterNota(notas, getContext(), new ListAdapterNota.OnItemClickListener() {
            @Override
            public void onItemClick(ListNotas item) {
//                verNota();
            }
        });

        RecyclerView recyclerView = getActivity().findViewById(R.id.recyclerNotas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listAdapterNota);
    }

    public void createDialogNota(int id_procedimiento) {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View notaPopUp = getLayoutInflater().inflate(R.layout.popup_nota, null);
        dialogBuilder.setView(notaPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setLayout(1000, 800);

        btnCancelarNota = notaPopUp.findViewById(R.id.btnCancelarNota);
        btnGuardarNota = notaPopUp.findViewById(R.id.btnGuardarNota);
        edtNombreNota = notaPopUp.findViewById(R.id.edtNombreNota);

        btnCancelarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnGuardarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombreNota = edtNombreNota.getText().toString();
                Boolean sw = validar(nombreNota);
                if (sw) {
                    Boolean checkInsert = DB.insertarNota(nombreNota, id_procedimiento,getContext());
                    if (checkInsert) {
                        Toast.makeText(getContext(), "Se agregó una nota correctamente.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        initRecyclerNota(id_procedimiento);
                    } else {
                        Toast.makeText(getContext(), "Ocurrió un error, vuelva a intentarlo.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Rellene todos los campos.", Toast.LENGTH_LONG).show();
                }
                DB.close();
            }
        });
    }

    public Boolean validar(String nombre) {
        Boolean sw = true;
        if (nombre.isEmpty()) {
            sw = false;
        }
        return sw;
    }

    public void rellenarCampos(String nombreProce, String fechaProce, String nombreEjemplar) {
        tvNombreProce.setText(nombreProce);
        String[] splitFecha = fechaProce.split(" ");
        tvFechaProce.setText(splitFecha[0]);
        tvNombreEjemplar.setText(nombreEjemplar);
    }

    //Funciones PDF

    private boolean checkPermission() {
        // checking of permissions.
        int permission1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        // requesting permissions if not provided.
        ActivityCompat.requestPermissions(getActivity(), new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private void generatePDF(String[] arreglo,int id_especie,double masa,int id_proce) {
        PdfDocument pdfDocument = new PdfDocument();

        Paint paint = new Paint();
        Paint title = new Paint();
        Paint text = new Paint();

        PdfDocument.PageInfo mypageInfo = new PdfDocument.PageInfo.Builder(pagewidth, pageHeight, 1).create();
        PdfDocument.Page myPage = pdfDocument.startPage(mypageInfo);
        Canvas canvas = myPage.getCanvas();

        canvas.drawBitmap(scaledbmp, 56, 40, paint);
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        title.setTextSize(30);
        title.setColor(ContextCompat.getColor(getContext(), R.color.black));
        title.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Planilla de inmovilización química", pagewidth / 2, 100, title);

        //PDF datos
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);

        title.setTextSize(20);
        title.setTextAlign(Paint.Align.LEFT);

        text.setTextSize(20);
        text.setTextAlign(Paint.Align.LEFT);
        text.setColor(Color.BLACK);

        //Tabla de datos de procedimiento
        int starX = 150, starY = 300, endX = pagewidth - starX,middleX = 380;
        canvas.drawLine(starX, starY - 20, endX, starY - 20, paint);
        for (int i = 0; i < 5; i++) {
            canvas.drawText(informacionFicha[i], starX + 5, starY, title);
            canvas.drawText(arreglo[i], 390, starY, paint);
            canvas.drawLine(starX, starY + 3, endX, starY + 3, paint);
            starY += 20;
        }
        canvas.drawLine(starX, 280, starX, 383, paint);
        canvas.drawLine(380, 280, 380, 383, paint);
        canvas.drawLine(endX, 280, endX, 383, paint);

        //Tabla de datos de protocolos y dosis
        int starY2 = 450,starYDosis = 490,contador = 0;
        canvas.drawLine(starX, starY2, endX, starY2, paint);
        canvas.drawLine(starX, starY2+20, endX, starY2+20, paint);
        canvas.drawText("Protocolo",starX+5,starY2+18,title);
        canvas.drawText("Dosis",390,starY2+18,title);
        //Búsqueda de datos protocolos y dosis
        Cursor resProtocolo = DB.getWhere("proceso", "id_especie", id_especie);
        while (resProtocolo.moveToNext()) {
            canvas.drawText(resProtocolo.getString(1),starX+5,starY2+40,title);
            Cursor resultado = DB.getDosificacion(resProtocolo.getInt(0));
            while (resultado.moveToNext()) {
                contador++;
                if (masa != 0) {
                    String texto = resultado.getString(4) + ": " + dosificacion(masa, resultado.getString(2), resultado.getString(3)) + "ml";
                    canvas.drawText(texto,390,starYDosis,text);
                    starYDosis+=20;
                } else {
                    String texto = resultado.getString(4);
                }
            }
            starY2 = starY2+(20*contador);
            canvas.drawLine(starX,starY2+22,endX,starY2+22,paint);
            contador = 0;
        }
        canvas.drawLine(starX,450,starX,starY2+22,paint);
        canvas.drawLine(380,450,380,starY2+22,paint);
        canvas.drawLine(endX,450,endX,starY2+22,paint);

        //Tabla de datos notas del procedimiento
        int starY3 = starY2+130;
        canvas.drawText("Notas",pagewidth/2,starY2+80,title);
        canvas.drawLine(starX,starY2+130,endX,starY2+130,paint);
        canvas.drawLine(starX,starY2+150,endX,starY2+150,paint);
        canvas.drawText("Hora",starX+5,starY2+148,title);
        canvas.drawText("Encargado",starX+105,starY2+148,title);
        canvas.drawText("Detalle",starX+305,starY2+148,title);
        //Búsqueda de datos de notas
        Cursor resNotas = DB.getNotas(id_proce);
        while(resNotas.moveToNext()){
            String[] splitHora = resNotas.getString(1).split(" ");
            canvas.drawText(splitHora[1],starX+5,starY2+168,text);
            canvas.drawText(resNotas.getString(2)+" "+resNotas.getString(3),starX+105,starY2+168,text);
            canvas.drawText(resNotas.getString(0),starX+305,starY2+168,text);
            starY2+=20;
            canvas.drawLine(starX,starY2+150,endX,starY2+150,paint);
        }
        canvas.drawLine(starX,starY3,starX,starY2+150,paint);
        canvas.drawLine(starX+100,starY3,starX+100,starY2+150,paint);
        canvas.drawLine(starX+300,starY3,starX+300,starY2+150,paint);
        canvas.drawLine(endX,starY3,endX,starY2+150,paint);

        pdfDocument.finishPage(myPage);

//        File file = new File(Environment.getExternalStorageDirectory(), "GFG.pdf");

        Cursor resFolder = DB.getFolderPdf(id_proce);
        resFolder.moveToFirst();
        String[] splitFechaFolder = resFolder.getString(1).split(" ");

        ContextWrapper cw = new ContextWrapper(getContext());
        File directory = cw.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, "/"+resFolder.getString(2)+", "+resFolder.getString(0)+": "+splitFechaFolder[0]+".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(getContext(), "PDF generado correctamente.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getContext(), "Error PDF." + e, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        pdfDocument.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                // after requesting permissions we are showing
                // users a toast message of permission granted.
                boolean writeStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (writeStorage && readStorage) {
                    Toast.makeText(getContext(), "Permisos otorgados.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Permisos denegados.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}