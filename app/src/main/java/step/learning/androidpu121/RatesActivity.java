package step.learning.androidpu121;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import android.text.Editable;
import android.text.TextWatcher;
import step.learning.androidpu121.orm.NbuRate;
import step.learning.androidpu121.orm.NbuRateResponse;

public class RatesActivity extends AppCompatActivity {

    private final String nbuRatesUrl="https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    private NbuRateResponse nbuRateResponse;
    private Button btnRate;
    private  EditText editText;
    private List<NbuRate> nbuRateList =new ArrayList<NbuRate>();
    TextView arr_tv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);
        new Thread(this::loadUrlData).start();

        findViewById(R.id.rates_btn_alpha).setOnClickListener(this::byAlphaClick);
        findViewById(R.id.rates_btn_rate).setOnClickListener(this::byRateClick);
        editText = findViewById(R.id.edit_text);
        findViewById(R.id.rate_btn_edit).setOnClickListener(this::btnEditClick);

    }

    private void btnEditClick(View view) {
        List<NbuRate> ShowRates = nbuRateResponse.getRates().stream()
                .filter(rate -> rate.getCc().contains(editText.getText().toString().trim()))
                .collect(Collectors.toList());
        LinearLayout container = findViewById( R.id.rates_container ) ;
        container.removeAllViews();
        Drawable rateBg1 = AppCompatResources.getDrawable(
                getApplicationContext(),            R.drawable.rate_shape_1   ) ;
        LinearLayout.LayoutParams rateParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT    );
        rateParams.setMargins( 10, 5, 10, 5 );
        rateParams.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams horizontalMargin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT    );
        horizontalMargin.setMargins( 7, 0, 7, 0 );
        int str=0;
        for (NbuRate nbuRate: ShowRates) {
            LinearLayout line = new LinearLayout(this);
            if (str % 2 != 0) {

                line.setBackgroundResource(R.drawable.rate_shape_2);
                line.setPadding(15, 15, 15, 15);
                line.setOnClickListener(this::OnClick);
                TextView tv1 = new TextView(this);
                tv1.setText(nbuRate.getCc() + " ");
                tv1.setBackgroundResource(R.drawable.rate_shape_1);
                tv1.setPadding(15, 10, 15, 10);
                TextView tv2 = new TextView(this);
                tv2.setText(nbuRate.getTxt() + " ");
                tv2.setLayoutParams(horizontalMargin);
                tv2.setBackgroundResource(R.drawable.rate_shape_1);
                tv2.setPadding(15, 10, 15, 10);
                TextView tv3 = new TextView(this);
                tv3.setText(nbuRate.getRate() + "");
                tv3.setBackgroundResource(R.drawable.rate_shape_1);
                tv3.setPadding(15, 10, 15, 10);
                line.setLayoutParams(rateParams);
                line.addView(tv1);
                line.addView(tv2);
                line.addView(tv3);
                container.addView(line);
                str++;
            } else {

                line.setBackgroundResource(R.drawable.rate_shape_1);
                line.setPadding(15, 15, 15, 15);
                TextView tv1 = new TextView(this);
                tv1.setText(nbuRate.getCc() + " ");
                tv1.setBackgroundResource(R.drawable.rate_shape_2);
                tv1.setPadding(15, 10, 15, 10);
                TextView tv2 = new TextView(this);
                tv2.setText(nbuRate.getTxt() + " ");
                tv2.setLayoutParams(horizontalMargin);
                tv2.setBackgroundResource(R.drawable.rate_shape_2);
                tv2.setPadding(15, 10, 15, 10);
                TextView tv3 = new TextView(this);
                tv3.setText(nbuRate.getRate() + "");
                tv3.setBackgroundResource(R.drawable.rate_shape_2);
                tv3.setPadding(15, 10, 15, 10);
                line.setLayoutParams(rateParams);
                line.addView(tv1);
                line.addView(tv2);
                line.addView(tv3);
                container.addView(line);
                str++;
            }
        }
    }

    private void filterRate(String str)
    {
        nbuRateList.clear();
        for (NbuRate rate :  nbuRateResponse.getRates()) {
            if (rate.getTxt().contains(str) || rate.getCc().contains(str)) {
                nbuRateList.add(rate);
            }
        }
        showSearchRate();
    }
    private void showSearchRate()
    {
        LinearLayout container =findViewById(R.id.rates_container);
        container.removeAllViews();

        Drawable rateBg1 = AppCompatResources.getDrawable(
                getApplicationContext(),
                R.drawable.rate_shape_1
        );
        Drawable rateBg2 = AppCompatResources.getDrawable(
                getApplicationContext(),
                R.drawable.rate_shape_2
        );
        LinearLayout.LayoutParams rateParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rateParams.gravity=Gravity.CENTER;
        // Margin -  параметри контейнера (Layout), вони задають правила взаємного
        // розміщення елементів в одному контейнері
        rateParams.setMargins(10,5,10,5);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lineParams.setMargins(10,5,10,5);
        LinearLayout.LayoutParams horizontalMargin = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        horizontalMargin.setMargins(7,5,7,5);
        //       rateParams.gravity= Gravity.END;  // встановлення по правому краю
        int str=0;
        for(NbuRate nbuRate:nbuRateList)
        {
            LinearLayout line = new LinearLayout(this);

            TextView tv1 = new TextView(this);
            TextView tv2 = new TextView(this);
            TextView tv3 = new TextView(this);

            tv1.setText(nbuRate.getCc()+" ");
            tv2.setText(nbuRate.getTxt()+" ");
            tv3.setText(nbuRate.getRate()+" ");

            tv1.setPadding(10,5,10,5);
            tv2.setPadding(10,5,10,5);
            tv3.setPadding(10,5,10,5);

            tv3.setMinWidth(80);

            if(str % 2 != 0) {
                tv1.setBackground(rateBg1);
                tv2.setBackground(rateBg1);
                tv3.setBackground(rateBg1);
                line.setBackground(rateBg2);
            }
            else
            {
                tv1.setBackground(rateBg2);
                tv2.setBackground(rateBg2);
                tv3.setBackground(rateBg2);
                line.setBackground(rateBg1);
            }

            tv1.setLayoutParams(rateParams);
            tv2.setLayoutParams(rateParams);
            tv2.setLayoutParams(horizontalMargin);
            tv3.setLayoutParams(rateParams);
            line.setLayoutParams(lineParams);

            line.setTag( nbuRate );
            line.setOnClickListener( this::rateClick );

            line.addView(tv1);
            line.addView(tv2);
            line.addView(tv3);
            str++;

            container.addView(line);
        }

    }
    private void byAlphaClick(View view)
    {
        if(nbuRateResponse==null||nbuRateResponse.getRates()==null)
        {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            return;
        }
        //  nbuRateResponse.getRates().sort((r1,r2)->r1.getTxt().compareTo(r2.getTxt()));  повний запис
        // nbuRateResponse.getRates().sort(Comparator.comparing(NbuRate::getTxt));        //короткий запис
        nbuRateResponse.getRates().sort((r1,r2)->Collator.getInstance() // відсотрувати за мовою
                .compare(r1.getTxt(),r2.getTxt()));                     // (задали правило сортувати за українською абеткою)!

        showResponse();
    }
    private void byRateClick(View view)
    {
        if(nbuRateResponse==null||nbuRateResponse.getRates()==null)
        {
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
            return;
        }
        //  nbuRateResponse.getRates().sort((r1,r2)->r1.getTxt().compareTo(r2.getTxt()));  повний запис
        // nbuRateResponse.getRates().sort(Comparator.comparing(NbuRate::getTxt));        //короткий запис
        nbuRateResponse.getRates().sort(Comparator.comparing(NbuRate::getRate));

        showResponse();
    }
    private void loadUrlData()
    {
        try(InputStream stream=new URL (nbuRatesUrl).openStream()) {

            ByteArrayOutputStream builder= new ByteArrayOutputStream();
            byte[]buffer= new byte[1024*16];
            int receivedLength;
            while ((receivedLength=stream.read(buffer))>0)
            {
                builder.write(buffer,0,receivedLength);
            }

            nbuRateResponse=new NbuRateResponse(new JSONArray(builder.toString()));

            runOnUiThread(this::showResponse);


        }  catch (IOException | JSONException e) {
            Log.e("loadUrlData",e.getMessage());
        }
        catch( NetworkOnMainThreadException ignored ) {
            Log.e("loadUrlData",getString( R.string.rates_ex_thread ));

        }
    }

