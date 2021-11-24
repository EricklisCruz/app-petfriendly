
package br.com.gpro.petfriendly.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.gpro.petfriendly.R;
import br.com.gpro.petfriendly.adapter.AdapterAlimentador;
import br.com.gpro.petfriendly.api.DataService;
import br.com.gpro.petfriendly.model.Alimentador;
import br.com.gpro.petfriendly.service.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlimentadoresFragment extends Fragment {

    private RecyclerView recyclearView;
    private AdapterAlimentador adapter;
    private List<Alimentador> listaAlimentador = new ArrayList<>();
    private List<Alimentador> pesquisarAlimentador = new ArrayList<>();
    private ProgressBar progressBar;
    private String strAdd = "";
    private TextView textAlimentador;
    private SearchView searchView;

    public AlimentadoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alimentadores, container, false);

        recyclearView = view.findViewById(R.id.recyclerViewAlimentadores);
        progressBar = view.findViewById(R.id.progressBarAlimentadores);
        searchView = view.findViewById(R.id.searchViewPesquisa);

        //listarAlimentador
         ListarAlimentador();

        //Configurar searchview
        searchView.setQueryHint("Buscar Alimentador");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                pesquisarAlimentador(newText);
                return false;
            }
        });

        //Configurar adapter
        adapter = new AdapterAlimentador(getContext(), new ArrayList<>(0));


        //Configurar RecycliarView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclearView.setLayoutManager(layoutManager);
        recyclearView.setHasFixedSize(true);
        recyclearView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        recyclearView.setAdapter(adapter);

        return view;
    }

    public void filterAlimentador(String query, List<Alimentador> list){
        List<Alimentador> filterAlimentador = new ArrayList<>();

        for (Alimentador alimentador : list ){
            if(String.valueOf(alimentador.getId()).toLowerCase()
                    .contains(query.toLowerCase())){
                filterAlimentador.add(alimentador);
            }
        }
        adapter.filterList(filterAlimentador);

    }

    public void pesquisarAlimentador(String idAlimentador) {

        adapter.limparAdapter();

        DataService dataService = RetrofitClientInstance.getRetrofitInstance().create(DataService.class);
        Call<List<Alimentador>> call = dataService.recuperarAlimentadores();

        call.enqueue(new Callback<List<Alimentador>>() {
            @Override
            public void onResponse(Call<List<Alimentador>> call, Response<List<Alimentador>> response) {
                 if(response.isSuccessful()){
                     pesquisarAlimentador = response.body();
                     if(pesquisarAlimentador.isEmpty()){
                         Toast.makeText(getContext(), "Não há postos cadastrados", Toast.LENGTH_SHORT).show();
                     }else{
                         filterAlimentador(idAlimentador, pesquisarAlimentador);
                     }
                 }else {
                     Toast.makeText(getContext(), "Erro ao carregar", Toast.LENGTH_SHORT).show();
                 }

            }

            @Override
            public void onFailure(Call<List<Alimentador>> call, Throwable t) {
            }
        });
    }

    public void ListarAlimentador() {

        progressBar.setVisibility(View.VISIBLE);

        DataService dataService = RetrofitClientInstance.getRetrofitInstance().create(DataService.class);
        Call<List<Alimentador>> call = dataService.recuperarAlimentadores();

        call.enqueue(new Callback<List<Alimentador>>() {
            @Override
            public void onResponse(Call<List<Alimentador>> call, Response<List<Alimentador>> response) {
                if(response.isSuccessful()){
                    listaAlimentador = response.body();
                      if(listaAlimentador.isEmpty()){
                          Toast.makeText(getContext(), "Não há postos cadastrados", Toast.LENGTH_SHORT).show();
                          progressBar.setVisibility(View.GONE);
                    }else {
                          for(Alimentador a : listaAlimentador){
                              adapter.atualizarList(a);
                              progressBar.setVisibility(View.GONE);
                          }
                      }

                }else {
                    Toast.makeText(getContext(), "Erro ao carregar", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<List<Alimentador>> call, Throwable t) {

            }
        });

    }
}
