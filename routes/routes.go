package routes

import (
	. "seeme/models"

  "net/http"
  "encoding/json"
)

func HandlerAllUser(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal(CreateDummyUsers())
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}