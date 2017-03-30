package db

import (
	. "seeme/models"

  "errors"
)

func GetUser(username string) (User, error) {
  userList, err := GetQueryUserList("SELECT * FROM users WHERE username = ?", 7, username)
  if err != nil || len(userList) == 0 {
    return User{}, errors.New("User Not Found")
  }
  return userList[0], nil
}

func InsertUser(user User) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network_id) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                  user.FirstName, user.LastName, user.Occupation, user.Username, 
                  user.Secret, user.Discoverable, nil)
	return err
}

func DeleteUser(username string) (int64, error) {
	return PostDeleteQuery("DELETE FROM users WHERE username = ?", username)
}

func UpdateUser(user User) (error) {
	db := GetDatabaseInstance()
  _, err := db.Exec("UPDATE users SET first_name=?, last_name=?, role=?, secret=?, discoverable=? WHERE username = ?", 
                  user.FirstName, user.LastName, user.Occupation, 
                  user.Secret, user.Discoverable, user.Username)
  return err
}