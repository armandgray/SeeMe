package routes

import (
  . "seeme/controllers"

  "net/http"
)

func HandlerNewConnection(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  if username == "" || connection == "" || username == connection {
    w.Write([]byte("Invalid Connection!"))
    return
  }
  if _, err := GetUserFromDB(username); err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }

  if _, err := GetUserFromDB(connection); err != nil {
    w.Write([]byte("Requested User Not Found!"))
    return
  }
    
  w.Write([]byte("Request Sent"))
}