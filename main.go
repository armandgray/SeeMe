package main

import (
  . "./routes"

  "net/http"

  "database/sql"

  "github.com/urfave/negroni"
  _ "github.com/go-sql-driver/mysql"
)

var db *sql.DB

func main()  {
  db, _ = sql.Open("mysql", "root:#54nFr4nc15c0@/seeme_db")

  mux := http.NewServeMux()
  mux.HandleFunc("/", Handler)

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
