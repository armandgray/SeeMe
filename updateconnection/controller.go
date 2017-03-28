package updateconnection

import (

  "net/http"
)

func UpdateConnectionStatusController(w http.ResponseWriter, r *http.Request) {
  

  w.Write([]byte("Connection Deleted!"))

}

