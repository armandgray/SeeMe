package controllers

import (
  "seeme/db"

  "net/http"
  "errors"
)

func RenewUserNetwork(r *http.Request) (error) {
	db := db.GetDatabaseInstance()
  var networkId string
  row := db.QueryRow("select network_id from networks where network_id = ?", r.FormValue("networkId"))
  if err := row.Scan(&networkId); err != nil {
    return errors.New("Internal Network Error!")
  }

  if networkId == "" {
    if err := insertNewNetwork(r); err != nil {
    return errors.New("Network ID Error!")
    }
  }

  if err := updateUserNetwork(r); err != nil {
    return errors.New("Network Update Error!")
  }
  
  return nil
}

func insertNewNetwork(r *http.Request) (error) {
	db := db.GetDatabaseInstance()
  _, err := db.Exec("INSERT INTO networks (network_id, ssid) VALUES (?, ?)", 
                  r.FormValue("networkId"), r.FormValue("ssid"))
  return err
}

func updateUserNetwork(r *http.Request) (error) {
	db := db.GetDatabaseInstance()
  _, err := db.Exec("UPDATE users SET network_id=? WHERE username= ?", 
                  r.FormValue("networkId"), r.FormValue("username"))
  return err
}