package controllers

import (
  . "seeme/models"
  "seeme/db"

  "net/http"
  "html/template"

  "golang.org/x/crypto/bcrypt"
)

func LoginController(w http.ResponseWriter, r *http.Request) {
  templLogin := template.Must(template.ParseFiles("views/login.html"))
  var page Page

  if r.FormValue("register") != "" {
    http.Redirect(w, r, "/seeme/api/register", http.StatusFound)
    return
  } else if r.FormValue("login") != "" {
    user, err := db.GetUser(r.FormValue("username")); 
    if err != nil {
      page.Alert = err.Error()
    }
    if user.Username == "" {
      http.Redirect(w, r, "/seeme/api/login/user?username=", http.StatusFound)
      return
    } else {
      if err := bcrypt.CompareHashAndPassword(user.Secret, []byte(r.FormValue("password"))); err != nil {
        http.Redirect(w, r, "/seeme/api/login/user?username=", http.StatusFound)
        return
      } else {
        http.Redirect(w, r, "/seeme/api/login/user?username=" + user.Username, http.StatusFound)
        return
      }
    }
  }

  if err := templLogin.ExecuteTemplate(w, "login.html", page); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
}

