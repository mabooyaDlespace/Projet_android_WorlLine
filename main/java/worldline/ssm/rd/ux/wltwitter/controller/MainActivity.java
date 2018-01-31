package worldline.ssm.rd.ux.wltwitter.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.Key;

import worldline.ssm.rd.ux.wltwitter.R;
import worldline.ssm.rd.ux.wltwitter.model.User;


public class MainActivity extends Activity implements View.OnFocusChangeListener {

    private TextView mTextViewWelcome;
    private EditText mLogin;
    private EditText mPassword;
    private Button mSignIn;
    private User mUser;
    private final Integer MINIMUM_WORD_LENGTH = 2;
    private Boolean respectedPasswordLength = false;
    private Boolean respectedLoginLength = false;
    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private SharedPreferences mSharedPreferences;// s'initialise dans la méthode onCreate
    private String welcomeMessage;
    public static final String PREF_KEY_SCORE = "score";
    public static final String PREF_KEY_LOGIN = "mLogin";
    public static final String PREF_KEY_PASSWORD = "mPassword";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {//  on filtre  que c'est le bon request code
            //rechercher le score compris dans l'intent
            int score = data.getIntExtra(TopQuizActivity.BUNDLE_EXTRA_SCORE, 0);// au cas ou la val n'est pas présente on l'initie a 0
            mSharedPreferences.edit().putInt(PREF_KEY_SCORE, score).commit();// on stock le score
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// LAYOUT
        // BINDING et initialisation
        mTextViewWelcome = (TextView) findViewById(R.id.wlt_main_welcomeTextView);
        mLogin = (EditText) findViewById(R.id.wlt_main_login);
        mPassword = (EditText) findViewById(R.id.wlt_main_password);
        mSignIn = (Button) findViewById(R.id.wlt_main_buttonSignIn);
        mUser = new User();

        /*SEULE notre application aura accès à ce fichier là ce ne sont pas
           toutes les appli du téléphone qui auront accès */
        mSharedPreferences = getSharedPreferences("worldlinepref",MODE_PRIVATE);
        welcomeMessage = getResources().getString(R.string.login_welcome_text);
        welcomeMessage += " " + mSharedPreferences.getString(PREF_KEY_LOGIN, "") + " !";
        mLogin.setText(mSharedPreferences.getString(PREF_KEY_LOGIN, ""));
        mPassword.setText(mSharedPreferences.getString(PREF_KEY_PASSWORD, ""));

        //Enable signin
        enableIfLongEnough(mLogin,mPassword);
        //Listeners
        mTextViewWelcome.setText(welcomeMessage);

        mLogin.setOnFocusChangeListener(this);
        mPassword.setOnFocusChangeListener(this);
        mLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //mLogin.setText(mSharedPreferences.getString(PREF_KEY_LOGIN,""));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // je veux mettre a jour la taille du mode passe
                respectedLoginLength = charSequence.toString().length() > MINIMUM_WORD_LENGTH;
                mSignIn.setEnabled(respectedLoginLength && respectedPasswordLength);
            }


            @Override
            public void afterTextChanged(Editable editable) {


            }

        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                respectedPasswordLength = charSequence.toString().length() > MINIMUM_WORD_LENGTH;
                mSignIn.setEnabled(respectedLoginLength && respectedPasswordLength);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        });
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // un clik = User stock les donne et un file le contexte au top quiz
                mUser.setLogin(mLogin.getText().toString());
                mUser.setPassword(mPassword.getText().toString());
                //save preferencies
                mSharedPreferences.edit().putString((PREF_KEY_LOGIN), mUser.getLogin()).commit();//Je souhaite édité ces valeur
                mSharedPreferences.edit().putString((PREF_KEY_PASSWORD), mUser.getPassword()).commit();
                //update welcom message
                welcomeMessage = getResources().getString(R.string.login_welcome_text);
                welcomeMessage += " " + mSharedPreferences.getString(PREF_KEY_LOGIN, "") + " !";
                mTextViewWelcome.setText(welcomeMessage);
                // switch activity
                Intent gameActivity = new Intent(MainActivity.this, TopQuizActivity.class);
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);/*On ajoute une condition de filtre
                    on peut verifier que c'est bien l'activité 42 pour récupere le résultat
                */
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_action_logout) {
            mSharedPreferences.edit().clear().commit();
            //update message
            Toast.makeText(MainActivity.this, getResources().getString(R.string.action_logout), Toast.LENGTH_SHORT).show();

            welcomeMessage = getResources().getString(R.string.login_welcome_text);
            welcomeMessage += " " + mSharedPreferences.getString(PREF_KEY_LOGIN, "") + " !";
            mTextViewWelcome.setText(welcomeMessage);
            mLogin.setText(mSharedPreferences.getString(PREF_KEY_LOGIN, ""));
            mPassword.setText(mSharedPreferences.getString(PREF_KEY_PASSWORD, ""));

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Permet de savoir si un texte à finit d'etre édité ou et lance un toast si mots de passe /logine
     * n'est pas assez long
     *
     * @param view
     * @param b
     */
    @Override
    public void onFocusChange(View view, boolean b) {
        toastIfNotLongEnough();
    }

    public void toastIfNotLongEnough() {
        if (!(respectedLoginLength)) {
            String mstring = getResources().getString(R.string.error_no_login);
            Toast.makeText(MainActivity.this, mstring, Toast.LENGTH_SHORT).show();

        }
        if (!(respectedPasswordLength)) {
            String mstring = getResources().getString(R.string.error_no_password);
            Toast.makeText(MainActivity.this, mstring, Toast.LENGTH_SHORT).show();

        }
    }

    public void enableIfLongEnough(EditText editText1, EditText editText2) {
        if (editText1 != null && editText2 != null) {
            respectedLoginLength = editText1.getText().length() > MINIMUM_WORD_LENGTH;
            respectedPasswordLength = editText2.getText().length() > MINIMUM_WORD_LENGTH;
            mSignIn.setEnabled(respectedLoginLength && respectedPasswordLength);
        }
    }
}
