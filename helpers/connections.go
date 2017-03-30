package helpers

import (
  "seeme/db"
  "errors"
)

func VerifyConnection(username string, connection string) (error) {
  connectionsMap, err := db.GetConnectionsMap(username)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if connectionsMap[connection] {
    return nil
  }

  return errors.New("Connection Not Found")
}

func VerifyNewConnection(username string, connection string) (error) {
  userMap, err := db.GetConnectionsMap(connection)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if userMap[username] {
    return errors.New("Connection Already Exists")
  }

  userMap, err = db.GetConnectionsMap(username)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if userMap[connection] {
    return errors.New("Connection Already Exists")
  }

  return nil
}

