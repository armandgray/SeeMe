package connectionnetwork

import (
  "net/http"
  "encoding/json"
)

func ConnectionNetworkController(w http.ResponseWriter, r *http.Request) {
  js, err := json.Marshal([]string{})
  if err != nil {
    w.Write([]byte("Network Process Error!"))
    return
  }

  w.Header().Set("Content-Type", "application/json")
  w.Write(js)

}

