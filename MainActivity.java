package com.example.saira_000.drug_demo1016;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnDragListener {
    /**
     * ドラッグされたView
     */
    private View mDragView;
    /**
     * レイアウト
     */
    private GridLayout mParent;


    final int panelId[] = {R.id.panel1, R.id.panel2, R.id.panel3,
            R.id.panel4, R.id.panel5, R.id.panel6,
            R.id.panel7, R.id.panel8, R.id.panel9,
            R.id.panel10, R.id.panel11, R.id.panel12,
            R.id.panel13, R.id.panel14, R.id.panel15,R.id.panel16
    };        // パネル1～9のID

    int panelToDrawable[] = new int[16];
    ImageView panelImageView[] = new ImageView[16];

    final int imageDrawableId[] = {R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4, R.drawable.image5, R.drawable.image6,
            R.drawable.image7, R.drawable.image8, R.drawable.image9,
            R.drawable.image10, R.drawable.image11, R.drawable.image12,
            R.drawable.image13, R.drawable.image14, R.drawable.image15, R.drawable.image16
    };

    boolean isClicked = false;
    /**
     * アプリ起動時の最初に呼ばれるイベント
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 16; i++) {
            panelImageView[i] = (ImageView) this.findViewById(panelId[i]);
        }
        shuffle();    // シャッフルする



        Button buttonsyahhuru = findViewById(R.id.buttonsyahhuru);
        buttonsyahhuru.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                shuffle();
            }
        });

        Button buttonok = findViewById(R.id.buttonok);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }

        });

        // 各Viewに、ボタン長押し・ドラッグのイベントを設定していく
        mParent = findViewById(R.id.grid_layout);
        for (int i = 0; i < mParent.getChildCount(); i++) {
            View v = mParent.getChildAt(i);
            v.setOnLongClickListener(this);
            v.setOnDragListener(this);
        }

    }


    /**
     * Viewを長押ししたとき通知されるイベント
     *
     * @param v 対象のView
     * @return イベント有効有無
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onLongClick(View v) {
        // 長押ししているViewをドラッグ状態にする。
        v.startDragAndDrop(null, new View.DragShadowBuilder(v), v, 0);
        mDragView = v;

        // 図（b, c）から、ドラッグ中のViewは見えなくなる必要があるので、透過値調整して消す。
        // Visibilityで消すこともできるが、ドラッグ終了時のアニメーションが入るので、透過値で非表示を表現
        mDragView.setAlpha(0);
        return true;
    }

    /**
     * ドラッグされたとき通知されるイベント
     *
     * @param v     通知を受信したView
     * @param event イベント（使用するのは、ドラッグ終了、ドラッグしたまま他のViewの上にカーソルが合う)
     * @return イベント有効有無(必ずTrueにする)
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public boolean onDrag(final View v, DragEvent event) {
        switch (event.getAction()) {
            // 手を放し、ドラッグが終了した時の処理。ドラッグしているViewを表示させる。
            case DragEvent.ACTION_DRAG_ENDED:
                getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDragView.setAlpha(1);
                    }
                });
                break;

            // ドラッグ中他のViewの上に乗る時の処理。Viewの位置を入れ替える
            case DragEvent.ACTION_DRAG_LOCATION:
                getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.swap(v, mDragView);
                    }
                });
                break;
        }
        return true;
    }

    /**
     * Viewの位置を入れ替える
     *
     * @param v1 入れ替え対象１
     * @param v2 入れ替え対象2
     */
    private void swap(View v1, View v2) {
        // 同じViewなら入れ替える必要なし
        if (v1 == v2) return;

        // レイアウトパラメータを抜き出して、入れ替えを行う
        GridLayout.LayoutParams p1 = (GridLayout.LayoutParams) v1.getLayoutParams();
        GridLayout.LayoutParams p2 = (GridLayout.LayoutParams) v2.getLayoutParams();
        mParent.removeView(v1);
        mParent.removeView(v2);
        mParent.addView(v1, p2);
        mParent.addView(v2, p1);
    }

    public void shuffleProc(View v) {
        shuffle();
    }

    private void shuffle() {
        for (int i = 0; i < 16; i++) {
            panelToDrawable[i] = 0;    // パネルとdrawableの対応表を初期化（空にする）
        }
        // パネルをランダムに表示する
        Random r = new Random();
        for (int i = 0; i < 16; i++) {
            int temp;
            do {
                temp = r.nextInt(16);        // 0～15の乱数
            } while (panelToDrawable[temp] != 0);    // 空きでない間、乱数を作り続ける
            panelToDrawable[temp] = i;    // 空きパネルに入れる
        }
        for (int i = 0; i < 16; i++) {    // パネルを表示する
            panelImageView[i].setImageResource(imageDrawableId[panelToDrawable[i]]);

        }
    }
    private void swapPanel(int i){
        for (int idx = 0; idx < 16; idx++) {
            if (idx != panelToDrawable[idx]) {
                isClicked = !isClicked;
                return;    // 違っていた時点で抜ける
            }
        }
        Toast.makeText(this, "揃いました", Toast.LENGTH_SHORT).show();


    }

}