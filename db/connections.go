package db

func InsertNewConnection(username string, connection string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("INSERT INTO connections VALUES (?, ?, 'pending')", username, connection)
	return err
}

func UpdateConnectionStatus(username string, connection string, status string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("UPDATE connections SET status = ? WHERE username = ? AND connection = ?",
												status, username, connection)
	return err
}

func DeleteConnection(username string) (int64, error) {
	db := GetDatabaseInstance()
  var affect int64
  qry, err := db.Prepare("DELETE FROM users WHERE username = ?")
  if err != nil {
    return affect, err
  }

  res, err := qry.Exec(username)
  if err != nil {
    return affect, err
  }

  affect, err = res.RowsAffected()
  if err != nil {
    return affect, err
  }

  return affect, err
}

func GetConnectionsMap(user string) (map[string]bool, error) {
  return GetQueryResultsMap("SELECT connection FROM connections WHERE username = ?", user)
}
