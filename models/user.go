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