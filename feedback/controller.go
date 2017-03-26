package feedback

import (
  "seeme/db"

  "net/http"
)

func FeedbackController(w http.ResponseWriter, r *http.Request) {
  _, err := db.GetUser(r.FormValue("username"))
  if err != nil {
    w.Write([]byte("User Not Found!"))
    return
  }
  if err := InsertFeedback(r.FormValue("username"), r.FormValue("message")); err != nil { 
    w.Write([]byte("Message Upload Failed!"))
    return
  }
    
  w.Write([]byte("Message Sent!"))
}