package db

import (
	. "seeme/models"

	"database/sql"
  _ "github.com/go-sql-driver/mysql"
)

func GetUser(username string) (User, error) {
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

func InsertUser(user User) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network_id) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                  user.FirstName, user.LastName, user.Role, user.Username, 
                  user.Secret, user.Discoverable, nil)
	return err
}

func DeleteUser(username string) (int64, error) {
	return PostDeleteQuery("DELETE FROM users WHERE username = ?", username)
}

func UpdateUser(user User) (error) {
	db := GetDatabaseInstance()
  _, err := db.Exec("UPDATE users SET first_name=?, last_name=?, role=?, secret=?, discoverable=? WHERE username = ?", 
                  user.FirstName, user.LastName, user.Role, 
                  user.Secret, user.Discoverable, user.Username)
  return err
}