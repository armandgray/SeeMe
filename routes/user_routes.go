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
  js, err := json.Marshal(GetLocalUsersForNetwork(w, r))
  if err != nil {
    w.Write([]byte("Internal Network Error!"))
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}

func HandlerUpdateUserNetwork(w http.ResponseWriter, r *http.Request) {
  if err := UpdateUserNetwork(r); err != nil {
    fmt.Println("Network Update Error")
  }
}