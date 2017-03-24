package main

import (
  . "seeme/routes"
  "seeme/controllers"
  "seeme/db"

  "fmt"
  "net/http"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)


func main()  {
  db.InitMySQLConnection()

  mux := http.NewServeMux()
  mux.HandleFunc("/seeme/api/login/user", controllers.LoginUserController)
  mux.HandleFunc("/seeme/api/register", controllers.RegisterUserController)
  mux.HandleFunc("/seeme/api/discoverable/allusers", HandlerDiscoverableUser)
  mux.HandleFunc("/seeme/api/discoverable/localusers", HandlerLocalUser)
  mux.HandleFunc("/seeme/api/discoverable/update-network", HandlerUpdateUserNetwork)
  mux.HandleFunc("/seeme/api/connection/new", controllers.HandlerNewConnection)
  mux.HandleFunc("/seeme/api/profile/delete", HandlerProfileDelete)
  mux.HandleFunc("/seeme/api/profile/update", HandlerProfileUpdate)
  mux.HandleFunc("/seeme/api/feedback", controllers.FeedbackController)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(db.VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
