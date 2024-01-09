package com.vitor.alcan.sonsdepeido;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

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

public class MainActivity2 extends AppCompatActivity {
    private int segundos, secRestante;
    private SeekBar seekBar;
    private TextView tempo, tempo2;
    private Button botao;
    private MediaPlayer mediaPlayer;
    private Spinner spinner;
    private int opcoes = 0;

    int i = 0;


    /*Anuncios*/
    private AdView madView;
    private InterstitialAd mInterstitialAd;
    private int[] audioResources = {R.raw.peido46, R.raw.peido2, R.raw.peido3, R.raw.peido4, R.raw.peido23, R.raw.peido6,
            R.raw.peido7, R.raw.peido8, R.raw.peido25, R.raw.peido10, R.raw.peido11, R.raw.peido12,
            R.raw.peido43, R.raw.peido14, R.raw.peido15, R.raw.peido16, R.raw.peido17, R.raw.peido18,
            R.raw.peido19, R.raw.peido20, R.raw.peido21, R.raw.peido22, R.raw.peido5, R.raw.peido24,
            R.raw.peido9, R.raw.peido26, R.raw.peido27, R.raw.peido28, R.raw.peido29, R.raw.peido30,
            R.raw.peido31, R.raw.peido32, R.raw.peido33, R.raw.peido34, R.raw.peido35, R.raw.peido36,
            R.raw.peido37, R.raw.peido38, R.raw.peido39, R.raw.peido40, R.raw.peido41, R.raw.peido42,
            R.raw.peido13, R.raw.peido44, R.raw.peido45, R.raw.peido1};

    private static final int[] PEIDO_VALUES = {1, 2, 3, 4, 23, 6, 7, 8, 25, 10, 11, 12, 43, 14, 15, 16, 17, 18, 19, 20, 21,
            22, 5, 24, 9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 13, 44, 45, 1};
    private  String[] items = new String[]{"Peido 1", "Peido 2", "Peido 3", "Peido 4", "Peido 5", "Peido 6", "Peido 7", "Peido 8", "Peido 9", "Peido 10", "Peido 11", "Peido 12",
            "Peido 13", "Peido 14", "Peido 15", "Peido 16", "Peido 17", "Peido 18", "Peido 19", "Peido 20", "Peido 21", "Peido 22", "Peido 23", "Peido 24",
            "Peido 25", "Peido 26", "Peido 27", "Peido 28", "Peido 29", "Peido 30", "Peido 31", "Peido 32",
            "Peido 33", "Peido 34", "Peido 35", "Peido 36", "Peido 37", "Peido 38", "Peido 39", "Peido 40", "Peido 41", "Peido 42", "Peido 43", "Peido 44", "Peido 45", "Peido 46"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        seekBar = findViewById(R.id.seekBar);
        seekBar.getProgressDrawable().setColorFilter(Color.parseColor("#FFD470"), PorterDuff.Mode.SRC_IN);

        seekBar.getThumb().setColorFilter(Color.parseColor("#FFD470"), PorterDuff.Mode.SRC_IN);

        tempo = findViewById(R.id.textView);
        tempo2 = findViewById(R.id.textView2);
        botao = findViewById(R.id.botao);

        spinner = findViewById(R.id.spinner);

        segundos = 10;
        tempo.setText("00:" + segundos);
        tempo2.setText(segundos + "s");


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                segundos = i;
                tempo2.setText(segundos + "s");

                if (segundos == 60) {
                    tempo2.setText(1 + "s");

                }

                if (segundos < 10) {
                    tempo.setText("00:0" + segundos);

                } else if (segundos > 10 && segundos <= 59) {
                    tempo.setText("00:" + segundos);
                } else if (segundos == 60) {
                    tempo.setText("01:00");

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar();
            }
        });


        Spinner dropdown = findViewById(R.id.spinner);




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                opcoes = PEIDO_VALUES[i];


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                createAd();


            }
        });

        madView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        madView.loadAd(adRequest);


    }

    public void iniciar() {


        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        botao.setClickable(false);


        new CountDownTimer(segundos * 1000, 1000) {

            @Override
            public void onTick(long l) {
                secRestante = (int) (l / 1000);
                if (secRestante < 10) {
                    tempo.setText("00:0" + secRestante);

                } else if (secRestante > 10 && secRestante <= 59) {
                    tempo.setText("00:" + secRestante);
                } else if (secRestante == 60) {
                    tempo.setText("01:00");

                }
                tempo2.setText(String.valueOf(secRestante));
                seekBar.setProgress(secRestante);


            }

            @Override
            public void onFinish() {
                tocar();
                segundos = 10;
                seekBar.setProgress(10);
                tempo.setText("00:10");
                tempo2.setText(segundos + "s");

                seekBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        return false;
                    }
                });
                botao.setClickable(true);


            }
        }.start();
    }

    public void tocar() {
        i++;


        int index = opcoes - 1;
        if (index >= 0 && index < audioResources.length) {
            int audioResource = audioResources[index];
            mediaPlayer = MediaPlayer.create(getApplicationContext(), audioResource);

            if (mediaPlayer != null) {
                mediaPlayer.start();

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if (i == 2) {
                            if (mInterstitialAd != null) {
                                mInterstitialAd.show(MainActivity2.this);
                            } else {
                                Log.d("TAG", "The interstitial ad wasn't ready yet.");
                            }
                            i = 0;
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ((mediaPlayer != null)) {
            mediaPlayer.release();
            mediaPlayer = null;

        }
        madView.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        madView.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                finish();

        }

        return super.onOptionsItemSelected(item);
    }


    private void createAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        createInterstitialAd(adRequest);

    }

    private void createInterstitialAd(AdRequest adRequest) {

        InterstitialAd.load(this, "ca-app-pub-1530737633748031/3151651027", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");

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
            }
        });

    }
}