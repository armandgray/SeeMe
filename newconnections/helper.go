package newconnections

import (
  "seeme/db"
  "errors"
)

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
