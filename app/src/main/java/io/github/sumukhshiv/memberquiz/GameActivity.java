package io.github.sumukhshiv.memberquiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    //Create all the objects
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    TextView textViewScore;
    Button buttonEndGame;
    TextView textViewTimer;
    CountDownTimer timer, timer2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Find all elements/views in the XML file
        ImageView image = (ImageView) findViewById(R.id.imageView);
        buttonA = (Button) findViewById(R.id.buttonA);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonD = (Button) findViewById(R.id.buttonD);
        textViewScore = (TextView) findViewById(R.id.textViewScore);
        buttonEndGame = (Button) findViewById(R.id.buttonEndGame);
        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        int userScore = pref.getInt("score", 0); // getting last score from the SharedPrefs, if there is no previous score it sets it to 0
        String userScoreString = Integer.toString(userScore);
        textViewScore.setText(userScoreString);


        //Array of buttons
        Button[] buttonArray = new Button[4];
        buttonArray[0] = buttonA;
        buttonArray[1] = buttonB;
        buttonArray[2] = buttonC;
        buttonArray[3] = buttonD;

        //clear all button text when initializing
        for (int i = 0; i < buttonArray.length; i++) {
            buttonArray[i].setText("");
        }
        //ArrayList of all the names of images in the drawable folder
        ArrayList<String> imageNames = new ArrayList<>();

        //Names of all members from textfile
        String[] members = {"Jessica Cherny", "Kevin Jiang", "Jared Gutierrez", "Kristin Ho", "Christine Munar", "Mudit Mittal", "Richard Hu", "Shaan Appel", "Edward Liu", "Wilbur Shi", "Young Lin", "Abhinav Koppu", "Abhishek Mangla", "Akkshay Khoslaa", "Andy Wang", "Aneesh Jindal", "Anisha Salunkhe", "Ashwin Vaidyanathan", "Cody Hsieh", "Justin Kim", "Krishnan Rajiyah", "Lisa Lee", "Peter Schafhalter", "Sahil Lamba", "Sirjan Kafle", "Tarun Khasnavis", "Billy Lu", "Aayush Tyagi", "Ben Goldberg", "Candice Ye", "Eliot Han", "Emaan Hariri", "Jessica Chen", "Katharine Jiang", "Kedar Thakkar", "Leon Kwak", "Mohit Katyal", "Rochelle Shen", "Sayan Paul", "Sharie Wang", "Shreya Reddy", "Shubham Goenka", "Victor Sun", "Vidya Ravikumar"};

        //Go through drawable folder and add names of images to imageNames ArrayList
        //CONTAINS SOME HARDCODED ELEMENTS
        Field[] drawables = io.github.sumukhshiv.memberquiz.R.drawable.class.getFields();
        for (Field f : drawables) {
            try {
                String temp = "R.drawable." + f.getName();
                if (!temp.contains("_") && (!temp.contains("$") && (!temp.contains("serialVersionUID")))) {
                    imageNames.add(f.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //Random number for Picture and correct name
        Random rand = new Random();
        int correctAnswerRandomNumber = rand.nextInt(44);
        String displayImageName = imageNames.get(correctAnswerRandomNumber);

        //Random number for button 1st incorrect
        Random rand2 = new Random();
        int wrong1 = rand.nextInt(44);
        int wrong2 = rand.nextInt(44);
        int wrong3 = rand.nextInt(44);

        //Generate random numbers that are all unique
        while (wrong1 == correctAnswerRandomNumber) {
            wrong1 = rand.nextInt(44);
        }
        while (wrong2 == correctAnswerRandomNumber || wrong2 == wrong1) {
            wrong2 = rand.nextInt(44);
        }
        while (wrong3 == correctAnswerRandomNumber || wrong3 == wrong1 || wrong3 == wrong2) {
            wrong3 = rand.nextInt(44);
        }
        //Get the actual string using random index integer
        String wrong1Name = imageNames.get(wrong1);
        String wrong2Name = imageNames.get(wrong2);
        String wrong3Name = imageNames.get(wrong3);


        //id to set the image in the imageView
        Context c = getApplicationContext();
        int id = c.getResources().getIdentifier("drawable/"+displayImageName, null, c.getPackageName());


        //Gets name from String Array members to get correctly formatted names for all 4 choices
        for (int i = 0; i < members.length; i++) {
            if (members[i].replaceAll("\\s+","").equalsIgnoreCase(displayImageName)) {
                displayImageName = members[i];
            }
            if (members[i].replaceAll("\\s+","").equalsIgnoreCase(wrong1Name)) {
                wrong1Name = members[i];
            }
            if (members[i].replaceAll("\\s+","").equalsIgnoreCase(wrong2Name)) {
                wrong2Name = members[i];
            }
            if (members[i].replaceAll("\\s+","").equalsIgnoreCase(wrong3Name)) {
                wrong3Name = members[i];
            }
        }

        //We don't want the correct answer to be in the same location every time
        //Random number for the location of the correct answer
        Random randbutton = new Random();
        int buttonIndex = randbutton.nextInt(4);

        //Array of the wrong button choices' names
        String[] wrongButtonChoices = new String[4];
        wrongButtonChoices[0] = wrong1Name;
        wrongButtonChoices[1] = wrong2Name;
        wrongButtonChoices[2] = wrong3Name;

        //set the correct button in its location
        buttonArray[buttonIndex].setText(displayImageName);

        //Add the wrong choices in the reminaing places
        for (int i = 0,j = 0; i < buttonArray.length && j < wrongButtonChoices.length; i++) {
            if (buttonArray[i].getText() == "") {
                buttonArray[i].setText(wrongButtonChoices[j]);
                j++;
            }
        }

        //Set the image of the person
        image.setImageResource(id);


        //DISPLAY COUNTDOWN TIMER
        timer = new CountDownTimer(5500, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textViewTimer.setText("done!");
                if (textViewTimer.getText().equals("done!")) {
                    finish();
                    startActivity(getIntent());
                }

            }
        };

        timer.start();

        //New variable for contact
        final String contactName = displayImageName;

        //click the image to create a contact
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timer.cancel();
                if (timer2 != null) {
                    timer2.cancel();
                }

                // Creates a new Intent to insert a contact
                Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                // Sets the MIME type to match the Contacts Provider
                intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

                // Inserts the name of the contact
                intent.putExtra(ContactsContract.Intents.Insert.NAME, contactName);
                startActivity(intent);
                textViewTimer.setText("seconds remaining: VOID");
            }
        });


        //listeners for each of the four buttons
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = buttonA.getText().toString();
                if (!contactName.equalsIgnoreCase(buttonText)) {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                } else {
                    int userScore = pref.getInt("score", 0); // getting last score from the SharedPrefs, if there is no previous score it sets it to 0
                    userScore++;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("score", userScore); // Storing the new user score
                    editor.commit(); //saving the changes
                }
                timer.cancel();
                finish();
                startActivity(getIntent());
            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = buttonB.getText().toString();
                if (!contactName.equalsIgnoreCase(buttonText)) {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                } else {
                    int userScore = pref.getInt("score", 0); // getting last score from the SharedPrefs, if there is no previous score it sets it to 0
                    userScore++;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("score", userScore); // Storing the new user score
                    editor.commit(); //saving the changes
                }
                timer.cancel();
                finish();
                startActivity(getIntent());
            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = buttonC.getText().toString();
                if (!contactName.equalsIgnoreCase(buttonText)) {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                } else {
                    int userScore = pref.getInt("score", 0); // getting last score from the SharedPrefs, if there is no previous score it sets it to 0
                    userScore++;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("score", userScore); // Storing the new user score
                    editor.commit(); //saving the changes
                }
                timer.cancel();
                finish();
                startActivity(getIntent());
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buttonText = buttonD.getText().toString();
                if (!contactName.equalsIgnoreCase(buttonText)) {
                    Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                } else {
                    int userScore = pref.getInt("score", 0); // getting last score from the SharedPrefs, if there is no previous score it sets it to 0
                    userScore++;
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putInt("score", userScore); // Storing the new user score
                    editor.commit(); //saving the changes
                }
                timer.cancel();
                finish();
                startActivity(getIntent());
            }
        });

        buttonEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //timer.cancel();
                //textViewTimer.setText("seconds remaining: VOID");
                AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
                alertDialog.setTitle("END GAME");
                alertDialog.setMessage("Are you sure you want to quit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("score", 0); // Storing the new user score
                                editor.commit(); //saving the changes
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();


            }


        });

    }

    @Override
    public void onStop() {
        timer.cancel();
        super.onStop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        timer2 = new CountDownTimer(5500, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                textViewTimer.setText("done!");
                if (textViewTimer.getText().equals("done!")) {
                    finish();
                    startActivity(getIntent());
                }

            }
        };

        timer2.start();

    }
}
