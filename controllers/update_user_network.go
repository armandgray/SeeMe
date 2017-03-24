package controllers

import (
  "net/http"
  "fmt"
)

func UpdateUserNetworkController(w http.ResponseWriter, r *http.Request) {
  if err := RenewUserNetwork(r); err != nil {
    fmt.Println("Network Update Error")
  }
}