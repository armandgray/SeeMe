package helpers

import (
	. "seeme/models"

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
	_, err := db.Exec("INSERT INTO users (first_name, last_name, role, username, secret, discoverable, network) VALUES (?, ?, ?, ?, ?, ?, ?)", 
                  user.FirstName, user.LastName, user.Role, user.Username, 
                  user.Secret, user.Discoverable, user.Network)
	return err
}

func GetUserFromDB(username string) (User, error) {
  var user User
  row := db.QueryRow("select * from users where username = ?", username)
  err := row.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, &user.Secret, &user.Discoverable, &user.Network)

  return user, err
}

func GetAllUsersFromDB(page Page) ([]User) {
  var userList []User
  var user User

  rows, err := db.Query("select * from users")
  if err != nil {
    page.Alert = err.Error()
  }
  defer rows.Close()
  for rows.Next() {
    if err := rows.Scan(&user.FirstName, &user.LastName, &user.Role, &user.Username, 
                        &user.Secret, &user.Discoverable, &user.Network); err != nil {
      page.Alert = err.Error()
    } else {
      userList = append(userList, user)
    }
  }
  if err = rows.Err(); err != nil {
    page.Alert = err.Error()
  }

  return userList
}
