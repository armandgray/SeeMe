package controllers

import (
	. "seeme/models"
  . "seeme/helpers"

  "net/http"
  "errors"

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

func InsertFeedback(username string, message string) (error) {
	db := GetDatabaseInstance()
  if message == "" { return errors.New("Message Empty") }
  _, err := db.Exec("INSERT INTO feedback VALUES (?, ?, ?)", 
                  username, nil, message)
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

func GetDiscoverableUsersFromDB(w http.ResponseWriter) ([]User) {
	db := GetDatabaseInstance()
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