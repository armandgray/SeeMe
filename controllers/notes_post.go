package controllers

import (
	"seeme/db"

	"fmt"
  "net/http"
  "encoding/json"
)

func AllUsersController(w http.ResponseWriter, r *http.Request) {
	userList, err := db.GetQueryUserList("SELECT * FROM users WHERE discoverable = 1 AND !(network_id = 'NULL')", 7)
	if err != nil {
		fmt.Println(err.Error())
		http.Error(w, err.Error(), http.StatusInternalServerError)
    return
	}

  js, err := json.Marshal(userList)
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}