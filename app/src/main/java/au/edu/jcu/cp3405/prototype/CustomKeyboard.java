package au.edu.jcu.cp3405.prototype;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class CustomKeyboard extends LinearLayout implements View.OnClickListener {

    private ImageButton mButtonDelete;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mButton5;
    private Button mButton6;
    private Button mButton7;
    private Button mButton8;
    private Button mButton9;
    private Button mButton10;
    private Button mButtonEnter;

    long lastTouchTime = 0;
    long currentTouchTime = 0;
    int tapCount = 0;

    int clicks = 0;

    // This will map the button resource id to the String value that we want to
    // input when that button is clicked.
    SparseArray<String> keyValues = new SparseArray<>();
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
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attributeSet) {
        LayoutInflater.from(context).inflate(R.layout.keyboard, this, true);
        mButtonDelete = findViewById(R.id.kbButtonDelete);
        mButton2 = findViewById(R.id.kbButton2);
        mButton3 = findViewById(R.id.kbButton3);
        mButton4 = findViewById(R.id.kbButton4);
        mButton5 = findViewById(R.id.kbButton5);
        mButton6 = findViewById(R.id.kbButton6);
        mButton7 = findViewById(R.id.kbButton7);
        mButton8 = findViewById(R.id.kbButton8);
        mButton9 = findViewById(R.id.kbButton9);
        mButton10 = findViewById(R.id.kbButton10);
        mButtonEnter = findViewById(R.id.kbButtonEnter);

        mButtonDelete.setOnClickListener(this);
        mButton2.setOnClickListener(this);
        mButton3.setOnClickListener(this);
        mButton4.setOnClickListener(this);
        mButton5.setOnClickListener(this);
        mButton6.setOnClickListener(this);
        mButton7.setOnClickListener(this);
        mButton8.setOnClickListener(this);
        mButton9.setOnClickListener(this);
        mButton10.setOnClickListener(this);
        mButtonEnter.setOnClickListener(this);

        keyValues.put(R.id.kbButtonDelete, getLetter(R.id.kbButtonDelete));
        keyValues.put(R.id.kbButton2, getLetter(R.id.kbButton2));
        keyValues.put(R.id.kbButton3, getLetter(R.id.kbButton3));
        keyValues.put(R.id.kbButton4, getLetter(R.id.kbButton4));
        keyValues.put(R.id.kbButton5, getLetter(R.id.kbButton5));
        keyValues.put(R.id.kbButton6, getLetter(R.id.kbButton6));
        keyValues.put(R.id.kbButton7, getLetter(R.id.kbButton7));
        keyValues.put(R.id.kbButton8, getLetter(R.id.kbButton8));
        keyValues.put(R.id.kbButton9, getLetter(R.id.kbButton9));
        keyValues.put(R.id.kbButton10, getLetter(R.id.kbButton10));
        keyValues.put(R.id.kbButtonEnter, getLetter(R.id.kbButtonEnter));
    }

    private String getLetter(int button) {
        switch (button) {
            case R.id.kbButtonDelete:
                return "";
            case R.id.kbButton2:
                return "A B C";
            case R.id.kbButton3:
                return "D E F";
            case R.id.kbButton4:
                return "G H I";
            case R.id.kbButton5:
                return "J K L";
            case R.id.kbButton6:
                return "M N O";
            case R.id.kbButton7:
                return "P Q R S";
            case R.id.kbButton8:
                return "T U V";
            case R.id.kbButton9:
                return "W X Y Z";
            case R.id.kbButton10:
                return "1 2 3";
            case R.id.kbButtonEnter:
                return "GO";
            default:
                return "";
        }
    }

    @Override
    public void onClick(View view) {
        // do nothing if the InputConnection has not been set yet
        if (inputConnection == null) return;

        // Delete text or input key value
        // All communication goes through the InputConnection
        if (view.getId() == R.id.kbButtonDelete) {
            CharSequence selectedText = inputConnection.getSelectedText(0);
            if (TextUtils.isEmpty(selectedText)) {
                // no selection, so delete previous character
                inputConnection.deleteSurroundingText(1, 0);
            } else {
                // delete the selection
                inputConnection.commitText("", 1);
            }
        } else if(view.getId() == R.id.kbButtonEnter) {
            //if there is a next editview set focus else hide keyboard
            ((View) view.getParent().getParent()).setVisibility(View.GONE);
            view.setEnabled(false);

        } else {
            clicks = newGetClicks();
            String value = keyValues.get(view.getId());
            String[] letters = value.split(" ");
            String input = letters[clicks-1];
            inputConnection.commitText(input, 1);
        }
    }

    private int newGetClicks() {
        lastTouchTime = currentTouchTime;
        currentTouchTime = System.currentTimeMillis();

        if ((currentTouchTime - lastTouchTime < 250) && tapCount == 2) {
            Log.d("CustomKeyboard", "TRIPLE TAP=======" + tapCount + "==========================================");
            lastTouchTime = 0;
            currentTouchTime = 0;
            tapCount = 0;
            inputConnection.deleteSurroundingText(1, 2);
            return 3;
        } else if ((currentTouchTime - lastTouchTime < 250) && tapCount < 2) {
            Log.d("CustomKeyboard", "DOUBLE TAP==========="+tapCount+"=====================================");
            lastTouchTime = 0;
            currentTouchTime = 0;
            tapCount = 2;
            inputConnection.deleteSurroundingText(1, 0);
            return 2;
        } else {
            return 1;
        }
    }

    // The activity (or some parent or controller) must give us
    // a reference to the current EditText's InputConnection
    public void setInputConnection(InputConnection ic) {
        this.inputConnection = ic;
    }
}
