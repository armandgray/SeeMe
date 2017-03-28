package deleteconnection

import (
  "seeme/db"

  "net/http"
)

func DeleteConnectionController(w http.ResponseWriter, r *http.Request) {
  _, err := db.GetUser(r.FormValue("username"))
  if err != nil {
    w.Write([]byte(err.Error()))
    return
  }
  affect, err := db.DeleteConnection(r.FormValue("username"), r.FormValue("password")); 
  if err != nil || affect < 1 {
    w.Write([]byte(err.Error()))
    return
  }

  w.Write([]byte("Connection Deleted!"))

}