//    private void showResponse()
//    {
//        LinearLayout container =findViewById(R.id.rates_container);
//
//        for(NbuRate nbuRate:nbuRateResponse.getRates())
//        {
//
//            // Створення нового контейнера для кожного Button
//            LinearLayout btcCantainer = new LinearLayout(this);
//            btcCantainer.setOrientation(LinearLayout.HORIZONTAL); // Встановлення орієнтації контейнера
//            btcCantainer.setTag(nbuRate.getCc());
//            btcCantainer.setBackgroundResource(R.drawable.calc_btn_choice);
////Кнопка
//            Button rateButton = new Button(this);
//            rateButton.setText(String.format(  Locale.UK,  "%s",nbuRate.getCc()));
//            rateButton.setPadding(10,5,10,5);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                    150, LinearLayout.LayoutParams.WRAP_CONTENT
//            );
//            params.setMargins(10,5,10,5);
//
//            rateButton.setLayoutParams(params);
//            rateButton.setBackgroundResource(R.drawable.rate_btn_default);
//            rateButton.setTextColor(ContextCompat.getColor(this,  R.color.white));
//            rateButton.setTextSize(20);
//
//
//            rateButton.setOnClickListener(this::rateClick);
//            rateButton.setTag(nbuRate);
////---
//
//            btcCantainer.addView(rateButton);
//            container.addView(btcCantainer);
//
//
//        }
//
//    }
//    private void  rateClick(View view)
//    {
//
//        Clear();
//        if (btnRate != null) {
//            btnRate.setBackgroundResource(R.drawable.rate_btn_default);
//        }
//        view.setBackgroundResource(R.drawable.rate_btn_choice);
//        LinearLayout container =findViewById(R.id.rates_container);
//        NbuRate nbuRate = (NbuRate) view.getTag();
//        LinearLayout viewWithTag = container.findViewWithTag(nbuRate.getCc());
//
//        TextView tv = new TextView(this);
//        tv.setText(String.format(
//        Locale.UK,"%s (%s) %f грн\n курс на  \" %s \" ",
//        nbuRate.getCc(),  nbuRate.getTxt(), nbuRate.getRate(), nbuRate.getExchangedate()));
//
//        tv.setPadding(10,5,10,5);
//        tv.setTextColor(ContextCompat.getColor(this,  R.color.white));
//        tv.setTextSize(14);
//        tv.setOnClickListener(this::rateClick);
//        tv.setTag(nbuRate.getCc());
//        btnRate=(Button) view;;
//        arr_tv=tv;
//        viewWithTag.addView(tv);
//
//    }

    private void Clear()
    {
        if(arr_tv!=null) {
            arr_tv.setText("");
        }
    }

    private void showResponse()
    {
        LinearLayout container =findViewById(R.id.rates_container);
        container.removeAllViews();

        Drawable rateBg1 = AppCompatResources.getDrawable(
                getApplicationContext(),
                R.drawable.rate_shape_1
        );
        Drawable rateBg2 = AppCompatResources.getDrawable(
                getApplicationContext(),
                R.drawable.rate_shape_2
        );
        LinearLayout.LayoutParams rateParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        rateParams.gravity=Gravity.CENTER;
        // Margin -  параметри контейнера (Layout), вони задають правила взаємного
        // розміщення елементів в одному контейнері
        rateParams.setMargins(10,5,10,5);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lineParams.setMargins(10,5,10,5);
        LinearLayout.LayoutParams horizontalMargin = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        horizontalMargin.setMargins(7,5,7,5);
        //       rateParams.gravity= Gravity.END;  // встановлення по правому краю
        int x=0;
        for(NbuRate nbuRate:nbuRateResponse.getRates())
        {
            LinearLayout line = new LinearLayout(this);

            TextView tv1 = new TextView(this);
            TextView tv2 = new TextView(this);
            TextView tv3 = new TextView(this);

            tv1.setText(nbuRate.getCc()+" ");
            tv2.setText(nbuRate.getTxt()+" ");
            tv3.setText(nbuRate.getRate()+" ");

            tv1.setPadding(10,5,10,5);
            tv2.setPadding(10,5,10,5);
            tv3.setPadding(10,5,10,5);

            tv3.setMinWidth(80);

            if(x % 2 != 0) {
                tv1.setBackground(rateBg1);
                tv2.setBackground(rateBg1);
                tv3.setBackground(rateBg1);
                line.setBackground(rateBg2);
            }
            else
            {
                tv1.setBackground(rateBg2);
                tv2.setBackground(rateBg2);
                tv3.setBackground(rateBg2);
                line.setBackground(rateBg1);
            }

            tv1.setLayoutParams(rateParams);
            tv2.setLayoutParams(rateParams);
            tv2.setLayoutParams(horizontalMargin);
            tv3.setLayoutParams(rateParams);
            line.setLayoutParams(lineParams);

            line.setTag( nbuRate );
            line.setOnClickListener( this::rateClick );

            line.addView(tv1);
            line.addView(tv2);
            line.addView(tv3);
            x++;
//
            container.addView(line);
        }

    }
    private void  rateClick(View view)
    {
        NbuRate nbuRate = (NbuRate) view.getTag();
        showRateDialog(nbuRate);
    }

    private void OnClick(View view) {
        NbuRate nbuRate = (NbuRate) view.getTag();
        showRateDialog(nbuRate);
    }
    private void showRateDialog(NbuRate nbuRate) {

        new AlertDialog.Builder( this, androidx.appcompat.R.style.Theme_AppCompat_Dialog)
                .setTitle( nbuRate.getCc())
                .setMessage(String.format(
                        Locale.UK, "%s\n 1 %s = %f грн\nактуален на %s",
                        nbuRate.getTxt(),
                        nbuRate.getCc(),
                        nbuRate.getRate(),
                        nbuRate.getExchangedate()))

                .show();
    }

    private void showResponseTxt()
    {
        StringBuilder sb =new StringBuilder();
        for(NbuRate nbuRate:nbuRateResponse.getRates())
        {
            sb.append(String.format(
                    Locale.UK,
                    "%s (%s) %f грн\n",
                    nbuRate.getCc(),
                    nbuRate.getTxt(),
                    nbuRate.getRate()));
        }
    }

}

