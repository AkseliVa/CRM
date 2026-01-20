export type Role = 'ADMIN' | 'USER'

export interface UserDTO {
  id: number
  email: string
  role: Role
  createdAt: string
  updatedAt: string
}

export interface UserCreateDTO {
  email: string
  password: string
  role: Role
}

export interface UserUpdateDTO {
  email: string
  password: string
  role: Role
}
