package routes

import (
  . "seeme/models"
  . "seeme/helpers"

  "fmt"
  "net/http"
  "html/template"

  "encoding/json"
)

func HandlerAllUser(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal(CreateDummyUsers())
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}

func HandlerRegisterUser(w http.ResponseWriter, r *http.Request) {
  templLogin := template.Must(template.ParseFiles("views/register.html"))
  var page Page

  if r.FormValue("register") != "" {
    if err := InsertNewUser(CreateUserFromRequest(r)); err != nil { 
      page.Alert = err.Error()
      fmt.Println("Database Insert Failure: " + err.Error())
    } else {
      page.Alert = "User Registered"
    }
  }

  if err := templLogin.ExecuteTemplate(w, "register.html", page); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
}

func HandlerLoginUser(w http.ResponseWriter, r *http.Request) {
  templLogin := template.Must(template.ParseFiles("views/login.html"))
  var page Page

  db := GetDatabaseInstance()

  if r.FormValue("register") != "" {
    http.Redirect(w, r, "/register", http.StatusFound)
    return
  } else if r.FormValue("login") != "" {
    var username string
    var secret []byte

    rows, err := db.Query("select username, secret from users where username = ?", r.FormValue("username"))
    if err != nil {
      page.Alert = err.Error()
    }
    defer rows.Close()
    for rows.Next() {
      if err := rows.Scan(&username, &secret); err != nil {
        page.Alert = err.Error()
      }
    }
    fmt.Println(username, secret)
  }

  if err := templLogin.ExecuteTemplate(w, "login.html", page); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
}