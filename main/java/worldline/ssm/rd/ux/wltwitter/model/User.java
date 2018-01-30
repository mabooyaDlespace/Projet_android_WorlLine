package worldline.ssm.rd.ux.wltwitter.model;

/**
 * Created by ISEN on 21/01/2018.
 */

public class User {

    private String mLogin;
    private  String mPassword;

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        mLogin = login;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "mLogin='" + mLogin + '\'' +
                ", mPassword='" + mPassword + '\'' +
                '}';
    }
}
