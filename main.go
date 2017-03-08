package main

import (
  "fmt"
  "net/http"
  "encoding/json"
)

type User struct {
  FirstName string
  LastName string
}

func main()  {
  http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
    user1 := User{"Armand", "Gray"}
    user2 := User{"Daniela", "Meza"}
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
