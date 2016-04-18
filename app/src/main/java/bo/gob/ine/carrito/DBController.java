package bo.gob.ine.carrito;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andresvasquez on 11/4/16.
 */
public class DBController extends SQLiteOpenHelper
{
    public static final String NOMBREBD = "carrito.sqlite";
    public static final int VERSION = 1;
    //Versión de la base de datos
    public DBController(Context context)
    {
        super(context, NOMBREBD, null, VERSION);

    }

    //Método utilizado cuando se crea la base de datos.
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table productos (_id integer primary key autoincrement not null, producto text, precio double,cantidad integer);");
        db.execSQL("create table cliente (_id integer primary key autoincrement not null, nombres varchar, apellidos varchar, nrodocumento varchar,usuario varchar, contrasena varchar,tip_cliente varchar,fechareg date);");
        db.execSQL("create table pedido (_id integer primary key autoincrement not null, id_producto integer, id_cliente integer,cant_pedido,fechaped varchar);");
        Log.d("Todos los tablas: ", "Se crearon las tablas");
    }

    //Método utilizado cuando se actualiza la base de datos
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
    public String conseguircontrasena(String usuario)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String valor="";
        try {
            Cursor cursor = database.rawQuery("SELECT contrasena FROM cliente WHERE usuario='" + usuario + "'", null);
            if(cursor.getCount()<1)
            {
                if(usuario=="administrador")
                        valor="adm_1";
                else valor="nousuario";
                cursor.close();
            }else{
                cursor.moveToFirst();
                valor= cursor.getString(cursor.getColumnIndex("contrasena"));
                cursor.close();}
        }
        catch (SQLiteException e) {
            if (database.inTransaction())
                database.endTransaction();
            Log.e("Error", e.getMessage().toString() + "primer error");
            valor="nousuario";
        }
        return valor;
    }
    public String conseguirtipo(String usuario)
    {
        String valor="";
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            Cursor cursor = database.rawQuery("SELECT idtipo FROM cliente WHERE usuario='"+usuario+"'", null);
            if(cursor.getCount()<1) // UserName Not Exist
            {
                valor="null";
                cursor.close();
            }
            cursor.moveToFirst();
            valor = cursor.getString(cursor.getColumnIndex("idtipo"));
            cursor.close();
        }
        catch (SQLiteException e) {
            if (database.inTransaction())
                database.endTransaction();
            Log.e("Error", e.getMessage().toString() + "error de cargo persona");
            valor="null";
        }
        return valor;
    }

}