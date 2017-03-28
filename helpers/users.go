package helpers

import (
  . "seeme/models"
  "seeme/db"

  "net/http"
  "fmt"
  "errors"
  "reflect"

  "golang.org/x/crypto/bcrypt"
)

func CreateUserFromRequest(r *http.Request) (User) {
  var secret []byte
	if r.FormValue("password") != "" {
	  secret, _ = bcrypt.GenerateFromPassword ([]byte(r.FormValue("password")), bcrypt.DefaultCost)		
	}

	user := User{r.FormValue("firstName"), r.FormValue("lastName"), r.FormValue("username"), secret, false, r.FormValue("role"), ""}
  discoverable := r.FormValue("discoverable")
  if discoverable != "" && discoverable != "false" && discoverable != "0" {
    user.Discoverable = true
  }

  return user
}

func VerifyUsers(username string, connection string) (error) {
  if username == "" || connection == "" || username == connection {
    return errors.New("Invalid Connection!")
  }

  if _, err := db.GetUser(username); err != nil {
    return errors.New("User Not Found!")
  }
  if _, err := db.GetUser(connection); err != nil {
    return errors.New("Requested User Not Found!")
  }

  return nil
}

func ReflectUsers(oldUser User, newUser User) (User) {
	if newUser.Secret == nil {
  	newUser.Secret = oldUser.Secret
  }
	oldUserAsFields := reflect.ValueOf(&oldUser).Elem()
  newUserAsFields := reflect.ValueOf(&newUser).Elem()
  typeOfUser := oldUserAsFields.Type()

  for i := 0; i < oldUserAsFields.NumField(); i++ {
    oldField := oldUserAsFields.Field(i)
    newField:= newUserAsFields.Field(i)
    fmt.Printf("%d: %s %s = %v\n", i,
        typeOfUser.Field(i).Name, oldField.Type(), oldField.Interface())
    fmt.Printf("%d: %s %s = %v\n", i,
        typeOfUser.Field(i).Name, newField.Type(), newField.Interface())
    if newField.Interface() == "" {
    	newField.Set(reflect.Value(oldField))
    }
    fmt.Printf("%d: %s %s = %v\n", i,
        typeOfUser.Field(i).Name, newField.Type(), newField.Interface())
  }

	fmt.Println("\n\n\n")
  fmt.Println(oldUser)
	fmt.Println(newUser)
	return newUser
}