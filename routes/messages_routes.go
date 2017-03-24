package routes

import (
  "seeme/db"

  "net/http"
)

func HandlerFeedback(w http.ResponseWriter, r *http.Request) {
  _, err := db.GetUser(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if err := db.InsertFeedback(r.FormValue("username"), r.FormValue("message")); err != nil { 
    w.Write([]byte("Message Upload Failed!"))
    return
  }
    
  w.Write([]byte("Message Sent!"))
}