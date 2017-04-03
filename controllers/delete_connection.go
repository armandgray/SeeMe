package controllers

import (
  "seeme/db"
  "seeme/helpers"

  "net/http"
)

func DeleteConnectionController(w http.ResponseWriter, r *http.Request) {
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
  affect, err := db.DeleteConnection(username, connection)
  if err != nil || affect < 1 {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Connection Deleted!"))

}

