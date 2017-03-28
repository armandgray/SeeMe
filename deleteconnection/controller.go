package deleteconnection

import (
  "seeme/db"

  "net/http"
)

func DeleteConnectionController(w http.ResponseWriter, r *http.Request) {
  if _, err := db.GetUser(r.FormValue("username")); err != nil {
    w.Write([]byte(err.Error()))
    return
  }
  affect, err := db.DeleteConnection(r.FormValue("username")); 
  if err != nil || affect < 1 {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Connection Deleted!"))

}

