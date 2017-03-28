package db

import ("fmt")

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

func DeleteConnection(username string, connection string) (int64, error) {
	db := GetDatabaseInstance()
	fmt.Println("Username: " + username)
	fmt.Println("Connection: " + connection)

  var affect int64
  qry, err := db.Prepare("DELETE FROM connections WHERE username = ? AND connection = ?")
  if err != nil {
    return affect, err
  }

  res, err := qry.Exec(username, connection)
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
