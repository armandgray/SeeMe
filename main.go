package main

import (
  . "seeme/routes"
  . "seeme/models"

  "fmt"
  "net/http"

  "database/sql"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)

var db *sql.DB

func main()  {
  db, _ = sql.Open("mysql", "root:#54nFr4nc15c0@/seeme_db")

  mux := http.NewServeMux()
  mux.HandleFunc("/allusers", HandlerAllUser)

  var s []byte
  s = make([]byte, 5, 5)
  user := User{"Armand", "Gray", "email", s, true, "Software Engineer", "Instruct2"}
  fmt.Println(user)

  _, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                    user.FirstName, user.LastName, user.Role, user.Username, 
                    user.Secret, user.Discoverable, user.Network)
  if (err != nil) {
    fmt.Println("Database Insert Failure: " + err.Error())
  }

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(verifyDB))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}

func verifyDB(w http.ResponseWriter, r *http.Request, next http.HandlerFunc) {
  if err := db.Ping(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  next(w, r)
}
