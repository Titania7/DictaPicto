package com.example.dictapicto;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.*;
import android.widget.*;

import java.io.*;

import androidx.annotation.Nullable;

import java.util.*;

public class MainActivity extends AppCompatActivity{
	
	public static final String EXTRA_MESSAGE = "com.example.dictapicto.MESSAGE";
    public GridLayout layout;

    // Objets boutons et images affichés à l'écran
    Button valider;
    ImageButton microphone;
    ImageButton question;

    // Liste des noms des pictos par ordre d'importance (des plus longs aux plus courts)
    private ArrayList<String> PictoList6w;
    private ArrayList<String> PictoList5w;
    private ArrayList<String> PictoList4w;
    private ArrayList<String> PictoList3w;
    private ArrayList<String> PictoList2w;
    private ArrayList<String> PictoList1w;

    // Phrase résultante de la reconnaissance vocale stockée dans la variable :
    private String RESULT_VOCAL;

    String [][] tabResult;
    BandePhrase da = new BandePhrase();
    BandePhrase imgchange = new BandePhrase();
    LinearLayout[] pictos;
    Bitmap yourSelectedImage;

    TextView txtmodif ;
    ImageView imgmodif;
    ImageView imgpostmod;
    Button close;
    Button chooseimg;
    Button valid ;
    View layoutpop;
    int[][] IDs ;
    String pathselected;

    ImageButton student1;
    ImageButton student2;
    ImageButton student3;
    ImageButton student4;
    ImageButton student5;
    ImageButton student6;


    String pathDir;
    File dirImgUser;

    boolean changed = false;

