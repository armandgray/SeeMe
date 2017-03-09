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
  mux.HandleFunc("/allusers", HandlerAllUser)
  mux.HandleFunc("/login", HandlerLoginUser)
  mux.HandleFunc("/register", HandlerRegisterUser)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
