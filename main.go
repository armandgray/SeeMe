package main

import (
  "net/http"
  "encoding/json"

  "database/sql"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)

type User struct {
  FirstName string
  LastName string
  Username string
  Secret []byte
  Discoverable bool

  Role string
  Network string
}

var db *sql.DB

func main()  {
  mux := http.NewServeMux()

  db, _ = sql.Open("mysql", "root:#54nFr4nc15c0@/seeme_db")

  mux.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
    var s []byte
    s = make([]byte, 5, 5)
    user1 := User{"Armand", "Gray", "email", s, true, "Software Engineer", "Instruct2"}
    user2 := User{"Daniela", "Meza", "username", s, false, "Program Coordinator", "Instruct2"}
    slcUser := []User{user1, user2}
      
    js, err := json.Marshal(slcUser)
    if err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
      return
    }

    w.Header().Set("Content-Type", "application/json")
    w.Write(js)
  })

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(verifyDB))
  n.UseHandler(mux)
  n.Run(":8080")
}

func verifyDB(w http.ResponseWriter, r *http.Request, next http.HandlerFunc) {
  if err := db.Ping(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  next(w, r)
}
