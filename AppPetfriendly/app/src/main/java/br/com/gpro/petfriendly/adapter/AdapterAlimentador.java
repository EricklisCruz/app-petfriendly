package br.com.gpro.petfriendly.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gpro.petfriendly.R;
import br.com.gpro.petfriendly.model.Alimentador;
import br.com.gpro.petfriendly.model.LocalizacaoGeocoder;

public class AdapterAlimentador extends RecyclerView.Adapter<AdapterAlimentador.MyViewHolder> {

   private List<Alimentador> listarAlimentador;
   private Context context;
   private LocalizacaoGeocoder localizacaoGeocoder = new LocalizacaoGeocoder();

   public AdapterAlimentador(Context context, List<Alimentador> lista) {
      this.context = context;
      this.listarAlimentador = lista;
   }

   public void limparAdapter(){
      listarAlimentador.clear();
   }
   public void atualizarList(Alimentador alimentador) {
      inserirItem(alimentador);
   }

   // Método responsável por inserir um novo usuário na lista
   //e notificar que há novos itens.
   private void inserirItem(Alimentador alimentador) {
      listarAlimentador.add(alimentador);
      notifyItemInserted(getItemCount());
      notifyDataSetChanged();
   }

   public void removerItem(int position){
      listarAlimentador.remove(position);
      notifyDataSetChanged();
   }

   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View itemLista = LayoutInflater.from(parent.getContext())
              .inflate(R.layout.layout_recyclear_view, parent,false);
      return new MyViewHolder(itemLista);
   }

   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      holder.textAlimentador.setText("Posto: "+String.valueOf(listarAlimentador.get(position).getId()));
      holder.textAgua.setText(listarAlimentador.get(position).getBebedouro()+"%");
      holder.textRacao.setText(listarAlimentador.get(position).getComedouro()+"%");
      holder.progressBarAgua.setProgress(listarAlimentador.get(position).getBebedouro());
      holder.progressBarRacao.setProgress((int) listarAlimentador.get(position).getComedouro());

      LocalizacaoGeocoder l = new  LocalizacaoGeocoder();
      l = localizacaoGeocoder.getInfo(context,listarAlimentador.get(position));

      holder.textCidade.setText("Cidade: "+l.getCidade());
      holder.textRua.setText("Rua: "+l.getRua());
      holder.textBairro.setText("Bairro: "+l.getBairro());

   }

   @Override
   public int getItemCount() {
      return listarAlimentador.size();
   }

   public void filterList(List<Alimentador> filterList){
      listarAlimentador = filterList;
      notifyDataSetChanged();
   }

   public class MyViewHolder extends RecyclerView.ViewHolder{

      public TextView textAlimentador, textAgua, textRacao, textCidade, textRua,textBairro;
      public ImageButton imageButton;
      public ProgressBar progressBarAgua, progressBarRacao;

      public MyViewHolder(@NonNull View itemView) {
         super(itemView);

         textAlimentador = itemView.findViewById(R.id.textAlimentador);
         textAgua = itemView.findViewById(R.id.textAgua);
         textRacao = itemView.findViewById(R.id.textRacao);
         textCidade = itemView.findViewById(R.id.textCidade);
         textRua = itemView.findViewById(R.id.textRua);
         textBairro = itemView.findViewById(R.id.textBairro);
         progressBarAgua = itemView.findViewById(R.id.progress_bar_agua);
         progressBarRacao = itemView.findViewById(R.id.progress_bar_racao);

      }

   }

}
