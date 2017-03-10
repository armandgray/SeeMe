package main

import (
  . "seeme/routes"
  . "seeme/helpers"

  "fmt"
  "net/http"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)


func main()  {
  InitMySQLConnection()

  mux := http.NewServeMux()
  mux.HandleFunc("/login", HandlerLogin)
  mux.HandleFunc("/login/user", HandlerLoginUser)
  mux.HandleFunc("/register", HandlerRegisterUser)
  mux.HandleFunc("/discoverable/allusers", HandlerDiscoverableUser)
  mux.HandleFunc("/discoverable/localusers", HandlerLocalUser)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
