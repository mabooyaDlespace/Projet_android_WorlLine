package worldline.ssm.rd.ux.wltwitter.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import worldline.ssm.rd.ux.wltwitter.R;
import worldline.ssm.rd.ux.wltwitter.model.User;


public class MainActivity extends Activity implements View.OnFocusChangeListener {

    private EditText mLogin;
    private EditText mPassword;
    private Button mSignIn;
    private User mUser;
    private final Integer MINIMUM_WORD_LENGTH = 2;
    private Boolean respectedPasswordLength = false;
    private Boolean respectedLoginLength = false;
    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            //rechercher le score compris dans l'intent
            int score = data.getIntExtra(TopQuizActivity.BUNDLE_EXTRA_SCORE, 0);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLogin = (EditText) findViewById(R.id.wlt_main_login);
        mPassword = (EditText) findViewById(R.id.wlt_main_password);
        mSignIn = (Button) findViewById(R.id.wlt_main_buttonSignIn);
        mUser = new User();
        mSignIn.setEnabled(false);
        mLogin.setOnFocusChangeListener(this);
        mPassword.setOnFocusChangeListener(this);
        mLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
                // switch activity
                Intent gameActivity = new Intent(MainActivity.this, TopQuizActivity.class);
                startActivityForResult(gameActivity, GAME_ACTIVITY_REQUEST_CODE);// On ajoute une condition de filtre
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

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if(!(respectedLoginLength)){
            String mstring = getResources().getString(R.string.error_no_login);
            Toast.makeText(MainActivity.this, mstring,Toast.LENGTH_SHORT).show();

        }
        if(!(respectedPasswordLength)){
            String mstring = getResources().getString(R.string.error_no_password);
            Toast.makeText(MainActivity.this, mstring,Toast.LENGTH_SHORT).show();

        }
    }
}
