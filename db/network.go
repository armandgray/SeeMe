package db

import (
  "seeme/models"

  "net/http"
  "errors"
)

func GetLocalUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]models.User, error) {
  if err := RenewUserNetwork(r); err != nil {
    var userList []models.User
    return userList, err
  }
  username := r.FormValue("username")
  network := r.FormValue("networkId")
  query := "SELECT first_name, last_name, role, users.username, secret, discoverable, ssid, connections.status FROM users INNER JOIN networks USING (network_id) LEFT JOIN connections ON users.username = connections.connection AND connections.username = ? OR users.username = connections.username AND connections.connection = ? WHERE discoverable = 1 AND network_id=? AND !(users.username = ?)"
  return GetQueryUserList(query, 8, username, username, network, username)
}

func RenewUserNetwork(r *http.Request) (error) {
	db := GetDatabaseInstance()
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
	db := GetDatabaseInstance()
  _, err := db.Exec("INSERT INTO networks (network_id, ssid) VALUES (?, ?)", 
                  r.FormValue("networkId"), r.FormValue("ssid"))
  return err
}

func updateUserNetwork(r *http.Request) (error) {
	db := GetDatabaseInstance()
  _, err := db.Exec("UPDATE users SET network_id=? WHERE username= ?", 
                  r.FormValue("networkId"), r.FormValue("username"))
  return err
}