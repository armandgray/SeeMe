package db

import (
  "errors"
  "fmt"
)

func UpdateUserNotes(username string, notes []string) (error) {
	db := GetDatabaseInstance()
  fmt.Println(db)
  if len(notes) == 0 { return errors.New("No Notes Found") }
  query := "INSERT INTO notes VALUES "

  for _, note := range notes {
    query += "(?, ?),"
    fmt.Println(note)
    fmt.Println(query)
	}
	//trim the last ,
	query = query[0:len(query)-2]
	//prepare the statement

	//format all vals at once

  return nil
}