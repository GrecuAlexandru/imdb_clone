package org.example;


/**
 * Credentials credentials = new Credentials.Builder("user@example.com", "password123").build();
 */


public class Credentials {
    public String email;
    public String password;

    public Credentials(
            String email,
            String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public static class Builder {
        private String email;
        private String password;

        public Builder(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Credentials build() {
            return new Credentials(email, password);
        }
    }
}
