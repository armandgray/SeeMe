package registeruser

import (
  "seeme/helpers"
  "seeme/db"

  "fmt"
  "net/http"
)

func RegisterUserController(w http.ResponseWriter, r *http.Request) {
  if r.FormValue("register") != "" {
    if err := db.InsertUser(helpers.CreateUserFromRequest(r)); err != nil { 
      fmt.Println("Database Insert Failure: " + err.Error())
    } else {
      http.Redirect(w, r, "/seeme/api/login/user?username=" + r.FormValue("username"), http.StatusFound)
      return
    }
  }
}