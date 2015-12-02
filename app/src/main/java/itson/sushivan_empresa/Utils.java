package itson.sushivan_empresa;

import android.content.Context;

import com.firebase.client.Firebase;
import com.google.gson.Gson;

import itson.sushivan_empresa.modelo.Pedido;
import itson.sushivan_empresa.modelo.Producto;

/**
 * Created by Alberto on 30/11/2015.
 */
public class Utils {

    static final String PREFS = "shushi-van-empresa-prefs";
    static final String PEDIDO = "shushi-van-empresa-pedido-tmp";
    private static Firebase firebase;

    public static Firebase getFirebase(){
        if(firebase == null)
            firebase = new Firebase("https://itson-sushivan.firebaseio.com/");
        return firebase;
    }

    public static void guardaProducto(Context context, Producto p){
        Gson gson = new Gson();
        String value = gson.toJson(p);
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(PEDIDO, value)
                .commit();
    }

    public static Producto obtenProductoGuardado(Context context){
        return new Gson().fromJson(context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        .getString(PEDIDO, ""), Producto.class);
    }
}
