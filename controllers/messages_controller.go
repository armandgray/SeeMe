package controllers

import (
  . "seeme/helpers"

  "errors"
)

func InsertFeedback(username string, message string) (error) {
	db := GetDatabaseInstance()
  if message == "" { return errors.New("Message Empty") }
  _, err := db.Exec("INSERT INTO feedback VALUES (?, ?, ?)", 
                  username, nil, message)
  return err
}