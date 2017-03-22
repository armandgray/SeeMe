package routes

import (
  . "seeme/helpers"

  "net/http"
  "fmt"

  "encoding/json"
)

func HandlerDiscoverableUser(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal(GetDiscoverableUsersFromDB(w))
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}

func HandlerLocalUser(w http.ResponseWriter, r *http.Request) {
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

func HandlerUpdateUserNetwork(w http.ResponseWriter, r *http.Request) {
    fmt.Println("Network Update Error")
}