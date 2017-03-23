package routes

import (
  . "seeme/controllers"

  "net/http"
)

func HandlerNewConnection(w http.ResponseWriter, r *http.Request) {
  _, err := GetUserFromDB(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }    
}