package controllers

import (
  "seeme/db"

  "net/http"

  "golang.org/x/crypto/bcrypt"
)

func ProfileDeleteController(w http.ResponseWriter, r *http.Request) {
  user, err := db.GetUser(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if err := bcrypt.CompareHashAndPassword(user.Secret, []byte(r.FormValue("password"))); err != nil {
    w.Write([]byte("Password Incorrect!"))
    return
  } else {
    affect, err := db.DeleteUser(r.FormValue("username")); 
    if err != nil || affect < 1 {
      w.Write([]byte("Update Failed!"))
      return
    }

    w.Write([]byte("Account Deleted!"))
  
  }
}