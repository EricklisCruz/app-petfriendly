package br.com.gpro.petfriendly.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import br.com.gpro.petfriendly.R;
import br.com.gpro.petfriendly.api.DataService;
import br.com.gpro.petfriendly.model.Alimentador;
import br.com.gpro.petfriendly.model.LocalizacaoGeocoder;
import br.com.gpro.petfriendly.permissoes.Permissoes;
import br.com.gpro.petfriendly.service.BuscaEnderecoCompleto;
import br.com.gpro.petfriendly.service.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapaAlimentadoresFragment extends Fragment {

    MapView mMapView;
    private GoogleMap mMap;
    private View view;
    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout linearLayoutBSheet;
    private BuscaEnderecoCompleto buscaEnderecoCompleto;
    private LocalizacaoGeocoder localizacaoGeocoder;
    private List<Alimentador> listaAlimentador = new ArrayList<>();


    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;

    private TextView idAlimentador, textCidade, textEndereco, textCep, textBairro, textLatitude,
            textLongetude, textAguaMapa, textRacaoMapa;
    private ProgressBar progressBarAgua, progressBarRacao;

    public MapaAlimentadoresFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mapa_alimentadores, container, false);

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, getActivity(), 1);

        inicializar();

        this.linearLayoutBSheet = view.findViewById(R.id.bottomSheet);
        this.bottomSheetBehavior = BottomSheetBehavior.from(linearLayoutBSheet);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                //Objeto responsável por gerenciar a localização do usuário
                locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        Double latitude = location.getLatitude();
                        Double logentude = location.getLongitude();

                        LatLng myLocation = new LatLng(latitude, logentude);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15.0f));

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            600000,
                            10000,
                            locationListener
                    );

                }

                    mostrarMarcador(mMap);
            }
        });


        return view;
    }

    public void inicializar() {

        idAlimentador = view.findViewById(R.id.idAlimentador);
        textCidade = view.findViewById(R.id.textCidade);
        textEndereco = view.findViewById(R.id.textEndereco);
        textCep = view.findViewById(R.id.textCep);
        textBairro = view.findViewById(R.id.textBairro);
        textLatitude = view.findViewById(R.id.textLatitude);
        textLongetude = view.findViewById(R.id.textLongetude);
        textAguaMapa = view.findViewById(R.id.textAguaMapa);
        textRacaoMapa = view.findViewById(R.id.textRacaoMapa);
        progressBarAgua = view.findViewById(R.id.progress_bar_agua_mapa);
        progressBarRacao = view.findViewById(R.id.progress_bar_racao_mapa);
    }


    public void mostrarMarcador(GoogleMap map) {
        mMap = map;

        DataService dataService = RetrofitClientInstance.getRetrofitInstance().create(DataService.class);
        Call<List<Alimentador>> call = dataService.recuperarAlimentadores();

        call.enqueue(new Callback<List<Alimentador>>() {
            @Override
            public void onResponse(Call<List<Alimentador>> call, Response<List<Alimentador>> response) {
                if (response.isSuccessful()) {
                    listaAlimentador = response.body();
                    for (Alimentador alimentador : listaAlimentador) {
                        LatLng latLng = new LatLng(alimentador.getLatitude(), alimentador.getLongetude());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marcador));

                        Marker m = mMap.addMarker(markerOptions);
                        m.setTag(alimentador);

                       // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                                Alimentador a = (Alimentador) marker.getTag();
                                getInfo(a);
                                return false;
                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Alimentador>> call, Throwable t) {

            }
        });

    }


    public void getInfo(Alimentador alimentador) {

        localizacaoGeocoder = new LocalizacaoGeocoder();
        LocalizacaoGeocoder l = localizacaoGeocoder.getInfo(getContext(), alimentador);

        idAlimentador.setText("Posto: " + alimentador.getId());
        textCidade.setText("Cidade: " + l.getCidade());
        textCep.setText("CEP: " + l.getCep());
        textEndereco.setText("Rua: " + l.getRua());
        textBairro.setText("Bairro: " + l.getBairro());
        textLatitude.setText("Latitude: " + alimentador.getLatitude());
        textLongetude.setText("Longetude: " + alimentador.getLongetude());
        textAguaMapa.setText(""+alimentador.getBebedouro());
        textRacaoMapa.setText(""+alimentador.getComedouro());
        progressBarAgua.setProgress(alimentador.getBebedouro());
        progressBarRacao.setProgress((int) alimentador.getComedouro());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {

            //Permission denied (negada)
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                //Alerta
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                //Recuperar localizacao do usuario
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener
                    );

                }
            }

        }
    }

    private LatLng pegaMyLocation() {
        try {
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            // Getting Current Location

            @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);

            if(location!=null){
                // Getting latitude of the current location
                double latitude = location.getLatitude();

                // Getting longitude of the current location
                double longitude = location.getLongitude();

                LatLng my = new LatLng(latitude, longitude);

                return my;
            }
        }catch(Exception e){
            Log.e("Erro", "(pegaMyLocation) : "+e);
        }


        return null;
    }

    public void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
