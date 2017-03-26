package localusers

import (
  "net/http"
  "encoding/json"
)

func LocalUsersController(w http.ResponseWriter, r *http.Request) {
  userList, err := GetLocalUsersForNetwork(w, r)
  if err != nil {
    http.Error(w, err.Error(), http.StatusInternalServerError)
    return
  }
  js, err := json.Marshal(userList)
  if err != nil {
    w.Write([]byte("Network Process Error!"))
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)
}