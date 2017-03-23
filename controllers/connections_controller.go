package controllers

import (
  . "seeme/helpers"
  "errors"
)

func NewConnectionController(username string, connection string) (error) {
	if username == "" || connection == "" || username == connection {
    return errors.New("Invalid Connection!")
  }
  if _, err := GetUserFromDB(username); err != nil {
    return errors.New("User Not Found!")
  }

  if _, err := GetUserFromDB(connection); err != nil {
    return errors.New("Requested User Not Found!")
  }

  if err := InsertNewConnection(username, connection); err != nil { 
    return errors.New("Internal Connection Error")
  }

  return nil
}