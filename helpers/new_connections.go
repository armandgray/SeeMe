package helpers

import (
  "seeme/db"
  "errors"

  "net/http"
)

func InsertNewConnection(username string, connection string) (error) {
	if username == "" || connection == "" || username == connection {
    return errors.New("Invalid Connection!")
  }
  if _, err := db.GetUser(username); err != nil {
    return errors.New("User Not Found!")
  }

  if _, err := db.GetUser(connection); err != nil {
    return errors.New("Requested User Not Found!")
  }

  if err := db.InsertNewConnection(username, connection); err != nil { 
    return errors.New("Internal Connection Error")
  }

  return nil
}