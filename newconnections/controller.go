package newconnections

import (
  "net/http"
  "encoding/json"
)

func NewConnectionController(w http.ResponseWriter, r *http.Request) {
  username := r.FormValue("username")
  connection := r.FormValue("connection")
  if err := VerifyConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  userList, err := GetExistingConnectionsFor(connection)
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }
  js, err := json.Marshal(userList)
  if err != nil {
    w.Write([]byte("Network Process Error!"))
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
  return
  

  if err := InsertNewConnection(username, connection); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Request Sent"))
}