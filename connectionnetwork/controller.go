package connectionnetwork

import (
  "seeme/db"

  "net/http"
  "encoding/json"
)

func ConnectionNetworkController(w http.ResponseWriter, r *http.Request) {
  userMap, err := db.GetNetworkList(r.FormValue("username"))
  if err != nil {
    w.Write([]byte(err.Error()))
    return
  }
  js, err := json.Marshal(userMap)
  if err != nil {
    w.Write([]byte("Network Process Error!"))
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)

}

