package helpers

import (
	. "seeme/models"

  "net/http"
  "golang.org/x/crypto/bcrypt"
)

func CreateUserFromRequest(r *http.Request) (User) {
  secret, _ := bcrypt.GenerateFromPassword ([]byte(r.FormValue("password")), bcrypt.DefaultCost)

	user := User{r.FormValue("firstName"), r.FormValue("lastName"), r.FormValue("username"), secret, false, r.FormValue("role"), ""}
  if r.FormValue("discoverable") != "" && r.FormValue("discoverable") != "false" {
    user.Discoverable = true
  }

  return user
}