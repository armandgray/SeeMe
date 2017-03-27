package newconnections

import (
  "seeme/db"
  "errors"
  "fmt"
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

  userMap, err := getExistingConnectionsFor(connection)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  fmt.Println(userMap)
  if userMap[connection] {
    return errors.New("Connection Already Exists")
  }

  return nil
}

func getExistingConnectionsFor(user string) (map[string]bool, error) {
  db := db.GetDatabaseInstance()
  var connection string
  connectionMap := make(map[string]bool)

  rows, err := db.Query("SELECT connection FROM connections WHERE username = ?", user)
  if err != nil {
    return connectionMap, err
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&connection); err != nil {
      return connectionMap, err
    } else {
      connectionMap[connection] = true
    }
  }
  if err = rows.Err(); err != nil {
    return connectionMap, err
  }

  return connectionMap, nil
}