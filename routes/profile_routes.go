package routes

import (
  . "seeme/helpers"

  "net/http"

  "golang.org/x/crypto/bcrypt"
)

func HandlerProfileDelete(w http.ResponseWriter, r *http.Request) {
  user, err := GetUserFromDB(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User " + r.FormValue("username") + " Not Found!"))
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }
  if user.Username == "" {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  } else {
    if err := bcrypt.CompareHashAndPassword(user.Secret, []byte(r.FormValue("password"))); err != nil {
      w.Write([]byte("Password Incorrect!"))
      return
    } else {
      // _, err := DeleteUserFromDB(r.FormValue("username")); 
      // if err != nil {
      //   w.Write([]byte("Update Failed!"))
      // }
      w.Write([]byte("Account Deleted!"))
      return
    }
  }

}



