package step.learning.androidpu121;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Работа с элементами UI (views) - только после setContentView
        tvTitle = findViewById(R.id.main_tv_title);
        Button btnViews = findViewById(R.id.main_btn_views);
        btnViews.setOnClickListener( this::btnViewClick);

        Button btnCalc = findViewById(R.id.main_btn_calc);
        btnCalc.setOnClickListener( this::btnCalcClick);
    }
    // обробчики событий имеют одинаковые прототип
    private void btnViewClick (View view){   // view - sender
        Intent intent = new Intent(
                this.getApplicationContext(),
                ViewsActivity.class
        );
        startActivity( intent );
    }

    private void btnCalcClick (View view){   // view - sender
        Intent intent = new Intent(
                this.getApplicationContext(),
                CalcActivity.class
        );
        startActivity( intent );
    }
}