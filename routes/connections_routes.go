package routes

import (
  . "seeme/controllers"

  "net/http"
)

func HandlerNewConnection(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  if err := NewConnectionController(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Request Sent"))
}