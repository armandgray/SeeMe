package routes

import (
  . "seeme/helpers"

  "net/http"
)

func HandlerFeedback(w http.ResponseWriter, r *http.Request) {
  _, err := GetUserFromDB(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if err := InsertFeedback(r); err != nil { 
    w.Write([]byte("Message Upload Failed!"))
    return
  }
    
  w.Write([]byte("Message Sent!"))
}