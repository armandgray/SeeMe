package db

import (
  "errors"
  "strings"
)

func GetNotesList(username string) ([]string, error) {
  query := "SELECT note FROM notes WHERE username = ?"
  notesMap, err := GetQueryResultsMap(query, username)
  if err != nil {
    return []string{}, err
  }
  return mapKeys(notesMap), nil
}

func UpdateUserNotes(username string, notes []string) (error) {
  if _, err := removeOldNotes(username, notes); err != nil {
    return err
  }
	return addNewNotes(username, notes)
}

func mapKeys(mapForKeys map[string]bool) ([]string) {
  keys := make([]string, 0, len(mapForKeys))
  for key := range mapForKeys {
      keys = append(keys, key)
  }
  return keys
}

func removeOldNotes(username string, notes []string) (int64, error) {
  return PostDeleteQuery("DELETE FROM notes WHERE username = ?", username)
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