package controllers

import (
  "seeme/helpers"
  "seeme/db"

  "net/http"
)

func UpdateConnectionStatusController(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  
  if err := helpers.VerifyUsers(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  if err := helpers.VerifyConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  if err := db.UpdateConnectionStatus(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Connection Confirmed"))

}

