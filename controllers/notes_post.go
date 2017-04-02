package controllers

import (
  "io"
	"fmt"
  "net/http"
  "encoding/json"
)

type Patient struct {
    FirstName string
    LastName  string
}


func PostNotesController(w http.ResponseWriter, r *http.Request) {
    r.ParseForm()
    p := new(Patient)
    err := json.NewDecoder(r.Body).Decode(p)

    if err != nil && err != io.EOF {
        fmt.Println(err.Error())
    }

    fmt.Println(p)
}