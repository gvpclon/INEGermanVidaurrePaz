package bo.gob.ine.carrito;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import bo.gob.ine.carrito.R;

public class MainPedido extends AppCompatActivity {
    ArrayList<HashMap<String, String>> myList;
    Base_datos base=new Base_datos(this);
    TextView tvtit;
    private Context context;
    private SQLiteDatabase db;
    private String valcan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpedido);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        tvtit=(TextView)findViewById(R.id.tvtitulo);
        ListView lista=(ListView)findViewById(R.id.lista);
        myList= base.conseguirproductos();
        if (myList.size() != 0) {
            //Agregamos algunas filas
            ListAdapter adapter = new SimpleAdapter(MainPedido.this, myList,
                    R.layout.vista_producto, new String[]{"_id","producto","precio","cantidad"}, new int[]{
                    R.id.idprod,R.id.titulo, R.id.precio,R.id.cantidad,});
            lista.setAdapter(adapter);
        }else{
            tvtit.setText("No existe productos.");
        }
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> array, View vista, int posicion,
                                    long id) {
                final TextView idprodu = (TextView) vista.findViewById(R.id.idprod);
                TextView produc = (TextView) vista.findViewById(R.id.titulo);
                TextView canti = (TextView) vista.findViewById(R.id.cantidad);
                TextView preci = (TextView) vista.findViewById(R.id.precio);
                //Toast.makeText(MainPedido.this,"Valor del id."+produc.getText().toString(),Toast.LENGTH_SHORT).show();
                final Dialog dialog = new Dialog(MainPedido.this);
                dialog.setContentView(R.layout.activity_cantpedido);
                dialog.setTitle("Cantidad solicitada:");
                final TextView tvpro1=(TextView) dialog.findViewById(R.id.tvpro);
                final TextView tvcan1=(TextView) dialog.findViewById(R.id.tvcan);
                final TextView tvcos1=(TextView) dialog.findViewById(R.id.tvcos);
                final EditText etcanti=(EditText) dialog.findViewById(R.id.etcanu);
                //valcan=etcanti.getText().toString();
                Button aceptar=(Button) dialog.findViewById(R.id.btnaceptarp);
                Button cancelar=(Button) dialog.findViewById(R.id.btncancelarp);
                tvpro1.setText(produc.getText().toString());
                tvcan1.setText(canti.getText().toString());
                tvcos1.setText(preci.getText().toString());


                aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            String[] valores1=tvcan1.getText().toString().split(":");
                            if(etcanti.getText().toString().length()==0){ etcanti.setError("Ingrese la cantidad de unidades."); return; }else{ etcanti.setError(null);}
                            if(Integer.parseInt(etcanti.getText().toString())>Integer.parseInt(valores1[1].toString().trim())){ etcanti.setError("La cantidad ingresada es superior a la existente."); return;}else{ etcanti.setError(null);}

                            Base_datos base = new Base_datos(context);
                            //para guardar el pedido
                            String[] variables = {"id_producto", "id_cliente", "cant_pedido", "fechapedido"};
                            String[] valores = {idprodu.getText().toString(), idcliente(), etcanti.getText().toString(), fecha()};
                            base.insert("pedido", variables, valores);
                            base.close();
                            Toast.makeText(getApplicationContext(), "Producto pedido con éxito.", Toast.LENGTH_SHORT).show();
                            reiniciarActividad();
                        }

                });
                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        etcanti.setText("");
                    }
                });
                dialog.show();
            }
        });



    }
    public String fecha(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechareg = sdf.format(new java.util.Date());
        return fechareg;
    }
    public String usuario(){
        Mainlogin Mainlogin=new Mainlogin();
        String incoming_name =Mainlogin.name;//I can not use like that ?
        return incoming_name;
    }
    public String idcliente(){
        Mainlogin Mainlogin=new Mainlogin();
        String incoming_id =Mainlogin.idcli;//I can not use like that ?
        return incoming_id;
    }
    private void reiniciarActividad() {
        Intent a=new Intent(context,MainPedido.class);
        finish();
        startActivity(a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salir:
                Intent a = new Intent(context, Mainlogin.class);
                finish();
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