    @Override
    // Lors de la création, on a toutes ces opérations qui sont opérées
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);

        // On stocke les objets via leur identifiant dans le layout
        valider = findViewById(R.id.valider);
        microphone = findViewById(R.id.microphone);
        question = findViewById(R.id.question);
        student1 = findViewById(R.id.student1);
        student2 = findViewById(R.id.student2);
        student3 = findViewById(R.id.student3);
        student4 = findViewById(R.id.student4);
        student5 = findViewById(R.id.student5);
        student6 = findViewById(R.id.student6);

        layout = findViewById(R.id.layout);

        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutpop = inflater.inflate(R.layout.popup,null);
        txtmodif = layoutpop.findViewById(R.id.txt);
        imgmodif = layoutpop.findViewById(R.id.img);
        close = layoutpop.findViewById(R.id.close);
        chooseimg = layoutpop.findViewById(R.id.chooseImg);
        valid = layoutpop.findViewById(R.id.confirm);

        // OK - On appelle la fonction ReadTXTfile pour initialiser tous les vecteurs des noms de pictos (définis plus haut)
        PictoList6w = ReadTXTfile("6_WordsVect.txt");
        PictoList5w = ReadTXTfile("5_WordsVect.txt");
        PictoList4w = ReadTXTfile("4_WordsVect.txt");
        PictoList3w = ReadTXTfile("3_WordsVect.txt");
        PictoList2w = ReadTXTfile("2_WordsVect.txt");
        PictoList1w = ReadTXTfile("1_WordsVect.txt");

        // On dit au micro de se mettre en "écoute" pour un futur clic.
        microphone.setOnClickListener(imgClk);
        valider.setOnClickListener(imgClk);
        question.setOnClickListener(imgClk);
        student1.setOnClickListener(imgClk);
        student2.setOnClickListener(imgClk);
        student3.setOnClickListener(imgClk);
        student4.setOnClickListener(imgClk);
        student5.setOnClickListener(imgClk);
        student6.setOnClickListener(imgClk);


        // Création du dossier d'images enregistrées
        pathDir = this.getExternalFilesDir(null).getAbsolutePath() + "/DictaPicto";
        dirImgUser = new File(pathDir);
        dirImgUser.mkdirs();

        if(dirImgUser.exists() && dirImgUser.isDirectory()){
            File[] listOfFiles = dirImgUser.listFiles();
            for(File file : listOfFiles){
                if(file.isFile()){
                    //Toast.makeText(getApplicationContext(), file.getName(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public View.OnClickListener imgClk = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(view == microphone){
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.FRENCH);
                startActivityForResult(intent, 10);
            }
            else if(view == valider){
                displayMessage(valider);
            }
            else if(view == question){
                if(da != null) layout.removeAllViews();

                da.appendEl("question", "?");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }

            else if(view == student1){
                if(da != null) layout.removeAllViews();

                da.appendEl("boyone", "NOM1");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }

            else if(view == student2){
                if(da != null) layout.removeAllViews();

                da.appendEl("boytwo", "NOM2");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }

            else if(view == student3){
                if(da != null) layout.removeAllViews();

                da.appendEl("boythree", "NOM3");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }

            else if(view == student4){
                if(da != null) layout.removeAllViews();

                da.appendEl("girlone", "NOM4");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }

            else if(view == student5){
                if(da != null) layout.removeAllViews();

                da.appendEl("girltwo", "NOM5");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }

            else if(view == student6){
                if(da != null) layout.removeAllViews();

                da.appendEl("girlthree", "NOM6");

                IDs = new int[da.getCount()][3];
                pictos = new LinearLayout[da.getCount()];
                LinearLayout[] pictos = new LinearLayout[da.getCount()];
                // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                for (int k=0; k<da.getCount(); k++) {
                    int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                    pictos[k] = findViewById(IDpic[0]);
                    IDs[k] = IDpic;
                }

                for (LinearLayout picto : pictos) {
                    registerForContextMenu(picto);
                }
            }
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //menu.setHeaderTitle("Que faire ?");
        // add menu items
        menu.add(0, v.getId(), 0, "Modifier");
        menu.add(1, v.getId(), 0, "Supprimer");
    }

    // menu item select listener
    @SuppressLint("ResourceType")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Supprimer") {
            //Toast.makeText(getApplicationContext(), item.getItemId()+" =? "+layout.getChildAt(item.getItemId()).getId(), Toast.LENGTH_LONG).show();
            da.delElement(item.getItemId());
            layout.removeAllViews();

            IDs = new int[da.getCount()][3];
            pictos = new LinearLayout[da.getCount()];

            for (int k=0; k<da.getCount(); k++) {
                IDs[k] = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                pictos[k] = findViewById(IDs[k][0]);
            }

            for (int j=0; j<da.getCount(); j++){
                registerForContextMenu(pictos[j]);
            }
        }
        else if (item.getTitle() == "Modifier") {
            imgpostmod = findViewById(item.getItemId()+200);
            String nFile = da.getString()[item.getItemId()][0]; // on prend le nom du fichier actuel
            String nPic = da.getString()[item.getItemId()][1];

            showWinners(nFile, nPic, item.getItemId()+200);

        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case 10: // Se déclenche lorsque la reconnaissance vocale est termniée
                if(da != null) layout.removeAllViews();
                if (resultCode == RESULT_OK && data != null){
                    // On récupère le résultat de la reconnaissance vocale
                    ArrayList<String> ResultVocal = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    // On en prend la valeur en String => Il nous faut 2 variables : Une à *afficher* (RESULT_VOCAL)
                    RESULT_VOCAL = String.valueOf(ResultVocal);
                    // Enlève les crochets autour du message : "[Je suis malade]" => "Je suis malade"
                    RESULT_VOCAL = RESULT_VOCAL.substring(1, RESULT_VOCAL.length() -1);
                    RESULT_VOCAL = corrNums(RESULT_VOCAL);

                    String RESULT_VOCALA = RESULT_VOCAL.toUpperCase();

                    // Et une pour la *comparer* aux noms des fichiers (result_vocal)
                    String result_vocalc = nametoFileFormat(RESULT_VOCAL);

                    // On appelle PictoFound pour qu'il renvoie la liste des pictos existants pour chaque partie de la phrase
                    // Les pictos inexistants sont #encadrés par des carrés#
                    ArrayList<String> PicNamesFound = PictoFind(result_vocalc);
                    tabResult = returnTab(RESULT_VOCALA, PicNamesFound);

                    for (String[] strings : tabResult) da.appendEl(strings[0], strings[1]);

                    IDs = new int[da.getCount()][3];
                    pictos = new LinearLayout[da.getCount()];
                    //LinearLayout[] pictos = new LinearLayout[da.getCount()];
                    // Pour chacun de ces noms de pictos, on va créer une nouvelle carte ou un nouveau TextView pour les afficher
                    for (int k=0; k<da.getCount(); k++) {
                        int[] IDpic = callPicto(layout, k, da.getString()[k][0], da.getString()[k][1]);
                        pictos[k] = findViewById(IDpic[0]);
                        IDs[k] = IDpic;
                    }

                    for (LinearLayout picto : pictos) {
                        registerForContextMenu(picto);
                    }


                }
                break;

            case 1234:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    pathselected = selectedImage.toString();
                    try {
                        yourSelectedImage = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imgmodif.setImageBitmap(yourSelectedImage);
                    //Toast.makeText(getApplicationContext(), txtmodif.getText(), Toast.LENGTH_LONG).show();
                    changed = true;

                    String newfile = corrNums((String) txtmodif.getText());
                    newfile = nametoFileFormat(newfile);

                    saveBitmap(yourSelectedImage, newfile);
                }
                break;

            default:
                Toast.makeText(getApplicationContext(), "Échec", Toast.LENGTH_LONG).show();
                break;

        }
    }



    private void showWinners(String nFile, String nPic, int idPic){

        float density=MainActivity.this.getResources().getDisplayMetrics().density;
        final PopupWindow pw = new PopupWindow(layoutpop, (int)density*800, (int)density*550, true);

        //Button to close the pop-up
        View.OnClickListener popupclick = new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(view == close){
                    pw.dismiss();
                    changed = false;
                }
                else if (view == chooseimg){
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    final int ACTIVITY_SELECT_IMAGE = 1234;
                    startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
                }
                else if(view == valid && changed){
                    imgpostmod.setImageBitmap(yourSelectedImage);
                    imgchange.appendEl(String.valueOf(idPic), pathselected);
                    pw.dismiss();
                    changed = false;
                }
            }
        };


        chooseimg.setOnClickListener(popupclick);
        close.setOnClickListener(popupclick);
        valid.setOnClickListener(popupclick);


        //Set up touch closing outside of pop-up
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    changed = false;
                    return true;
                }
                return false;
            }
        });


        char[] phraseChars = nFile.toCharArray();
        int idimg = 0;
        if (phraseChars[0] == '#' && phraseChars[phraseChars.length-1] == '#'){ // Le pictogramme n'existe pas
            idimg = getResources().getIdentifier("empty", "drawable", getPackageName());
        }else{ // Le pictogramme existe
            idimg = getResources().getIdentifier(nFile, "drawable", getPackageName());
        }
        imgmodif.setImageResource(idimg);
        txtmodif.setText(nPic);


        pw.setOutsideTouchable(true);
        pw.showAtLocation(layoutpop, Gravity.CENTER, 0, 0);
    }

    public String saveBitmap(Bitmap bitmap, String namefile){
        String name = "";
        try {
            File f = new File(pathDir, namefile);
            name = f.getAbsolutePath();
            if(!f.exists()){
                FileOutputStream fos = new FileOutputStream(name);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                //Toast.makeText(getApplicationContext(), "Dir name = "+name, Toast.LENGTH_LONG).show();
            }else{
                f.delete();
                FileOutputStream fos = new FileOutputStream(name);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
                //Toast.makeText(getApplicationContext(), "Existe déjà à l'adresse : "+name, Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Echec de l'enregistrement de l'image", Toast.LENGTH_LONG).show();
        }
        return name;
    }

    public void setImview(String namefile, ImageView imageView){
        char[] phraseChars = namefile.toCharArray();
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
    }


    public  String[][] returnTab(String AFFICHER, ArrayList<String> fichiers){
        // On veut comparer les deux objets suivants et créer un lien entre eux.

        ArrayList<String> AtoF = new ArrayList<String>(Arrays.asList(AFFICHER.split("\\s+")));
        // => [JE, SUIS, MAL'ADE, COMME, UN, CHIEN]
        ArrayList<String> AtoFbis = new ArrayList<>();
        AtoFbis = (ArrayList<String>)AtoF.clone();
        for (int i=0; i<AtoFbis.size(); i++){
            AtoFbis.set(i, AtoFbis.get(i).toLowerCase());
            char[] AtoFbisChar = AtoFbis.get(i).toCharArray();

            int count = 0;
            for (char x : AtoFbisChar){
                if(x == 'à' || x == 'á' || x == 'ä' || x == 'â'){   AtoFbisChar[count] = 'a';}
                if(x == 'è' || x == 'é' || x == 'ë' || x == 'ê'){   AtoFbisChar[count] = 'e';}
                if(x == 'ò' || x == 'ó' || x == 'ö' || x == 'ô'){   AtoFbisChar[count] = 'o';}
                if(x == 'ù' || x == 'ú' || x == 'ü' || x == 'û'){   AtoFbisChar[count] = 'u';}
                if(x == 'ì' || x == 'í' || x == 'ï' || x == 'î'){   AtoFbisChar[count] = 'i';}
                if(x == 'ç'){   AtoFbisChar[count] = 'c';}
                //if(x == '-'){   AtoFbisChar[count] = ' ';}
                count++;
            }
            AtoFbis.set(i, String.valueOf(AtoFbisChar));
        }
        // C'est ce qu'on va comparer avec les noms de fichiers (même taille que AtoF)

        ArrayList<String> FtoA = new ArrayList<String>();
        FtoA = (ArrayList<String>)fichiers.clone();

        for (int k=0; k<FtoA.size(); k++){ // On prend un pictogramme à la fois
            char[] FtoAChars = FtoA.get(k).toCharArray(); // On le transforme en vecteur de char

            // On parcourt la string char par char
            for(int i = 0; i<FtoAChars.length; i++){

                if(i<FtoAChars.length-1 && FtoAChars[i] == 'y' && FtoAChars[i+1] == 'y'){
                    FtoAChars[i] = '\'';
                    i++;
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.valueOf(FtoAChars));
                    sb.deleteCharAt(i);
                    char[] array = new char[sb.length()];
                    sb.getChars(0, sb.length(), array, 0);
                    FtoAChars = array;
                }

                if(FtoAChars[i] == '_' || FtoAChars[i] == '-'){   FtoAChars[i] = ' ';}
                //if(FtoAChars[i] == '-'){   FtoAChars[i] = ' ';}

                if(FtoAChars[i] == '#'){
                    StringBuilder sb = new StringBuilder();
                    sb.append(String.valueOf(FtoAChars));
                    sb.deleteCharAt(i);
                    char[] array = new char[sb.length()];
                    sb.getChars(0, sb.length(), array, 0);
                    FtoAChars = array;
                }

            }

            FtoA.set(k, String.valueOf(FtoAChars));
        }
        // Maintenant FtoA = [je, suis, mal'ade, comme un chien]

        // On va comparer AtoF et FtoA pour trouver les similitudes et ressortir la bonne combinaison à afficher
        //System.out.println("AtoFbis = "+ AtoFbis+ " Lié à AtoF : "+AtoF); // Lié à AFFICHER
        //System.out.println("FtoA = "+ FtoA+ " Lié à fichiers : "+fichiers); // Lié à fichiers


        String[][] tabResult = new String[FtoA.size()][2];



        for (int k=0; k<FtoA.size(); k++){ // String par string de FtoA

            List<String> tempList = Arrays.asList(FtoA.get(k).split("\\s+")); //On split les pictos à plusieurs mots
            int nbremots = tempList.size(); // nbre de mots dans FtoA.get(k)
            //System.out.println("Picto à trouver : "+FtoA.get(k));











            String tempStr = "";
            // On parcourt la string mot par mot en diminuant à chaque fois
            for(int l=k; l<k+nbremots; l++){
                if (l != k){
                    tempStr = tempStr + ' ' + AtoFbis.get(l);
                //cverif
                }else{
                    tempStr = AtoFbis.get(l);
                }
            }
            //System.out.println("String recontstruite : "+tempStr);

            // Comparaison mot à mot
            if(tempStr.equals(FtoA.get(k))){
                for(int l = k; l<k+nbremots; l++){
                    if (l != k){
                        tempStr = tempStr + ' ' + AtoF.get(l);
                    }else{
                        tempStr = AtoF.get(l);
                    }
                }
                //System.out.println("String à ajouter dans le tableau : "+tempStr);
                tabResult[k][0] = fichiers.get(k);
                tabResult[k][1] = tempStr;
            }

        }

        // Affichage du tableau
        /*for (String[] tab: tabResult) { // On parcourt ligne par ligne
            for (String s: tab) { // On parcourt colonne par colonne
                System.out.print(s + "\t");
            }
            System.out.println("\n");
        }*/
        return tabResult;
    }

    public int[] callPicto(GridLayout Layout, int idpic ,String namefile, String phrase){

        int[] IDpic = null;
        TextView textView = new TextView(MainActivity.this);
        textView.setText(phrase);
        ImageView imageView = new ImageView(MainActivity.this);

        setImview(namefile, imageView);

        IDpic = addPicto(Layout, idpic, imageView, textView);
        return IDpic;
    }

    private int[] addPicto(GridLayout Layout, int IDinit, ImageView imageView, TextView textView) { // OK
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
        //princiPal.setBackgroundColor(color);
        imageView.setBackgroundColor(color);
        textView.setBackgroundColor(color);

        int ID = IDinit;
        int IDtxt = ID+100;
        int IDimg = ID+200;
        textView.setId(IDtxt);
        imageView.setId(IDimg);

        imageView.setLayoutParams(params);

        sousLayout.setId(ID);

        sousLayout.addView(imageView);
        sousLayout.addView(textView);

        Layout.addView(sousLayout);
        int[] IDs = {ID, IDtxt, IDimg};
        return IDs;
    }

    // Fonction qui renvoie une ArrayList<String> des pictos existant pour la phrase String qu'on lui donne
    public ArrayList<String> PictoFind(String namefile){

        // Transformation de la requête vocale String en ArrayList<String>
        ArrayList<String> initAL = new ArrayList<String>(Arrays.asList(namefile.split("_")));
        // On note la taille de la requête
        int lenInAL = initAL.size();

        // Prépration du résultat à return à la fin
        ArrayList<String> resultat = new ArrayList<String>();

        for (int i = 0; i < lenInAL; i++){ // On parcourt "initAL" -> i

            // Les booléens servent à dire si une suite de pictogramme de longueur donnée a été trouvée (true) ou pas (false)
            boolean flag6 = false;
            boolean flag5 = false;
            boolean flag4 = false;
            boolean flag3 = false;
            boolean flag2 = false;
            boolean flag1 = false;

            // Si on a "void", on s'en occupe pas car picto déjà trouvé => Voir fonction ReplaceFound
            if (initAL.get(i).equals("void") || initAL.get(i).equals("")){break;}

            // D'abord pictos à 6 mots : on vérifie que la longueur nécessaire est encore disponible
            if (lenInAL-i >= 6 && !initAL.get(i).equals("void") && !initAL.get(i+1).equals("void") && !initAL.get(i+2).equals("void") && !initAL.get(i+3).equals("void") && !initAL.get(i+4).equals("void") && !initAL.get(i+5).equals("void")){
                String temp = initAL.get(i) + "_" + initAL.get(i+1) + "_" + initAL.get(i+2)+ "_" + initAL.get(i+3)+ "_" + initAL.get(i+4)+ "_" + initAL.get(i+5);

                // Si le dataset contient le pictogramme
                if(PictoList6w.contains(temp)){
                    int index = PictoList6w.indexOf(temp);
                    String result = PictoList6w.get(index);
                    // On ajoute le picto au résultat
                    resultat.add(result);
                    // On note que les 6 premiers mots sont classés
                    flag6 = true;
                    // On remplace par "void" dans la phrase initiale via la fonction ReplaceFound plus bas
                    initAL = ReplaceFound(initAL, result);
                    // On fait en sorte de passer les 5 prochains mots puisqu'ils ont déjà été traités
                    i=i+5;
                } // Si il ne le contient pas, on ne fait rien
            }

            // Le reste : même principe

            // Puis pictos à 5 mots
            if (!flag6 && lenInAL-i >= 5 && !initAL.get(i).equals("void") && !initAL.get(i+1).equals("void") && !initAL.get(i+2).equals("void") && !initAL.get(i+3).equals("void") && !initAL.get(i+4).equals("void")){
                String temp = initAL.get(i) + "_" + initAL.get(i+1) + "_" + initAL.get(i+2)+ "_" + initAL.get(i+3)+ "_" + initAL.get(i+4);

                if(PictoList5w.contains(temp)){
                    int index = PictoList5w.indexOf(temp);
                    String result = PictoList5w.get(index);
                    resultat.add(result);
                    flag5 = true;
                    initAL = ReplaceFound(initAL, result);
                    i=i+4;
                }
            }

            // Puis pictos à 4 mots
            if (!flag6 && !flag5 && lenInAL-i >= 4 && !initAL.get(i).equals("void") && !initAL.get(i+1).equals("void") && !initAL.get(i+2).equals("void") && !initAL.get(i+3).equals("void")){
                String temp = initAL.get(i) + "_" + initAL.get(i+1) + "_" + initAL.get(i+2)+ "_" + initAL.get(i+3);

                if(PictoList4w.contains(temp)){
                    int index = PictoList4w.indexOf(temp);
                    String result = PictoList4w.get(index);
                    resultat.add(result);
                    flag4 = true;
                    initAL = ReplaceFound(initAL, result);
                    i=i+3;
                }
            }

            // Puis pictos à 3 mots
            if (!flag6 && !flag5 && !flag4 && lenInAL-i >= 3 && !initAL.get(i).equals("void") && !initAL.get(i+1).equals("void") && !initAL.get(i+2).equals("void")){
                String temp = initAL.get(i) + "_" + initAL.get(i+1) + "_" + initAL.get(i+2);

                if(PictoList3w.contains(temp)){
                    int index = PictoList3w.indexOf(temp);
                    String result = PictoList3w.get(index);
                    resultat.add(result);
                    flag3 = true;
                    initAL = ReplaceFound(initAL, result);
                    i=i+2;
                }
            }

            // Puis pictos à 2 mots
            if (!flag6 && !flag5 && !flag4 && !flag3 && lenInAL-i >= 2 && !initAL.get(i).equals("void") && !initAL.get(i+1).equals("void")){
                String temp = initAL.get(i) + "_" + initAL.get(i+1);

                if(PictoList2w.contains(temp)){
                    int index = PictoList2w.indexOf(temp);
                    String result = PictoList2w.get(index);
                    resultat.add(result);
                    flag2 = true;
                    initAL = ReplaceFound(initAL, result);
                    i++;
                }
            }

            // Enfin, pictos à 1 mot
            if (!flag6 && !flag5 && !flag4 && !flag3 && !flag2 && lenInAL-i >= 1 && !initAL.get(i).equals("void")){
                String temp3 = initAL.get(i);

                if (PictoList1w.contains(temp3)){
                    int index = PictoList1w.indexOf(temp3);
                    String result = PictoList1w.get(index);
                    resultat.add(result);
                    flag1 = true;
                    initAL = ReplaceFound(initAL, result);
                }
            }

            // Si on a pas de correspondance pour aucun des types précédents :
            if (!flag6 && !flag5 && !flag4 && !flag3 && !flag2 && !flag1 && !initAL.get(i).equals("void")){
            // On ajoute des #carrés# quand on ne trouve pas de correspondance dans les pictogrammes
            resultat.add("#"+initAL.get(i)+"#");
            }

        }
        return resultat;
    }

    // Fonction appelée par PictoFind pour passer les mots déjà traités => Remplace par "void"
    public static ArrayList<String> ReplaceFound(ArrayList<String> initial, String tochange){
        // Transformation de la string en ArrayList
        ArrayList<String> changeAL = new ArrayList<String>(Arrays.asList(tochange.split("_")));
        for (int k=0; k<changeAL.size(); k++){
            initial.set(initial.indexOf(changeAL.get(k)), new String("void"));
        }
        return initial;
    }

    // Permet d'avoir une liste des noms de pictogrammes disponibles et plus tard, pouvoir garder en mémoire les modifications utilisateur
    public ArrayList<String> ReadTXTfile(String namefile){ //OK
            String contenu = "";

            try {
                InputStream stream = getAssets().open(namefile);

                int size = stream.available();
                byte[] buffer = new byte[size];
                stream.read(buffer);
                stream.close();
                contenu = new String(buffer);
            }
            catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Erreur lors de l'ouverture du fichier "+namefile, Toast.LENGTH_LONG).show();
            }

        // On divise la string avec les points-virgule (norme du fichier texte)
        String[] strSplit = contenu.split(";");
        // On convertit le résultat en ArrayList
        ArrayList<String> strList = new ArrayList<String>(Arrays.asList(strSplit));
        return strList;
    }

    public String nametoFileFormat(String phrase){
        // Correction des caractères individuels :
        phrase = phrase.toLowerCase();
        //System.out.println("En entrantdans nametoFileFormat = "+ phrase);
        char[] phraseChars = phrase.toCharArray();
        int k = 0;
        for(char i:phraseChars){
            if(i == 'à' || i == 'á' || i == 'ä' || i == 'â'){   phraseChars[k] = 'a';}
            if(i == 'è' || i == 'é' || i == 'ë' || i == 'ê'){   phraseChars[k] = 'e';}
            if(i == 'ò' || i == 'ó' || i == 'ö' || i == 'ô'){   phraseChars[k] = 'o';}
            if(i == 'ù' || i == 'ú' || i == 'ü' || i == 'û'){   phraseChars[k] = 'u';}
            if(i == 'ì' || i == 'í' || i == 'ï' || i == 'î'){   phraseChars[k] = 'i';}
            if(i == 'ç'){   phraseChars[k] = 'c';}
            if(i == ' ' || i == '-'){   phraseChars[k] = '_';}
            //if(i == '-'){   phraseChars[k] = '_';} // Peut poser problème lors de la traduction dans l'autre sens pour l'affichage => fonction returnTab
            if(i == '\''){
                phraseChars[k] = 'y';
                String temp1 = new String(phraseChars);
                StringBuilder temp = new StringBuilder(temp1);
                temp.insert(k+1, 'y');
                phraseChars = temp.toString().toCharArray();
                k++;
            }
            k++;
        }
        return String.valueOf(phraseChars);
    }

    public String corrNums(String phrase){ // OK - Correction chiffres : "6" => "six"

        char[] chari = phrase.toCharArray();
        int count=0;
        for (char x : chari){
            if(x == '-'){
                chari[count] = ' ';
            }
            count++;
        }
        phrase = String.valueOf(chari);

        ArrayList<String> initial = new ArrayList<String>(Arrays.asList(phrase.split("\\s+")));

        ArrayList<String> exceptions = new ArrayList<String>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "20", "50", "100", "200", "500"));
        ArrayList<String> replace = new ArrayList<String>(Arrays.asList("zéro", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit", "neuf", "dix", "onze", "douze", "vingt", "cinquante", "cent", "deux cent", "cinq cent"));

        int j = 0 ;
        for (String i:exceptions){
            if(initial.contains(i)){
                initial.set(initial.indexOf(i), replace.get(j));
            }
            j++;
        }
        //System.out.println(initial);
        String str = String.join(" ", initial);
        //System.out.println(str);
        return str;
    }

    // On crée une nouvelle page(activité) pour l'affichage simple du message
    public void displayMessage(View view) { // OK
        if(da.getCount() != 0){
            Intent newpage = new Intent(this, DisplayMessageActivity.class);
            if(imgchange.getCount() > 0){
                newpage.putExtra(EXTRA_MESSAGE, tabtoStr(da.getString(), da.getCount())+"§"+tabtoStr1(imgchange.getString(), imgchange.getCount()));
            }
            else{
                newpage.putExtra(EXTRA_MESSAGE, tabtoStr(da.getString(), da.getCount())+"§"+tabtoStr1(null, 0));
            }
            startActivity(newpage);
        }
    }

    public String tabtoStr(String [][] tokens, int size){
        String finale = "";
        for (int i=0; i<size; i++){ // pour chaque couple image-texte
            finale = finale + String.join(";", tokens[i]);
            if (i<size-1){
                finale = finale + "/";
            }
        }
        return finale;
    }

    public String tabtoStr1(String [][] tokens, int size){
        String finale = "";
        if(tokens != null && size > 0){
            for (int i=0; i<size; i++){ // pour chaque couple image-texte
                finale = finale + String.join(";", tokens[i]);
                if (i<size-1){
                    finale = finale + "@";
                }
            }
        }
        else{
            finale = "void;void";
        }

        return finale;
    }
}