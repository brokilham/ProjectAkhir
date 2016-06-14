package com.agung.ilham.monsilva;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class peralatan_kalkulator_peralatan_hasil_perhitungan extends AppCompatActivity
{
    int JumlahPendaki,LamaPendakian;
    TextView TextHasilMatras,TextHasilTrashBag,TextHasilKomporLapangan,TextHasilNesting,
            TextHasilKorekApi,TextHasilPisau,TextHasilBahanBakar,TextHasilPenunjukArah;
    Button TextHasilTenda,TextHasilAlatJahit;
    int width;
    int height;
    String TotalJumlahTenda4,TotalJumlahTenda3, TotalJumlahTenda2, TotalJumlahTenda1;
    int JumlahTenda4 = 0,JumlahTenda3 = 0,JumlahTenda2 = 0,JumlahTenda1 = 0;
    int JumlahPendakiTrashBag,JumlahPendakiTenda,JumlahPendakiKomporLapangan,JumlahPendakiNesting,
            JumlahPendakiKorekApi,JumlahPendakiPisau,JumlahPendakiBahanBakar,JumlahPendakiPenunjukArah,
            JumlahPendakiAlatJahit;
    int JumlahBenang = 1;
    int JumlahJarum  = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peralatan_kalkulator_peralatan_hasil_perhitungan);

        Intent page = getIntent();
        JumlahPendaki = Integer.parseInt(page.getStringExtra("JumlahPendaki"));
        LamaPendakian = Integer.parseInt(page.getStringExtra("LamaPendakian"));
        JumlahPendakiTrashBag = JumlahPendaki;
        JumlahPendakiTenda = JumlahPendaki;
        JumlahPendakiKomporLapangan = JumlahPendaki;
        JumlahPendakiNesting = JumlahPendaki;
        JumlahPendakiKorekApi = JumlahPendaki;
        JumlahPendakiPisau = JumlahPendaki;
        JumlahPendakiBahanBakar = JumlahPendaki;
        JumlahPendakiPenunjukArah = JumlahPendaki;
        JumlahPendakiAlatJahit = JumlahPendaki;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true); // button back di atas
        actionBar.setTitle("Kalkulator Peralatan");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00786f"));
        actionBar.setBackgroundDrawable(colorDrawable);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        width = displaymetrics.widthPixels;
        height = displaymetrics.heightPixels;
        int widthRelativeLayout = width/2;
        int TinggiLayoutKonten = height/12;
        RelativeLayout LayoutPeralatanAlatJahit,LayoutPeralatanKomporLapangan,LayoutPeralatanBahanBakar,
                LayoutPeralatanPenunjukArah,LayoutPeralatanKorekApi,LayoutPeralatanMatras,LayoutPeralatanNesting,
                LayoutPeralatanPisau,LayoutPeralatanTenda,LayoutPeralatanTrashBag;

        LayoutPeralatanAlatJahit      = (RelativeLayout) findViewById(R.id.LayoutPeralatanAlatJahit);
        LayoutPeralatanKomporLapangan = (RelativeLayout) findViewById(R.id.LayoutPeralatanKomporLapangan);
        LayoutPeralatanBahanBakar     = (RelativeLayout) findViewById(R.id.LayoutPeralatanBahanBakar);
        LayoutPeralatanPenunjukArah   = (RelativeLayout) findViewById(R.id.LayoutPeralatanPenunjukArah);
        LayoutPeralatanKorekApi       = (RelativeLayout) findViewById(R.id.LayoutPeralatanKorekApi);
        LayoutPeralatanMatras         = (RelativeLayout) findViewById(R.id.LayoutPeralatanMatras);
        LayoutPeralatanNesting        = (RelativeLayout) findViewById(R.id.LayoutPeralatanNesting);
        LayoutPeralatanPisau          = (RelativeLayout) findViewById(R.id.LayoutPeralatanPisau);
        LayoutPeralatanTenda          = (RelativeLayout) findViewById(R.id.LayoutPeralatanTenda);
        LayoutPeralatanTrashBag       = (RelativeLayout) findViewById(R.id.LayoutPeralatanTrashBag);


        RelativeLayout.LayoutParams Tinggi = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        LayoutPeralatanAlatJahit.setLayoutParams(Tinggi);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanKomporLapangan = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanKomporLapangan.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanAlatJahit);
        LayoutPeralatanKomporLapangan.setLayoutParams(TinggiLayoutPeralatanKomporLapangan);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanBahanBakar = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanBahanBakar.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanKomporLapangan);
        LayoutPeralatanBahanBakar.setLayoutParams(TinggiLayoutPeralatanBahanBakar);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanPenunjukArah = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanPenunjukArah.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanBahanBakar);
        LayoutPeralatanPenunjukArah.setLayoutParams(TinggiLayoutPeralatanPenunjukArah);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanKorekApi = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanKorekApi.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanPenunjukArah);
        LayoutPeralatanKorekApi.setLayoutParams(TinggiLayoutPeralatanKorekApi);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanMatras = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanMatras.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanKorekApi);
        LayoutPeralatanMatras.setLayoutParams(TinggiLayoutPeralatanMatras);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanNesting = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanNesting.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanMatras);
        LayoutPeralatanNesting.setLayoutParams(TinggiLayoutPeralatanNesting);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanPisau = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanPisau.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanNesting);
        LayoutPeralatanPisau.setLayoutParams(TinggiLayoutPeralatanPisau);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanTenda = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanTenda.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanPisau);
        LayoutPeralatanTenda.setLayoutParams(TinggiLayoutPeralatanTenda);

        RelativeLayout.LayoutParams TinggiLayoutPeralatanTrashBag = new RelativeLayout.LayoutParams(width, TinggiLayoutKonten);
        TinggiLayoutPeralatanTrashBag.addRule(RelativeLayout.BELOW, R.id.LayoutPeralatanTenda);
        LayoutPeralatanTrashBag.setLayoutParams(TinggiLayoutPeralatanTrashBag);

        RelativeLayout LayoutTextHasilTenda,LayoutTextHasilMatras,LayoutTextHasilAlatJahit,LayoutTextHasilKomporLapangan
                ,LayoutTextHasilPenunjukArah,LayoutTextHasilKorekApi,LayoutTextHasilNesting,LayoutTextHasilPisau,
                LayoutTextHasilTrashBag,LayoutTextHasilBahanBakar;

        LayoutTextHasilTenda            = (RelativeLayout) findViewById(R.id.LayoutTextHasilTenda);
        LayoutTextHasilMatras           = (RelativeLayout) findViewById(R.id.LayoutTextHasilMatras);
        LayoutTextHasilAlatJahit        = (RelativeLayout) findViewById(R.id.LayoutTextHasilAlatJahit);
        LayoutTextHasilKomporLapangan   = (RelativeLayout) findViewById(R.id.LayoutTextHasilKomporLapangan);
        LayoutTextHasilPenunjukArah     = (RelativeLayout) findViewById(R.id.LayoutTextHasilPenunjukArah);
        LayoutTextHasilKorekApi         = (RelativeLayout) findViewById(R.id.LayoutTextHasilKorekApi);
        LayoutTextHasilNesting          = (RelativeLayout) findViewById(R.id.LayoutTextHasilNesting);
        LayoutTextHasilPisau            = (RelativeLayout) findViewById(R.id.LayoutTextHasilPisau);
        LayoutTextHasilTrashBag         = (RelativeLayout) findViewById(R.id.LayoutTextHasilTrashBag);
        LayoutTextHasilBahanBakar       = (RelativeLayout) findViewById(R.id.LayoutTextHasilBahanBakar);



        RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(widthRelativeLayout, TinggiLayoutKonten);
        LayoutTextHasilTenda.setLayoutParams(parms);
        LayoutTextHasilMatras.setLayoutParams(parms);
        LayoutTextHasilAlatJahit.setLayoutParams(parms);
        LayoutTextHasilKomporLapangan.setLayoutParams(parms);
        LayoutTextHasilPenunjukArah.setLayoutParams(parms);
        LayoutTextHasilKorekApi.setLayoutParams(parms);
        LayoutTextHasilNesting.setLayoutParams(parms);
        LayoutTextHasilPisau.setLayoutParams(parms);
        LayoutTextHasilTrashBag.setLayoutParams(parms);
        LayoutTextHasilBahanBakar.setLayoutParams(parms);


        TextHasilMatras = (TextView) findViewById(R.id.TextHasilMatras);
        TextHasilTrashBag = (TextView) findViewById(R.id.TextHasilTrashBag);
        TextHasilKomporLapangan = (TextView) findViewById(R.id.TextHasilKomporLapangan);
        TextHasilNesting = (TextView) findViewById(R.id.TextHasilNesting);
        TextHasilKorekApi = (TextView) findViewById(R.id.TextHasilKorekApi);
        TextHasilPisau = (TextView) findViewById(R.id.TextHasilPisau);
        TextHasilBahanBakar = (TextView) findViewById(R.id.TextHasilBahanBakar);
        TextHasilPenunjukArah = (TextView) findViewById(R.id.TextHasilPenunjukArah);

        TextHasilAlatJahit  = (Button) findViewById(R.id.TextHasilAlatJahit);
        TextHasilTenda = (Button) findViewById(R.id.TextHasilTenda);
        TextHasilTenda.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialogTenda();

            }
        });
        TextHasilAlatJahit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alertDialogAlatJahit();

            }
        });


        TextHasilMatras.setText(Integer.toString(JumlahPendaki));

        KomporLapangan();
        tenda();
        TrashBag();
        Nesting();
        KorekApi();
        Pisau();
        BahanBakar();
        PenunjukArah();
        AlatJahit();
    }

    public void AlatJahit()

    {

        int JumlahAlatJahit = 0 ;
        boolean CheckAlatJahitSebelumnya = false;
        do
        {
            if(JumlahPendakiAlatJahit >=20)
            {
                JumlahAlatJahit++;
                JumlahPendakiAlatJahit = JumlahPendakiAlatJahit-20;
                CheckAlatJahitSebelumnya = true;

            }
            if(JumlahPendakiAlatJahit <20)
            {
                if(CheckAlatJahitSebelumnya == false)
                {
                    JumlahAlatJahit++;
                    JumlahPendakiAlatJahit = JumlahPendakiAlatJahit-JumlahPendakiAlatJahit;

                }
                if(CheckAlatJahitSebelumnya == true)
                {
                    if(JumlahPendakiAlatJahit >=15)
                    {
                        JumlahAlatJahit++;

                    }
                    JumlahPendakiAlatJahit = JumlahPendakiAlatJahit-JumlahPendakiAlatJahit;

                }


            }

        }
        while(JumlahPendakiAlatJahit>=1);

        JumlahBenang = JumlahBenang*JumlahAlatJahit;
        JumlahJarum  = JumlahJarum*JumlahAlatJahit;



    }

    private void alertDialogAlatJahit()
    {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_peralatan_kalkulator_peralatan_hasil_perhitungan_alat_jahit, null);

        alertDialogBuilder.setView(view).setCancelable(false);
        Button Ok  = (Button)view.findViewById(R.id.Ok);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        toolbar.setTitle("Alat Jahit");


        TextView TextviewJarum,TextviewBenang;

        TextviewJarum = (TextView)  view.findViewById(R.id.TextviewJarum);
        TextviewBenang = (TextView)  view.findViewById(R.id.TextviewBenang);

        TextviewJarum.setText ("Benang : "+Integer.toString(JumlahBenang));
        TextviewBenang.setText("Jarum   : "+Integer.toString(JumlahJarum));



        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/2);
        alert.show();

        Ok.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();

            }
        });




    }

    public void BahanBakar()
    {

        int JumlahBahanBakar = 0 ;
        boolean CheckBahanBakarSebelumnya = false;
        do
        {
            if(JumlahPendakiBahanBakar >=5)
            {
                JumlahBahanBakar++;
                JumlahPendakiBahanBakar = JumlahPendakiBahanBakar-5;
                CheckBahanBakarSebelumnya = true;

            }
            if(JumlahPendakiBahanBakar <5)
            {
                if(CheckBahanBakarSebelumnya == false)
                {
                    JumlahBahanBakar++;
                    JumlahPendakiBahanBakar = JumlahPendakiBahanBakar-JumlahPendakiBahanBakar;

                }
                if(CheckBahanBakarSebelumnya == true)
                {
                    if(JumlahPendakiBahanBakar >=2)
                    {
                        JumlahBahanBakar++;

                    }
                    JumlahPendakiBahanBakar = JumlahPendakiBahanBakar-JumlahPendakiBahanBakar;

                }


            }

        }
        while(JumlahPendakiBahanBakar>=1);
        JumlahBahanBakar = JumlahBahanBakar*LamaPendakian;
        JumlahBahanBakar++;
        TextHasilBahanBakar.setText(Integer.toString(JumlahBahanBakar));

    }

    public void PenunjukArah()
    {
        int JumlahPenunjukArah = 0 ;
        boolean CheckPenunjukSebelumnya = false;
        do
        {
            if(JumlahPendakiPenunjukArah >=10)
            {
                JumlahPenunjukArah++;
                JumlahPendakiPenunjukArah = JumlahPendakiPenunjukArah-10;
                CheckPenunjukSebelumnya = true;

            }
            if(JumlahPendakiPenunjukArah <10)
            {
                if(CheckPenunjukSebelumnya == false)
                {
                    JumlahPenunjukArah++;
                    JumlahPendakiPenunjukArah = JumlahPendakiPenunjukArah-JumlahPendakiPenunjukArah;

                }
                if(CheckPenunjukSebelumnya == true)
                {
                    if(JumlahPendakiPenunjukArah >=4)
                    {
                        JumlahPenunjukArah++;
                    }
                    JumlahPendakiPenunjukArah = JumlahPendakiPenunjukArah-JumlahPendakiPenunjukArah;

                }


            }

        }
        while(JumlahPendakiPenunjukArah>=1);
        TextHasilPenunjukArah.setText(Integer.toString(JumlahPenunjukArah));


    }

    public void Pisau()
    {
        int JumlahPisau = 0 ;
        boolean CheckPisauSebelumnya = false;
        do
        {
            if(JumlahPendakiPisau >=4)
            {
                JumlahPisau++;
                JumlahPendakiPisau = JumlahPendakiPisau-4;
                CheckPisauSebelumnya = true;

            }
            if(JumlahPendakiPisau <4)
            {
                if(CheckPisauSebelumnya == false)
                {
                    JumlahPisau++;
                    JumlahPendakiPisau = JumlahPendakiPisau-JumlahPendakiPisau;

                }
                if(CheckPisauSebelumnya == true)
                {
                    if(JumlahPendakiPisau >=3)
                    {
                        JumlahPisau++;
                    }
                    JumlahPendakiPisau = JumlahPendakiPisau-JumlahPendakiPisau;

                }


            }

        }
        while(JumlahPendakiPisau>=1);
        TextHasilPisau.setText(Integer.toString(JumlahPisau));

    }
    public void KorekApi()
    {

        int JumlahKorekApi = 0 ;
        boolean CheckKorekApiSebelumnya = false;
        do
        {
            if(JumlahPendakiKorekApi >=10)
            {
                JumlahKorekApi++;
                JumlahPendakiKorekApi = JumlahPendakiKorekApi-10;
                CheckKorekApiSebelumnya = true;

            }
            if(JumlahPendakiKorekApi <10)
            {
                if(CheckKorekApiSebelumnya == false)
                {
                    JumlahKorekApi++;
                    JumlahPendakiKorekApi = JumlahPendakiKorekApi-JumlahPendakiKorekApi;

                }
                if(CheckKorekApiSebelumnya == true)
                {
                    if(JumlahPendakiKorekApi >=5)
                    {
                        JumlahKorekApi++;
                    }
                    JumlahPendakiKorekApi = JumlahPendakiKorekApi-JumlahPendakiKorekApi;

                }


            }

        }
        while(JumlahPendakiKorekApi>=1);
        TextHasilKorekApi.setText(Integer.toString(JumlahKorekApi));


    }
    public void Nesting()
    {

        int JumlahNesting = 0 ;
        boolean CheckNestingSebelumnya = false;
        do
        {
            if(JumlahPendakiNesting >=4)
            {
                JumlahNesting++;
                JumlahPendakiNesting = JumlahPendakiNesting-4;
                CheckNestingSebelumnya = true;

            }
            if(JumlahPendakiNesting <4)
            {
                if(CheckNestingSebelumnya == false)
                {
                    JumlahNesting++;
                    JumlahPendakiNesting = JumlahPendakiNesting-JumlahPendakiNesting;

                }
                if(CheckNestingSebelumnya == true)
                {
                    if(JumlahPendakiNesting >=2)
                    {
                        JumlahNesting++;
                    }
                    JumlahPendakiNesting = JumlahPendakiNesting-JumlahPendakiNesting;

                }


            }

        }
        while(JumlahPendakiNesting>=1);
        TextHasilNesting.setText(Integer.toString(JumlahNesting));




    }

    public void KomporLapangan()
    {

        int JumlahKomporLapangan = 0;
        boolean CheckKomporLapanganSebelumnya = false;
        do
        {

            if(JumlahPendakiKomporLapangan >=4)
            {

                JumlahKomporLapangan++ ;
                JumlahPendakiKomporLapangan=JumlahPendakiKomporLapangan-4;
                CheckKomporLapanganSebelumnya = true;

            }
            if(JumlahPendakiKomporLapangan<4)
            {

                if(CheckKomporLapanganSebelumnya == false)
                {
                    JumlahKomporLapangan++ ;


                }

                JumlahPendakiKomporLapangan=JumlahPendakiKomporLapangan-JumlahPendakiKomporLapangan;


            }

        }
        while(JumlahPendakiKomporLapangan>=1);
        JumlahKomporLapangan++;
        TextHasilKomporLapangan.setText(Integer.toString(JumlahKomporLapangan));


    }

    public void TrashBag()
    {
        int JumlahTrashBag = 0 ;
        boolean CheckTrashBagSebelumnya = false;

        do
        {
            if(JumlahPendakiTrashBag >10)
            {
                JumlahPendakiTrashBag=JumlahPendakiTrashBag-10;
                JumlahTrashBag++;
                CheckTrashBagSebelumnya = true;
            }
            if (JumlahPendakiTrashBag<=10)
            {
                if(CheckTrashBagSebelumnya == false)
                {
                    JumlahTrashBag++;
                    JumlahPendakiTrashBag=JumlahPendakiTrashBag-JumlahPendakiTrashBag;

                }
                if(CheckTrashBagSebelumnya == true)
                {
                    if(JumlahPendakiTrashBag>=4)
                    {
                        JumlahTrashBag++;
                    }
                    JumlahPendakiTrashBag=JumlahPendakiTrashBag-JumlahPendakiTrashBag;

                }

            }

        }
        while(JumlahPendakiTrashBag>=1);
        TextHasilTrashBag.setText(Integer.toString(JumlahTrashBag));
    }

    public void tenda()
    {

        do
        {
            if(JumlahPendakiTenda>=4)
            {
                JumlahPendakiTenda=JumlahPendakiTenda-4;
                JumlahTenda4 = JumlahTenda4 + 1;


            }
            if(JumlahPendakiTenda==3)
            {
                JumlahPendakiTenda=JumlahPendakiTenda-3;
                JumlahTenda3 = JumlahTenda3 + 1;


            }
            if(JumlahPendakiTenda==2)
            {

                JumlahPendakiTenda=JumlahPendakiTenda-2;
                JumlahTenda2 = JumlahTenda2 + 1;


            }
            if(JumlahPendakiTenda==1)
            {
                JumlahPendakiTenda=JumlahPendakiTenda-1;
                JumlahTenda1 = JumlahTenda1 + 1;

            }

        }
        while(JumlahPendakiTenda>=1);
        // while(JumlahPendaki>=0);


        //Toast.makeText(getBaseContext(), Integer.toString(JumlahTenda4) + " & " + Integer.toString(JumlahTenda3), Toast.LENGTH_LONG).show();
        //Toast.makeText(getBaseContext(),Integer.toString(JumlahTenda2) +" & "+ Integer.toString(JumlahTenda1),Toast.LENGTH_LONG).show();


       /* Toast.makeText(getBaseContext(), Integer.toString(TotalJumlahTenda4) + " & " + Integer.toString(TotalJumlahTenda3), Toast.LENGTH_LONG).show();
        Toast.makeText(getBaseContext(),Integer.toString(TotalJumlahTenda2) +" & "+ Integer.toString(TotalJumlahTenda1),Toast.LENGTH_LONG).show()*/

    }


    private void alertDialogTenda()
    {


        TotalJumlahTenda4 = Integer.toString(JumlahTenda4);
        TotalJumlahTenda3 = Integer.toString(JumlahTenda3);
        TotalJumlahTenda2 = Integer.toString(JumlahTenda2);
        TotalJumlahTenda1 = Integer.toString(JumlahTenda1);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_peralatan_kalkulator_peralatan_hasil_perhitungan_tenda, null);

        alertDialogBuilder.setView(view).setCancelable(false);
        Button Ok  = (Button)view.findViewById(R.id.Ok);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.my_toolbar);
        toolbar.setTitle("Jumlah Tenda");


        TextView TextviewTendaKapasitas4,TextviewTendaKapasitas3,TextviewTendaKapasitas2,TextviewTendaKapasitas1;

        TextviewTendaKapasitas4 = (TextView)  view.findViewById(R.id.TextviewTendaKapasitas4);
        TextviewTendaKapasitas3 = (TextView)  view.findViewById(R.id.TextviewTendaKapasitas3);
        TextviewTendaKapasitas2 = (TextView)  view.findViewById(R.id.TextviewTendaKapasitas2);
        TextviewTendaKapasitas1 = (TextView)  view.findViewById(R.id.TextviewTendaKapasitas1);

        TextviewTendaKapasitas4.setText("Tenda Kapasitas 4 Orang : "+TotalJumlahTenda4);
        TextviewTendaKapasitas3.setText("Tenda Kapasitas 3 Orang : "+TotalJumlahTenda3);
        TextviewTendaKapasitas2.setText("Tenda Kapasitas 2 Orang : "+TotalJumlahTenda2);
        TextviewTendaKapasitas1.setText("Tenda Kapasitas 1 Orang : "+TotalJumlahTenda1);



        final AlertDialog alert = alertDialogBuilder.create();
        alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alert.getWindow().setLayout(width - 40,height/2);
        alert.show();

        Ok.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                alert.dismiss();

            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_peralatan_kalkulator_peralatan_hasil_perhitungan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed()
    {
        Intent page = new Intent(peralatan_kalkulator_peralatan_hasil_perhitungan.this, peralatan_kalkulator_peralatan.class);
        startActivity(page);
        peralatan_kalkulator_peralatan_hasil_perhitungan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
    public  boolean onKeyDown (int keyCode, KeyEvent event)
    {
        Intent page = new Intent(peralatan_kalkulator_peralatan_hasil_perhitungan.this, peralatan_kalkulator_peralatan.class);
        startActivity(page);
        peralatan_kalkulator_peralatan_hasil_perhitungan.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        return  true;
    }
}
