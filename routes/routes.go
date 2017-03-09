package routes

import (
  . "seeme/models"
  . "seeme/helpers"

  "fmt"
  "net/http"
  "html/template"

  "encoding/json"

  "golang.org/x/crypto/bcrypt"
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

  db := GetDatabaseInstance()

  if r.FormValue("register") != "" {
    secret, _ := bcrypt.GenerateFromPassword ([]byte(r.FormValue("password")), bcrypt.DefaultCost)

    user := User{r.FormValue("firstName"), r.FormValue("lastName"), r.FormValue("username"), secret, false, r.FormValue("role"), "Instruct2"}
    if r.FormValue("discoverable") != "" {
      user.Discoverable = true
    }

    _, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                  user.FirstName, user.LastName, user.Role, user.Username, 
                  user.Secret, user.Discoverable, user.Network)
    if (err != nil) {
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