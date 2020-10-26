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
    private static final int MAX_TIME_BETWEEN_TAPS = 900;
    SoundManager soundManager;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;
    Button mButton6;
    Button mButton7;
    Button mButton8;
    Button mButton9;
    Button mButtonNums;
    Button mButtonCaps;
    Button mButtonSpace;
    Button mButtonDelete;
    ImageButton mButtonEnter;
    boolean capsLock = false;
    boolean nums = false;
    private boolean delayTime = true;
    private boolean nextPressed = false;
    int previousButton = 0;
    int maxTaps = 3;
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

        nextPressed = false;
        soundManager = (SoundManager) context.getApplicationContext();

        mButtonDelete = findViewById(R.id.kbButtonDelete);
        mButton2 = findViewById(R.id.kbButton2);
        mButton3 = findViewById(R.id.kbButton3);
        mButton4 = findViewById(R.id.kbButton4);
        mButton5 = findViewById(R.id.kbButton5);
        mButton6 = findViewById(R.id.kbButton6);
        mButton7 = findViewById(R.id.kbButton7);
        mButton8 = findViewById(R.id.kbButton8);
        mButton9 = findViewById(R.id.kbButton9);
        mButtonNums = findViewById(R.id.kbButtonNums);
        mButtonCaps = findViewById(R.id.kbButtonCaps);
        mButtonSpace = findViewById(R.id.kbButtonSpace);
        mButtonEnter = findViewById(R.id.kbButtonEnter);

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
        mButtonSpace.setOnTouchListener(this);
        mButtonEnter.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View view, MotionEvent event) {

        // All communication goes through the InputConnection
        if (inputConnection == null) return false;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            switch (view.getId()) {
                case R.id.kbButtonDelete:
                    //changes to '1' on number keyboard
                    if (nums) {
                        soundManager.playSound(SoundManager.NEUTRAL);
                        maxTaps = 1;
                        //check number of taps
                        return checkNumTaps(view);
                    }
                    else {
                        soundManager.playSound(SoundManager.CANCEL);
                        deleteButton();
                    }
                    break;
                case R.id.kbButtonEnter:
                    //changes to 'delete button' on number keyboard
                    if (nums) {
                        deleteButton();
                        soundManager.playSound(SoundManager.CANCEL);
                    }
                    else {
                        //Todo: if there is a next editview set focus else save/continue/confirm
                        //hide keyboard
                        ((View) view.getParent().getParent().getParent()).setVisibility(View.GONE);
                        //Todo: scroll view back to normal
                        nextPressed = true;
                        soundManager.playSound(SoundManager.CONFIRM);
                    }
                    break;
                case R.id.kbButtonNums:
                    soundManager.playSound(SoundManager.NEUTRAL);
                    nums = !nums;
                    toggleNums();
                    break;
                case R.id.kbButtonCaps:
                    soundManager.playSound(SoundManager.NEUTRAL);
                    //changes to 'symbols button' on number keyboard
                    if (nums) {
                        maxTaps = 3;
                        Log.d("Symbols", "BttnID:" + view.getId());
                        return checkNumTaps(view);
                    } else {
                        capsLock = !capsLock;
                        toggleCaps();
                        break;
                    }
                case R.id.kbButtonSpace:
                    soundManager.playSound(SoundManager.NEUTRAL);
                    maxTaps = 1;
                    //check number of taps
                    return checkNumTaps(view);
                case R.id.kbButton7:
                case R.id.kbButton9:
                    soundManager.playSound(SoundManager.NEUTRAL);
                    if (nums) {maxTaps = 1;} else { maxTaps = 4; }
                    //check number of taps
                    return checkNumTaps(view);
                default:
                    soundManager.playSound(SoundManager.NEUTRAL);
                    if (nums) {maxTaps = 1;} else { maxTaps = 3; }
                    //check number of taps
                    return checkNumTaps(view);
            }
        }
        return false;
    }

    private boolean checkNumTaps(final View view) {
        /*if (previousButton != view.getId()) {
            getLetter(view, 0);
            handler.removeCallbacks(runnable);
            taps.clear();
            delayTime = true;
        }*/

        if (nums && (view != mButtonCaps)) {
            getLetter(view, 0);
        } else {
            //get system current milliseconds
            long time = System.currentTimeMillis();
            if (delayTime) {
                delayTime = false;
                handler.postDelayed(runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (taps.size() >= maxTaps) {
                            Log.d("Touch Event", "Max Taps===");
                            handler.removeCallbacks(runnable);
                            getLetter(view, 0);
                        } else {
                            Log.d("Touch Event", taps.size() + " - Taps" + maxTaps);
                            getLetter(view, taps.size());
                        }
                        taps.clear();
                        delayTime = true;
                        previousButton = view.getId();
                    }
                }, MAX_TIME_BETWEEN_TAPS);
            } else {
                taps.add(time);
                //record button pressed
                previousButton = view.getId();
            }
        }
        return true;
    }

    public void getLetter(View view, int letter) {
        String input;
        Button pressed = (Button) view;
        String value = (String) pressed.getText();
        input = value.substring(letter, letter + 1);
        if (capsLock) {
            input = input.toUpperCase();
            capsLock = false;
            toggleCaps();
        }
        else input = input.toLowerCase();
        if (input.equals("\u2423")) {input = " ";}
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

    private void toggleNums() {
        if (nums) {
            mButton2.setText("2");
            mButton3.setText("3");
            mButton4.setText("4");
            mButton5.setText("5");
            mButton6.setText("6");
            mButton7.setText("7");
            mButton8.setText("8");
            mButton9.setText("9");
            mButtonCaps.setText(".@+");
            mButtonSpace.setText("0");
            mButtonNums.setText(getResources().getString(R.string.ABC));
            mButtonEnter.setBackground(getResources().getDrawable(R.drawable.button_red));
            mButtonEnter.setImageResource(android.R.drawable.ic_input_delete);
            mButtonDelete.setBackground(getResources().getDrawable(R.drawable.button_aqua));
            mButtonDelete.setText("1");
        }else {
            mButton2.setText(getResources().getString(R.string.abc));
            mButton3.setText(getResources().getString(R.string.def));
            mButton4.setText(getResources().getString(R.string.ghi));
            mButton5.setText(getResources().getString(R.string.jkl));
            mButton6.setText(getResources().getString(R.string.mno));
            mButton7.setText(getResources().getString(R.string.pqrs));
            mButton8.setText(getResources().getString(R.string.tuv));
            mButton9.setText(getResources().getString(R.string.wxyz));
            mButtonCaps.setText(getResources().getString(R.string.aa));
            mButtonSpace.setText(getResources().getString(R.string.space_char));
            mButtonNums.setText(getResources().getString(R.string._1_2_3));
            mButtonEnter.setBackground(getResources().getDrawable(R.drawable.button_green));
            mButtonEnter.setImageResource(R.drawable.tick);
            mButtonDelete.setBackground(getResources().getDrawable(R.drawable.button_delete));
            mButtonDelete.setText("");
        }
    }

    public void setNumKeyboard() {
        nums = true;
        toggleNums();
    }

    public void setLettersKeyboard() {
        nums = false;
        toggleNums();
    }

    private void deleteButton() {
        CharSequence selectedText = inputConnection.getSelectedText(0);
        if (TextUtils.isEmpty(selectedText)) {
            // no selection, so delete previous character
            inputConnection.deleteSurroundingText(1, 0);
        } else {
            // delete the selection
            inputConnection.commitText("", 1);
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }

    public void setPressed(boolean pressed) {
        nextPressed = pressed;
    }

    public boolean getPressed() {
        return nextPressed;
    }
}
