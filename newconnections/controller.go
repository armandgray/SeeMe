package newconnections

import (
  "seeme/db"
  "net/http"
)

func NewConnectionController(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  if err := VerifyNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  if err := db.InsertNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Request Sent"))
}