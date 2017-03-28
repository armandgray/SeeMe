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
	db := GetDatabaseInstance()

  var affect int64
  qry, err := db.Prepare("DELETE FROM connections WHERE username = ? AND connection = ?")
  if err != nil {
    return affect, errors.New("Prepare Update Error!")
  }

  res, err := qry.Exec(username, connection)
  if err != nil {
    return affect, errors.New("Update Query Error!")
  }

  affect, err = res.RowsAffected()
  if err != nil {
    return affect, errors.New("Internal Update Error!")
  }

  return affect, nil
}

func GetConnectionsMap(user string) (map[string]bool, error) {
  return GetQueryResultsMap("SELECT connection FROM connections WHERE username = ?", user)
}
