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
  mux.HandleFunc("/seeme/api/login", HandlerLogin)
  mux.HandleFunc("/seeme/api/login/user", HandlerLoginUser)
  mux.HandleFunc("/seeme/api/register", HandlerRegisterUser)
  mux.HandleFunc("/seeme/api/discoverable/allusers", HandlerDiscoverableUser)
  mux.HandleFunc("/seeme/api/discoverable/localusers", HandlerLocalUser)
  mux.HandleFunc("/seeme/api/discoverable/update-network", HandlerUpdateUserNetwork)
  mux.HandleFunc("/seeme/api/profile/delete", HandlerProfileDelete)
  mux.HandleFunc("/seeme/api/profile/update", HandlerProfileUpdate)
  mux.HandleFunc("/seeme/api/feedback", HandlerFeedback)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
