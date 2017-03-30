package controllers

import (
  . "seeme/models"
  "seeme/helpers"
  "seeme/db"

  "fmt"
  "net/http"
  "html/template"
)

func RegisterUserController(w http.ResponseWriter, r *http.Request) {
  templLogin := template.Must(template.ParseFiles("views/register.html"))
  var page Page

  if r.FormValue("register") != "" {
    if err := db.InsertUser(helpers.CreateUserFromRequest(r)); err != nil { 
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