package controllers

import (
  "net/http"
)

func GetUserNotesController(w http.ResponseWriter, r *http.Request) {

    w.Write([]byte("JSON data"))
}