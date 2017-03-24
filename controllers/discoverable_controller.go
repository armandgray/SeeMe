package controllers

import (
	. "seeme/models"
  "seeme/db"

  "net/http"
)

func GetDiscoverableUsersFromDB(w http.ResponseWriter) ([]User) {
	db := db.GetDatabaseInstance()
  var userList []User
  var user User

  rows, err := db.Query("select * from users where discoverable = 1 AND !(network_id = 'NULL')")
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, 
                        &user.Secret, &user.Discoverable, &user.Network); err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
    } else {
      userList = append(userList, user)
    }
  }
  if err = rows.Err(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }

  return userList
}

func GetLocalUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]User, error) {
  if err := RenewUserNetwork(r); err != nil {
    var userList []User
    return userList, err
  }
  return getExistingUsersForNetwork(w, r), nil
}

func getExistingUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]User) {
  db := db.GetDatabaseInstance()
  var userList []User
  var user User

  rows, err := db.Query("SELECT first_name, last_name, role, username, secret, discoverable, ssid FROM users INNER JOIN networks USING (network_id) WHERE discoverable = ? AND network_id=? AND !(username = ?)", 
    1, r.FormValue("networkId"), r.FormValue("username"))
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, 
                        &user.Secret, &user.Discoverable, &user.Network); err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
    } else {
      userList = append(userList, user)
    }
  }
  if err = rows.Err(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }

  return userList
}