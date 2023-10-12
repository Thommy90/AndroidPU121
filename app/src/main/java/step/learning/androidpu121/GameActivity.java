package step.learning.androidpu121;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private static final int N = 4;
    private final int[][] cells = new int[N][N];
    private final TextView[][] tvCells = new TextView[N][N];
    private final Random random = new Random() ;
    private int score;
    private TextView tvScore;

    private int scoreNow=random.nextInt(2048);
    private int scoreMax= random.nextInt(2048);;
    private TextView tvScoreMax;
    private TextView tvScoreNow;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );

        // Собираем посылание на клетки игрового поля
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tvCells[i][j] = findViewById(
                        getResources().getIdentifier(
                                "game_cell_" + i + j,
                                "id",
                                getPackageName()
                        )
                );
            }

        }
        tvScoreNow =  findViewById(getResources().getIdentifier("score_now", "id", getPackageName() ));
        tvScoreMax =  findViewById(getResources().getIdentifier("score_max", "id", getPackageName() ));

        TableLayout tableLayout = findViewById( R.id.game_table );
        tableLayout.post( () -> {
            int margin = 7 ;
            int w = this.getWindow().getDecorView().getWidth() - 2 * margin;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( w, w ) ;
            layoutParams.setMargins( 7, 50, 7, 50 );
            tableLayout.setLayoutParams( layoutParams ) ;}
        ) ;

        tableLayout.setOnTouchListener(
                new OnSwipeListener( GameActivity.this ) {
                    @Override
                    public void onSwipeBottom() {
                        Toast.makeText(   // повідомлення, що з'являється та зникає з часом
                                GameActivity.this,
                                "onSwipeBottom",
                                Toast.LENGTH_SHORT   // час відображення повідомлення
                        ).show();
                    }
                    @Override
                    public void onSwipeLeft() {
                        Toast.makeText( GameActivity.this, "onSwipeLeft", Toast.LENGTH_SHORT ).show();
                    }
                    @Override
                    public void onSwipeRight() {
                        Toast.makeText( GameActivity.this, "onSwipeRight", Toast.LENGTH_SHORT ).show();
                    }
                    @Override
                    public void onSwipeTop() {
                        Toast.makeText( GameActivity.this, "onSwipeTop", Toast.LENGTH_SHORT ).show();
                    }
                } );

        spawnCell();
        spawnCell();
        showField();
    }

    /**
     * Появление нового числа на поле
     * @return Добавилось ли число (есть ли свободные клетки)
     */

    private boolean spawnCell() {
        // посколько не известно где и сколько свободных клеток, ищем их все
        List<Integer> freeCellIndexes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if(cells[i][j] == 0) {
                    freeCellIndexes.add(10 * i + j);
                }
            }
        }
        // проверяем есть ли свободные клетки
        int cnt = freeCellIndexes.size() ;
        if(cnt == 0){
            return false;
        }
        // генерируем случайный индекс в пределах длины массива
        int randIndex = random.nextInt(cnt);
        // получаем собранный индекс клетки
        int randCellIndex = freeCellIndexes.get( randIndex );
        // Делим его на координаты
        int x = randCellIndex / 10;
        int y = randCellIndex % 10;
        // генерируем слуайно число: 2 (с вероятностью 0.9) или 4 (0.1)
        cells[x][y] = random.nextInt(10) == 0 // "один с 10"
                ? 4     // этот блок с вероятностью 1 / 10
                : 2;    // этот в других случаях
        return true;
    }

    /**
     * Показание поля - отображение числовых данных на View и
     * подбор стилей в соответствии со значением числа
     */
    private void showField(){
        // особенность - некоторые параметры "на лету" можно изменять
        // через стили , но не все, для некоторых приходится подавать
        // отдельные инструкции
        Resources resources = getResources();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                //  устанавливаем текст - изображением числа в массиве
                tvCells[i][j].setText(String.valueOf(cells[i][j]));

                // изменяем стиль в соответствии до значения этого числа (cells[i][j]);
                tvCells[i][j].setTextAppearance(
                        resources.getIdentifier(
                                "game_cell_" + cells[i][j],
                                "style",
                                getPackageName()
                        )
                );

                // отдельно изменяем background, через стиль игнорриурется
                tvCells[i][j].setBackgroundColor(
                        resources.getColor(
                                resources.getIdentifier(
                                        "game_bg_" + cells[i][j],
                                        "color",
                                        getPackageName()
                                ),
                                getTheme()
                        )
                );
            }
        }
        if(scoreNow < scoreMax)
        {
            tvScoreMax.setText( "SCORE MAX\n" + scoreMax );
            tvScoreNow.setText( "SCORE\n" + scoreNow );
        }
        else {
            tvScoreMax.setText( "SCORE MAX\n" + scoreNow );
            tvScoreNow.setText( "SCORE\n" + scoreMax );
        }
    }
}
/*
Д.З. Реалізувати верстку головної активності для гри 2048
 */