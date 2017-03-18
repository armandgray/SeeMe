package helpers

import (
	. "seeme/models"

  "net/http"
  "fmt"
  "reflect"

  "golang.org/x/crypto/bcrypt"
)

func CreateUserFromRequest(r *http.Request) (User) {
  secret, _ := bcrypt.GenerateFromPassword ([]byte(r.FormValue("password")), bcrypt.DefaultCost)

	user := User{r.FormValue("firstName"), r.FormValue("lastName"), r.FormValue("username"), secret, false, r.FormValue("role"), ""}
  discoverable := r.FormValue("discoverable")
  if discoverable != "" && discoverable != "false" && discoverable != "0" {
    user.Discoverable = true
  }

  return user
}

func ReflectUsers(oldUser User, newUser User) (User) {
	fmt.Println(oldUser)
	fmt.Println(oldUser)
	fmt.Println("\n\n\n")
	oldUserAsFields := reflect.ValueOf(&oldUser).Elem()
  newUserAsFields := reflect.ValueOf(&newUser).Elem()
  typeOfUser := oldUserAsFields.Type()
  fmt.Println("oldUser Fields: ", oldUserAsFields)
  fmt.Println("newUser Fields: ", newUserAsFields)
  fmt.Println("typeOfUser: ", typeOfUser)

	return newUser
}