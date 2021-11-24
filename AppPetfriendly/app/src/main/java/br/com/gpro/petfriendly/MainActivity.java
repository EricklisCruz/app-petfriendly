package br.com.gpro.petfriendly;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import br.com.gpro.petfriendly.fragment.AlimentadoresFragment;
import br.com.gpro.petfriendly.fragment.MapaAlimentadoresFragment;
import br.com.gpro.petfriendly.model.Alimentador;

public class MainActivity extends AppCompatActivity {

    private List<Alimentador> listaAlimentador = new ArrayList<>();
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarPrincipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        configuraBottomNavigationEx();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.viewPager, new AlimentadoresFragment()).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void configuraBottomNavigationEx(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bnve);

        //Configurações iniciais no Button Navigator
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

        //habilitar navegação
        habilitarNavegacao(bottomNavigationViewEx);

    }
    public void habilitarNavegacao(BottomNavigationViewEx viewEx){

        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                switch (menuItem.getItemId()){

                    case R.id.ic_alimentadores:
                        fragmentTransaction.replace(R.id.viewPager, new AlimentadoresFragment()).commit();
                        return true;
                    case R.id.ic_mapaAlimentadores:
                        fragmentTransaction.replace(R.id.viewPager, new MapaAlimentadoresFragment()).commit();
                        return true;
                }

                return false;
            }
        });
    }

}
