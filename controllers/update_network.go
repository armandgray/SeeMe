package controllers

import (
	"seeme/db"

  "net/http"
  "fmt"
)

func UpdateUserNetworkController(w http.ResponseWriter, r *http.Request) {
  if err := db.RenewUserNetwork(r); err != nil {
    fmt.Println("Network Update Error")
  }
}