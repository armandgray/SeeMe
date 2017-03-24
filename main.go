package main

import (
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
  mux.HandleFunc("/seeme/api/discoverable/allusers", controllers.AllUsersController)
  mux.HandleFunc("/seeme/api/discoverable/localusers", controllers.LocalUsersController)
  mux.HandleFunc("/seeme/api/discoverable/update-network", controllers.UpdateUserNetworkController)
  mux.HandleFunc("/seeme/api/connection/new", controllers.HandlerNewConnection)
  mux.HandleFunc("/seeme/api/profile/delete", controllers.ProfileDeleteController)
  mux.HandleFunc("/seeme/api/profile/update", controllers.ProfileUpdateController)
  mux.HandleFunc("/seeme/api/feedback", controllers.FeedbackController)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(db.VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
