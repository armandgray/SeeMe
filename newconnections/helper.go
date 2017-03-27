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

  userList, err := getExistingConnectionsFor(connection)
  if err != nil {
    return errors.New("Connection Search Error!")
  }
  if len(userList) != 0 {
    return errors.New("Connection Already Exists")
  }

  return nil
}

func getExistingConnectionsFor(user string) ([]string, error) {
  db := db.GetDatabaseInstance()
  var connection string
  var connectionList []string

  rows, err := db.Query("SELECT connection FROM connections WHERE username = ?", user)
  if err != nil {
    return connectionList, err
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&connection); err != nil {
      return connectionList, err
    } else {
      connectionList = append(connectionList, connection)
    }
  }
  if err = rows.Err(); err != nil {
    return connectionList, err
  }

  return connectionList, nil
}