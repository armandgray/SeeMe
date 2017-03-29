package db

import (
  . "seeme/models"

  "net/http"
  "errors"

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

func PostDeleteQuery(query string, params ...interface{}) (int64, error) {
  db := GetDatabaseInstance()
  qry, err := db.Prepare(query)
  if err != nil {
    return 0, errors.New("Prepare Update Error!")
  }

  res, err := qry.Exec(params...)
  if err != nil {
    return 0, errors.New("Update Query Error!")
  }

  affect, err := res.RowsAffected()
  if err != nil {
    return affect, errors.New("Internal Update Error!")
  }

  return affect, nil
}

func GetQueryUserList(query string, params ...interface{}) ([]User, error) {
  db := GetDatabaseInstance()
  var userList []User
  var user User
  var network sql.NullString
  var role sql.NullString
  var status sql.NullString

  rows, err := db.Query(query, params...)
  if err != nil {
    return []User{}, err
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&user.FirstName, &user.LastName, &role, &user.Username, 
                        &user.Secret, &user.Discoverable, &network, &status); err != nil {
      return []User{}, err
    } else {
      if role.Valid {
        if val, err := role.Value(); err == nil {
          user.Role = val.(string)
        }
      }
      if network.Valid {
        if val, err := network.Value(); err == nil {
          user.Network = val.(string)
        }
      }
      if status.Valid {
        if val, err := status.Value(); err == nil {
          user.Status = val.(string)
        } else {
          user.Status = "unknown"
        }
      }
      userList = append(userList, user)
    }
  }
  if err = rows.Err(); err != nil {
    return []User{}, err
  }

  return userList, nil
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
