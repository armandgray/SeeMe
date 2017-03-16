package helpers

import (
	. "seeme/models"
  "fmt"

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

func InsertNewUser(user User) (error) {
	_, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network_id) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                  user.FirstName, user.LastName, user.Role, user.Username, 
                  user.Secret, user.Discoverable, nil)
	return err
}

func GetUserFromDB(username string) (User, error) {
  var user User
  row := db.QueryRow("select * from users where username = ?", username)
  err := row.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, &user.Secret, &user.Discoverable, &user.Network)

  return user, err
}

func GetDiscoverableUsersFromDB(w http.ResponseWriter) ([]User) {
  var userList []User
  var user User

  rows, err := db.Query("select * from users where discoverable = 1 AND !(network_id = 'NULL')")
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, 
                        &user.Secret, &user.Discoverable, &user.Network); err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
    } else {
      userList = append(userList, user)
    }
  }
  if err = rows.Err(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }

  return userList
}

func GetLocalUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]User) {
  var networkId string
  row := db.QueryRow("select network_id from networks where network_id = ?", r.FormValue("networkId"))
  if err := row.Scan(&networkId); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  fmt.Println(networkId)
  if networkId == "" {
    if err := insertNewNetwork(r); err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
    }
  }

  if err := updateUserNetwork(r); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  return getExistingUsersForNetwork(w, r)
}

func insertNewNetwork(r *http.Request) (error) {
  _, err := db.Exec("INSERT INTO networks (network_id, ssid) VALUES (?, ?)", 
                  r.FormValue("networkId"), r.FormValue("ssid"))
  return err
}

func updateUserNetwork(r *http.Request) (error) {
  _, err := db.Exec("UPDATE users SET network_id=? WHERE username= ?", 
                  r.FormValue("networkId"), r.FormValue("username"))
  return err
}

func getExistingUsersForNetwork(w http.ResponseWriter, r *http.Request) ([]User) {
  var userList []User
  var user User

  rows, err := db.Query("SELECT first_name, last_name, role, username, secret, discoverable, ssid FROM users INNER JOIN networks USING (network_id) WHERE discoverable = ? AND network_id=? AND !(username = ?)", 
    1, r.FormValue("networkId"), r.FormValue("username"))
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  defer rows.Close()
  for rows.Next() {
    if err = rows.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, 
                        &user.Secret, &user.Discoverable, &user.Network); err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
    } else {
      userList = append(userList, user)
    }
  }
  if err = rows.Err(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }

  return userList
}
