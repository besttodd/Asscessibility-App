package au.edu.jcu.cp3405.prototype;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CustomKeyboard extends LinearLayout implements View.OnTouchListener {
    private static final int MAX_TIME_BETWEEN_TAPS = 1000;
    SoundManager soundManager;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;
    Button mButton6;
    Button mButton7;
    Button mButton8;
    Button mButton9;
    boolean capsLock = false;
    private boolean delayTime = true;
    private final Handler handler = new Handler();
    private Runnable runnable;
    private List<Long> taps = new ArrayList<>();

    // Our communication link to the EditText
    InputConnection inputConnection;

    public CustomKeyboard(Context context) {
        this(context, null, 0);
    }

    public CustomKeyboard(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CustomKeyboard(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true);

        soundManager = (SoundManager) context.getApplicationContext();

        ImageButton mButtonDelete = findViewById(R.id.kbButtonDelete);
        mButton2 = findViewById(R.id.kbButton2);
        mButton3 = findViewById(R.id.kbButton3);
        mButton4 = findViewById(R.id.kbButton4);
        mButton5 = findViewById(R.id.kbButton5);
        mButton6 = findViewById(R.id.kbButton6);
        mButton7 = findViewById(R.id.kbButton7);
        mButton8 = findViewById(R.id.kbButton8);
        mButton9 = findViewById(R.id.kbButton9);
        Button mButtonNums = findViewById(R.id.kbButtonNums);
        Button mButtonCaps = findViewById(R.id.kbButtonCaps);
        Button mButtonEnter = findViewById(R.id.kbButtonEnter);

        mButtonDelete.setOnTouchListener(this);
        mButton2.setOnTouchListener(this);
        mButton3.setOnTouchListener(this);
        mButton4.setOnTouchListener(this);
        mButton5.setOnTouchListener(this);
        mButton6.setOnTouchListener(this);
        mButton7.setOnTouchListener(this);
        mButton8.setOnTouchListener(this);
        mButton9.setOnTouchListener(this);
        mButtonNums.setOnTouchListener(this);
        mButtonCaps.setOnTouchListener(this);
        mButtonEnter.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View view, MotionEvent event) {
        // All communication goes through the InputConnection
        if (inputConnection == null) return false;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (view.getId()) {
                case R.id.kbButtonDelete:
                    CharSequence selectedText = inputConnection.getSelectedText(0);
                    if (TextUtils.isEmpty(selectedText)) {
                        // no selection, so delete previous character
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        // delete the selection
                        inputConnection.commitText("", 1);
                    }
                    soundManager.playSound(SoundManager.CANCEL);
                    break;
                case R.id.kbButtonEnter:
                    //Todo: if there is a next editview set focus else save/continue/confirm
                    //hide keyboard
                    ((View) view.getParent().getParent().getParent()).setVisibility(View.GONE);
                    soundManager.playSound(SoundManager.CONFIRM);
                    break;
                case R.id.kbButtonNums:
                    //Todo: swap letters for numbers
                    break;
                case R.id.kbButtonCaps:
                    capsLock = !capsLock;
                    toggleCaps();
                    break;
                    //Todo: add 'space' and '.@'
                default:
                    //check number of taps
                    //get system current milliseconds
                    long time = System.currentTimeMillis();
                    if (delayTime) {
                        delayTime = false;
                        handler.postDelayed(runnable = new Runnable() {
                            @Override
                            public void run() {
                                Log.d("Touch Event", taps.size() + " - Taps===");
                                getLetter(view, taps.size());
                                taps.clear();
                                delayTime = true;
                            }
                        }, MAX_TIME_BETWEEN_TAPS);
                    } else if (taps.size() == 4) {
                        Log.d("Touch Event", "FOUR Taps=============================================");
                        handler.removeCallbacks(runnable);
                        taps.clear();
                        delayTime = true;
                        return true;
                    } else {
                        taps.add(time);
                    }
                    soundManager.playSound(SoundManager.NEUTRAL);
                    break;
            }
        }
        return false;
    }

    public void getLetter(View view, int letter) {
        Button pressed = (Button) view;
        String value = (String) pressed.getText();
        String input = value.substring(letter, letter+1);
        if (capsLock) input = input.toUpperCase(); else input = input.toLowerCase();
        inputConnection.commitText(input, 1);
    }

    public void toggleCaps() {
        mButton2.setAllCaps(capsLock);
        mButton3.setAllCaps(capsLock);
        mButton4.setAllCaps(capsLock);
        mButton5.setAllCaps(capsLock);
        mButton6.setAllCaps(capsLock);
        mButton7.setAllCaps(capsLock);
        mButton8.setAllCaps(capsLock);
        mButton9.setAllCaps(capsLock);
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }
}
