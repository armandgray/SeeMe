package newconnections

import (
	"seeme/db"
)

func InsertNewConnection(username string, connection string) (error) {
	db := db.GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO connections VALUES (?, ?)", username, connection)
	return err
}