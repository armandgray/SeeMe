package main

import (
  "seeme/loginuser"
  "seeme/registeruser"
  "seeme/allusers"
  "seeme/localusers"
  "seeme/updatenetwork"
  "seeme/newconnections"
  "seeme/profiledelete"
  "seeme/profileupdate"
  "seeme/feedback"
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
  mux.HandleFunc(apiUrl + "/login/user", loginuser.LoginUserController)
  mux.HandleFunc(apiUrl + "/register", registeruser.RegisterUserController)
  mux.HandleFunc(apiUrl + "/discoverable/allusers", allusers.AllUsersController)
  mux.HandleFunc(apiUrl + "/discoverable/localusers", localusers.LocalUsersController)
  mux.HandleFunc(apiUrl + "/discoverable/update-network", updatenetwork.UpdateUserNetworkController)
  mux.HandleFunc(apiUrl + "/connection/new", newconnections.HandlerNewConnection)
  mux.HandleFunc(apiUrl + "/profile/delete", profiledelete.ProfileDeleteController)
  mux.HandleFunc(apiUrl + "/profile/update", profileupdate.ProfileUpdateController)
  mux.HandleFunc(apiUrl + "/feedback", feedback.FeedbackController)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(db.VerifyMySQLConnection))
  n.UseHandler(mux)
  fmt.Println("Running...")
  n.Run(":8080")
}
