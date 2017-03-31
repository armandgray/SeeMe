package helpers

import (
  "seeme/db"
  "errors"
)

func VerifyConnection(username string, connection string) (error) {
  connectionsMap, err := db.GetAllConnectionsMap(username)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if connectionsMap[connection] {
    return nil
  }

  return errors.New("Connection Not Found")
}

func VerifyNewConnection(username string, connection string) (error) {
  connectionsMap, err := db.GetAllConnectionsMap(username)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if connectionsMap[connection] {
    return errors.New("Connection Already Exists")
  }

  return nil
}

