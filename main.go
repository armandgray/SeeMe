package main

import (
  . "./models"
  _ "./routes"

  "net/http"
  "encoding/json"

  "database/sql"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)

var db *sql.DB

func main()  {
  mux := http.NewServeMux()

  db, _ = sql.Open("mysql", "root:#54nFr4nc15c0@/seeme_db")

  mux.HandleFunc("/", Handler)

  n := negroni.Classic()
  n.Use(negroni.HandlerFunc(verifyDB))
  n.UseHandler(mux)
  n.Run(":8080")
}

func Handler(w http.ResponseWriter, r *http.Request) {
    js, err := json.Marshal(CreateDummyUsers())
    if err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
      return
    }

    w.Header().Set("Content-Type", "application/json")
    w.Write(js)
  }

func verifyDB(w http.ResponseWriter, r *http.Request, next http.HandlerFunc) {
  if err := db.Ping(); err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
  }
  next(w, r)
}