/*
Робота з Інтернет. АРІ.
На прикладі курсу валют.
1. Основу роботи з Інтернет становить об'єкт класу URL (схожий за
змістом з File). Створення об'єкту не спричиняє роботу з мережею,
лише з'являється сам об'єкт.

2. Мережна активність запускається спробою "відкриття" URL, зокрема,
openConnection() або openStream()
при цьому можна одержати NetworkOnMainThreadException, що свідчить
про спробу відкриття з'єднання з UI (основного) потоку. Це виключення
може не потрапити у catch, спостерігаємо у логері.

3. Підключення до інтернету вимагає включеного WIFI (на емуляторі), а
також дозволу, відсутність якого призводить до
SecurityException: Permission denied (missing INTERNET permission?)
Даний дозвіл має бути зазначений в маніфесті
   <uses-permission android:name="android.permission.INTERNET"/>
   Інколи зміна дозволів вимагає перевстановлення застосунку

4.  Винесення роботи з мережою до окремого потоку призводить до
 зворотної проблеми повязаною з виключенням
CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
яка забороняє змінювати представлення (View) з іншого потоку.
Звертатись слід через делегування
runOnUiThread(()->...);

-------------------------------------------------------
5. ORM. Після одержання сирих даних їх слід відобразити (map) на обєкти
та колукції платформи (мови програмування). Для цього створюють класи
що відповідають структурі даних та їх конструктори (фабрики),
які приймають JSON
 */