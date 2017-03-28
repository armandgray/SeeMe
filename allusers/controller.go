package allusers

import (
  "net/http"
  "encoding/json"
)

func AllUsersController(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal(GetDiscoverableUsers(w))
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}