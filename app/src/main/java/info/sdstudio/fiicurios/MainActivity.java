package info.sdstudio.fiicurios;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREFS_NAME = "MyPrefsFile1";
    public static SharedPreferences sharedPref;
    public static boolean skip = true;
    public static boolean mIsInForegroundMode = false;
    public CheckBox dontShowAgain;
    public String pageVeche = "";
    private Button btnFiltru;
    private ImageView drawableHeart, drawableShare;
    private List<Curiozitate> listaCurenta = new ArrayList<>();
    private List<Curiozitate> listaSearch = new ArrayList<>();
    private List<Curiozitate> listAnimale = new ArrayList<>();
    private List<Curiozitate> listGeografie = new ArrayList<>();
    private List<Curiozitate> listIstorie = new ArrayList<>();
    private List<Curiozitate> listFavorite = new ArrayList<>();
    private List<Curiozitate> listDiverse = new ArrayList<>();
    private TextView txtCuvant, txtPage;
    //private AdView adView;
    //private InterstitialAd interAd;
    //private AdRequest adRequest;
    //   private ImageView imagineFundal;
    private RelativeLayout contentLayout;
    private String ind;
    private int indexFavorite = 0;
    private Calendar cal = Calendar.getInstance();
    private Curiozitate curiozNoua = new Curiozitate();
    private Curiozitate curiozVeche = new Curiozitate();
    private Curiozitate curiozInCurs = new Curiozitate();
    //private ProgressBar pb;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.3F);
    private AlphaAnimation txtSwipe = new AlphaAnimation(0F, 1F);
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Toate");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_random);

        contentLayout = (RelativeLayout) findViewById(R.id.relLayout);
        txtCuvant = (TextView) findViewById(R.id.txtCuvant);
        txtCuvant.setTextColor(Color.WHITE);

        txtPage = (TextView) findViewById(R.id.txtPage);
        txtPage.setTextColor(Color.WHITE);

        btnFiltru = (Button) findViewById(R.id.btnFiltru);
        btnFiltru.setVisibility(View.GONE);

        //      imagineFundal = (ImageView) findViewById(R.id.imageFundal);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

       /* pb = (ProgressBar) findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);*/
        skipIntro();

        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 20);
        cal.set(Calendar.SECOND, 10);
        Intent intentNot = new Intent(MainActivity.this, NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, intentNot,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        //reclame
       /* interAd = new InterstitialAd(this);
        interAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();
        adRequest = new AdRequest.Builder().build();
        interAd.loadAd(adRequest);*/


        btnFiltru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listaCurenta.clear();
                listaCurenta.addAll(listAnimale);
                listaCurenta.addAll(listIstorie);
                listaCurenta.addAll(listGeografie);
                listaCurenta.addAll(listDiverse);
                Curiozitate curioz = getCuriozitate();
                txtCuvant.setText(curioz.getText());
                curiozInCurs = curioz;
                toolbar.setTitle("Toate");
                btnFiltru.setVisibility(View.GONE);
            }
        });


        drawableHeart = (ImageView) findViewById(R.id.btnHeart);
        drawableHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (!isFavorite(curiozInCurs)) {
                    setFavorites(curiozInCurs);
                    drawableHeart.setImageResource(R.drawable.ic_like_full);
                } else {
                    removeFromFavorites(curiozInCurs);
                    listFavorite = showFavorite();
                    if (toolbar.getTitle().equals("Favorite")) {
                        listaCurenta = listFavorite;
                    }
                    drawableHeart.setImageResource(R.drawable.ic_like);
                    if (listFavorite.isEmpty() && toolbar.getTitle().equals("Favorite")) {
                        listaCurenta = listFavorite;
                        txtPage.setText("");
                        txtCuvant.setText("Nu ai curiozitati favorite!");
                        drawableHeart.setEnabled(false);
                        drawableShare.setEnabled(false);
                    }

                }
            }
        });

        drawableShare = (ImageView) findViewById(R.id.btnShare);
        drawableShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    pb.setVisibility(View.VISIBLE);
                v.startAnimation(buttonClick);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "\"" + txtCuvant.getText() + "\"" + " - Fii Curios!");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(Intent.createChooser(intent, "Share cu prietenii"));

            }

        });

        listAnimale = readTextFile("animale.txt");
        listGeografie = readTextFile("geografie.txt");
        listIstorie = readTextFile("istorie.txt");
        listDiverse = readTextFile("diverse.txt");

        listaCurenta = showToate();
        int i = getRandomIndex();
        txtCuvant.setText(listaCurenta.get(i).getText());
        txtPage.setText("#" + i);

       contentLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
                if (!curiozVeche.getText().equals("")) {
                    isFavorite(curiozVeche);
                    txtCuvant.setText(curiozVeche.getText());
                    txtPage.setText(pageVeche);
                    curiozInCurs = curiozVeche;
                }

            }

            public void onSwipeLeft() {
                txtSwipe.setDuration(1000);
                txtCuvant.startAnimation(txtSwipe);
                String leftVar = txtCuvant.getText().toString();
                String leftIndex = String.valueOf(ind);
                pageVeche = txtPage.getText().toString();
                curiozVeche.setId(leftIndex);
                curiozVeche.setText(leftVar);
                int index = 0;
                if (listaCurenta.containsAll(listFavorite) && listFavorite.containsAll(listaCurenta)) {
                    index = getNextIndex();
                } else {
                    boolean aleatoriu = sharedPref.getBoolean("aleatoriu", true);
                    if (aleatoriu) {
                        index = getRandomIndex();
                    } else {
                        index = getNextIndex();
                    }
                }
                curiozNoua = listaCurenta.get(index);
                txtPage.setText("#" + index);
                ind = curiozNoua.getId();
                txtCuvant.setText(curiozNoua.getText());
                isFavorite(curiozNoua);
                curiozInCurs = curiozNoua;
                if (!(listaCurenta.containsAll(listFavorite) && listFavorite.containsAll(listaCurenta))) {
                    Curiozitate deSters = listaCurenta.get(index);
                    listaCurenta.remove(deSters);
                }
                //reclame
             /*  double rand = Math.random() * 13;
               if (((int) rand) % 8 == 0) {
                    if (interAd.isLoaded()) {
                        interAd.show();
                    }
                }*/
            }

        });
    }

    //reclame
   /* private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        interAd.loadAd(adRequest);
    }*/


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*  @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          getMenuInflater().inflate(R.menu.main, menu);
          return true;
      }*/
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int nr = 0;
                for (Curiozitate item : listaCurenta) {
                    if (item.getText().contains(query)) {
                        listaSearch.add(item);
                        nr = nr + 1;
                    }
                }
                if (nr == 0) {
                    Toast.makeText(MainActivity.this, "Nu s-au gasit rezultate!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    btnFiltru.setVisibility(View.VISIBLE);
                    btnFiltru.setText(query);
                    listaCurenta.clear();
                    listaCurenta.addAll(listaSearch);
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawableShare.setEnabled(true);
        drawableHeart.setEnabled(true);

        if (id == R.id.nav_random) {
            listaCurenta.addAll(listAnimale);
            listaCurenta.addAll(listIstorie);
            listaCurenta.addAll(listGeografie);
            listaCurenta.addAll(listDiverse);
            Curiozitate curioz = getCuriozitate();
            txtCuvant.setText(curioz.getText());
            curiozInCurs = curioz;
            toolbar.setTitle("Toate");
        }
        if (id == R.id.nav_favorite) {
            listFavorite = showFavorite();
            listaCurenta = listFavorite;
            if (listaCurenta.isEmpty()) {
                txtCuvant.setText("Nu ai curiozitati favorite!");
                txtPage.setText("");
                drawableShare.setEnabled(false);
                drawableHeart.setEnabled(false);
            } else {
                Curiozitate curioz = listaCurenta.get(getNextIndex());
                txtCuvant.setText(curioz.getText());
                txtPage.setText("#" + curioz.getIndex());
                isFavorite(curioz);
                curiozInCurs = curioz;
            }
            toolbar.setTitle("Favorite");
        } else if (id == R.id.nav_animale) {
            listaCurenta = listAnimale;
            toolbar.setTitle("Animale");
            Curiozitate curioz = getCuriozitate();
            txtCuvant.setText(curioz.getText());
            txtPage.setText("#" + curioz.getIndex());
            isFavorite(curioz);
            curiozInCurs = curioz;
        } else if (id == R.id.nav_istorie) {
            listaCurenta = listIstorie;
            toolbar.setTitle("Istorie");
            Curiozitate curioz = getCuriozitate();
            txtCuvant.setText(curioz.getText());
            txtPage.setText("#" + curioz.getIndex());
            isFavorite(curioz);
            curiozInCurs = curioz;
        } else if (id == R.id.nav_geografie) {
            listaCurenta = listGeografie;
            toolbar.setTitle("Geografie");
            Curiozitate curioz = getCuriozitate();
            txtCuvant.setText(curioz.getText());
            txtPage.setText("#" + curioz.getIndex());
            isFavorite(curioz);
            curiozInCurs = curioz;
        } else if (id == R.id.nav_diverse) {
            listaCurenta = listDiverse;
            Curiozitate curioz = getCuriozitate();
            txtCuvant.setText(curioz.getText());
            txtPage.setText("#" + curioz.getIndex());
            isFavorite(curioz);
            curiozInCurs = curioz;
            toolbar.setTitle("Diverse");
        } else if (id == R.id.nav_facebook) {
            launchFacebook();
        } else if (id == R.id.nav_evalueaza) {
            launchMarket();
        } else if (id == R.id.nav_settings) {
            Intent myIntent = new Intent(this, Settings.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Curiozitate> readTextFile(String fromFile) {
        List<Curiozitate> listaCat = new ArrayList<Curiozitate>();
        try {
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(getAssets().open(fromFile), "UTF-8"));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                String[] res = mLine.split(">>");
                Curiozitate curioz = new Curiozitate(res[0], res[1]);
                listaCat.add(curioz);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaCat;
    }

    private Set<String> getFavorites() {
        Set<String> listaFavorite = new HashSet<>();
        listaFavorite = sharedPref.getStringSet("favorite", new HashSet<String>());
        return listaFavorite;
    }

    private void setFavorites(Curiozitate curioz) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> setfav = getFavorites();
        setfav.add(curioz.getId());
        editor.putStringSet("favorite", setfav);
        editor.commit();
    }

    private void removeFromFavorites(Curiozitate curioz) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> setfav = getFavorites();
        setfav.remove(curioz.getId());
        editor.putStringSet("favorite", setfav);
        editor.commit();
    }


    private boolean isFavorite(Curiozitate curioz) {
        Set<String> setfav = getFavorites();
        if (setfav.contains(curioz.getId())) {
            drawableHeart.setImageResource(R.drawable.ic_like_full);
            return true;
        }
        drawableHeart.setImageResource(R.drawable.ic_like);
        return false;
    }

    private int getRandomIndex() {
        int index = (int) (Math.random() * (listaCurenta.size()));
        return index;
    }

    private int getNextIndex() {
        if (indexFavorite >= listaCurenta.size() - 1) {

            indexFavorite = 0;
            return indexFavorite;
        } else {
            indexFavorite++;
        }

        return indexFavorite;
    }

    private List<Curiozitate> showFavorite() {

        List<Curiozitate> listaFav = new ArrayList<Curiozitate>();
        List<Curiozitate> listaToate = showToate();

        for (Curiozitate curioz : listaToate) {
            if (isFavorite(curioz)) {
                listaFav.add(curioz);
            }
        }

        return listaFav;

    }

    private List<Curiozitate> showToate() {
        List<Curiozitate> listaToate = new ArrayList<Curiozitate>();
        listaToate.addAll(listAnimale);
        listaToate.addAll(listIstorie);
        listaToate.addAll(listGeografie);
        listaToate.addAll(listDiverse);
        return listaToate;
    }

    private Curiozitate getCuriozitate() {
        boolean aleatoriu = sharedPref.getBoolean("aleatoriu", true);
        Curiozitate curioz;
        int index = 0;
        if (aleatoriu) {
            index = getRandomIndex();
            curioz = listaCurenta.get(index);
        } else {
            curioz = listaCurenta.get(index);
        }
        curioz.setIndex(index + 1);
        return curioz;
    }

    public final void launchFacebook() {
        final String urlFb = "fb://page/1768929610091296";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(urlFb));
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() == 0) {
            final String urlBrowser = "https://www.facebook.com/pages/1768929610091296";
            intent.setData(Uri.parse(urlBrowser));
        }
        startActivity(intent);
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " Nu exista pagina!", Toast.LENGTH_LONG).show();
        }
    }

    private void skipIntro() {
        if (skip) {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater adbInflater = LayoutInflater.from(MainActivity.this);
            View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
            dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
            adb.setView(eulaLayout);
            adb.setIcon(R.drawable.ic_inform);
            adb.setTitle("Instructiuni:");
            adb.setMessage("\n● Glisează în stânga pentru o nouă curiozitate\n\n● Glisează în dreapta pentru curiozitatea anterioară");

            adb.setPositiveButton("Am înțeles!", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String checkBoxResult = "NOT checked";
                    if (dontShowAgain.isChecked())
                        checkBoxResult = "checked";
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("skipMessage", checkBoxResult);
                    // Commit the edits!
                    editor.commit();
                    // Intent myIntent = new Intent(MainActivity.this, Info.class);
                    // startActivity(myIntent);
                    return;
                }
            });


            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String skipMessage = settings.getString("skipMessage", "NOT checked");
            if (!skipMessage.equals("checked"))
                adb.show();

        }
        skip = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsInForegroundMode = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsInForegroundMode = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsInForegroundMode = false;
    }

    // Some function.
    public boolean isInForeground() {
        return mIsInForegroundMode;
    }

}
