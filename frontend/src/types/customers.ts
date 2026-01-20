export interface CustomerDTO {
  id: number
  companyId: number
  name: string
  email: string
  phone: string
  createdAt: string
  updatedAt: string
}

export interface CustomerCreateDTO {
  name: string
  email: string
  phone: string
  companyId: number
}

export interface CustomerUpdateDTO {
  name?: string
  email?: string
  phone?: string
  companyId?: number
}
