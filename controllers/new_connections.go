package controllers

import (
  "seeme/db"
  "errors"

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

func NewConnectionController(username string, connection string) (error) {
	if username == "" || connection == "" || username == connection {
    return errors.New("Invalid Connection!")
  }
  if _, err := GetUserFromDB(username); err != nil {
    return errors.New("User Not Found!")
  }

  if _, err := GetUserFromDB(connection); err != nil {
    return errors.New("Requested User Not Found!")
  }

  if err := db.InsertNewConnection(username, connection); err != nil { 
    return errors.New("Internal Connection Error")
  }

  return nil
}