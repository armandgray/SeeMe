package controllers

import (
  "seeme/models"
  "seeme/db"
  "io"
  "net/http"
  "encoding/json"
)

func PostNotesController(w http.ResponseWriter, r *http.Request) {
  r.ParseForm()
  var notes models.NotesJSON
  if err := json.NewDecoder(r.Body).Decode(&notes); err != nil && err != io.EOF {
    w.Write([]byte(err.Error()))
    return
  }
  w.Write([]byte(notes.Notes[0]))

  if err := db.UpdateUserNotes(r.FormValue("username"), notes.Notes); err != nil {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Notes Uploaded"))
}