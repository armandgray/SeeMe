package routes

import (
  . "seeme/helpers"

  "net/http"

  "golang.org/x/crypto/bcrypt"
)

func HandlerProfileDelete(w http.ResponseWriter, r *http.Request) {
  user, err := GetUserFromDB(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if err := bcrypt.CompareHashAndPassword(user.Secret, []byte(r.FormValue("password"))); err != nil {
    w.Write([]byte("Password Incorrect!"))
    return
  } else {
    affect, err := DeleteUserFromDB(r.FormValue("username")); 
    if err != nil || affect < 1 {
      w.Write([]byte("Update Failed!"))
      return
    }

    w.Write([]byte("Account Deleted!"))
  
  }
}

func HandlerProfileUpdate(w http.ResponseWriter, r *http.Request) {
  oldUser, err := GetUserFromDB(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if oldUser.Username == "" {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  } else {
    if err := bcrypt.CompareHashAndPassword(oldUser.Secret, []byte(r.FormValue("oldSecret"))); err != nil {
      w.Write([]byte("Password Incorrect!"))
      return
    } else {
      newUser := ReflectUsers(oldUser, CreateUserFromRequest(r))
      if err := UpdateUser(newUser); err != nil {
        w.Write([]byte("Update Failed!"))
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
      }
      http.Redirect(w, r, "/seeme/api/login/user?username=" + newUser.Username, http.StatusFound)
    }
  }
}

