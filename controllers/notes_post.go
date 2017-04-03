package controllers

import (
  "seeme/models"
  "seeme/db"

  "io"
  "net/http"
  "encoding/json"
)

func PostNotesController(w http.ResponseWriter, r *http.Request) {
  if _, err := db.GetUser(r.FormValue("username")); err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  
  r.ParseForm()
  var notes models.NotesJSON
  if err := json.NewDecoder(r.Body).Decode(&notes); err != nil && err != io.EOF {
    w.Write([]byte(err.Error()))
  }

  if err := db.UpdateUserNotes(r.FormValue("username"), notes.Notes); err != nil {
    w.Write([]byte(err.Error()))
  }

  w.Write([]byte("Notes Uploaded"))
}