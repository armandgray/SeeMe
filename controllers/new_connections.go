package controllers

import (
  "seeme/helpers"

  "net/http"
)

func HandlerNewConnection(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  if err := helpers.InsertNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Request Sent"))
}