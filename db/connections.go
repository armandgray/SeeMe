package db

func InsertNewConnection(username string, connection string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO connections VALUES (?, ?)", username, connection)
	return err
}

func UpdateConnectionStatus(username string, connection string, status string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("UPDATE connections SET status = ? WHERE username = ? AND connection = ?",
												status, username, connection)
	return err
}

func DeleteConnection(username string, connection string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("DELETE FROM connections WHERE username = ? AND connection = ?", 
												username, connection)
	return err
}

func GetConnectionsMap(user string) (map[string]bool, error) {
	db := GetDatabaseInstance()
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

func GetQueryResultsMap(query string) (map[string]bool, error) {
	var data string
  dataMap := make(map[string]bool)

	rows, err := db.Query("SELECT connection FROM connections WHERE username = ?", query)
  if err != nil {
    return dataMap, err
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&data); err != nil {
      return dataMap, err
    } else {
      dataMap[data] = true
    }
  }
  if err = rows.Err(); err != nil {
    return dataMap, err
  }

  return dataMap, nil
}
