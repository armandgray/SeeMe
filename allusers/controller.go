package allusers

import (
  "seeme/models"
  "seeme/db"

  "net/http"
)

func GetDiscoverableUsers(w http.ResponseWriter) ([]models.User) {
	db := db.GetDatabaseInstance()
  var userList []models.User
  var user models.User

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