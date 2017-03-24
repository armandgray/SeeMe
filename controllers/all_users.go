package controllers

import (
  "net/http"
  "fmt"

  "encoding/json"
)

func AllUsersController(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal(GetDiscoverableUsersFromDB(w))
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}

func LocalUsersController(w http.ResponseWriter, r *http.Request) {
  userList, err := GetLocalUsersForNetwork(w, r)
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
}

func UpdateUserNetworkController(w http.ResponseWriter, r *http.Request) {
  if err := RenewUserNetwork(r); err != nil {
    fmt.Println("Network Update Error")
  }
}