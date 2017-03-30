package db

import (
  . "seeme/models"

	"errors"
)

func GetConnectionsMap(user string) (map[string]bool, error) {
  return GetQueryResultsMap("SELECT connection FROM connections WHERE username = ?", user)
}

func GetNetworkList(user string) ([]User, error) {
  query := "SELECT first_name, last_name, role, users.username, secret, discoverable, ssid, connections.status FROM users LEFT JOIN networks USING (network_id) INNER JOIN connections ON users.username = connections.connection AND connections.username = ? OR users.username = connections.username AND connections.connection = ?"
  return GetQueryUserList(query, 8, user, user)
}

func InsertNewConnection(username string, connection string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO connections VALUES (?, ?, 'pending')", username, connection)
	return err
}

func UpdateConnectionStatus(username string, connection string) (error) {
	db := GetDatabaseInstance()
  primaryUser, connectUser, err := getUserRelationship(username, connection)
  if err != nil {
    return err
  }
	_, err = db.Exec("UPDATE connections SET status = 'connected' WHERE username = ? AND connection = ?",
												primaryUser, connectUser)
	return err
}

func DeleteConnection(username string, connection string) (int64, error) {
  primaryUser, connectUser, err := getUserRelationship(username, connection)
  if err != nil {
    return 0, errors.New("Connection Search Error!")
  }
  return PostDeleteQuery("DELETE FROM connections WHERE username = ? AND connection = ?", 
                              primaryUser, connectUser)
}

func getUserRelationship(username string, connection string) (string, string, error) {
  userMap, err := GetConnectionsMap(connection)
  if err != nil {
    return username, connection, err
  }
  if userMap[username] {
    primaryUser := connection
    connectUser := username
    return primaryUser, connectUser, nil
  }

  return username, connection, nil
}

