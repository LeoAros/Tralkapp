package com.example.tralkapp.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tralkapp.R;

public class ContactFragment extends Fragment {
    private EditText edtAsunto,edtTelefono,edtMensaje;
    private Button btnCorreo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        btnCorreo = view.findViewById(R.id.btnCorreo);
        edtAsunto = view.findViewById(R.id.edtAsunto);
        edtTelefono = view.findViewById(R.id.edtTelefono);
        edtMensaje = view.findViewById(R.id.edtMensaje);

        btnCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = "leitoaros18@gmail.com";
                String asunto = edtAsunto.getText().toString();
                String telefono = edtTelefono.getText().toString();
                String mensaje = edtMensaje.getText().toString();
                //validar
                //Defino mi Intent y hago uso del objeto ACTION_SEND
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                // Defino los Strings Email, Asunto y Mensaje con la función putExtra
                intent.putExtra(Intent.EXTRA_EMAIL,
                        new String[] { email });
                intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
                if(telefono.isEmpty()){
                    intent.putExtra(Intent.EXTRA_TEXT, mensaje);
                }else{
                    intent.putExtra(Intent.EXTRA_TEXT, "Teléfono: "+telefono+"\n \n"+mensaje);
                }

                // Establezco el tipo de Intent
                intent.setType("message/rfc822");

                // Lanzo el selector de cliente de Correo
                startActivity(
                        Intent
                                .createChooser(intent,
                                        "Elije un cliente de Correo:"));

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    public void sendGmail(Activity activity, String asunto, String text) {
        Intent gmailIntent = new Intent();
        gmailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        gmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, asunto);
        gmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        try {
            activity.startActivity(gmailIntent);
        } catch(ActivityNotFoundException ex) {
            // handle error
        }
    }
}