package controllers

import (
  "net/http"
)

func GetUserNotesController(w http.ResponseWriter, r *http.Request) {
  userNotes, err := db.GetUserNotesList(r.FormValue("username"))
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