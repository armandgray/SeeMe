package localusers

import (
  "seeme/models"
  "seeme/db"

  "net/http"
)

func GetLocalUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]models.User, error) {
  if err := db.RenewUserNetwork(r); err != nil {
    var userList []models.User
    return userList, err
  }
  username := r.FormValue("username")
  network := r.FormValue("networkId")
  query := "SELECT first_name, last_name, role, users.username, secret, discoverable, ssid, connections.status FROM users INNER JOIN networks USING (network_id) LEFT JOIN connections ON users.username = connections.connection AND connections.username = ? OR users.username = connections.username AND connections.connection = ? WHERE discoverable = 1 AND network_id=? AND !(users.username = ?)"
  return db.GetQueryUserList(query, 8, username, username, network, username)
}