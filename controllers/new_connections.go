package controllers

import (
  "seeme/db"
  "seeme/helpers"
  "net/http"
)

func NewConnectionController(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")

  if err := helpers.VerifyUsers(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }
  if err := helpers.VerifyNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  if err := db.InsertNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Request Sent"))
}