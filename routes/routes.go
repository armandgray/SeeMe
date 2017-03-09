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
  templLogin := template.Must(template.ParseFiles("views/login.html"))

  db := GetDatabaseInstance()

  var s []byte
  s = make([]byte, 5, 5)
  user := User{"Armand", "Gray", "email", s, true, "Software Engineer", "Instruct2"}

  _, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                    user.FirstName, user.LastName, user.Role, user.Username, 
                    user.Secret, user.Discoverable, user.Network)
  if (err != nil) {
    fmt.Println("Database Insert Failure: " + err.Error())
  }

  if err := templLogin.ExecuteTemplate(w, "login.html", nil); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
}