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

func DeleteConnection(username string, connection string) (error) {
	db := GetDatabaseInstance()
	_, err := db.Exec("DELETE FROM connections WHERE username = ? AND connection = ?", 
												username, connection)
	return err
}

func GetConnectionsMap(user string) (map[string]bool, error) {
  return GetQueryResultsMap("SELECT connection FROM connections WHERE username = ?", user)
}
