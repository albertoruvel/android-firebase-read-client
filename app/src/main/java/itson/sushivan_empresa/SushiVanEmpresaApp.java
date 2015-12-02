package itson.sushivan_empresa;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Alberto on 30/11/2015.
 */
public class SushiVanEmpresaApp extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
