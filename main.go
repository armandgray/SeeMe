package main

import (
  . "seeme/routes"
  . "seeme/models"
  . "seeme/helpers"

  "fmt"
  "net/http"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)


func main()  {
  InitMySQLConnection()
  db := GetMySQLConnection()

  mux := http.NewServeMux()
  mux.HandleFunc("/allusers", HandlerAllUser)
  mux.HandleFunc("/register", HandlerRegisterUser)

  var s []byte
  s = make([]byte, 5, 5)
  user := User{"Armand", "Gray", "email", s, true, "Software Engineer", "Instruct2"}

  _, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                    user.FirstName, user.LastName, user.Role, user.Username, 
                    user.Secret, user.Discoverable, user.Network)
  if (err != nil) {
    fmt.Println("Database Insert Failure: " + err.Error())
  }

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
