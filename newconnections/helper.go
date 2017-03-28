package newconnections

import (
  "seeme/db"
  "errors"
)

func VerifyConnection(username string, connection string) (error) {
	if username == "" || connection == "" || username == connection {
    return errors.New("Invalid Connection!")
  }

  if _, err := db.GetUser(username); err != nil {
    return errors.New("User Not Found!")
  }
  if _, err := db.GetUser(connection); err != nil {
    return errors.New("Requested User Not Found!")
  }
  userMap, err := db.GetConnectionsMap(connection)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if userMap[username] {
    return errors.New("Connection Already Exists")
  }

  return nil
}
