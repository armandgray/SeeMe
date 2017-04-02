package controllers

import (
	"fmt"
  "net/http"
  "encoding/json"
)

func PostNotesController(w http.ResponseWriter, r *http.Request) {
  decoder := json.NewDecoder(r.Body)
  var t []string  
  err := decoder.Decode(&t)
  if err != nil {
      fmt.Println(err.Error())
  }
  defer r.Body.Close()
  fmt.Println(t)
}