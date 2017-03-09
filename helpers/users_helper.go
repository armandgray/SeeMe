package helpers

import (
	. "seeme/models"

  "net/http"
  "golang.org/x/crypto/bcrypt"
)

func CreateUserFromRequest(r *http.Request) (User) {
  secret, _ := bcrypt.GenerateFromPassword ([]byte(r.FormValue("password")), bcrypt.DefaultCost)

	user := User{r.FormValue("firstName"), r.FormValue("lastName"), r.FormValue("username"), secret, false, r.FormValue("role"), "Instruct2"}
  if r.FormValue("discoverable") != "" {
    user.Discoverable = true
  }

  return user
}