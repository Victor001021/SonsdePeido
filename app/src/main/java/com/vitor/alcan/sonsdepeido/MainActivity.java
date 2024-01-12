package com.vitor.alcan.sonsdepeido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer mediaPlayer;
    private RecyclerView recyclerView;

    private Adapter adapter;
    private AdView madView;
    private InterstitialAd mInterstitialAd;



    private List<Sons> listaPeidos = new ArrayList<>();
    private int opcoes = 0, i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Sons sons = new Sons();


        recyclerView = findViewById(R.id.recyclerView);


        this.carregarListas();


        //configura recycler view
        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);*/

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        adapter = new Adapter(listaPeidos);
        recyclerView.setAdapter(adapter);

        setupRecyclerView();


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createAd();

            }
        });


        madView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        madView.loadAd(adRequest);


    }

    private void setupRecyclerView() {
        // Carregar lista de peidos
        carregarListas();

        // Configurar adaptador
        adapter = new Adapter(listaPeidos, null);  // O segundo parâmetro (InterstitialAd) será atualizado posteriormente
        recyclerView.setAdapter(adapter);
    }

    public void carregarListas() {
        for (int i = 1; i <= 47; i++) {
            Sons sons = new Sons(i);
            listaPeidos.add(sons);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;

        }
        madView.destroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        madView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        madView.resume();

    }

    private void createAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        createInterstitialAd(adRequest);

    }

    private void createInterstitialAd(AdRequest adRequest) {

        InterstitialAd.load(this, "ca-app-pub-1530737633748031/7440" +
                "" +
                "" +
                "848348", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");

                adapter.setInterstitialAd(mInterstitialAd);


                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        createAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("TAG", loadAdError.getMessage());
                mInterstitialAd = null;
                adapter.setInterstitialAd(null);


            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                compartilhar();
                break;

            case R.id.timer:
                mudar();
                break;

            case R.id.avaliar:
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));

                }
        }


        return super.onOptionsItemSelected(item);
    }

    public void compartilhar() {

        String link = "Divirta-se com esse app!\n\n";

        link = link + "https://play.google.com/store/apps/details?id=com.vitor.alcan.sonsdepeido";

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_SUBJECT, new String("Sons de Peido"));
        intent.putExtra(Intent.EXTRA_TEXT, link);


        intent.setType("text/plain");

        startActivity(Intent.createChooser(intent, "Compartilhar"));

    }

    public void mudar() {
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        startActivity(intent);
    }
}
