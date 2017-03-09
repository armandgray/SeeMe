package models

import (
  "golang.org/x/crypto/bcrypt"
)

type User struct {
  FirstName string
  LastName string
  Username string
  Secret []byte
  Discoverable bool

  Role string
  Network string
}

func CreateDummyUsers() ([]User) {
  password := "pass"
  secret, _ := bcrypt.GenerateFromPassword ([]byte(password), bcrypt.DefaultCost)

	user1 := User{"Armand", "Gray", "email", secret, true, "Software Engineer", "Instruct2"}
  user2 := User{"Daniela", "Meza", "username", secret, false, "Program Coordinator", "Instruct2"}
  return []User{user1, user2}
}