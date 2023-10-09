package step.learning.androidpu121;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CalcActivity extends AppCompatActivity {
    private final int MAX_DIGITS = 10 ;
    private TextView tvResult;
    private TextView tvExpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        tvResult = findViewById(R.id.calc_tv_result);
        tvResult.setText(R.string.calc_btn_0);
        tvExpression = findViewById(R.id.calc_tv_exppression);
        tvExpression.setText("");
        findViewById(R.id.calc_inverse).setOnClickListener(this::inverseClick);
        findViewById(R.id.calc_plus).setOnClickListener(this::plusClick);
        findViewById(R.id.calc_minus).setOnClickListener(this::minusClick);
        findViewById(R.id.calc_equel).setOnClickListener(this::equalClick);
        findViewById(R.id.calc_multiplication).setOnClickListener(this::multClick);
        findViewById(R.id.calc_divide).setOnClickListener(this::divideClick);
        findViewById(R.id.calc_comma).setOnClickListener(this::commaClick);
        findViewById(R.id.calc_plus_minus).setOnClickListener(this::plusMinus);
        findViewById(R.id.calc_ce).setOnClickListener(this::clearAllClick);
        findViewById(R.id.calc_percent).setOnClickListener(this::percentClick);
        findViewById(R.id.calc_backspace).setOnClickListener(this::backspaceClick);
        findViewById(R.id.calc_square).setOnClickListener(this::squareClick);
        findViewById(R.id.calc_sqrt).setOnClickListener(this::sqrtClick);

        for( int i = 0; i < 10; i++){
            findViewById(
            getResources().getIdentifier(
                    "calc_btn_" + i,
                    "id",
                    getPackageName()
            ) ).setOnClickListener(this::digitClick);
        }
        findViewById( R.id.calc_c).setOnClickListener(this::clearClick);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvResult.setText( savedInstanceState.getCharSequence("result"));
        tvExpression.setText( savedInstanceState.getCharSequence("expression"));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("result", tvResult.getText());
        outState.putCharSequence("expression", tvExpression.getText());
    }

    private void inverseClick( View view ) {
        String str = tvResult.getText().toString() ;
        double arg = getResult( str ) ;
        if( arg == 0 ) {
            tvResult.setText( R.string.calc_div_zero_message );
        }
        else {
            str = "1 / " + str + " =" ;
            tvExpression.setText( str );
            showResult( 1 / arg );
        }
    }

    private void percentClick( View view ) {
        String str = tvExpression.getText().toString() ;
        String tmp = str.substring(str.indexOf(" ") + 1);
        if (str.length() < 1)
        {
            tvExpression.setText("0");
            tvResult.setText("0");
        }
        else {
            if (tmp.length() == 1) {
                double num = getResult(str.substring(0, str.indexOf(" ")));
                String str2 = tvResult.getText().toString();
                double num2 = getResult(str2);
                showResult( num / 100 * num2 );
            }
        }
    }

    private void plusClick (View view){
        String str = tvExpression.getText().toString() ;
        String tmp = str.substring(str.indexOf(" ") + 1);
        if (str.length() == 0 || tmp.length() > 1)
        {
            String st = tvResult.getText().toString() ;
            st = st + " +" ;
            tvExpression.setText( st );
            tvResult.setText(R.string.calc_btn_0);
        }
        else {
            if (tmp.equals("+")) {
                double num = getResult(str.substring(0, str.indexOf(" ")));
                String str2 = tvResult.getText().toString();
                double num2 = getResult(str2);
                String s = showResultExp(num + num2);
                tvExpression.setText(s + " +");
                tvResult.setText(R.string.calc_btn_0);
            }
            else{
                int num = tvExpression.getText().toString().length();
                str = tvExpression.getText().toString();
                str = str.substring(0, num - 1);
                str = str + "+";
                tvExpression.setText(str);
            }
        }
    }

    private void commaClick (View view){
        if(tvResult.getText().toString().indexOf(",") == -1)
        tvResult.setText(tvResult.getText().toString() + ",");
    }

    private void plusMinus (View view){
        if (getResult(tvResult.getText().toString()) == 0)
            return;
        if (getResult(tvResult.getText().toString()) < 0)
        tvResult.setText( tvResult.getText().toString().substring(1));
        else tvResult.setText( "-" + tvResult.getText().toString());
    }

    private void sqrtClick (View view){
        String str = tvResult.getText().toString() ;
        double arg = getResult( str ) ;
        if( arg < 0 ) {
            tvResult.setText( R.string.calc_sqrt_underZero_message );
        }
        else showResult(Math.sqrt(arg));
    }

    private void squareClick (View view){
        String str = tvResult.getText().toString() ;
        double arg = getResult( str ) ;
        showResult(Math.pow(arg, 2));
    }

    private void backspaceClick (View view){
        int num = tvResult.getText().toString().length();
        if (num > 1 )
            tvResult.setText( tvResult.getText().toString().substring(0, num - 1));
        if (num == 1)
            tvResult.setText(R.string.calc_btn_0);
    }

    private void minusClick (View view){
        String str = tvExpression.getText().toString() ;
        String tmp = str.substring(str.indexOf(" ") + 1);
        if (str.length() == 0 || tmp.length() > 1)
        {
            String st = tvResult.getText().toString() ;
            st = st + " -" ;
            tvExpression.setText( st );
            tvResult.setText(R.string.calc_btn_0);
        }
        else {
            if (tmp.equals("-")) {
            double num = getResult(str.substring(0, str.indexOf(" ")));
            String str2 = tvResult.getText().toString();
            double num2 = getResult(str2);
            String s = showResultExp(num - num2);
            tvExpression.setText(s + " -");
            tvResult.setText(R.string.calc_btn_0);
            }
            else{
                int num = tvExpression.getText().toString().length();
                str = tvExpression.getText().toString();
                str = str.substring(0, num - 1);
                str = str + "-";
                tvExpression.setText(str);
            }
        }
    }

    private void divideClick (View view){
        String str = tvExpression.getText().toString() ;
        String tmp = str.substring(str.indexOf(" ") + 1);
        if (str.length() == 0 || tmp.length() > 1)
        {
            String st = tvResult.getText().toString() ;
            st = st + " /" ;
            tvExpression.setText( st );
            tvResult.setText(R.string.calc_btn_0);
        }
        else {
            if (tmp.equals("/")) {
            double num = getResult(str.substring(0, str.indexOf(" ")));
            String str2 = tvResult.getText().toString();
            double num2 = getResult(str2);
            String s = showResultExp(num / num2);
            tvExpression.setText(s + " /");
            tvResult.setText(R.string.calc_btn_0);
        }
            else{
            int num = tvExpression.getText().toString().length();
            str = tvExpression.getText().toString();
            str = str.substring(0, num - 1);
            str = str + "/";
            tvExpression.setText(str);
        }
        }
    }

    private void multClick (View view){
        String str = tvExpression.getText().toString() ;
        String tmp = str.substring(str.indexOf(" ") + 1);
        if (str.length() == 0 || tmp.length() > 1)
        {
            String st = tvResult.getText().toString() ;
            st = st + " *" ;
            tvExpression.setText( st );
            tvResult.setText(R.string.calc_btn_0);
        }
        else {
            if (tmp.equals("*")) {
            double num = getResult(str.substring(0, str.indexOf(" ")));
            String str2 = tvResult.getText().toString();
            double num2 = getResult(str2);
            String s = showResultExp(num * num2);
            tvExpression.setText(s + " *");
            tvResult.setText(R.string.calc_btn_0);
        }
            else{
            int num = tvExpression.getText().toString().length();
            str = tvExpression.getText().toString();
            str = str.substring(0, num - 1);
            str = str + "*";
            tvExpression.setText(str);
        }
        }
    }

    private void equalClick (View view){
        String str = tvExpression.getText().toString() ;
        double num = getResult(str.substring(0, str.indexOf(" ")));
        String str2 = tvResult.getText().toString() ;
        double num2 = getResult(str2);
        if(str.charAt(str.indexOf(" ") + 1) == '+') {
            String tmp = str.substring(str.indexOf(" ") + 1);
            if (tmp.length() > 1){
                tmp = tmp.substring(2);
                num = getResult(tmp);
                showResult(num2 + num);
                tvExpression.setText(res(num2) + " + " + res(num));
            }
            else {
                showResult(num + num2);
                tvExpression.setText(str + " " + res(num2));
            }
        }
        if(str.charAt(str.indexOf(" ") + 1) == '-') {
            String tmp = str.substring(str.indexOf(" ") + 1);
            if (tmp.length() > 1){
                tmp = tmp.substring(2);
                num = getResult(tmp);
                showResult(num2 - num);
                tvExpression.setText(res(num2) + " - " + res(num));
            }
            else {
                showResult(num - num2);
                tvExpression.setText(str + " " + res(num2));
            }
        }
        if(str.charAt(str.indexOf(" ") + 1) == '*') {
            String tmp = str.substring(str.indexOf(" ") + 1);
            if (tmp.length() > 1){
                tmp = tmp.substring(2);
                num = getResult(tmp);
                showResult(num2 * num);
                tvExpression.setText(res(num2) + " * " + res(num));
            }
            else {
                showResult(num * num2);
                tvExpression.setText(str + " " + res(num2));
            }
        }
        if(str.charAt(str.indexOf(" ") + 1) == '/') {
            String tmp = str.substring(str.indexOf(" ") + 1);
            if (tmp.length() > 1){
                tmp = tmp.substring(2);
                num = getResult(tmp);
                if( num == 0 ) {
                    tvResult.setText( R.string.calc_div_zero_message );
                }
                else {
                    showResult(num2 / num);
                    tvExpression.setText(res(num2) + " / " + res(num));
                }
            }
            else {
                if( num2 == 0 ) {
                    tvResult.setText( R.string.calc_div_zero_message );
                }
                else {
                    showResult(num / num2);
                    tvExpression.setText(str + " " + res(num2));
                }
            }
        }
    }

    private void clearClick (View view){
        tvResult.setText(R.string.calc_btn_0);
    }

    private void clearAllClick (View view){
        tvExpression.setText("");
        tvResult.setText(R.string.calc_btn_0);
    }
    private void digitClick (View view){
        String str = tvResult.getText().toString();
        if( str.equals( getString( R.string.calc_btn_0))){
            str="";
        }
        str += ((Button) view).getText();
        tvResult.setText(str);
    }

    private String res(double res){
        String str = String.valueOf( res );
        if( str.length() > MAX_DIGITS ) {
            str = str.substring( 0, MAX_DIGITS ) ;
        }
        if(str.substring(str.lastIndexOf(".") + 1).equals("0"))
            if( str.substring(str.lastIndexOf(".") + 1).length() == 1)
                str = str.substring(0 , str.lastIndexOf("."));

        str = str.replaceAll( "0", getString( R.string.calc_btn_0 ) ) ;
        str = str.replaceAll("\\.", getString( R.string.calc_tv_comma ) );
        return str;
    }

    private void showResult( double res ) {
         tvResult.setText( res(res) );
    }

    private String showResultExp( double res ) {
        return res(res) ;
    }

    private double getResult( String str ) {
        str = str.replaceAll( getString( R.string.calc_btn_0 ), "0" );
        str = str.replaceAll( getString( R.string.calc_tv_comma ), "." );
        return Double.parseDouble( str );
    }
}