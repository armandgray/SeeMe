package db

import (
  "net/http"

	"database/sql"
  _ "github.com/go-sql-driver/mysql"
)

var db *sql.DB

func InitMySQLConnection() {
  db, _ = sql.Open("mysql", "root:#54nFr4nc15c0@/seeme_db")
}

func VerifyMySQLConnection(w http.ResponseWriter, r *http.Request, next http.HandlerFunc) {
  if err := db.Ping(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  next(w, r)
}

func GetDatabaseInstance() (*sql.DB) {
	return db
}

func GetQueryResultsMap(query string, params ...interface{}) (map[string]bool, error) {
	db := GetDatabaseInstance()
	var data string
  dataMap := make(map[string]bool)

	rows, err := db.Query(query, params...)
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
