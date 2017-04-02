package controllers

import (
  "seeme/models"
  "seeme/db"

  "io"
	"fmt"
  "net/http"
  "encoding/json"
)

func PostNotesController(w http.ResponseWriter, r *http.Request) {
    r.ParseForm()
    var notes models.NotesJSON
    if err := json.NewDecoder(r.Body).Decode(&notes); err != nil && err != io.EOF {
        fmt.Println(err.Error())
    }

    fmt.Println(notes.Notes)
    db.UpdateUserNotes("armandgray@gmail.com", notes.Notes)
}