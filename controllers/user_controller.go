package controllers

import (
	. "seeme/models"
  . "seeme/helpers"

  "net/http"

	"database/sql"
  _ "github.com/go-sql-driver/mysql"
)

func InsertNewUser(user User) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network_id) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                  user.FirstName, user.LastName, user.Role, user.Username, 
                  user.Secret, user.Discoverable, nil)
	return err
}

func GetUserFromDB(username string) (User, error) {
	db := GetDatabaseInstance()
  var user User
  var network sql.NullString
  var role sql.NullString
  row := db.QueryRow("select * from users where username = ?", username)
  err := row.Scan(&user.FirstName, &user.LastName, &role, &user.Username, &user.Secret, &user.Discoverable, &network)
  if role.Valid {
    if val, err := role.Value(); err == nil {
      user.Role = val.(string)
    }
  }
  if network.Valid {
    if val, err := network.Value(); err == nil {
      user.Network = val.(string)
    }
  }

  return user, err
}

func DeleteUserFromDB(username string) (int64, error) {
	db := GetDatabaseInstance()
  var affect int64
  qry, err := db.Prepare("DELETE FROM users WHERE username = ?")
  if err != nil {
    return affect, err
  }

  res, err := qry.Exec(username)
  if err != nil {
    return affect, err
  }

  affect, err = res.RowsAffected()
  if err != nil {
    return affect, err
  }

  return affect, err
}

func UpdateUser(user User) (error) {
	db := GetDatabaseInstance()
  _, err := db.Exec("UPDATE users SET first_name=?, last_name=?, role=?, secret=?, discoverable=? WHERE username = ?", 
                  user.FirstName, user.LastName, user.Role, 
                  user.Secret, user.Discoverable, user.Username)
  return err
}

func getExistingUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]User) {
	db := GetDatabaseInstance()
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