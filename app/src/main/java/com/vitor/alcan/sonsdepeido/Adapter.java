package com.vitor.alcan.sonsdepeido;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private List<Sons> peidos;
    private int opcoes = 0, i = 0;
    private MediaPlayer mediaPlayer;
    private int[] recursosAudio = {
            R.raw.peido1, R.raw.peido2, R.raw.peido3, R.raw.peido4, R.raw.peido5,
            R.raw.peido6, R.raw.peido7, R.raw.peido8, R.raw.peido9, R.raw.peido10,
            R.raw.peido11, R.raw.peido12, R.raw.peido13, R.raw.peido14, R.raw.peido15,
            R.raw.peido16, R.raw.peido17, R.raw.peido18, R.raw.peido19, R.raw.peido20,
            R.raw.peido21, R.raw.peido22, R.raw.peido23, R.raw.peido24, R.raw.peido25,
            R.raw.peido26, R.raw.peido27, R.raw.peido28, R.raw.peido29, R.raw.peido30,
            R.raw.peido31, R.raw.peido32, R.raw.peido33, R.raw.peido34, R.raw.peido35,
            R.raw.peido36, R.raw.peido37, R.raw.peido38, R.raw.peido39, R.raw.peido40,
            R.raw.peido41, R.raw.peido42, R.raw.peido43, R.raw.peido44, R.raw.peido45,
            R.raw.peido46, R.raw.peido47
    };
    private InterstitialAd interstitialAd;


    public Adapter(List<Sons> peidosL, InterstitialAd interstitialAd) {
        this.peidos = peidosL;
        this.interstitialAd = interstitialAd;
    }

    public Adapter(List<Sons> peidos) {
        this.peidos = peidos;
    }

    public Adapter(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View opcoes = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista, parent, false);


        return new MyViewHolder(opcoes);
    }

    public void setInterstitialAd(InterstitialAd interstitialAd) {
        this.interstitialAd = interstitialAd;
        notifyDataSetChanged();  // Notificar o RecyclerView sobre as alterações
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Sons sons = peidos.get(position);
        holder.textView.setText(String.valueOf(sons.getSons()));

        holder.imageButton.setOnClickListener(v -> {
            Sons som = peidos.get(position);
            opcoes = sons.getSons();
            tocar(v.getContext());

        });




        holder.imageButtonCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sons som = peidos.get(position);


                String audioResourceName = "peido" + som.getSons();
                int audioResourceId = view.getResources().getIdentifier(audioResourceName, "raw", view.getContext().getPackageName());

                // Criar um InputStream para ler o arquivo de áudio raw
                InputStream inputStream = view.getResources().openRawResource(audioResourceId);

                // Criar o caminho do arquivo temporário para o áudio
                File audioCachePath = new File(view.getContext().getCacheDir(), "audios");
                audioCachePath.mkdirs();
                File audioFile = new File(audioCachePath, "audio.mp3"); // você pode escolher a extensão adequada

                try {
                    // Copiar o conteúdo do InputStream para o arquivo de áudio
                    OutputStream outputStream = new FileOutputStream(audioFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();

                    // Obter a URI do arquivo de áudio temporário
                    Uri audioContentUri = FileProvider.getUriForFile(view.getContext(),
                            "com.vitor.alcan.sonsdepeido.fileprovider", audioFile);

                    // Criar um Intent para compartilhar no WhatsApp
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("audio/*");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, audioContentUri);
                    view.getContext().startActivity(Intent.createChooser(shareIntent, "Compartilhar via"));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("erro", "porra: " + e.getMessage());
                }


            }
        });


    }

    public void tocar(Context c) {
        i++;

        if (opcoes >= 1 && opcoes <= recursosAudio.length) {
            mediaPlayer = MediaPlayer.create(c, recursosAudio[opcoes - 1]);
            if (mediaPlayer != null) {
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(mediaPlayer -> {
                    mediaPlayer.release();
                    if (i == 8 && interstitialAd != null) {
                        interstitialAd.show((Activity) c);
                        i = 0;
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return peidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageButton imageButton, imageButtonCompartilhar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            textView = itemView.findViewById(R.id.textView7);
            imageButton = itemView.findViewById(R.id.imageButton);
            imageButtonCompartilhar = itemView.findViewById(R.id.imageButtonCompartilhar);


        }

    }


}
