package com.example.csc311capstone.Functions;

/**
 * Login:
 * The application will utilize a login method with a standard ROT-13 encryption method for storage in the database
 * (Letternum = Letternum + 13 % 26)
 *
 * author: @AaronScott2025
 */

public class Login {
    private String username, password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * encryption()
     * Only needs encryption, because this is only used to ensure security in the database. Both creation and login uses this.
     * The method uses "ROT-13" for encryption. Simple, but ensures damage control on potential data leaks.
     * @return
     */
    public String encryption() {
        StringBuilder encryptedPassword = new StringBuilder();
        for (char ch: password.toCharArray()) {
            int value = ch - 'a' + 1;
            value = value + 13 % 26;
            char letter = (char)('a' + value - 1);
            encryptedPassword.append(letter);
        }
        String encryptedPasswordString = encryptedPassword.toString();
        return encryptedPasswordString;
    }
}
