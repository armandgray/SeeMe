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
  apiUrl := "/seeme/api"

  mux := http.NewServeMux()
  mux.HandleFunc(apiUrl + "/login", controllers.LoginController)
  mux.HandleFunc(apiUrl + "/login/user", controllers.LoginUserController)
  mux.HandleFunc(apiUrl + "/register", controllers.RegisterUserController)
  mux.HandleFunc(apiUrl + "/discoverable/allusers", controllers.AllUsersController)
  mux.HandleFunc(apiUrl + "/discoverable/localusers", controllers.LocalUsersController)
  mux.HandleFunc(apiUrl + "/discoverable/update-network", controllers.UpdateUserNetworkController)
  mux.HandleFunc(apiUrl + "/connection/new", controllers.NewConnectionController)
  mux.HandleFunc(apiUrl + "/connection/update-status", controllers.UpdateConnectionStatusController)
  mux.HandleFunc(apiUrl + "/connection/delete", controllers.DeleteConnectionController)
  mux.HandleFunc(apiUrl + "/connection/network", controllers.ConnectionNetworkController)
  mux.HandleFunc(apiUrl + "/profile/delete", controllers.ProfileDeleteController)
  mux.HandleFunc(apiUrl + "/profile/update", controllers.ProfileUpdateController)
  mux.HandleFunc(apiUrl + "/feedback", controllers.FeedbackController)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(db.VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
