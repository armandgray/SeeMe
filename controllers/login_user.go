package controllers

import (
  "seeme/models"
  "seeme/db"

  "net/http"
  "encoding/json"
)

func LoginUserController(w http.ResponseWriter, r *http.Request) {
  var userSlice = []models.User{}
  if r.FormValue("username") != "" {
    user, _ := db.GetUser(r.FormValue("username"))
    userSlice = []models.User{user}
  }

  js, err := json.Marshal(userSlice)
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}

