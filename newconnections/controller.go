package newconnections

import (
  "net/http"
)

func NewConnectionController(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  if err := VerifyConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  if err := InsertNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Request Sent"))
}