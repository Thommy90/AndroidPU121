package step.learning.androidpu121;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
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
    private  boolean collapse;
    private int score;
    private TextView tvScore;

    private Animation spawnCellAnimation;
    private Animation collapsCellsAnimation;
    private MediaPlayer spawnSound;
    private CheckBox soundOffOn;
    private int scoreNow=random.nextInt(2048);
    private int scoreMax= random.nextInt(2048);;
    private TextView tvScoreMax;
    private TextView tvScoreNow;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_game );
        spawnSound = MediaPlayer.create(GameActivity.this, R.raw.jump_00);
        soundOffOn = findViewById(R.id.game_sound);
        spawnCellAnimation = AnimationUtils.loadAnimation(
                GameActivity.this,
                R.anim.game_spawn_cell
        );
        spawnCellAnimation.reset();

        collapsCellsAnimation = AnimationUtils.loadAnimation(
                GameActivity.this,
                R.anim.game_spawn_cell
        );
        collapsCellsAnimation.reset();

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
                        if(moveBottom())
                        {
                            spawnCell();
                            showField();
                        }
                        else {
                            Toast.makeText(GameActivity.this, getString( R.string.game_toast_no_move ), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onSwipeLeft() {
                        if(moveLeft())
                        {
                            spawnCell();
                            showField();
                        }
                        else {
                            Toast.makeText(GameActivity.this, getString( R.string.game_toast_no_move ), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onSwipeRight() {
                        if(moveRight())
                        {
                            spawnCell();
                            showField();
                        }
                        else {
                            Toast.makeText(GameActivity.this, getString( R.string.game_toast_no_move ), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onSwipeTop() {
                        if(moveTop())
                        {
                            spawnCell();
                            showField();
                        }
                        else {
                            Toast.makeText(GameActivity.this, getString( R.string.game_toast_no_move ), Toast.LENGTH_SHORT).show();
                        }
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
        tvCells[x][y].startAnimation(spawnCellAnimation);
        if (soundOffOn.isChecked()) spawnSound.start();
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
            tvScoreMax.setText( getString( R.string.game_tv_scoreMax, scoreMax ));
            tvScoreNow.setText( getString( R.string.game_tv_score, scoreNow ) );
        }
        else {
            tvScoreMax.setText( getString( R.string.game_tv_score, scoreNow ) );
            tvScoreNow.setText( getString( R.string.game_tv_scoreMax, scoreMax ) );
        }
    }

    private boolean moveRight() {
        boolean result = false;
        collapse =false;
        // все переміщуємо ліворуч
        boolean needRepeat;
        for (int i = 0; i < N; i++) {
            do {
                needRepeat = false;
                for (int j = N - 1; j > 0; j--) {
                    if (cells[i][j] == 0 && cells[i][j - 1] != 0) {
                        cells[i][j] = cells[i][j - 1];
                        cells[i][j - 1] = 0;
                        needRepeat = true;
                        result = true;
                    }
                }
            } while (needRepeat);
            for (int j = N - 1; j > 0; j--) {
                if (cells[i][j] != 0 && cells[i][j] == cells[i][j - 1]) {
                    collapse=true;
                    cells[i][j] *= 2;
                    //Animation
                    tvCells[i][j].startAnimation(collapsCellsAnimation);
                    scoreNow += cells[i][j];
                    for (int k = j - 1; k > 0; k--) {
                        cells[i][k] = cells[i][k - 1];
                    }
                    cells[i][0] = 0;
                    result = true;
                }
            }
        }
        return result;
    }
    private boolean moveLeft() {
        collapse=false;
        boolean result = false;
        // все переміщуємо ліворуч
        boolean needRepeat;
        for (int i = 0; i < N; i++) {
            do {
                needRepeat = false;
                for (int j = 0; j < N - 1; j++) {
                    if (cells[i][j] == 0 && cells[i][j + 1] != 0) {
                        cells[i][j] = cells[i][j + 1];
                        cells[i][j + 1] = 0;
                        needRepeat = true;
                        result = true;
                    }
                }
            } while (needRepeat);
            for (int j = 0; j < N - 1; j++) {
                if (cells[i][j] != 0 && cells[i][j] == cells[i][j + 1]) {
                    collapse=true;
                    cells[i][j] *= 2;
                    //Animation
                    tvCells[i][j].startAnimation(collapsCellsAnimation);
                    scoreNow += cells[i][j];
                    for (int k = j + 1; k < N - 1; k++) {
                        cells[i][k] = cells[i][k + 1];
                    }
                    cells[i][N - 1] = 0;
                    result = true;
                }
            }
        }
        //перевіряемо
        //переміщуємо ліворуч
        return result;
    }
    private boolean moveTop() {
        collapse=false;
        boolean result = false;
        boolean needRepeat;
        for (int j = 0; j < N; j++) {
            do {
                needRepeat = false;
                for (int i = 0; i < N - 1; i++) {
                    if (cells[i][j] == 0 && cells[i + 1][j] != 0) {
                        cells[i][j] = cells[i + 1][j];
                        cells[i + 1][j] = 0;
                        needRepeat = true;
                        result = true;
                    }
                }
            } while (needRepeat);
            for (int i = 0; i < N - 1; i++) {
                if (cells[i][j] != 0 && cells[i][j] == cells[i + 1][j]) {
                    collapse=true;
                    cells[i][j] *= 2;
                    //Animation
                    tvCells[i][j].startAnimation(collapsCellsAnimation);
                    scoreNow += cells[i][j];
                    for (int k = j + 1; k < N - 1; k++) {
                        cells[k][j] = cells[k + 1][j];
                    }
                    cells[N - 1][j] = 0;
                    result = true;
                }
            }
        }
        return result;
    }
    private boolean moveBottom() {
        collapse=false;
        boolean result = false;
        boolean needRepeat;
        for (int j = 0; j < N; j++) {
            do {
                needRepeat = false;
                for (int i = N - 1; i > 0; i--) {
                    if (cells[i][j] == 0 && cells[i - 1][j] != 0) {
                        cells[i][j] = cells[i - 1][j];
                        cells[i - 1][j] = 0;
                        needRepeat = true;
                        result = true;
                    }
                }} while (needRepeat);
            for (int i = N - 1; i > 0; i--) {
                if (cells[i][j] != 0 && cells[i][j] == cells[i - 1][j]) {
                    collapse=true;
                    cells[i][j] *= 2;
                    tvCells[i][j].startAnimation(collapsCellsAnimation);
                    scoreNow += cells[i][j];
                    for (int k = i - 1; k > 0; k--) {
                        cells[k][j] = cells[k - 1][j];
                    }
                    cells[0][j] = 0;
                    result = true;
                }
            }
        }
        return result;
    }

}

/*
Анімації (double-anim) - плавні переходи числових параметрів
між початковим та кінцевим значеннями. Закладаються декларативно (у xml)
та проробляються ОС.
Створюємо ресурсну папку (anim, назва важлива)
у ній - game_spawn_cell.xml (див. коментарі у ньому)
Завантажуємо анімацію (onCreate)  та ініціалізуємо її
Призначаємо (викликаємо) анімацію при появі комірки (див. spawnCell)
 */