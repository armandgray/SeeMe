package com.armandgray.seeme.models;

public class User {

    private String firstName;
    private String lastName;

    private User(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
    }

    public static class Builder {
        private String firstName;
        private String lastName;

        public Builder() { }

        public Builder firstName(String s) {
            firstName = s;
            return this;
        }

        public Builder lastName(String s) {
            lastName = s;
            return this;
        }

        public User build() { return new User(this); }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
