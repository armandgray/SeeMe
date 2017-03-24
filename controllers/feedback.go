package controllers

import (
  "seeme/db"

  "errors"
)

func InsertFeedback(username string, message string) (error) {
	db := db.GetDatabaseInstance()
  if message == "" { return errors.New("Message Empty") }
  _, err := db.Exec("INSERT INTO feedback VALUES (?, ?, ?)", 
                  username, nil, message)
  return err
}