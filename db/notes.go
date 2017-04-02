package db

import (
  "errors"
  "fmt"
  "strings"
)

func UpdateUserNotes(username string, notes []string) (error) {
	db := GetDatabaseInstance()
  fmt.Println(db)
  if len(notes) == 0 { return errors.New("No Notes Found") }
  query := "INSERT INTO notes VALUES "
  params := []string{}

  for _, note := range notes {
    query += "(?, ?),"
    params = append(params, username, note)
	}

	query = strings.TrimSuffix(query, ",")

  _, err := db.Exec(query, params...)
  return err
}