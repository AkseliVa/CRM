export interface CustomerNoteDTO {
  id: number
  customerId: number
  userId: number
  content: string
  createdAt: string
  updatedAt: string
}

export interface CustomerNoteCreateDTO {
  customerId: number
  userId: number
  content: string
}

export interface CustomerNoteUpdateDTO {
  customerId: number
  userId: number
  content: string
}
