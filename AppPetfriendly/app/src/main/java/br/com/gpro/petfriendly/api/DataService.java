package br.com.gpro.petfriendly.api;

import java.util.List;

import br.com.gpro.petfriendly.model.Alimentador;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DataService {

    @GET("/api/listar/alimentadores")
    Call<List<Alimentador>> recuperarAlimentadores();

    @GET("/api/buscar/alimentador/{id}")
    Call<Alimentador> recuperarAlimentadorUnico(@Path("id") long id);

}

