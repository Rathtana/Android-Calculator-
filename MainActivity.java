package com.example.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Declaring the fields/creating references
    private EditText result = null;
    private EditText newNumber = null;
    private TextView displayOperation = null;

    //Variables to hold the operands and type of calculations
    //Using class Double because we don't want to set it to any value and
    //this is a way to indicate that there isn't any input yet
    private Double operand1 = null;
    private String pendingOperation = "=";
    private static final String STATE_PENDING_OPERATION = "pending"; //creating ref for bundle
    private static final String STATE_OPERAND1 = "STATE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing objects by referencing it to its id on the display
        result = (EditText) findViewById(R.id.result);
        newNumber = (EditText) findViewById(R.id.newNumber);
        displayOperation = (TextView) findViewById(R.id.operation);

        //Creating References for all the buttons and initializing to its id on the display
        //All the buttons for the numbers and a dot

        Button button0 = (Button) findViewById(R.id.button0);
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button4 = (Button) findViewById(R.id.button4);
        Button button5 = (Button) findViewById(R.id.button5);
        Button button6 = (Button) findViewById(R.id.button6);
        Button button7 = (Button) findViewById(R.id.button7);
        Button button8 = (Button) findViewById(R.id.button8);
        Button button9 = (Button) findViewById(R.id.button9);
        Button buttonDot = (Button) findViewById(R.id.buttonDot);
        Button buttonNeg = (Button) findViewById(R.id.buttonNeg);
        Button buttonClear = (Button) findViewById(R.id.buttonClear);

        //All the buttons for operators
        Button buttonEqual = (Button) findViewById(R.id.buttonEqual);
        Button buttonDivid = (Button) findViewById(R.id.buttonDivide);
        Button buttonMultiply = (Button) findViewById(R.id.buttonMultiply);
        Button buttonMinus = (Button) findViewById(R.id.buttonMinus);
        Button buttonPlus = (Button) findViewById(R.id.buttonPlus);

        //listener holds a ref to all un-click instance and onClick method
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            //view is a ref to the button that was tapped
            public void onClick(View view) {
                //Converting view to Button widget since all we have is Button type
                Button b = (Button) view;
                //getting the text on the button
                String text = b.getText().toString();

                String editableText = newNumber.getText().toString();

                if (text.equals("neg")) {
                    //editableText has to be empty or contains "-" sign
                    switch (editableText) {
                        case "":
                            newNumber.append("-");
                            break;
                        case "-":
                            newNumber.setText("");
                            break;
                    }
                }
                else if (text.equals("ce")) {
                    if(editableText.length() > 0) {
                        newNumber.setText("");
                    }
                }

                    //display on newNumber for all the number value
                    newNumber.append(text);
                }
        };
        //Assigning the buttons so it can be called when they are clicked
        //Will call the onClick method when a button is clicked
        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        buttonDot.setOnClickListener(listener);
        buttonNeg.setOnClickListener(listener);
        buttonClear.setOnClickListener(listener);

        //Creating a separate listener for operations buttons
        View.OnClickListener opListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                //get String from textView that was clicked on
                String opText = b.getText().toString();
                //get String from newNumber editText
                String value = newNumber.getText().toString();

                //if there is number in the editText then it is possible to perform operation
                //if doubleValue doesn't exist, it will not performOperation
                //this is the value in the newNumber widget.
                try {
                    Double doubleValue = Double.valueOf(value);
                    performOperation(doubleValue, opText);
                } catch (NumberFormatException e) {

                }
                //do not display in final result if the screen only contains a "-" sign
                if (!opText.equals("neg")) {
                    //set the operation that was clicked in the textView
                    pendingOperation = opText;
                    displayOperation.setText(pendingOperation);
                }
            }
        };
        //Setting listener for operations
        //opListener holds a ref to all the operation buttons
        buttonEqual.setOnClickListener(opListener);
        buttonDivid.setOnClickListener(opListener);
        buttonMultiply.setOnClickListener(opListener);
        buttonMinus.setOnClickListener(opListener);
        buttonPlus.setOnClickListener(opListener);


    }

    public void performOperation(Double value, String opText) {
        if (operand1 == null) {
            operand1 = value;
        }
        else {
            if (pendingOperation.equals("=")) {
                pendingOperation = opText;
            }
                switch (pendingOperation) {
                    case "=":
                        operand1 = value;
                        break;
                    case "+":
                        operand1 += value;
                        break;
                    case "-":
                        operand1 -= value;
                        break;
                    case "/":
                        if (value == 0) {
                            operand1 = null;
                            result.setText("Error");
                        } else {
                            operand1 /= value;
                        }
                        break;
                    case "*":
                        operand1 *= value;
                        break;
                }
            }

        if (operand1 != null) {
            result.setText(operand1.toString());
        }
        newNumber.setText("");
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //map the displayOperation to the bundle with TEXT_CONTENTS as an object reference to it
        outState.putString(STATE_PENDING_OPERATION, pendingOperation);
        //double value cannot be null or you will get a compile error
        if(operand1 != null) {
            //map a double value of operand1 with STATE_OPERAND1 as a ref to it
            outState.putDouble(STATE_OPERAND1, operand1);
        }
        super.onSaveInstanceState(outState); // called after to save the state
    }
    @Override //Ctr + o for shortcut
    protected void onRestoreInstanceState(Bundle saveInstanceState) {
        super.onRestoreInstanceState(saveInstanceState); //called to get the restored state
        //display the state again in the text view
        //need to get previous pendingOperation and store it before displaying it
        pendingOperation = saveInstanceState.getString(STATE_PENDING_OPERATION);
        displayOperation.setText(pendingOperation);

        //restoring the value of operand1
        operand1 = saveInstanceState.getDouble(STATE_OPERAND1);
    }


}





















