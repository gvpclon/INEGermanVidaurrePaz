package bo.gob.ine.carrito;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Mainlogin extends AppCompatActivity {
    public static String name=null;
    public static String idcli=null;
    Button btnen,btnlim;
    EditText tvusu,tvcon;

    private SQLiteDatabase db;
    Base_datos base=new Base_datos(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlogin);
        db=base.getWritableDatabase();


        btnen=(Button)findViewById(R.id.btnenviar);
        btnlim=(Button)findViewById(R.id.btnlimpiar);
        tvusu=(EditText)findViewById(R.id.txtUsuario);
        tvcon=(EditText)findViewById(R.id.txtContrasena);


        btnen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario=tvusu.getText().toString().trim();
                String contrasena=tvcon.getText().toString().trim();
                //valido que s¿se ingrese el usuario y la contraseña
                if (usuario.length() == 0 && contrasena.length() == 0) {
                    tvusu.setError("Ingrese su usuario");
                    tvcon.setError("Ingrese su contraseña");
                    return;
                }
                if (usuario.length() == 0) {
                    tvusu.setError("Ingrese su usuario.");
                    return;
                    }
                if (contrasena.length() == 0) {
                    tvcon.setError("Ingrese su contraseña.");
                    return;
                }

                //verifico la contraseña del usuario en base de datos
                String[] rescon = base.conseguircontrasena(usuario);

                if (rescon[0].equals("nousuario")) {
                    Toast.makeText(Mainlogin.this, "Usuario no registrado.", Toast.LENGTH_LONG).show();
                } else {
                    if (rescon[0].toString()!="") {
                        name=usuario;
                        idcli=rescon[1].toString();
                        switch (rescon[0].toString()) {
                            case "adm_1":
                                if (contrasena.equals("321")) {
                                    Intent intent = new Intent(Mainlogin.this, MenuAdministrador.class);
                                    tvcon.setText("");
                                    tvusu.setText("");
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Mainlogin.this, "Contraseña incorrecta.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                if (contrasena.equals(rescon[0])) {
                                    String tipo = base.conseguirtipo(usuario);
                                    if (tipo.equals("1")) {
                                        Intent intent = new Intent(Mainlogin.this, MenuAdministrador.class);
                                        tvcon.setText("");
                                        tvusu.setText("");
                                        finish();
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Mainlogin.this, MenuCliente.class);
                                        tvcon.setText("");
                                        tvusu.setText("");
                                        finish();
                                        startActivity(intent);
                                    }
                                }else{
                                    Toast.makeText(Mainlogin.this,"Contraseña incorrecta.",Toast.LENGTH_SHORT).show();
                                }
                        }
                    }
                    else
                    {
                        Toast.makeText(Mainlogin.this, "Contraseña incorrecta.", Toast.LENGTH_LONG).show();
                    }



                }
            }
        });
        btnlim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvcon.setText("");
                tvusu.setText("");
            }
        });
    }

}
