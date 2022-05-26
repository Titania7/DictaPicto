package com.example.dictapicto;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

public class DisplayMessageActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    TextToSpeech tts;
    ImageButton outLoud;
    public GridLayout layout2;
    BandePhrase da;
    BandePhrase modimg;
    String pathDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_display_message);
        modimg = new BandePhrase();
        da = new BandePhrase();

        pathDir = this.getExternalFilesDir(null).getAbsolutePath() + "/DictaPicto";

        tts = new TextToSpeech(this, this);
        tts.setLanguage(Locale.FRENCH);

        // Get the Intent that started this activity and extract the string
        Intent sentence = getIntent();
        String message = sentence.getStringExtra(MainActivity.EXTRA_MESSAGE);
        String[] strArray = message.split("§");

        String[][] tab1 = strtoTab1(strArray[1]);
        if(!tab1[0][0].equals("void")){
            for (int i=0 ; i<tab1.length ; i++){
                modimg.appendEl(tab1[i][0], tab1[i][1]);
            }
        }

        String[][] tab = strtoTab(strArray[0]);
        for (int i=0 ; i<tab.length ; i++){
            da.appendEl(tab[i][0], tab[i][1]);
        }

        int[][] IDpic = new int[da.getCount()][3];

        LinearLayout[] pictos = new LinearLayout[da.getCount()];
        ImageView[] images = new ImageView[da.getCount()];
        if (da.getCount() > 0) {
            for (int k=0; k<da.getCount(); k++){
                IDpic[k] = callPicto(da.getString()[k][0], da.getString()[k][1], k);
                pictos[k] = findViewById(IDpic[k][0]);
                images[k] = findViewById(IDpic[k][2]);
            }
        }

        if(modimg != null){
            for(int j=0; j<modimg.getCount(); j++){
                int id = Integer.parseInt(modimg.getString()[j][0]);
                for(int i=0; i<images.length; i++){
                    if(images[i].getId() == id){
                        Bitmap newimg = null;
                        try {
                            Uri myuri = Uri.parse(modimg.getString()[j][1]);
                            newimg = BitmapFactory.decodeStream(getContentResolver().openInputStream(myuri));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        images[i].setImageBitmap(newimg);
                    }
                }
            }
        }

        outLoud = findViewById(R.id.outLoud);
        outLoud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0 ; i<pictos.length; i++){
                    TextView txt = findViewById(IDpic[i][1]);
                    ImageView img = findViewById(IDpic[i][2]);

                    speakOut(da.getString()[i][1], txt, img);

                    SystemClock.sleep(1000);

                }
            }
        });
    }

    @Override
    public void onInit(int i) { }


    public int[] callPicto(String namefile, String phrase, int ID){
        char[] phraseChars = namefile.toCharArray();
        int[] IDpic = null;
        ImageView imageView = new ImageView(DisplayMessageActivity.this);
        if (phraseChars[0] == '#' && phraseChars[phraseChars.length-1] == '#'){ // Le pictogramme n'existe pas


            char[] name = new char[phraseChars.length-2];
            for(int i=1; i<phraseChars.length-1; i++){ // retire les #
                name[i-1] = phraseChars[i];
            }

            File f = new File(pathDir, String.valueOf(name));
            if(f.exists()){
                String absPath = f.getAbsolutePath();
                Bitmap newImg = BitmapFactory.decodeFile(absPath);
                imageView.setImageBitmap(newImg);
            }else{
                imageView.setImageResource(getResources().getIdentifier("empty", "drawable", getPackageName()));
            }

        }else{ // Le pictogramme existe
            File f = new File(pathDir, namefile);
            if(f.exists()){
                String absPath = f.getAbsolutePath();
                Bitmap newImg = BitmapFactory.decodeFile(absPath);
                imageView.setImageBitmap(newImg);
            }else{
                imageView.setImageResource(getResources().getIdentifier(namefile, "drawable", getPackageName()));
            }
        }
        TextView textView = new TextView(DisplayMessageActivity.this);

        IDpic = addPicto(imageView, textView, phrase, ID);
        textView.setText(phrase);
        int[] toreturn = IDpic;
        return toreturn;
    }

    private int[] addPicto(ImageView imageView, TextView textView, String phrase, int IDinit){ // OK
        layout2 = findViewById(R.id.layout2);
        int width = 400;
        int height = 400;
        // Pour sous-layout image + phrase vertical !
        LinearLayout sousLayout = new LinearLayout(this);
        LinearLayout.LayoutParams sousLparams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        sousLayout.setOrientation(LinearLayout.VERTICAL);
        sousLparams.setMargins(20, 0, 0, 10);

        textView.setLayoutParams(sousLparams);
        textView.setTextAlignment(LinearLayout.TEXT_ALIGNMENT_CENTER);

        // Pour layout complet !
        LinearLayout princiPal = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(20, 10, 0, 0);
        princiPal.setLayoutParams(params);


        int color = Color.argb(255, 255, 255, 255); // White color
        imageView.setBackgroundColor(color);
        textView.setBackgroundColor(color);

        imageView.setLayoutParams(params);

        int ID = IDinit;
        int IDtxt = ID+100;
        int IDimg = ID+200;
        textView.setId(IDtxt);
        imageView.setId(IDimg);

        sousLayout.setId(ID);
        textView.setId(IDtxt);
        imageView.setId(IDimg);

        sousLayout.addView(imageView);
        sousLayout.addView(textView);

        layout2.addView(sousLayout);

        int[] toreturn = {IDinit, IDtxt, IDimg};

        return toreturn;
    }

    public String[][] strtoTab(String mess){
        String[] strArray = null;
        String[] strArray2 = null;
        strArray = mess.split("/");
        String[][] tabResult = new String[strArray.length][2];
        for (int i=0; i<strArray.length; i++){
            strArray2 = strArray[i].split(";");
            tabResult[i][0] = strArray2[0];
            tabResult[i][1] = strArray2[1];
        }
        return tabResult;
    }

    public String[][] strtoTab1(String mess){
        String[] strArray = null;
        String[] strArray2 = null;
        strArray = mess.split("@");
        String[][] tabResult = new String[strArray.length][2];
        for (int i=0; i<strArray.length; i++){
            strArray2 = strArray[i].split(";");
            if(strArray2[0] == "null" && strArray2[2] == "null"){
                tabResult[0][0] = "null";
                tabResult[0][1] = "null";
            }
            else{
                tabResult[i][0] = strArray2[0];
                tabResult[i][1] = strArray2[1];
            }
        }
        return tabResult;
    }

    private void speakOut(String text, TextView txt, ImageView img){

        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

                final String keyword = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Started" + keyword, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDone(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Done ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(String s) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(), "Error ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, null);
        }

    }
