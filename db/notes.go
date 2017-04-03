package db

import (
  "errors"
  "fmt"
  "strings"
)

func UpdateUserNotes(username string, notes []string) (error) {
  fmt.Println("call")
	return addNewNotes(username, notes)
}

func addNewNotes(username string, notes []string) (error) {
  db := GetDatabaseInstance()
  if len(notes) == 0 { return errors.New("No Notes Found") }
  query := "INSERT INTO notes (username, note) VALUES "
  params := make([]interface{}, 0)

  for _, note := range notes {
    query += "(?, ?),"
    params = append(params, username, note)
  }
  query = strings.TrimSuffix(query, ",")

  _, err := db.Exec(query, params...)
  return err
}