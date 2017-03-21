package routes

import (
  . "seeme/helpers"

  "net/http"

  "encoding/json"
)

func HandlerFeedback(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal(GetDiscoverableUsersFromDB(w))
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}