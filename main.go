package main

import (
  "fmt"
  "net/http"
  "encoding/json"
)

type User struct {
  FirstName string
  LastName string
  Username string
  Secret []byte
}

func main()  {
  http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
    var s []byte
    s = make([]byte, 5, 5)
    user1 := User{"Armand", "Gray", "email", s}
    user2 := User{"Daniela", "Meza", "username", s}
    slcUser := []User{user1, user2}
      
    js, err := json.Marshal(slcUser)
    if err != nil {
      http.Error(w, err.Error(), http.StatusInternalServerError)
      return
    }

    w.Header().Set("Content-Type", "application/json")
    w.Write(js)
  })

  fmt.Println("Listening on port 8080")
  fmt.Println(http.ListenAndServe(":8080", nil))
}
