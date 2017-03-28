package db

import (
	"errors"
)

func InsertNewConnection(username string, connection string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO connections VALUES (?, ?, 'pending')", username, connection)
	return err
}

func UpdateConnectionStatus(username string, connection string, status string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("UPDATE connections SET status = ? WHERE username = ? AND connection = ?",
												status, username, connection)
	return err
}

func DeleteConnection(username string, connection string) (int64, error) {
  primaryUser := username
  connectUser := connection

  userMap, err := GetConnectionsMap(connection)
  if err != nil {
    return 0, errors.New("Connection Search Error!")
  }
  if userMap[username] {
    primaryUser = connection
    connectUser = username
  }

  return PostDeleteQuery("", primaryUser, connectUser)
}

func GetConnectionsMap(user string) (map[string]bool, error) {
  return GetQueryResultsMap("SELECT connection FROM connections WHERE username = ?", user)
}
