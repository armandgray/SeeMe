package models

type User struct {
  FirstName string
  LastName string
  Username string
  Secret []byte
  Discoverable bool

  Role string
  Network string
}

func CreateDummyUsers() ([]User) {
	var s []byte
    s = make([]byte, 5, 5)

	user1 := User{"Armand", "Gray", "email", s, true, "Software Engineer", "Instruct2"}
    user2 := User{"Daniela", "Meza", "username", s, false, "Program Coordinator", "Instruct2"}
    return []User{user1, user2}
}