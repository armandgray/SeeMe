package profileupdate

import (
  "seeme/helpers"
  "seeme/db"

  "net/http"

  "golang.org/x/crypto/bcrypt"
)

func ProfileUpdateController(w http.ResponseWriter, r *http.Request) {
  oldUser, err := db.GetUser(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if oldUser.Username == "" {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  } else {
    if err := bcrypt.CompareHashAndPassword(oldUser.Secret, []byte(r.FormValue("oldSecret"))); err != nil {
      w.Write([]byte("Password Incorrect!"))
      return
    } else {
      newUser := helpers.ReflectUsers(oldUser, helpers.CreateUserFromRequest(r))
      if err := db.UpdateUser(newUser); err != nil {
        w.Write([]byte("Update Failed!"))
        http.Error(w, err.Error(), http.StatusInternalServerError)
        return
      }
      http.Redirect(w, r, "/seeme/api/login/user?username=" + newUser.Username, http.StatusFound)
    }
  }
}

