package controllers

import (
  "seeme/db"

  "net/http"
  "encoding/json"
)

func GetUserNotesController(w http.ResponseWriter, r *http.Request) {
  userNotes, err := db.GetNotesList(r.FormValue("username"))
  if err != nil {
    w.Write([]byte(err.Error()))
    return
  }
  js, err := json.Marshal(userNotes)
  if err != nil {
    w.Write([]byte("Notes Processing Error!"))
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}