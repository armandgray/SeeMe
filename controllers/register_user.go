package controllers

import (
  . "seeme/models"
  . "seeme/helpers"
  "seeme/db"

  "fmt"
  "net/http"
  "html/template"

  "encoding/json"
  "golang.org/x/crypto/bcrypt"
)

func RegisterUserController(w http.ResponseWriter, r *http.Request) {
  templLogin := template.Must(template.ParseFiles("views/register.html"))
  var page Page

  if r.FormValue("register") != "" {
    if err := db.InsertUser(CreateUserFromRequest(r)); err != nil { 
      page.Alert = err.Error()
      fmt.Println("Database Insert Failure: " + err.Error())
    } else {
      http.Redirect(w, r, "/seeme/api/login/user?username=" + r.FormValue("username"), http.StatusFound)
      return
    }
  }

  if err := templLogin.ExecuteTemplate(w, "register.html", page); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
}

func HandlerLogin(w http.ResponseWriter, r *http.Request) {
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

func HandlerLoginUser(w http.ResponseWriter, r *http.Request) {
  var userSlice = []User{}
  if r.FormValue("username") != "" {
    user, _ := db.GetUser(r.FormValue("username"))
    userSlice = []User{user}
  }

  js, err := json.Marshal(userSlice)
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}
